package einstein.jmc.menu.cakeoven;

import einstein.jmc.blockentity.CakeOvenBlockEntity;
import einstein.jmc.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CakeOvenResultSlot extends Slot {

    private final Player player;
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
            removeCount += Math.min(count, getItem().getCount());
        }
        return super.remove(count);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack /* I think this is an ItemStack of the items in the slot */, int count/* I think this is the number of items in the slot */) {
        removeCount += count;
        checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack/* I think this is an ItemStack of the items in the slot */) {
        stack.onCraftedBy(player.level, player, removeCount);
        if (player instanceof ServerPlayer serverPlayer && container instanceof CakeOvenBlockEntity blockEntity) {
            blockEntity.awardUsedRecipesAndPopExperience(serverPlayer);
        }
        removeCount = 0;
        Services.HOOKS.fireSmeltEvent(player, stack);
    }
}
