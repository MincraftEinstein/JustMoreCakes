package einstein.jmc.network.packet;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.data.cakeeffect.CakeEffectsManager;
import einstein.jmc.network.Packet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class ClientboundCakeEffectsPacket implements Packet {

    private final Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> effects = new HashMap<>();

    @Override
    public void encode(FriendlyByteBuf buf) {
        Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> cakeEffectsMap = CakeEffectsManager.getCakeEffects();
        buf.writeInt(cakeEffectsMap.size());

        cakeEffectsMap.forEach((holder, combinedEffects) -> {
            buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey((Block) holder));
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

    @Override
    public void decode(FriendlyByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            Block cake = BuiltInRegistries.BLOCK.get(buf.readResourceLocation());
            if (cake instanceof CakeEffectsHolder holder) {
                int combinedEffectsListSize = buf.readInt();
                Map<MobEffect, Pair<Integer, Integer>> combinedEffects = new HashMap<>();

                for (int i1 = 0; i1 < combinedEffectsListSize; i1++) {
                    int duration = buf.readInt();
                    int amplifier = buf.readInt();
                    MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(buf.readResourceLocation());

                    if (effect != null) {
                        combinedEffects.put(effect, Pair.of(duration, amplifier));
                    }
                    else {
                        LOGGER.warn("Received unknown mob effect from server");
                    }
                }

                effects.put(holder, combinedEffects);
            }
            else {
                LOGGER.warn("Received cake effects for unknown cake effect holder from server");
            }
        }
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        CakeEffectsManager.setEffectsOnHolders(effects);
        LOGGER.info("Received cake effects from server");
    }
}
