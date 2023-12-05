package einstein.jmc.network.packet;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.cakeeffect.CakeEffects;
import einstein.jmc.data.cakeeffect.CakeEffectsManager;
import einstein.jmc.network.Packet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientboundCakeEffectsPacket implements Packet {

    private final Map<ResourceLocation, CakeEffects> effects = new HashMap<>();

    @Override
    public void encode(FriendlyByteBuf buf) {
        Map<ResourceLocation, CakeEffects> cakeEffectsMap = CakeEffectsManager.getCakeEffects();
        buf.writeInt(cakeEffectsMap.size());

        cakeEffectsMap.forEach((id, cakeEffects) -> {
            buf.writeResourceLocation(id);
            buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey(cakeEffects.cake()));
            buf.writeInt(cakeEffects.mobEffects().size());

            for (CakeEffects.MobEffectHolder holder : cakeEffects.mobEffects()) {
                buf.writeInt(holder.duration().orElse(0));
                buf.writeInt(holder.amplifier().orElse(0));
                ResourceLocation mobEffectId = BuiltInRegistries.MOB_EFFECT.getKey(holder.effect());
                if (mobEffectId != null) {
                    buf.writeResourceLocation(mobEffectId);
                }
                else {
                    throw new NullPointerException("Attempted to send unknown mob effect to client");
                }
            }
        });
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            Block cake = BuiltInRegistries.BLOCK.get(buf.readResourceLocation());
            int mobEffectListSize = buf.readInt();
            List<CakeEffects.MobEffectHolder> holders = new ArrayList<>();

            for (int i1 = 0; i1 < mobEffectListSize; i1++) {
                int duration = buf.readInt();
                int amplifier = buf.readInt();
                MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(buf.readResourceLocation());

                if (mobEffect != null) {
                    holders.add(new CakeEffects.MobEffectHolder(mobEffect, duration, amplifier));
                }
                else {
                    throw new NullPointerException("Received unknown mob effect from server");
                }
            }

            effects.put(id, new CakeEffects(cake, holders));
        }
    }

    @Override
    public void handle(@Nullable ServerPlayer player) {
        CakeEffectsManager.clearAndSet(effects);
        CakeEffectsManager.loadCakeEffects();
        JustMoreCakes.LOGGER.info("Received cake effects from server");
    }
}
