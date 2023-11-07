package einstein.jmc;

import einstein.jmc.advancement.criterian.CakeEatenTrigger;
import einstein.jmc.init.*;
import einstein.jmc.util.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static einstein.jmc.init.ModBlocks.SUPPORTED_CANDLES;
import static einstein.jmc.init.ModCommonConfigs.CAKE_BAKERY_GENERATION_WEIGHT;
import static einstein.jmc.util.Util.registerVillageBuilding;

public class JustMoreCakes {

    public static final String MOD_ID = "jmc";
    public static final String MOD_NAME = "Just More Cakes!";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final CakeEatenTrigger CAKE_EATEN_TRIGGER = CriteriaTriggers.register(MOD_ID + ":cake_eaten", new CakeEatenTrigger());

    public static void init() {
        ModItems.init();
        ModBlocks.init();
        ModBlockEntityTypes.init();
        ModMenuTypes.init();
        ModRecipes.init();
        ModVillagers.init();
        ModPotions.init();
        ModCreativeModeTabs.init();
        ModGameEvents.init();
    }

    public static void commonSetup() {
        Items.CAKE.maxStackSize = 64;
        ModPotions.registerPotionRecipes();
    }

    public static void onServerStarting(MinecraftServer server) {
        registerVillageBuilding(server, "plains", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "plains", "bakery_2", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "desert", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "savanna", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "snowy", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "snowy", "bakery_2", CAKE_BAKERY_GENERATION_WEIGHT.get());
        registerVillageBuilding(server, "taiga", "bakery_1", CAKE_BAKERY_GENERATION_WEIGHT.get());

        if (ModCommonConfigs.DISABLE_DEFAULT_CAKE_RECIPE.get()) {
            Util.removeRecipe(server.getRecipeManager(), RecipeType.CRAFTING, mcLoc("cake"));
        }
    }

    public static void addSupportedCandles() {
        SUPPORTED_CANDLES.put(Blocks.CANDLE, mcLoc(""));
        SUPPORTED_CANDLES.put(Blocks.WHITE_CANDLE, mcLoc("white_"));
        SUPPORTED_CANDLES.put(Blocks.ORANGE_CANDLE, mcLoc("orange_"));
        SUPPORTED_CANDLES.put(Blocks.MAGENTA_CANDLE, mcLoc("magenta_"));
        SUPPORTED_CANDLES.put(Blocks.LIGHT_BLUE_CANDLE, mcLoc("light_blue_"));
        SUPPORTED_CANDLES.put(Blocks.YELLOW_CANDLE, mcLoc("yellow_"));
        SUPPORTED_CANDLES.put(Blocks.LIME_CANDLE, mcLoc("lime_"));
        SUPPORTED_CANDLES.put(Blocks.PINK_CANDLE, mcLoc("pink_"));
        SUPPORTED_CANDLES.put(Blocks.GRAY_CANDLE, mcLoc("gray_"));
        SUPPORTED_CANDLES.put(Blocks.LIGHT_GRAY_CANDLE, mcLoc("light_gray_"));
        SUPPORTED_CANDLES.put(Blocks.CYAN_CANDLE, mcLoc("cyan_"));
        SUPPORTED_CANDLES.put(Blocks.PURPLE_CANDLE, mcLoc("purple_"));
        SUPPORTED_CANDLES.put(Blocks.BLUE_CANDLE, mcLoc("blue_"));
        SUPPORTED_CANDLES.put(Blocks.BROWN_CANDLE, mcLoc("brown_"));
        SUPPORTED_CANDLES.put(Blocks.GREEN_CANDLE, mcLoc("green_"));
        SUPPORTED_CANDLES.put(Blocks.RED_CANDLE, mcLoc("red_"));
        SUPPORTED_CANDLES.put(Blocks.BLACK_CANDLE, mcLoc("black_"));
    }

    public static ResourceLocation loc(String string) {
        return new ResourceLocation(MOD_ID, string);
    }

    public static ResourceLocation mcLoc(String string) {
        return new ResourceLocation("minecraft", string);
    }
}