package einstein.jmc.platform;

import einstein.jmc.network.Packet;
import einstein.jmc.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

import static einstein.jmc.JustMoreCakes.loc;

public class FabricNetworkHelper implements NetworkHelper {

    private static final Map<String, PacketHolder> PACKETS = new HashMap<>();

    @Override
    public <T extends Packet> void registerPacket(String name, PacketData<T> data) {
        PACKETS.put(name, new PacketHolder(data.packetSupplier().get(), data));
    }

    @Override
    public void toServer(String name) {
        PacketHolder holder = PACKETS.get(name);
        FriendlyByteBuf buf = PacketByteBufs.create();
        holder.packet().encode(buf);
        ClientPlayNetworking.send(loc(name), buf);
    }

    @Override
    public void toClient(String name, ServerPlayer player) {
        PacketHolder holder = PACKETS.get(name);
        FriendlyByteBuf buf = PacketByteBufs.create();
        holder.packet().encode(buf);
        ServerPlayNetworking.send(player, loc(name), buf);
    }

    public static void init() {
        PACKETS.forEach((name, holder) -> {
            Packet packet = holder.packet();
            PacketData<?> data = holder.data();
            if (data.direction() == Direction.TO_CLIENT) {
                ClientPlayNetworking.registerGlobalReceiver(loc(name), (minecraft, handler, buf, responseSender) -> {
                    packet.decode(buf);
                    minecraft.execute(() -> packet.handle(null));
                });
            }
            else if (data.direction() == Direction.TO_SERVER) {
                ServerPlayNetworking.registerGlobalReceiver(loc(name), (server, player, handler, buf, responseSender) -> {
                    packet.decode(buf);
                    server.execute(() -> packet.handle(player));
                });
            }
        });
    }
}
