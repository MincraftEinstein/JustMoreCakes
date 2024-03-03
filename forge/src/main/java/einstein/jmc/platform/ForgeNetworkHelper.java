package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.network.Packet;
import einstein.jmc.platform.services.NetworkHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgeNetworkHelper implements NetworkHelper {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(JustMoreCakes.loc("main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    private static int id = 0;

    @Override
    public <T extends Packet> void registerPacket(String name, PacketData<T> data) {
        PACKETS.put(name, new PacketHolder(data.packetSupplier().get(), data));
    }

    @Override
    public void toServer(String name) {
        if (PACKETS.containsKey(name)) {
            PacketHolder holder = PACKETS.get(name);
            CHANNEL.send(PacketDistributor.SERVER.noArg(), holder.packet());
        }
        else {
            JustMoreCakes.LOGGER.warn("Failed to find packet named: {}", name);
        }
    }

    @Override
    public void toClient(String name, ServerPlayer player) {
        if (PACKETS.containsKey(name)) {
            PacketHolder holder = PACKETS.get(name);
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), holder.packet());
        }
        else {
            JustMoreCakes.LOGGER.warn("Failed to find packet named: {}", name);
        }
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        PACKETS.forEach((name, holder) -> {
            Packet packet = holder.packet();
            PacketData<?> data = holder.data();
            CHANNEL.messageBuilder((Class<Packet>) packet.getClass(), id++, data.direction() == Direction.TO_CLIENT ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER)
                    .decoder(buf -> {
                        packet.decode(buf);
                        return packet;
                    })
                    .encoder(Packet::encode)
                    .consumerMainThread(ForgeNetworkHelper::handle)
                    .add();
        });
    }

    private static <T extends Packet> void handle(T packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        packet.handle(player);
    }
}
