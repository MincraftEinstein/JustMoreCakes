package einstein.jmc;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public class JustMoreCakesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_MUSHROOM_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROWN_MUSHROOM_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHORUS_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENCASING_ICE.get(), RenderType.translucent());
    }
}