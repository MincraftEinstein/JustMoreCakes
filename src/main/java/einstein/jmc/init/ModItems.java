package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModItems {

	public static final Item CHEESE = RegistryHandler.registerItem(JustMoreCakes.MODID, "cheese", new Item(new Item.Properties().tab(JustMoreCakes.JMC_TAB).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()).stacksTo(16)));
	public static final Item CUPCAKE = RegistryHandler.registerItem(JustMoreCakes.MODID, "cupcake", new ItemNameBlockItem(ModBlocks.CUPCAKE, new Item.Properties().tab(JustMoreCakes.JMC_TAB).food(new FoodProperties.Builder().nutrition(2).saturationMod(0).build())));
	
}
