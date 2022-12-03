package einstein.jmc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CakeEffectsManager extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().create();
    private Map<ResourceLocation, CakeEffects> registeredCakeEffects = ImmutableMap.of();

    public CakeEffectsManager() {
        super(GSON, "cake_effects");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        ImmutableMap.Builder<ResourceLocation, CakeEffects> builder = ImmutableMap.builder();

        resources.forEach((location, element) -> {
            try {
                CakeBuilder.BUILDER_BY_CAKE.forEach((block, cakeBuilder) -> {
                    if (cakeBuilder.getCakeName().equals(location.getPath())) {
                        JsonObject object = GsonHelper.convertToJsonObject(element, "cake effect");
                        CakeEffects.CODEC.parse(JsonOps.INSTANCE, object)
                                .resultOrPartial(error -> JustMoreCakes.LOGGER.error("Failed to decode cake effect with json id {} - Error: {}", location, error))
                                .ifPresent(entry -> {
                                    builder.put(location, entry);
                                });
                    }
                });
            }
            catch (Exception exception) {
                JustMoreCakes.LOGGER.error("Error occurred while loading resource json " + location.toString(), exception);
            }
        });

        registeredCakeEffects = builder.build();
        JustMoreCakes.LOGGER.info("Loaded {} cake effects", registeredCakeEffects.size());
    }

    public Map<ResourceLocation, CakeEffects> getRegisteredCakeEffects() {
        return registeredCakeEffects;
    }
}
