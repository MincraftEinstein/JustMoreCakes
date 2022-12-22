package einstein.jmc.util;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class FabricMenuDataProvider implements ExtendedScreenHandlerFactory {

    private final MenuDataProvider provider;

    public FabricMenuDataProvider(MenuDataProvider provider) {
        this.provider = provider;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        provider.writeMenuData(player, buf);
    }

    @Override
    public Component getDisplayName() {
        return provider.getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return provider.createMenu(id, inventory, player);
    }
}
