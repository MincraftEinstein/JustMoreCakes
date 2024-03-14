package einstein.jmc.block.entity;

import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.MixingRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CeramicBowlBlockEntity extends BlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {

    public static final int SLOT_COUNT = 4;
    public static final int MAX_PROGRESS = 5;
    private final NonNullList<ItemStack> stacks = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int mixingProgress;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<CeramicBowlBlockEntity, MixingRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.MIXING_RECIPE.get());

    public CeramicBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CERAMIC_BOWL.get(), pos, state);
    }

    public boolean tryCraft(Player player) {
        Optional<MixingRecipe> matchingRecipe = quickCheck.getRecipeFor(this, level);

        if (matchingRecipe.isPresent()) {
            MixingRecipe recipe = matchingRecipe.get();
            ItemStack resultStack = recipe.assemble(this, level.registryAccess());
            if (!resultStack.isEmpty()) {
                if (mixingProgress < MAX_PROGRESS && !isEmpty()) {
                    mixingProgress++;
                    return true;
                }

                mixingProgress = 0;
                resultStack.onCraftedBy(level, player, resultStack.getCount());
                Block.popResourceFromFace(level, worldPosition, Direction.UP, resultStack);

                for (ItemStack stack : stacks) {
                    Item item = stack.getItem();
                    if (item.hasCraftingRemainingItem()) {
                        Block.popResourceFromFace(level, worldPosition, Direction.UP, new ItemStack(item.getCraftingRemainingItem()));
                    }
                }

                recipe.consumeIngredients(this);
                itemsChanged(player);
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
                ItemStack currentStack = getItem(i);
                if (CakeOvenBlockEntity.canAddToStack(stack, currentStack, getMaxStackSize(), 1)) {
                    if (currentStack.isEmpty()) {
                        stacks.set(i, stack.copyWithCount(1));
                    }
                    else if (currentStack.is(stack.getItem())) {
                        currentStack.grow(1);
                    }

                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    itemsChanged(player);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean takeItem(Player player) {
        if (!isEmpty() && mixingProgress <= 0) {
            for (int i = stacks.size() - 1; i > -1; i--) {
                ItemStack currentStack = getItem(i);
                if (!currentStack.isEmpty()) {
                    Block.popResourceFromFace(level, worldPosition, Direction.UP, removeItem(i, currentStack.getCount()));
                    itemsChanged(player);
                    return true;
                }
            }
        }
        return false;
    }

    private void itemsChanged(Player player) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(player, getBlockState()));
        setUpdated();
    }

    private void setUpdated() {
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
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
        return ContainerHelper.removeItem(stacks, slotIndex, count);
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
        return true;
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        return true;
    }

    @Override
    public void clearContent() {
        stacks.clear();
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
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        clearContent();
        ContainerHelper.loadAllItems(tag, stacks);
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
}
