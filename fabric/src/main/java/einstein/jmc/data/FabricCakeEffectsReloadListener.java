package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.effects.CakeEffectsManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class FabricCakeEffectsReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return JustMoreCakes.loc(CakeEffectsManager.EFFECTS_DIRECTORY);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        CakeEffectsManager.deserializeCakeEffects(manager);
    }
}
