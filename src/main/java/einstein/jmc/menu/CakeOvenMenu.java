package einstein.jmc.menu;

import einstein.jmc.init.ModMenuTypes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class CakeOvenMenu extends AbstractContainerMenu implements CakeOvenConstants {

	private Container container;
	private ContainerData data;
	private RecipeType<? extends CakeOvenRecipe> recipeType;
	
	public CakeOvenMenu(int id, Inventory inventory) {
		this(id, inventory, null, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}
	
	public CakeOvenMenu(int id, Inventory inventory, RecipeType<? extends CakeOvenRecipe> recipeType, Container container, ContainerData data) {
		super(ModMenuTypes.CAKE_OVEN, id);
		this.recipeType = recipeType;
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.container = container;
		this.data = data;
								 // 	 id,		   x,   y
		addSlot(new Slot(container, INGREDIENT_SLOT_1, 52, 26));
		addSlot(new Slot(container, INGREDIENT_SLOT_2, 70, 26));
		addSlot(new Slot(container, INGREDIENT_SLOT_3, 52, 44));
		addSlot(new Slot(container, INGREDIENT_SLOT_4, 70, 44));
		addSlot(new CakeOvenFuelSlot(this, container, FUEL_SLOT, 19, 44));
		addSlot(new CakeOvenResultSlot(inventory.player, container, RESULT_SLOT, 130, 35));
		
		// Adds the player's inventory slots
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		// Adds the player's hotbar slots
		for (int k = 0; k < 9; ++k) {
			addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}
		
		addDataSlots(data);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return container.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY; // Default stack to return
		Slot slot = slots.get(slotIndex); // The slot the stack is moving from
		
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			
			int containerSize = SLOT_COUNT;
			int inventoryEnd = containerSize + 27;	// 27 being the number of slots for the player inventory
			int hotbarEnd = inventoryEnd + 9; // 9 being the number of slots in the player hotbar (also is the total number of slots in the GUI)
			
			if (slotIndex == RESULT_SLOT) {	// For the result slot
				if (!moveItemStackTo(stack1, containerSize, hotbarEnd, true)) { // I think the boolean is weather this slot is the last slot
					return ItemStack.EMPTY;
				}
				
				slot.onQuickCraft(stack1, stack);
			}
			else if (slotIndex >= containerSize) { // Only the player inventory slots
				if (isFuel(stack1)) { // For the fuel slot
					if (!moveItemStackTo(stack1, FUEL_SLOT, containerSize, false)) {
						return ItemStack.EMPTY;
					}
				}
				else // For the ingredient slots
					if (!moveItemStackTo(stack1, 0, FUEL_SLOT, false)) { // All slots from 0 to fuel (fuel slot is excluded)
						return ItemStack.EMPTY;
					}
				else if (slotIndex >= containerSize && slotIndex < inventoryEnd) {
					if (!moveItemStackTo(stack1, inventoryEnd, hotbarEnd, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (slotIndex >= inventoryEnd && slotIndex < hotbarEnd && !moveItemStackTo(stack1, containerSize, inventoryEnd, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!moveItemStackTo(stack1, containerSize, hotbarEnd, false)) {
				return ItemStack.EMPTY;
			}
			
			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}
			else {
				slot.setChanged();
			}
			
			if (stack1.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, stack1);
		}
		return stack;
	}
	
	protected boolean isFuel(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack, this.recipeType) > 0;
	}
	
	public int getBurnProgress() {
		int i = data.get(2);
		int i2 = data.get(3);
		return i2 != 0 && i != 0 ? i * 24 / i2 : 0;
	}
	
	public int getLitProgress() {
		int i = data.get(1);
		if (i == 0) {
			i = 200;
		}
		return data.get(0) * 13 / i;
	}
	
	public boolean isLit() {
		return data.get(0) > 0;
	}
}
