package einstein.jmc.client;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
    	ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_MUSHROOM_CAKE.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(ModBlocks.BROWN_MUSHROOM_CAKE.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHORUS_CAKE.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENCASING_ICE.get(), RenderType.translucent());
    	
    	MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);
    }
}
