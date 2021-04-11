package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModItems {

	public static final Item CHEESE = RegistryHandler.registerItem(JustMoreCakes.MODID, "cheese", new Item(new Item.Properties().group(JustMoreCakes.JMC_GROUP).food(new Food.Builder().hunger(5).saturation(0.6F).build()).maxStackSize(16)));
	public static final Item CUPCAKE = RegistryHandler.registerItem(JustMoreCakes.MODID, "cupcake", new BlockNamedItem(ModBlocks.CUPCAKE, new Item.Properties().group(JustMoreCakes.JMC_GROUP).food(new Food.Builder().hunger(2).saturation(0).build())));
	
}
