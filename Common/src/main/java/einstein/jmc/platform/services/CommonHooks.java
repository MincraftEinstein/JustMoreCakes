package einstein.jmc.platform.services;

import einstein.jmc.util.MenuDataProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface CommonHooks {

    int getBurnTime(ItemStack stack);

    void fireSmeltEvent(Player player, ItemStack stack);

    void openMenu(ServerPlayer player, MenuDataProvider provider);
}
