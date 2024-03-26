package einstein.jmc.init;

import einstein.jmc.item.CakeSpatulaItem;
import einstein.jmc.item.CupcakeItem;
import einstein.jmc.platform.Services;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import java.util.function.Supplier;

public class ModItems {

    public static final Supplier<Item> CREAM_CHEESE = Services.REGISTRY.registerItem("cream_cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()).stacksTo(16)));
    public static final Supplier<Item> CUPCAKE = Services.REGISTRY.registerItem("cupcake", () -> new CupcakeItem(ModBlocks.CUPCAKE_VARIANT.getCake().get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build())));
    public static final Supplier<Item> CAKE_SPATULA = Services.REGISTRY.registerItem("cake_spatula", () -> new CakeSpatulaItem(new Item.Properties().stacksTo(1).durability(64)));
    public static final Supplier<Item> WHISK = Services.REGISTRY.registerItem("whisk", () -> new Item(new Item.Properties().stacksTo(1).durability(100)));
    public static final Supplier<Item> CAKE_DOUGH = Services.REGISTRY.registerItem("cake_dough", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).build())));
    public static final Supplier<Item> CUPCAKE_DOUGH = Services.REGISTRY.registerItem("cupcake_dough", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.4F).build())));

    public static void init() {
    }
}
