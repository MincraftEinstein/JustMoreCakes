package einstein.jmc.blockentity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import einstein.jmc.blocks.CakeOvenBlock;
import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.menu.CakeOvenMenu;
import einstein.jmc.util.CakeOvenConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class CakeOvenBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, CakeOvenConstants {

	private static final int DEFAULT_BURN_TIME = 200;
	private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	private int litTime;
	private int litDuration;
	private int cookingProgress;
	private int cookingTotalTime;
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			switch (index) {
			case 0:
				return CakeOvenBlockEntity.this.litTime;
			case 1:
				return CakeOvenBlockEntity.this.litDuration;
			case 2:
				return CakeOvenBlockEntity.this.cookingProgress;
			case 3:
				return CakeOvenBlockEntity.this.cookingTotalTime;
			default:
				return 0;
			}
		}
		
		public void set(int index, int value) {
			switch (index) {
			case 0:
				CakeOvenBlockEntity.this.litTime = value;
				break;
			case 1:
				CakeOvenBlockEntity.this.litDuration = value;
				break;
			case 2:
				CakeOvenBlockEntity.this.cookingProgress = value;
				break;
			case 3:
				CakeOvenBlockEntity.this.cookingTotalTime = value;
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
		return new TranslatableComponent("container.jmc.cake_oven");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new CakeOvenMenu(id, inventory, ModRecipes.CAKE_OVEN_RECIPE, this, dataAccess);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, CakeOvenBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit();
		boolean flag1 = false;
		if (blockEntity.isLit()) {	// Reduces lit time by 1 every tick
			--blockEntity.litTime;
		}
		
		ItemStack fuelStack = blockEntity.items.get(FUEL_SLOT);
		if (blockEntity.isLit() || !fuelStack.isEmpty() && (!blockEntity.items.get(INGREDIENT_SLOT_1).isEmpty() ||
				!blockEntity.items.get(INGREDIENT_SLOT_2).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_3).isEmpty() || !blockEntity.items.get(INGREDIENT_SLOT_4).isEmpty())) {
			Recipe<?> recipe = level.getRecipeManager().getRecipeFor((RecipeType<CakeOvenRecipe>)ModRecipes.CAKE_OVEN_RECIPE, blockEntity, level).orElse(null);
			int i = blockEntity.getMaxStackSize();
			
			// Controls the fuel progress and fuel items
			if (!blockEntity.isLit() && blockEntity.hasResultSpace(recipe, blockEntity.items, i)) {
				blockEntity.litTime = blockEntity.getBurnDuration(fuelStack);
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					flag1 = true;
					if (fuelStack.hasContainerItem()) {
						blockEntity.items.set(FUEL_SLOT, fuelStack.getContainerItem());
					}
					else if (!fuelStack.isEmpty()) {
						fuelStack.shrink(1);
						if (fuelStack.isEmpty()) {
							blockEntity.items.set(FUEL_SLOT, fuelStack.getContainerItem());
						}
					}
				}
			}
			
			// Controls the burn progress and outputs the items
			if (blockEntity.isLit() && blockEntity.hasResultSpace(recipe, blockEntity.items, i)) {
				++blockEntity.cookingProgress;
				if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
					if (blockEntity.smeltRecipe(recipe, blockEntity.items, i)) {
						blockEntity.setRecipeUsed(recipe);
					}
				}
			}
		}
		else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) { 
			blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
		}
		
		if (flag != blockEntity.isLit()) {
			flag1 = true;
			state = state.setValue(CakeOvenBlock.LIT, Boolean.valueOf(blockEntity.isLit()));
			level.setBlock(pos, state, 3);
		}
		
		if (flag1) {
			setChanged(level, pos, state);
		}
	}
	
	private boolean hasResultSpace(Recipe<?> recipe, NonNullList<ItemStack> slotItems, int maxStackSize) {
		if (recipe != null) {
			ItemStack stack = ((CakeOvenRecipe) recipe).assemble(this);	
			if (stack.isEmpty()) {
				return false;
			}
			else {
				ItemStack resultStack = slotItems.get(RESULT_SLOT);
				if (resultStack.isEmpty()) {
					return true;
				}
				else if (!resultStack.sameItem(stack)) {
					return false;
				}
				else if (resultStack.getCount() + stack.getCount() <= maxStackSize && resultStack.getCount() + stack.getCount() <= resultStack.getMaxStackSize()) {
					return true;
				}
				else {
					return resultStack.getCount() + stack.getCount() <= stack.getMaxStackSize();
				} 	
			}
		}
		else {
			return false;
		}
	}
	
	private boolean smeltRecipe(@Nullable Recipe<?> recipe, NonNullList<ItemStack> slotItems, int maxStackSize) {
		if (recipe != null && hasResultSpace(recipe, slotItems, maxStackSize)) {
			ItemStack resultStack = slotItems.get(RESULT_SLOT);
			ItemStack stack = ((CakeOvenRecipe) recipe).assemble(this);
			if (resultStack.isEmpty()) { // If the result slot is empty set the slot items to the recipe result
				slotItems.set(RESULT_SLOT, stack.copy());
			}
			else if (resultStack.is(stack.getItem())) { // Else if the result slot has the same items as the recipe
				resultStack.grow(stack.getCount());
			}
			
			((CakeOvenRecipe) recipe).consumeIngredients(this);
			return true;
		}
		else {
			return false;
		}
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
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return ContainerHelper.removeItem(items, p_18942_, p_18943_);
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slotIndex) {
		return ContainerHelper.takeItem(items, slotIndex);
	}
	
	@Override
	public void setItem(int slotIndex, ItemStack stack) {
		ItemStack stack1 = items.get(slotIndex);
		boolean flag = !stack.isEmpty() && stack.sameItem(stack1) && ItemStack.tagMatches(stack, stack1);
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
		return level.getRecipeManager().getRecipeFor((RecipeType<CakeOvenRecipe>)ModRecipes.CAKE_OVEN_RECIPE, container, level).map(CakeOvenRecipe::getCookingTime).orElse(DEFAULT_BURN_TIME);
	}
	
	@Override
	public boolean stillValid(Player player) {
		if (level.getBlockEntity(worldPosition) != this) {
			return false;
		}
		else {
			return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64;
		}
	}
	
	@Override
	public boolean canPlaceItem(int slotIndex, ItemStack stack) {
		if (slotIndex == RESULT_SLOT) {	// If result slot, no items can be placed
			return false;
		}
		else if (slotIndex != FUEL_SLOT) {	// If is not fuel slot (most likely ingredient slot), any item can be placed 
			return true;
		}
		else {	// Else (most likely fuel slot), only items with a burn time can be placed
			return ForgeHooks.getBurnTime(stack, ModRecipes.CAKE_OVEN_RECIPE) > 0;
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
		items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);	// Sets the default items in the container
		ContainerHelper.loadAllItems(tag, items);
		litTime = tag.getInt("BurnTime");
		cookingProgress = tag.getInt("CookTime");
		cookingTotalTime = tag.getInt("CookTimeTotal");
		litDuration = getBurnDuration(items.get(FUEL_SLOT));
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("BurnTime", litTime);
		tag.putInt("CookTime", cookingProgress);
		tag.putInt("CookTimeTotal", cookingTotalTime);
		ContainerHelper.saveAllItems(tag, items);
	}
	
	protected int getBurnDuration(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		else {
			return ForgeHooks.getBurnTime(stack, ModRecipes.CAKE_OVEN_RECIPE);
		}
	}
	
	@Override
	public void setRecipeUsed(Recipe<?> recipe) {
		if (recipe != null) {
			recipesUsed.addTo(recipe.getId(), 1);
		}
	}
	
	@Override
	public Recipe<?> getRecipeUsed() {
		return null;
	}

	@Override
	public void awardUsedRecipes(Player player) {
	}
	
	public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
		List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position());
		player.awardRecipes(list);
		this.recipesUsed.clear();
	}

	public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
		List<Recipe<?>> list = Lists.newArrayList();

		for (Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				AbstractFurnaceBlockEntity.createExperience(level, pos, entry.getIntValue(), ((CakeOvenRecipe) recipe).getExperience());
			});
		}

		return list;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (!remove && direction != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (direction == Direction.UP) {
				return handlers[0].cast();
			}
			else if (direction == Direction.DOWN) {
				return handlers[1].cast();
			}
			else {
				return handlers[2].cast();
			}
		}
		return super.getCapability(capability, direction);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].invalidate();
		}
	}
	
	@Override
	public void reviveCaps() {
		super.reviveCaps();
		handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
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
	public boolean canPlaceItemThroughFace(int slotIndex, ItemStack stack, Direction direction) {
		return canPlaceItem(slotIndex, stack);
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction direction) {
		return true;
	}
}
