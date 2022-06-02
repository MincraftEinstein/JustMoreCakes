package einstein.jmc.menu.cakeoven;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CakeOvenFuelSlot extends Slot {

	private CakeOvenMenu menu;
	
	public CakeOvenFuelSlot(CakeOvenMenu menu, Container container, int slotId, int x, int y) {
		super(container, slotId, x, y);
		this.menu = menu;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return menu.isFuel(stack);
	}
}
