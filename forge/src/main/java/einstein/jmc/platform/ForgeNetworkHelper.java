package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.network.Packet;
import einstein.jmc.platform.services.NetworkHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

import java.util.HashMap;
import java.util.Map;

public class ForgeNetworkHelper implements NetworkHelper {

    private static final int PROTOCOL_VERSION = 1;
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(JustMoreCakes.loc("main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();
    private static final Map<String, PacketHolder> PACKETS = new HashMap<>();

    @Override
    public <T extends Packet> void registerPacket(String name, PacketData<T> data) {
        PACKETS.put(name, new PacketHolder(data.packetSupplier().get(), data));
    }

    @Override
    public void toServer(String name) {
        PacketHolder holder = PACKETS.get(name);
        CHANNEL.send(holder.packet(), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void toClient(String name, ServerPlayer player) {
        PacketHolder holder = PACKETS.get(name);
        CHANNEL.send(holder.packet(), PacketDistributor.PLAYER.with(player));
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
