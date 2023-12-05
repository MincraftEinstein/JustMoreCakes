package einstein.jmc.init;

import einstein.jmc.network.packet.ClientboundCakeEffectsPacket;
import einstein.jmc.platform.Services;
import einstein.jmc.platform.services.NetworkHelper;

public class ModPackets {

    public static final String CLIENTBOUND_CAKE_EFFECTS = "clientbound_cake_effects";

    public static void init() {
        Services.NETWORK.registerPacket(CLIENTBOUND_CAKE_EFFECTS,
                new NetworkHelper.PacketData<>(ClientboundCakeEffectsPacket::new, NetworkHelper.Direction.TO_CLIENT));
    }
}
