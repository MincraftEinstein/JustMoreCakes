package einstein.jmc.platform;

import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.platform.services.CommonHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.neoforge.common.NeoForgeEventHandler;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class NeoForgeCommonHooks implements CommonHooks {

    @Override
    public int getBurnTime(ItemStack stack) {
        return stack.getBurnTime(null);
    }

    @Override
    public void fireSmeltEvent(Player player, ItemStack stack) {
        EventHooks.firePlayerSmeltedEvent(player, stack);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuDataProvider provider) {
        player.openMenu(provider, buf -> provider.writeMenuData(player, buf));
    }

    @Override
    public void registerCompostableInternal(ItemLike item, float chance) {}

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
