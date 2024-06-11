package einstein.jmc.init;

import commonnetwork.api.Network;
import einstein.jmc.network.clientbound.ClientboundCakeEffectsPacket;

public class ModPackets {

    public static void init() {
        Network.registerPacket(ClientboundCakeEffectsPacket.CHANNEL, ClientboundCakeEffectsPacket.class, ClientboundCakeEffectsPacket::encode, ClientboundCakeEffectsPacket::decode, ClientboundCakeEffectsPacket::handle);
    }
}
