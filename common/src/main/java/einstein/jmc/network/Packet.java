package einstein.jmc.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public interface Packet {

    void encode(FriendlyByteBuf buf);

    void decode(FriendlyByteBuf buf);

    void handle(@Nullable ServerPlayer player);
}
