package einstein.jmc.data.cakeeffect;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.init.ModPackets;
import einstein.jmc.platform.Services;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class CakeEffectsManager {

    private static final Map<ResourceLocation, CakeEffects> RAW_CAKE_EFFECTS = new HashMap<>();
    private static final Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> CAKE_EFFECTS = new HashMap<>();

    public static void syncToPlayer(ServerPlayer player) {
        Services.NETWORK.toClient(ModPackets.CLIENTBOUND_CAKE_EFFECTS, player);
    }

    public static void loadCakeEffects() {
        CAKE_EFFECTS.clear();
        RAW_CAKE_EFFECTS.forEach((location, cakeEffects) -> {
            if (cakeEffects.cake() instanceof CakeEffectsHolder holder) {
                cakeEffects.mobEffects().forEach(effectHolder -> {
                    MobEffect effect = effectHolder.effect();
                    int duration = effectHolder.duration().orElse(0);
                    int amplifier = effectHolder.amplifier().orElse(0);
                    if (CAKE_EFFECTS.containsKey(holder)) {
                        Map<MobEffect, Pair<Integer, Integer>> combinedEffects = CAKE_EFFECTS.get(holder);
                        if (combinedEffects.containsKey(effect)) {
                            Pair<Integer, Integer> pair = combinedEffects.get(effect);
                            int currentDuration = pair.getSecond();

                            combinedEffects.put(effect, Pair.of(
                                    duration == -1 || currentDuration == -1 ? -1 : Math.max(duration, currentDuration),
                                    Math.max(amplifier, pair.getSecond())
                            ));
                        }
                        else {
                            combinedEffects.put(effect, Pair.of(duration, amplifier));
                        }
                    }
                    else {
                        CAKE_EFFECTS.put(holder, new HashMap<>(Map.of(effect, Pair.of(duration, amplifier))));
                    }
                });
            }
            else {
                LOGGER.error("Failed to load cake effect for block {} as it is not valid cake effect holder", cakeEffects.cake());
            }
        });

        RAW_CAKE_EFFECTS.clear();
        setEffectsOnHolders(CAKE_EFFECTS);
    }

    public static void setEffectsOnHolders(Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> cakeEffects) {
        cakeEffects.forEach((holder, effects) -> {
            List<CakeEffects.MobEffectHolder> mobEffectHolders = new ArrayList<>();

            effects.forEach((mobEffect, pair) -> {
                mobEffectHolders.add(new CakeEffects.MobEffectHolder(mobEffect, pair.getFirst(), pair.getSecond()));
            });

            holder.setCakeEffects(new CakeEffects((Block) holder, mobEffectHolders));
        });
    }

    public static void deserializeCakeEffects(ResourceManager manager) {
        ImmutableMap.Builder<ResourceLocation, CakeEffects> builder = ImmutableMap.builder();
        Map<ResourceLocation, Resource> locations = manager.listResources("cake_effects", location -> location.getPath().endsWith(".json"));

        locations.forEach((location, resource) -> {
            try (InputStream stream = resource.open();
                 Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                JsonObject object = GsonHelper.fromJson(Util.GSON, reader, JsonObject.class);

                CakeEffects.CODEC.parse(JsonOps.INSTANCE, object)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode cake effect with json id {} - Error: {}", location, error))
                        .ifPresent(entry -> builder.put(location, entry));
            }
            catch (Exception exception) {
                LOGGER.error("Error occurred while loading resource json " + location.toString(), exception);
            }
        });

        RAW_CAKE_EFFECTS.putAll(builder.buildOrThrow());
        LOGGER.info("Loaded {} cake effects", RAW_CAKE_EFFECTS.size());
    }

    public static Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> getCakeEffects() {
        return CAKE_EFFECTS;
    }
}
