package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.network.Packet;
import einstein.jmc.platform.services.NetworkHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

public class ForgeNetworkHelper implements NetworkHelper {

    private static final int PROTOCOL_VERSION = 1;
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(JustMoreCakes.loc("main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();

    @Override
    public <T extends Packet> void registerPacket(String name, PacketData<T> data) {
        PACKETS.put(name, new PacketHolder(data.packetSupplier().get(), data));
    }

    @Override
    public void toServer(String name) {
        if (PACKETS.containsKey(name)) {
            PacketHolder holder = PACKETS.get(name);
            CHANNEL.send(holder.packet(), PacketDistributor.SERVER.noArg());
        }
        else {
            JustMoreCakes.LOGGER.warn("Failed to find packet named: {}", name);
        }
    }

    @Override
    public void toClient(String name, ServerPlayer player) {
        if (PACKETS.containsKey(name)) {
            PacketHolder holder = PACKETS.get(name);
            CHANNEL.send(holder.packet(), PacketDistributor.PLAYER.with(player));
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
            CHANNEL.messageBuilder((Class<Packet>) packet.getClass(), data.direction() == Direction.TO_CLIENT ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER)
                    .decoder(buf -> {
                        packet.decode(buf);
                        return packet;
                    })
                    .encoder(Packet::encode)
                    .consumerMainThread(ForgeNetworkHelper::handle)
                    .add();
        });
    }

    private static <T extends Packet> void handle(T packet, CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        packet.handle(player);
    }
}
