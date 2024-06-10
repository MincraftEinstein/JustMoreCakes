package einstein.jmc.block.entity;

import einstein.jmc.block.CeramicBowlBlock;
import einstein.jmc.data.BowlContents;
import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.MixingRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CeramicBowlBlockEntity extends BlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {

    public static final int INGREDIENT_SLOT_COUNT = 4;
    public static final int SLOT_COUNT = 5;
    public static final int DEFAULT_MIXING_PROGRESS = 5;
    public static final int RESULT_SLOT = 4;
    private final NonNullList<ItemStack> stacks = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final NonNullList<ItemStack> remainingItems = NonNullList.withSize(INGREDIENT_SLOT_COUNT, ItemStack.EMPTY);
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<CeramicBowlBlockEntity, MixingRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.MIXING_RECIPE.get());
    private int mixingProgress;
    private Holder<BowlContents> contentsHolder = BowlContents.EMPTY.getHolder();

    public CeramicBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CERAMIC_BOWL.get(), pos, state);
    }

    public boolean tryCraft(Player player) {
        Optional<MixingRecipe> matchingRecipe = getMatchingRecipe();

        if (matchingRecipe.isPresent()) {
            MixingRecipe recipe = matchingRecipe.get();
            ItemStack resultStack = recipe.assemble(this, level.registryAccess());
            if (!resultStack.isEmpty()) {
                if (mixingProgress < (recipe.getMixingTime() - 1) && !isEmpty()) {
                    mixingProgress++;
                    float fillPercent = (float) mixingProgress / recipe.getMixingTime();
                    level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CeramicBowlBlock.FILL_LEVEL, (int) (fillPercent * 4)));
                    contentsHolder = BowlContents.getHolder(level, recipe.getContentsId());
                    contentsChanged(player);
                    return true;
                }

                for (int i = 0; i < stacks.size(); i++) {
                    if (i != RESULT_SLOT) {
                        Item item = stacks.get(i).getItem();
                        if (item.hasCraftingRemainingItem()) {
                            addToStackList(new ItemStack(item.getCraftingRemainingItem()), remainingItems, getMaxStackSize());
                        }
                    }
                }

                setResult(resultStack);
                recipe.consumeIngredients(this);
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CeramicBowlBlock.FILL_LEVEL, 4));
                contentsChanged(player);
                setRecipeUsed(recipe);
                awardUsedRecipes(player, stacks);
                return true;
            }
        }

        return false;
    }

    public boolean addItem(Player player, ItemStack stack) {
        if (mixingProgress <= 0) {
            for (int i = 0; i < stacks.size(); i++) {
                if (i != RESULT_SLOT) {
                    ItemStack currentStack = getItem(i);
                    if (currentStack.isEmpty()) {
                        stacks.set(i, stack.split(1));
                        contentsChanged(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean addToStackList(ItemStack stack, NonNullList<ItemStack> stacks, int maxStackSize) {
        int emptySlot = -1;
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack currentStack = stacks.get(i);
            if (CakeOvenBlockEntity.canAddToStack(stack, currentStack, maxStackSize, 1)) {
                if (currentStack.is(stack.getItem())) {
                    currentStack.grow(1);
                    return true;
                }
                else if (currentStack.isEmpty() && emptySlot == -1) {
                    emptySlot = i;
                }
            }
        }

        if (emptySlot > -1) {
            stacks.set(emptySlot, stack.copyWithCount(1));
            return true;
        }
        return false;
    }

    public boolean takeItem(Player player) {
        if (!isEmpty() && mixingProgress <= 0) {
            for (int i = stacks.size() - 1; i > -1; i--) {
                if (i != RESULT_SLOT) {
                    ItemStack currentStack = getItem(i);
                    if (!currentStack.isEmpty()) {
                        Block.popResourceFromFace(level, worldPosition, Direction.UP, currentStack);
                        stacks.set(i, ItemStack.EMPTY);
                        contentsChanged(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean takeResult(Player player) {
        ItemStack result = getResult();
        if (!result.isEmpty()) {
            clear(false);
            result.onCraftedBy(level, player, result.getCount());
            Block.popResourceFromFace(level, worldPosition, Direction.UP, result);

            for (ItemStack remainingStack : remainingItems) {
                Block.popResourceFromFace(level, worldPosition, Direction.UP, remainingStack);
            }

            remainingItems.clear();
            setResult(ItemStack.EMPTY);
            contentsChanged(player);
            return true;
        }
        return false;
    }

    private void clear(boolean update) {
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CeramicBowlBlock.FILL_LEVEL, 0));
        contentsHolder = BowlContents.EMPTY.getHolder();
        mixingProgress = 0;

        if (update) {
            setChanged();
        }
    }

    private void contentsChanged(Player player) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(player, getBlockState()));
        setUpdated();
    }

    private void setUpdated() {
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void dropItems(Level level, BlockPos pos) {
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getResult());
        Containers.dropContents(level, pos, stacks);
        Containers.dropContents(level, pos, remainingItems);
        remainingItems.clear();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return new int[] {RESULT_SLOT};
        }
        return new int[] {0, 1, 2, 3};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotIndex, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slotIndex, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slotIndex) {
        return stacks.get(slotIndex);
    }

    @Override
    public ItemStack removeItem(int slotIndex, int count) {
        ItemStack stack = ContainerHelper.removeItem(stacks, slotIndex, count);
        if (!stack.isEmpty() && hasLevel()) {
            clear(true);
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        return ContainerHelper.takeItem(stacks, slotIndex);
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack) {
        ItemStack currentStack = stacks.get(slotIndex);
        boolean isSameStack = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, currentStack);
        stacks.set(slotIndex, stack);

        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        if (!isSameStack) {
            setUpdated();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        return stacks.get(slotIndex).isEmpty();
    }

    @Override
    public void clearContent() {
        stacks.clear();
        if (hasLevel()) {
            clear(true);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, stacks);
        contentsHolder.unwrapKey().ifPresent(key -> tag.putString("Contents", key.location().toString()));
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        stacks.clear();
        ContainerHelper.loadAllItems(tag, stacks);
        ContainerHelper.loadAllItems(tag.getCompound("RemainingItems"), remainingItems);
        setResult(ItemStack.of(tag.getCompound("ResultStack")));
        Optional.ofNullable(ResourceLocation.tryParse(tag.getString("Contents")))
                .map(id -> BowlContents.getHolder(level, id))
                .ifPresent(holder -> contentsHolder = holder);
        mixingProgress = tag.getInt("MixingProgress");
        CompoundTag recipesUsed = tag.getCompound("RecipesUsed");

        for (String recipeId : recipesUsed.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(recipeId), recipesUsed.getInt(recipeId));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, stacks);
        CompoundTag remainingItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(remainingItemsTag, remainingItems);
        tag.put("RemainingItems", remainingItemsTag);
        CompoundTag resultTag = new CompoundTag();
        getResult().save(resultTag);
        tag.put("ResultStack", resultTag);
        contentsHolder.unwrapKey().ifPresent(key -> tag.putString("Contents", key.location().toString()));
        tag.putInt("MixingProgress", mixingProgress);
        CompoundTag recipesUsed = new CompoundTag();
        this.recipesUsed.forEach((recipeId, i) -> recipesUsed.putInt(recipeId.toString(), i));
        tag.put("RecipesUsed", recipesUsed);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            recipesUsed.addTo(recipe.getId(), 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> stacks) {
        if (level.isClientSide) {
            return;
        }

        List<Recipe<?>> recipes = new ArrayList<>();

        for (Object2IntMap.Entry<ResourceLocation> entry : recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
                recipes.add(recipe);
                player.triggerRecipeCrafted(recipe, stacks);
            });
        }

        player.awardRecipes(recipes);
        recipesUsed.clear();
    }

    @Override
    public void fillStackedContents(StackedContents contents) {
        for (ItemStack stack : stacks) {
            contents.accountStack(stack);
        }
    }

    public Holder<BowlContents> getContents() {
        return contentsHolder;
    }

    public ItemStack getResult() {
        return stacks.get(RESULT_SLOT);
    }

    public void setResult(ItemStack result) {
        stacks.set(RESULT_SLOT, result);
    }

    public int getMixingProgress() {
        return mixingProgress;
    }

    public Optional<MixingRecipe> getMatchingRecipe() {
        return quickCheck.getRecipeFor(this, level);
    }
}
