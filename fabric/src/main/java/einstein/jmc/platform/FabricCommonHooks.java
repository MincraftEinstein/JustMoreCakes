package einstein.jmc.platform;

import einstein.jmc.JustMoreCakesFabric;
import einstein.jmc.menu.FabricMenuDataProvider;
import einstein.jmc.menu.MenuDataProvider;
import einstein.jmc.platform.services.CommonHooks;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class FabricCommonHooks implements CommonHooks {

    @Override
    public int getBurnTime(ItemStack stack) {
        Integer time = FuelRegistry.INSTANCE.get(stack.getItem());
        return time == null ? 0 : time;
    }

    // Left empty because I don't think Fabric has anything for this
    @Override
    public void fireSmeltEvent(Player player, ItemStack stack) {
    }

    @Override
    public void openMenu(ServerPlayer player, MenuDataProvider provider) {
        player.openMenu(new FabricMenuDataProvider(provider));
    }

    @Override
    public void registerCompostableInternal(ItemLike item, float chance) {
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return JustMoreCakesFabric.getCurrentServer();
    }
}
