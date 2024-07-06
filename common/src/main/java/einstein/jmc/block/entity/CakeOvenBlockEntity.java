package einstein.jmc.block.entity;

import einstein.jmc.block.CakeOvenBlock;
import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.ContainerRecipeInput;
import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import einstein.jmc.platform.Services;
import einstein.jmc.util.CakeOvenConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CakeOvenBlockEntity extends BaseContainerBlockEntity implements MenuDataProvider, WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible, CakeOvenConstants {

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private NonNullList<ItemStack> remainingItems = NonNullList.withSize(REMAINING_ITEMS, ItemStack.EMPTY);
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<ContainerRecipeInput, CakeOvenRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.CAKE_OVEN_RECIPE.get());
    private final ContainerData dataAccess = new ContainerData() {

        public int get(int index) {
            return switch (index) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> cookingProgress;
                case 3 -> cookingTotalTime;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> litTime = value;
                case 1 -> litDuration = value;
                case 2 -> cookingProgress = value;
                case 3 -> cookingTotalTime = value;
            }
        }

        public int getCount() {
            return DATA_COUNT;
        }
    };

    public CakeOvenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CAKE_OVEN.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.jmc.cake_oven");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CakeOvenMenu(id, inventory, this, dataAccess);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CakeOvenBlockEntity blockEntity) {
        boolean wasLit = blockEntity.isLit();
        boolean update = false;

        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack fuelStack = blockEntity.items.get(FUEL_SLOT);
        boolean hasIngredient = !blockEntity.items.get(INGREDIENT_SLOT_1).isEmpty()
                || !blockEntity.items.get(INGREDIENT_SLOT_2).isEmpty()
                || !blockEntity.items.get(INGREDIENT_SLOT_3).isEmpty()
                || !blockEntity.items.get(INGREDIENT_SLOT_4).isEmpty();

        if (blockEntity.isLit() || !fuelStack.isEmpty() && hasIngredient) {
            RecipeHolder<CakeOvenRecipe> recipe = hasIngredient ? blockEntity.quickCheck.getRecipeFor(new ContainerRecipeInput(blockEntity), level).orElse(null) : null;
            int stackSize = blockEntity.getMaxStackSize();
            RegistryAccess access = level.registryAccess();

            // Controls the fuel progress and fuel items
            if (!blockEntity.isLit() && blockEntity.hasResultSpace(access, recipe, hasIngredient, stackSize)) {
                blockEntity.litTime = blockEntity.getBurnDuration(fuelStack);
                blockEntity.litDuration = blockEntity.litTime;

                if (blockEntity.isLit()) {
                    update = true;
                    Item remainingItem = fuelStack.getItem().getCraftingRemainingItem();

                    if (fuelStack.getItem().hasCraftingRemainingItem()) {
                        blockEntity.items.set(FUEL_SLOT, remainingItem == null ? ItemStack.EMPTY : new ItemStack(remainingItem));
                    }
                    else if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);

                        if (fuelStack.isEmpty()) {
                            blockEntity.items.set(FUEL_SLOT, remainingItem == null ? ItemStack.EMPTY : new ItemStack(remainingItem));
                        }
                    }
                }
            }

            // Controls the burn progress and outputs the items
            if (blockEntity.isLit() && blockEntity.hasResultSpace(access, recipe, hasIngredient, stackSize)) {
                ++blockEntity.cookingProgress;

                if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = 0;
                    blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);

                    if (blockEntity.smeltRecipe(access, recipe, hasIngredient, stackSize)) {
                        blockEntity.setRecipeUsed(recipe);
                    }
                    update = true;
                }
            }
            else {
                blockEntity.cookingProgress = 0;
            }
        }
        else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        if (wasLit != blockEntity.isLit()) {
            update = true;
            state = state.setValue(CakeOvenBlock.LIT, blockEntity.isLit());
            level.setBlockAndUpdate(pos, state);
        }

        if (update) {
            setChanged(level, pos, state);
        }
    }

    private boolean hasResultSpace(RegistryAccess access, @Nullable RecipeHolder<CakeOvenRecipe> holder, boolean hasIngredient, int maxStackSize) {
        if (hasIngredient && holder != null) {
            ItemStack stack = holder.value().getResultItem(access);
            if (stack.isEmpty()) {
                return false;
            }

            return canAddToStack(stack, items.get(RESULT_SLOT), maxStackSize, stack.getCount());
        }
        return false;
    }

    public static boolean canAddToStack(ItemStack stack, ItemStack currentStack, int maxStackSize, int growSize) {
        if (currentStack.isEmpty()) {
            return true;
        }
        else if (!ItemStack.isSameItemSameComponents(currentStack, stack)) {
            return false;
        }

        int combinedCount = currentStack.getCount() + growSize;
        if (combinedCount <= maxStackSize && combinedCount <= currentStack.getMaxStackSize()) {
            return true;
        }
        return combinedCount <= stack.getMaxStackSize();
    }

    private boolean smeltRecipe(RegistryAccess access, @Nullable RecipeHolder<CakeOvenRecipe> holder, boolean hasIngredient, int maxStackSize) {
        if (hasResultSpace(access, holder, hasIngredient, maxStackSize)) {
            CakeOvenRecipe recipe = holder.value();
            ItemStack resultStack = items.get(RESULT_SLOT);
            ItemStack stack = recipe.assemble(new ContainerRecipeInput(this), access);

            if (resultStack.isEmpty()) { // If the result slot is empty set the slot items to the recipe result
                items.set(RESULT_SLOT, stack.copy());
            }
            else if (resultStack.is(stack.getItem())) { // Else if the result slot has the same items as the recipe
                resultStack.grow(stack.getCount());
            }

            recipe.consumeIngredients(this, remainingItems);
            return true;
        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slotIndex) {
        return items.get(slotIndex);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack) {
        ItemStack currentStack = items.get(slotIndex);
        boolean isSameStack = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, currentStack);
        items.set(slotIndex, stack);
        stack.limitSize(getMaxStackSize(stack));

        if ((slotIndex >= INGREDIENT_SLOT_1 && slotIndex <= INGREDIENT_SLOT_4) && !isSameStack) {
            cookingTotalTime = getTotalCookTime(level, this);
            cookingProgress = 0;
            setChanged();
        }
    }

    private static int getTotalCookTime(Level level, CakeOvenBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(new ContainerRecipeInput(blockEntity), level)
                .map(holder -> holder.value().getCookingTime()).orElse(DEFAULT_BURN_TIME);
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == RESULT_SLOT) {    // If result slot, no items can be placed
            return false;
        }
        else if (slotIndex != FUEL_SLOT) {    // If is not fuel slot (most likely ingredient slot), any item can be placed
            return true;
        }
        // Else (most likely fuel slot), only items with a burn time can be placed
        return Services.HOOKS.getBurnTime(stack) > 0;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    private boolean isLit() {
        return litTime > 0;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);    // Sets the default items in the container
        ContainerHelper.loadAllItems(tag, items, provider);
        remainingItems = NonNullList.withSize(REMAINING_ITEMS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag.getCompound("RemainingItems"), remainingItems, provider);
        litTime = tag.getShort("BurnTime");
        cookingProgress = tag.getShort("CookTime");
        cookingTotalTime = tag.getShort("CookTimeTotal");
        litDuration = getBurnDuration(items.get(FUEL_SLOT));
        CompoundTag usedRecipes = tag.getCompound("RecipesUsed");

        for (String recipeId : usedRecipes.getAllKeys()) {
            recipesUsed.put(ResourceLocation.parse(recipeId), usedRecipes.getInt(recipeId));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putShort("BurnTime", (short) litTime);
        tag.putShort("CookTime", (short) cookingProgress);
        tag.putShort("CookTimeTotal", (short) cookingTotalTime);
        ContainerHelper.saveAllItems(tag, items, provider);

        CompoundTag remainingItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(remainingItemsTag, remainingItems, provider);
        tag.put("RemainingItems", remainingItemsTag);

        CompoundTag usedRecipes = new CompoundTag();
        recipesUsed.forEach((id, par1) -> usedRecipes.putInt(id.toString(), par1));
        tag.put("RecipesUsed", usedRecipes);
    }

    private int getBurnDuration(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        return Services.HOOKS.getBurnTime(stack);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> holder) {
        if (holder != null) {
            recipesUsed.addTo(holder.id(), 1);
        }
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> stacks) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        List<RecipeHolder<?>> recipeHolders = getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
        player.awardRecipes(recipeHolders);

        for (RecipeHolder<?> holder : recipeHolders) {
            if (holder != null) {
                player.triggerRecipeCrafted(holder, items);
            }
        }

        recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
        List<RecipeHolder<?>> recipeHolders = new ArrayList<>();

        for (Entry<ResourceLocation> entry : recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(holder -> {
                recipeHolders.add(holder);
                AbstractFurnaceBlockEntity.createExperience(level, pos, entry.getIntValue(), ((CakeOvenRecipe) holder.value()).getExperience());
            });
        }

        return recipeHolders;
    }

    public void dropRemainingItems(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, remainingItems);
        remainingItems.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction faceDirection) {
        if (faceDirection == Direction.DOWN) {
            return HOPPER_PULL_SLOTS;
        }
        else if (faceDirection == Direction.UP) {
            return HOPPER_FEED_THROUGH_TOP_SLOTS;
        }
        return HOPPER_FEED_THROUGH_SIDE_SLOTS;
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
    public void fillStackedContents(StackedContents contents) {
        for (ItemStack stack : items) {
            contents.accountStack(stack);
        }
    }

    @Override
    public void writeMenuData(ServerPlayer player, FriendlyByteBuf buf) {
    }

    public NonNullList<ItemStack> getRemainingItems() {
        return remainingItems;
    }
}
