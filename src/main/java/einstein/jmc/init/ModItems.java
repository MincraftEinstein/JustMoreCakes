package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JustMoreCakes.MOD_ID);
	
	public static final RegistryObject<Item> CREAM_CHEESE = ITEMS.register("cream_cheese", () -> new Item(new Item.Properties().tab(JustMoreCakes.JMC_TAB).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()).stacksTo(16)));
	public static final RegistryObject<Item> CUPCAKE = ITEMS.register("cupcake", () -> new ItemNameBlockItem(ModBlocks.CUPCAKE.get(), new Item.Properties().tab(JustMoreCakes.JMC_TAB).food(new FoodProperties.Builder().nutrition(2).saturationMod(0).build())));
}
