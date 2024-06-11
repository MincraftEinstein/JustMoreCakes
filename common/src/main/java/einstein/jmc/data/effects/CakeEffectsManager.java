package einstein.jmc.data.effects;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import commonnetwork.api.Dispatcher;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.data.SerializableMobEffectInstance;
import einstein.jmc.network.clientbound.ClientboundCakeEffectsPacket;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;

import java.io.Reader;
import java.util.*;

import static einstein.jmc.JustMoreCakes.LOGGER;

public class CakeEffectsManager {

    public static final String EFFECTS_DIRECTORY = "jmc/cake_effects";
    private static final List<CakeEffects> RAW_CAKE_EFFECTS = new ArrayList<>();
    private static final Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> CAKE_EFFECTS = new HashMap<>();

    public static void syncToPlayer(ServerPlayer player) {
        Dispatcher.sendToClient(new ClientboundCakeEffectsPacket(CAKE_EFFECTS), player);
        GameProfile profile = player.getGameProfile();
        LOGGER.info("Sending cake effects to {} ({})", profile.getName(), profile.getId());
    }

    public static void loadCakeEffects() {
        Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> newCakeEffects = new HashMap<>();
        RAW_CAKE_EFFECTS.forEach((cakeEffects) -> {
            CakeEffectsHolder holder = cakeEffects.holder();
            cakeEffects.mobEffects().forEach(effectHolder -> {
                MobEffect effect = effectHolder.effect();
                int duration = effectHolder.duration().orElse(0);
                int amplifier = effectHolder.amplifier().orElse(0);
                if (newCakeEffects.containsKey(holder)) {
                    Map<MobEffect, Pair<Integer, Integer>> combinedEffects = newCakeEffects.get(holder);
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
        setEffectsOnHolders(newCakeEffects);
    }

    public static void setEffectsOnHolders(Map<CakeEffectsHolder, Map<MobEffect, Pair<Integer, Integer>>> newCakeEffects) {
        CAKE_EFFECTS.forEach((holder, mobEffectPairMap) -> holder.clear());
        CAKE_EFFECTS.clear();

        newCakeEffects.forEach((holder, effects) -> {
            CAKE_EFFECTS.put(holder, effects);
            List<SerializableMobEffectInstance> serializableMobEffectInstances = new ArrayList<>();

            effects.forEach((mobEffect, pair) -> {
                serializableMobEffectInstances.add(new SerializableMobEffectInstance(mobEffect, pair.getFirst(), pair.getSecond()));
            });

            holder.justMoreCakes$setCakeEffects(new CakeEffects(holder, serializableMobEffectInstances));
        });
    }

    public static void deserializeCakeEffects(ResourceManager manager) {
        Map<ResourceLocation, Resource> locations = manager.listResources(EFFECTS_DIRECTORY, location -> location.getPath().endsWith(".json"));

        locations.forEach((location, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                JsonObject object = GsonHelper.fromJson(Util.GSON, reader, JsonObject.class);

                getCodec(object).ifPresentOrElse(codec ->
                        codec.parse(JsonOps.INSTANCE, object)
                                .resultOrPartial(error -> decodingError(location, error))
                                .ifPresent(RAW_CAKE_EFFECTS::add),
                        () -> decodingError(location, "Unknown type for cake effects. Must be either 'block' or 'family'"));
            }
            catch (Exception exception) {
                LOGGER.error("Error occurred while loading resource json {}", location, exception);
            }
        });

        LOGGER.info("Loaded {} cake effects", RAW_CAKE_EFFECTS.size());
    }

    private static void decodingError(ResourceLocation location, String error) {
        LOGGER.error("Failed to decode cake effect with json id {} - Error: {}", location, error);
    }

    private static Optional<Codec<CakeEffects>> getCodec(JsonObject object) {
        if (object.has("block")) {
            return Optional.of(CakeEffects.BLOCK_CODEC);
        }
        else if (object.has("family")) {
            return Optional.of(CakeEffects.FAMILY_CODEC);
        }
        return Optional.empty();
    }
}
