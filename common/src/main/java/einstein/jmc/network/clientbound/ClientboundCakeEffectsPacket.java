package einstein.jmc.network.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.data.effects.CakeEffectsManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class ClientboundCakeEffectsPacket {

    public static final ResourceLocation CHANNEL = JustMoreCakes.loc("cake_effects");

    private final List<CakeEffects> cakeEffects;

    public ClientboundCakeEffectsPacket(List<CakeEffects> cakeEffects) {
        this.cakeEffects = cakeEffects;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cakeEffects.size());
        cakeEffects.forEach(cakeEffects -> CakeEffects.STREAM_CODEC.encode(buf, cakeEffects));
    }

    public static ClientboundCakeEffectsPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();

        List<CakeEffects> cakeEffects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cakeEffects.add(CakeEffects.STREAM_CODEC.decode(buf));
        }
        return new ClientboundCakeEffectsPacket(cakeEffects);
    }

    public static void handle(PacketContext<ClientboundCakeEffectsPacket> context) {
        if (context.side().equals(Side.CLIENT)) {
            CakeEffectsManager.clearCakeEffects();
            context.message().cakeEffects.forEach(CakeEffectsManager::setEffectsOnHolder);
            LOGGER.info("Received cake effects from server");
        }
    }
}
