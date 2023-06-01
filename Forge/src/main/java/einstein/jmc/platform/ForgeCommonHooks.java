package einstein.jmc.platform;

import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.platform.services.CommonHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class ForgeCommonHooks implements CommonHooks {

    @Override
    public int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    @Override
    public void fireSmeltEvent(Player player, ItemStack stack) {
        ForgeEventFactory.firePlayerSmeltedEvent(player, stack);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuDataProvider provider) {
        NetworkHooks.openScreen(player, provider, buf -> provider.writeMenuData(player, buf));
    }
}
