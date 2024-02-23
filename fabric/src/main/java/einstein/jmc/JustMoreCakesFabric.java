package einstein.jmc;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.client.renderers.blockentities.CakeStandRenderer;
import einstein.jmc.data.FabricCakeEffectsReloadListener;
import einstein.jmc.data.packs.providers.*;
import einstein.jmc.init.*;
import einstein.jmc.platform.FabricNetworkHelper;
import einstein.jmc.platform.services.NetworkHelper;
import einstein.jmc.util.CakeFamily;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.config.ModConfig;

import static einstein.jmc.JustMoreCakes.MOD_ID;
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

        VillagerInteractionRegistries.registerGiftLootTable(ModVillagers.CAKE_BAKER.get(), JustMoreCakes.CAKE_BAKER_GIFT);
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
        putFamilyRenderLayers(ModBlocks.CHORUS_CAKE_FAMILY, RenderType.cutout());
        putFamilyRenderLayers(ModBlocks.CRIMSON_FUNGUS_CAKE_FAMILY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENCASING_ICE.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAKE_STAND.get(), RenderType.cutout());

        BlockEntityRenderers.register(ModBlockEntityTypes.CAKE_STAND.get(), CakeStandRenderer::new);

        FabricNetworkHelper.init(NetworkHelper.Direction.TO_CLIENT);
    }

    private static void putFamilyRenderLayers(CakeFamily family, RenderType type) {
        family.forEach(cake -> BlockRenderLayerMap.INSTANCE.putBlock(cake.get(), type));
    }

    void onServerStarting() {
        ServerLifecycleEvents.SERVER_STARTING.register(JustMoreCakes::onServerStarting);
    }

    void onAddReloadListeners() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricCakeEffectsReloadListener());
    }

    void addVillagerTrades() {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 1, ModVillagers::noviceTrades);
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 2, ModVillagers::apprenticeTrades);
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 3, ModVillagers::journeymanTrades);
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 4, ModVillagers::expertTrades);
        TradeOfferHelper.registerVillagerOffers(ModVillagers.CAKE_BAKER.get(), 5, ModVillagers::masterTrades);
        TradeOfferHelper.registerWanderingTraderOffers(1, ModVillagers::wanderingTraderTrades);
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
