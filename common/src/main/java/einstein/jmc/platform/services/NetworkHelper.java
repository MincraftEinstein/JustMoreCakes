package einstein.jmc.platform.services;

import einstein.jmc.network.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface NetworkHelper {

    Map<String, PacketHolder> PACKETS = new HashMap<>();

    <T extends Packet> void registerPacket(String name, PacketData<T> data);

    void toServer(String name);

    void toClient(String name, ServerPlayer player);

    enum Direction {
        TO_CLIENT,
        TO_SERVER
    }

    record PacketData<T extends Packet>(Supplier<T> packetSupplier, Direction direction) {

    }

    record PacketHolder(Packet packet, PacketData<?> data) {

    }
}
