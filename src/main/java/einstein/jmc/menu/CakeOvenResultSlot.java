package einstein.jmc.menu;

import einstein.jmc.blockentity.CakeOvenBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

public class CakeOvenResultSlot extends Slot {

	private Player player;
	private int removeCount;
	
	public CakeOvenResultSlot(Player player, Container container, int slotId, int x, int y) {
		super(container, slotId, x, y);
		this.player = player;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack remove(int count /* I think this is the number of items in the slot */) {
		if (hasItem()) {
			removeCount += Math.min(count, this.getItem().getCount());
		}
		return super.remove(count);
	}
	
	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(player, stack);
	}
	
	@Override
	protected void onQuickCraft(ItemStack stack /* I think this is an ItemStack of the items in the slot */, int count/* I think this is the number of items in the slot */) {
		removeCount += count;
		this.checkTakeAchievements(stack);
	}
	
	@Override
	protected void checkTakeAchievements(ItemStack stack/* I think this is an ItemStack of the items in the slot */) {
		stack.onCraftedBy(player.level, player, removeCount);
		if (player instanceof ServerPlayer && container instanceof CakeOvenBlockEntity) {
			((CakeOvenBlockEntity)container).awardUsedRecipesAndPopExperience((ServerPlayer)player);
		}
		removeCount = 0;
		ForgeEventFactory.firePlayerSmeltedEvent(player, stack);
	}
}
