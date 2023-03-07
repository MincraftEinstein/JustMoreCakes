package einstein.jmc.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CakeEffectsManager extends SimpleJsonResourceReloadListener {

    private Map<ResourceLocation, CakeEffects> registeredCakeEffects = ImmutableMap.of();

    public CakeEffectsManager() {
        super(Util.GSON, "cake_effects");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        registeredCakeEffects = Util.deserializeCakeEffects(manager);
    }

    public Map<ResourceLocation, CakeEffects> getRegisteredCakeEffects() {
        return registeredCakeEffects;
    }
}
