package einstein.jmc.network.clientbound;

import com.mojang.datafixers.util.Pair;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.util.CakeFamily;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class ClientboundCakeEffectsPacket {

    public static final ResourceLocation CHANNEL = JustMoreCakes.loc("cake_effects");
    private static final int BLOCK = 0;
    private static final int FAMILY = 1;

    private final Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> effects;

    public ClientboundCakeEffectsPacket(Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> effects) {
        this.effects = effects;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(effects.size());

        effects.forEach((holder, combinedEffects) -> {
            int type;
            ResourceLocation key;

            if (holder instanceof Block block) {
                type = BLOCK;
                key = BuiltInRegistries.BLOCK.getKey(block);
            }
            else if (holder instanceof CakeFamily family) {
                type = FAMILY;
                key = family.getRegistryKey();
            }
            else {
                throw new NullPointerException("Attempted to send cake effects of an unknown type: " + holder);
            }

            buf.writeByte(type);
            buf.writeResourceLocation(key);
            buf.writeInt(combinedEffects.size());

            combinedEffects.forEach((effect, pair) -> {
                buf.writeInt(pair.getFirst());
                buf.writeInt(pair.getSecond());
                ResourceLocation mobEffectId = BuiltInRegistries.MOB_EFFECT.getKey(effect);
                if (mobEffectId != null) {
                    buf.writeResourceLocation(mobEffectId);
                }
                else {
                    throw new NullPointerException("Attempted to send unknown mob effect to client");
                }
            });
        });
    }

    public static ClientboundCakeEffectsPacket decode(FriendlyByteBuf buf) {
        Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> effects = new HashMap<>();
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            int type = buf.readByte();
            ResourceLocation key = buf.readResourceLocation();
            CakeEffectsHolder holder;

            if (type == BLOCK) {
                holder = (CakeEffectsHolder) BuiltInRegistries.BLOCK.get(key);
            }
            else if (type == FAMILY) {
                holder = CakeFamily.REGISTERED_CAKE_FAMILIES.get(key);
            }
            else {
                LOGGER.warn("Received cake effects for unknown type: {}", type);
                continue;
            }

            int combinedEffectsListSize = buf.readInt();
            Map<MobEffect, Pair<Integer, Integer>> combinedEffects = new HashMap<>();

            for (int i1 = 0; i1 < combinedEffectsListSize; i1++) {
                int duration = buf.readInt();
                int amplifier = buf.readInt();
                ResourceLocation effectId = buf.readResourceLocation();
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(effectId);

                if (effect != null) {
                    combinedEffects.put(effect, Pair.of(duration, amplifier));
                }
                else {
                    LOGGER.warn("Received cake effects with unknown mob effect: {}", effectId);
                }
            }

            effects.put(holder, combinedEffects);
        }
        return new ClientboundCakeEffectsPacket(effects);
    }

    public static void handle(PacketContext<ClientboundCakeEffectsPacket> context) {
        if (context.side().equals(Side.CLIENT)) {
            CakeEffectsManager.setEffectsOnHolders(context.message().effects);
            LOGGER.info("Received cake effects from server");
        }
    }
}
