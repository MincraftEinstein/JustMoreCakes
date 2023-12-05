package einstein.jmc.data.cakeeffect;

import com.google.gson.JsonElement;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class ForgeCakeEffectsReloadListener extends SimpleJsonResourceReloadListener {

    public ForgeCakeEffectsReloadListener() {
        super(Util.GSON, "cake_effects");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        CakeEffectsManager.deserializeCakeEffects(manager);
    }
}
