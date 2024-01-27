package einstein.jmc;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.client.renderers.blockentities.CakeStandRenderer;
import einstein.jmc.data.FabricCakeEffectsReloadListener;
import einstein.jmc.data.packs.providers.*;
import einstein.jmc.init.*;
import einstein.jmc.platform.FabricNetworkHelper;
import einstein.jmc.platform.services.NetworkHelper;
import einstein.jmc.util.CakeFamily;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.config.ModConfig;

import static einstein.jmc.JustMoreCakes.MOD_ID;
import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;
import static einstein.jmc.util.Util.getVanillaCandleCakes;

public class JustMoreCakesFabric implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint {

    @Override
    public void onInitialize() {
        JustMoreCakes.init();
        FabricNetworkHelper.init(NetworkHelper.Direction.TO_SERVER);
        onServerStarting();
        onAddReloadListeners();
        addVillagerTrades();

        ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.SERVER, ModServerConfigs.SPEC);
        ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, ModCommonConfigs.SPEC);

        VillagerInteractionRegistries.registerGiftLootTable(ModVillagers.CAKE_BAKER.get(), loc("gameplay/hero_of_the_village/cake_baker_gift"));
        JustMoreCakes.commonSetup();
        modifyLootTables();
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        FabricTagProvider.BlockTagProvider blockTags = pack.addProvider(ModBlockTagsProvider::new);
        pack.addProvider((output, registriesFuture) -> new ModItemTagsProvider(output, registriesFuture, blockTags));
        pack.addProvider(ModPOITagsProvider::new);
        pack.addProvider(ModGameEventTagsProvider::new);
        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModCakeEffectsProvider>) ModCakeEffectsProvider::new);
    }

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);

        putFamilyRenderLayers(ModBlocks.RED_MUSHROOM_CAKE_FAMILY, RenderType.cutout());
        putFamilyRenderLayers(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHORUS_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENCASING_ICE.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAKE_STAND.get(), RenderType.cutout());

        BlockEntityRenderers.register(ModBlockEntityTypes.CAKE_STAND.get(), CakeStandRenderer::new);

        FabricNetworkHelper.init(NetworkHelper.Direction.TO_CLIENT);
    }

    private static void putFamilyRenderLayers(CakeFamily family, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(family.getBaseCake().get(), type);
        BlockRenderLayerMap.INSTANCE.putBlock(family.getTwoTieredCake().get(), type);
        BlockRenderLayerMap.INSTANCE.putBlock(family.getThreeTieredCake().get(), type);
    }

    void onServerStarting() {
        ServerLifecycleEvents.SERVER_STARTING.register(JustMoreCakes::onServerStarting);
    }

    void onAddReloadListeners() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricCakeEffectsReloadListener());
    }

    void addVillagerTrades() {

        // Novice (Stone tier)
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 1, trades -> {
            trades.add(new EmeraldsForItems(Items.WHEAT, 20, 1, 16, 2));
            trades.add(new EmeraldsForItems(Items.EGG, 5, 1, 16, 2));
            trades.add(new ItemsForEmeralds(Items.SUGAR, 2, 4, 1));
            trades.add(new ItemsForEmeralds(Items.MILK_BUCKET, 2, 1, 2));
        });

        // Apprentice (Iron tier)
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 2, trades -> {
            trades.add(new ItemsForEmeralds(Blocks.CAKE.asItem(), 1, 1, 10));
            trades.add(new ItemsForEmeralds(Items.COCOA_BEANS, 3, 1, 5));
            trades.add(new ItemsForEmeralds(ModBlocks.CARROT_CAKE_FAMILY.getBaseCake().get(), 1, 1, 10));
        });

        // Journeyman (Gold tier)
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 3, trades -> {
            trades.add(new EmeraldsForItems(Items.COAL, 15, 1, 16, 10));
            trades.add(new EmeraldsForItems(Items.CARROT, 22, 1, 16, 20));
            trades.add(new EmeraldsForItems(Items.SUGAR_CANE, 2, 1, 10));
        });

        // Expert (Emerald tier)
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 4, trades -> {
            trades.add(new EmeraldsForItems(ModItems.CREAM_CHEESE.get(), 1, 6, 30));
            trades.add(new ItemsForEmeralds(ModItems.CUPCAKE.get(), 4, 1, 16, 15));
        });

        // Master (Diamond tier)
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 5, trades -> {
            trades.add(new ItemsForEmeralds(new ItemStack(ModItems.CAKE_SPATULA.get()), 6, 1, 3, 15, 0.2F));
            trades.add(new ItemsForEmeralds(ModBlocks.CREEPER_CAKE_FAMILY.getBaseCake().get().asItem(), 20, 1, 30));
        });

        // Wandering trader
        TradeOfferHelper.registerWanderingTraderOffers(1, trades ->
                trades.add(new ItemsForEmeralds(ModBlocks.SEED_CAKE_FAMILY.getBaseCake().get().asItem(), 2, 1, 12)));
    }

    void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (Blocks.CAKE.getLootTable().equals(id) && source.isBuiltin()) {
                addDropWhenCakeSpatulaPool(tableBuilder, Blocks.CAKE);
            }

            for (Block candleCake : getVanillaCandleCakes()) {
                if (candleCake.getLootTable().equals(id) && source.isBuiltin()) {
                    addDropWhenCakeSpatulaPool(tableBuilder, Blocks.CAKE);
                }
            }
        });
    }
}
