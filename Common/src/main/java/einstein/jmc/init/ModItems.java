package einstein.jmc.init;

import einstein.jmc.platform.Services;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import java.util.function.Supplier;

public class ModItems {

    public static final Supplier<Item> CREAM_CHEESE = Services.REGISTRY.registerItem("cream_cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()).stacksTo(16)));
    public static final Supplier<Item> CUPCAKE = Services.REGISTRY.registerItem("cupcake", () -> new ItemNameBlockItem(ModBlocks.CUPCAKE.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0).build())));

    public static void init() {
    }
}
