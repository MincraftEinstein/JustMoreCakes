package einstein.jmc.block.entity;

import com.google.common.collect.Lists;
import einstein.jmc.block.CakeOvenBlock;
import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import einstein.jmc.platform.Services;
import einstein.jmc.util.CakeOvenConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class CakeOvenBlockEntity extends BaseContainerBlockEntity implements MenuDataProvider, WorldlyContainer, RecipeHolder, StackedContentsCompatible, CakeOvenConstants {

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private NonNullList<ItemStack> remainingItems = NonNullList.withSize(REMAINING_ITEMS, ItemStack.EMPTY);
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final ContainerData dataAccess = new ContainerData() {

        public int get(int index) {
            return switch (index) {
                case 0 -> CakeOvenBlockEntity.this.litTime;
                case 1 -> CakeOvenBlockEntity.this.litDuration;
                case 2 -> CakeOvenBlockEntity.this.cookingProgress;
                case 3 -> CakeOvenBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> CakeOvenBlockEntity.this.litTime = value;
                case 1 -> CakeOvenBlockEntity.this.litDuration = value;
                case 2 -> CakeOvenBlockEntity.this.cookingProgress = value;
                case 3 -> CakeOvenBlockEntity.this.cookingTotalTime = value;
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
        boolean flag = blockEntity.isLit();
        boolean flag2 = false;
        if (blockEntity.isLit()) {    // Reduces lit time by 1 every tick
            --blockEntity.litTime;
        }

        ItemStack fuelStack = blockEntity.items.get(FUEL_SLOT);
        boolean hasIngredient = !blockEntity.items.get(INGREDIENT_SLOT_1).isEmpty() ||
                !blockEntity.items.get(INGREDIENT_SLOT_2).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_3).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_4).isEmpty();
        if (blockEntity.isLit() || !fuelStack.isEmpty() && (!blockEntity.items.get(INGREDIENT_SLOT_1).isEmpty() ||
                !blockEntity.items.get(INGREDIENT_SLOT_2).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_3).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_4).isEmpty())) {
            CakeOvenRecipe recipe = level.getRecipeManager().getRecipeFor(ModRecipes.CAKE_OVEN_RECIPE.get(), blockEntity, level).orElse(null);
            int stackSize = blockEntity.getMaxStackSize();
            RegistryAccess access = level.registryAccess();

            // Controls the fuel progress and fuel items
            if (!blockEntity.isLit() && blockEntity.hasResultSpace(access, recipe, blockEntity.items, stackSize)) {
                blockEntity.litTime = blockEntity.getBurnDuration(fuelStack);
                blockEntity.litDuration = blockEntity.litTime;
                if (blockEntity.isLit()) {
                    flag2 = true;
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
            if (blockEntity.isLit() && blockEntity.hasResultSpace(access, recipe, blockEntity.items, stackSize)) {
                ++blockEntity.cookingProgress;
                if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = 0;
                    blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
                    if (blockEntity.smeltRecipe(access, recipe, blockEntity.items, stackSize)) {
                        blockEntity.setRecipeUsed(recipe);
                    }
                }
            }
        }
        else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        if (flag != blockEntity.isLit()) {
            flag2 = true;
            state = state.setValue(CakeOvenBlock.LIT, blockEntity.isLit());
            level.setBlock(pos, state, 3);
        }

        if (flag2) {
            setChanged(level, pos, state);
        }
    }

    private boolean hasResultSpace(RegistryAccess access, @Nullable CakeOvenRecipe recipe, NonNullList<ItemStack> slotItems, int maxStackSize) {
        if (recipe != null) {
            ItemStack stack = recipe.assemble(this, access);
            if (stack.isEmpty()) {
                return false;
            }

            ItemStack resultStack = slotItems.get(RESULT_SLOT);
            if (resultStack.isEmpty()) {
                return true;
            }
            else if (!ItemStack.isSameItem(resultStack, stack)) {
                return false;
            }
            else if (resultStack.getCount() + stack.getCount() <= maxStackSize && resultStack.getCount() + stack.getCount() <= resultStack.getMaxStackSize()) {
                return true;
            }
            return resultStack.getCount() + stack.getCount() <= stack.getMaxStackSize();
        }
        return false;
    }

    private boolean smeltRecipe(RegistryAccess access, @Nullable CakeOvenRecipe recipe, NonNullList<ItemStack> slotItems, int maxStackSize) {
        if (hasResultSpace(access, recipe, slotItems, maxStackSize)) {
            ItemStack resultStack = slotItems.get(RESULT_SLOT);
            ItemStack stack = recipe.assemble(this, access);
            if (resultStack.isEmpty()) { // If the result slot is empty set the slot items to the recipe result
                slotItems.set(RESULT_SLOT, stack.copy());
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
    public ItemStack removeItem(int slotIndex, int count) {
        return ContainerHelper.removeItem(items, slotIndex, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        return ContainerHelper.takeItem(items, slotIndex);
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack) {
        ItemStack stack1 = items.get(slotIndex);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, stack1);
        items.set(slotIndex, stack);

        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        if ((slotIndex == INGREDIENT_SLOT_1 || slotIndex == INGREDIENT_SLOT_2 || slotIndex == INGREDIENT_SLOT_3 ||
                slotIndex == INGREDIENT_SLOT_4) && !flag) {
            cookingTotalTime = getTotalCookTime(level, this);
            cookingProgress = 0;
            setChanged();
        }
    }

    private static int getTotalCookTime(Level level, Container container) {
        return level.getRecipeManager().getRecipeFor(ModRecipes.CAKE_OVEN_RECIPE.get(), container, level).map(CakeOvenRecipe::getCookingTime).orElse(DEFAULT_BURN_TIME);
    }

    @Override
    public boolean stillValid(Player player) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64;
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == RESULT_SLOT) {    // If result slot, no items can be placed
            return false;
        }
        else if (slotIndex != FUEL_SLOT) {    // If is not fuel slot (most likely ingredient slot), any item can be placed
            return true;
        }
        else {    // Else (most likely fuel slot), only items with a burn time can be placed
            return Services.HOOKS.getBurnTime(stack) > 0;
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    private boolean isLit() {
        return litTime > 0;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);    // Sets the default items in the container
        ContainerHelper.loadAllItems(tag, items);
        remainingItems = NonNullList.withSize(REMAINING_ITEMS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag.getCompound("RemainingItems"), remainingItems);
        litTime = tag.getInt("BurnTime");
        cookingProgress = tag.getInt("CookTime");
        cookingTotalTime = tag.getInt("CookTimeTotal");
        litDuration = getBurnDuration(items.get(FUEL_SLOT));
        CompoundTag usedRecipes = tag.getCompound("RecipesUsed");

        for (String recipe : usedRecipes.getAllKeys()) {
            recipesUsed.put(new ResourceLocation(recipe), usedRecipes.getInt(recipe));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", litTime);
        tag.putInt("CookTime", cookingProgress);
        tag.putInt("CookTimeTotal", cookingTotalTime);
        ContainerHelper.saveAllItems(tag, items);
        CompoundTag remainingItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(remainingItemsTag, remainingItems);
        tag.put("RemainingItems", remainingItemsTag);
        CompoundTag usedRecipes = new CompoundTag();
        recipesUsed.forEach((id, par1) -> usedRecipes.putInt(id.toString(), par1));
        tag.put("RecipesUsed", usedRecipes);
    }

    protected int getBurnDuration(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        else {
            return Services.HOOKS.getBurnTime(stack);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            recipesUsed.addTo(recipe.getId(), 1);
        }
    }

    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> stacks) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        List<Recipe<?>> recipes = getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
        player.awardRecipes(recipes);

        for (Recipe<?> recipe : recipes) {
            if (recipe != null) {
                player.triggerRecipeCrafted(recipe, items);
            }
        }

        recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
        List<Recipe<?>> recipes = Lists.newArrayList();

        for (Entry<ResourceLocation> entry : recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
                recipes.add(recipe);
                AbstractFurnaceBlockEntity.createExperience(level, pos, entry.getIntValue(), ((CakeOvenRecipe) recipe).getExperience());
            });
        }

        return recipes;
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
        else {
            return HOPPER_FEED_THROUGH_SIDE_SLOTS;
        }
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

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
