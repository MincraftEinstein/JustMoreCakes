package einstein.jmc.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface MenuDataProvider extends MenuProvider {

    void writeMenuData(ServerPlayer player, FriendlyByteBuf buf);
}
