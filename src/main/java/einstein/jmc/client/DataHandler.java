package einstein.jmc.client;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD, value = { Dist.CLIENT })
public class DataHandler
{
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.RED_MUSHROOM_CAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BROWN_MUSHROOM_CAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.CHORUS_CAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.CRIMSON_FUNGUS_CAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ENCASING_ICE, RenderType.getTranslucent());
    }
}
