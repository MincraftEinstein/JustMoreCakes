package einstein.jmc.data.effects;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import commonnetwork.api.Dispatcher;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.network.clientbound.ClientboundCakeEffectsPacket;
import einstein.jmc.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class CakeEffectsManager {

    public static final String EFFECTS_DIRECTORY = "jmc/cake_effect";
    private static final List<CakeEffects> RAW_CAKE_EFFECTS = new ArrayList<>();
    private static final List<CakeEffects> CAKE_EFFECTS = new ArrayList<>();

    public static void syncToPlayer(ServerPlayer player) {
        Dispatcher.sendToClient(new ClientboundCakeEffectsPacket(CAKE_EFFECTS), player);
        GameProfile profile = player.getGameProfile();
        LOGGER.info("Sending cake effects to {} ({})", profile.getName(), profile.getId());
    }

    public static void loadCakeEffects() {
        Map<CakeEffectsHolder, Map<Holder<MobEffect>, Pair<Integer, Integer>>> newCakeEffects = new HashMap<>();
        RAW_CAKE_EFFECTS.forEach((cakeEffects) -> {
            CakeEffectsHolder holder = cakeEffects.holder();
            cakeEffects.mobEffects().forEach(effectInstance -> {
                Holder<MobEffect> effect = effectInstance.getEffect();
                int duration = effectInstance.getDuration();
                int amplifier = effectInstance.getAmplifier();

                if (newCakeEffects.containsKey(holder)) {
                    Map<Holder<MobEffect>, Pair<Integer, Integer>> combinedEffects = newCakeEffects.get(holder);
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
                    newCakeEffects.put(holder, new HashMap<>(Map.of(effect, Pair.of(duration, amplifier))));
                }
            });
        });

        RAW_CAKE_EFFECTS.clear();
        clearCakeEffects();

        newCakeEffects.forEach((holder, effects) -> {
            List<MobEffectInstance> instances = new ArrayList<>();

            effects.forEach((mobEffect, pair) ->
                    instances.add(new MobEffectInstance(mobEffect, pair.getFirst(), pair.getSecond())));

            setEffectsOnHolder(new CakeEffects(holder, instances));
        });
    }

    public static void clearCakeEffects() {
        CAKE_EFFECTS.forEach(cakeEffects -> cakeEffects.holder().clear());
        CAKE_EFFECTS.clear();
    }

    public static void setEffectsOnHolder(CakeEffects cakeEffects) {
        cakeEffects.holder().justMoreCakes$setCakeEffects(cakeEffects);
        CAKE_EFFECTS.add(cakeEffects);
    }

    public static void deserializeCakeEffects(ResourceManager manager) {
        Map<ResourceLocation, Resource> locations = manager.listResources(EFFECTS_DIRECTORY, location -> location.getPath().endsWith(".json"));

        locations.forEach((location, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                JsonObject object = GsonHelper.fromJson(Util.GSON, reader, JsonObject.class);

                CakeEffects.CODEC.parse(JsonOps.INSTANCE, object)
                        .resultOrPartial(error -> LOGGER.error("Failed to decode cake effect with json id {} - Error: {}", location, error))
                        .ifPresent(RAW_CAKE_EFFECTS::add);
            }
            catch (Exception exception) {
                LOGGER.error("Error occurred while loading resource json {}", location, exception);
            }
        });

        LOGGER.info("Loaded {} cake effects", RAW_CAKE_EFFECTS.size());
    }
}
