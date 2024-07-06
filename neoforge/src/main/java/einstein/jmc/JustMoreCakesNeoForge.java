package einstein.jmc;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.client.renderers.blockentities.CakeStandRenderer;
import einstein.jmc.client.renderers.blockentities.CeramicBowlRenderer;
import einstein.jmc.data.BowlContents;
import einstein.jmc.data.NeoForgeCakeEffectsReloadListener;
import einstein.jmc.data.packs.providers.*;
import einstein.jmc.init.*;
import einstein.jmc.platform.NeoForgeRegistryHelper;
import einstein.jmc.util.CakeUtil;
import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static einstein.jmc.JustMoreCakes.livingEntityJump;
import static einstein.jmc.JustMoreCakes.livingEntityTick;

@Mod(JustMoreCakes.MOD_ID)
public class JustMoreCakesNeoForge {

    public JustMoreCakesNeoForge(IEventBus modEventBus) {
        JustMoreCakes.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::generateData);
        modEventBus.addListener(this::registerEntityRenderers);
        modEventBus.addListener(this::createNewDatapackRegistries);
        modEventBus.addListener(this::registerMenuScreens);
        NeoForgeRegistryHelper.ITEMS.register(modEventBus);
        NeoForgeRegistryHelper.BLOCKS.register(modEventBus);
        NeoForgeRegistryHelper.BLOCK_ENTITIES.register(modEventBus);
        NeoForgeRegistryHelper.MENU_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.RECIPE_SERIALIZERS.register(modEventBus);
        NeoForgeRegistryHelper.RECIPE_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.POI_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.VILLAGER_PROFESSIONS.register(modEventBus);
        NeoForgeRegistryHelper.MOB_EFFECTS.register(modEventBus);
        NeoForgeRegistryHelper.POTIONS.register(modEventBus);
        NeoForgeRegistryHelper.CREATIVE_MODE_TABS.register(modEventBus);
        NeoForgeRegistryHelper.GAME_EVENTS.register(modEventBus);
        NeoForgeRegistryHelper.TRIGGER_TYPES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::onEntityJump);
        NeoForge.EVENT_BUS.addListener(this::onEntityTick);
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        NeoForge.EVENT_BUS.addListener(this::onVillagerTradesEvent);
        NeoForge.EVENT_BUS.addListener(this::onWanderingTradesEvent);
        NeoForge.EVENT_BUS.addListener(this::onBlockDrops);
        NeoForge.EVENT_BUS.addListener(this::registerBrewingRecipes);
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, this::onBlockClicked);

        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.SERVER, ModServerConfigs.SPEC);
        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.COMMON, ModCommonConfigs.SPEC);
    }

    void generateData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server providers
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        BlockTagsProvider blockTags = new ModBlockTagsProvider(output, lookupProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), fileHelper));
        generator.addProvider(event.includeServer(), new ModPOITagsProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new ModGameEventTagsProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new ModAdvancementProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModCakeEffectsProvider(output));
        generator.addProvider(event.includeServer(), new ModLootModifiersProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModDataMapProvider(output, lookupProvider));

        // Client providers
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, fileHelper));
    }

    void onBlockDrops(BlockDropsEvent event) {
        if (CakeUtil.inFamily(event.getState(), ModBlocks.SCULK_CAKE_FAMILY)) {
            event.setDroppedExperience(5);
        }
    }

    void onBlockClicked(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = JustMoreCakes.blockClicked(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    void createNewDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BowlContents.REGISTRY_KEY, BowlContents.CODEC, BowlContents.CODEC);
    }

    void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new NeoForgeCakeEffectsReloadListener());
    }

    void onServerStarting(ServerStartingEvent event) {
        JustMoreCakes.onServerStarting(event.getServer());
    }

    void commonSetup(FMLCommonSetupEvent event) {
        JustMoreCakes.commonSetup();
    }

    void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);
    }

    void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CAKE_STAND.get(), CakeStandRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CERAMIC_BOWL.get(), CeramicBowlRenderer::new);
    }

    void onEntityJump(final LivingEvent.LivingJumpEvent event) {
        livingEntityJump(event.getEntity());
    }

    void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity livingEntity) {
            livingEntityTick(entity.level(), livingEntity);
        }
    }

    void onVillagerTradesEvent(final VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.CAKE_BAKER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ModVillagers.noviceTrades(trades.get(1));
            ModVillagers.apprenticeTrades(trades.get(2));
            ModVillagers.journeymanTrades(trades.get(3));
            ModVillagers.expertTrades(trades.get(4));
            ModVillagers.masterTrades(trades.get(5));
        }
    }

    void onWanderingTradesEvent(final WandererTradesEvent event) {
        ModVillagers.wanderingTraderTrades(event.getGenericTrades());
    }

    void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        ModPotions.registerPotionRecipes(event.getBuilder());
    }
}
