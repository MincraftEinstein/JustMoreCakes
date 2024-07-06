package einstein.jmc.data;

import com.google.gson.JsonElement;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class NeoForgeCakeEffectsReloadListener extends SimpleJsonResourceReloadListener {

    public NeoForgeCakeEffectsReloadListener() {
        super(Util.GSON, CakeEffectsManager.EFFECTS_DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        CakeEffectsManager.deserializeCakeEffects(manager);
    }
}
