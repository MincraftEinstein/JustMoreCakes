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
    public ItemStack remove(int amount) {
        if (hasItem()) {
            removeCount += Math.min(amount, getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        checkTakeAchievements(stack);

        if (container instanceof CakeOvenBlockEntity cakeOvenBlockEntity) {
            for (int i = 0; i < cakeOvenBlockEntity.getRemainingItems().size(); i++) {
                ItemStack remainingItem = cakeOvenBlockEntity.getRemainingItems().get(i);
                if (!remainingItem.isEmpty()) {
                    if (!player.getInventory().add(remainingItem)) {
                        player.drop(remainingItem, false);
                    }
                    cakeOvenBlockEntity.getRemainingItems().set(i, ItemStack.EMPTY);
                }
            }
        }

        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack resultStack, int resultCount) {
        removeCount += resultCount;
        checkTakeAchievements(resultStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack resultStack) {
        resultStack.onCraftedBy(player.level(), player, removeCount);
        if (player instanceof ServerPlayer serverPlayer && container instanceof CakeOvenBlockEntity blockEntity) {
            blockEntity.awardUsedRecipesAndPopExperience(serverPlayer);
        }
        removeCount = 0;
        Services.HOOKS.fireSmeltEvent(player, resultStack);
    }
}
