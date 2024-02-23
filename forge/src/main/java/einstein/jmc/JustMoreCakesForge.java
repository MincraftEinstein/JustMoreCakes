package einstein.jmc;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.client.renderers.blockentities.CakeStandRenderer;
import einstein.jmc.data.ForgeCakeEffectsReloadListener;
import einstein.jmc.data.packs.providers.*;
import einstein.jmc.init.*;
import einstein.jmc.platform.ForgeNetworkHelper;
import einstein.jmc.platform.ForgeRegistryHelper;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import einstein.jmc.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static einstein.jmc.JustMoreCakes.*;

@Mod(MOD_ID)
public class JustMoreCakesForge {

    public JustMoreCakesForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        JustMoreCakes.init();
        ForgeNetworkHelper.init();
        modEventBus.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::generateData);
        modEventBus.addListener(this::registerEntityRenderers);
        ForgeRegistryHelper.ITEMS.register(modEventBus);
        ForgeRegistryHelper.BLOCKS.register(modEventBus);
        ForgeRegistryHelper.BLOCK_ENTITIES.register(modEventBus);
        ForgeRegistryHelper.MENU_TYPES.register(modEventBus);
        ForgeRegistryHelper.RECIPE_SERIALIZERS.register(modEventBus);
        ForgeRegistryHelper.RECIPE_TYPES.register(modEventBus);
        ForgeRegistryHelper.POI_TYPES.register(modEventBus);
        ForgeRegistryHelper.VILLAGER_PROFESSIONS.register(modEventBus);
        ForgeRegistryHelper.MOB_EFFECTS.register(modEventBus);
        ForgeRegistryHelper.POTIONS.register(modEventBus);
        ForgeRegistryHelper.CREATIVE_MODE_TABS.register(modEventBus);
        ForgeRegistryHelper.GAME_EVENTS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener(this::missingMappings);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJump);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::onVillagerTradesEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onWanderingTradesEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onBlockBreak);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC);
    }

    void generateData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server providers
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
        BlockTagsProvider blockTags = new ModBlockTagsProvider(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new ModPOITagsProvider(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new ModGameEventTagsProvider(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new ModAdvancementProvider(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output));
        generator.addProvider(event.includeServer(), new ModCakeEffectsProvider(output));
        generator.addProvider(event.includeServer(), new ModLootModifiersProvider(output));

        // Client providers
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
    }

    void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(ModBlocks.SCULK_CAKE_FAMILY.getBaseCake().get())
                || event.getState().is(ModBlocks.SCULK_CAKE_FAMILY.getTwoTieredCake().get())
                || event.getState().is(ModBlocks.SCULK_CAKE_FAMILY.getThreeTieredCake().get())) {
            event.setExpToDrop(5);
        }
    }

    void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ForgeCakeEffectsReloadListener());
    }

    void onServerStarting(ServerStartingEvent event) {
        JustMoreCakes.onServerStarting(event.getServer());
    }

    void commonSetup(final FMLCommonSetupEvent event) {
        GiveGiftToHero.GIFTS.put(ModVillagers.CAKE_BAKER.get(), CAKE_BAKER_GIFT);
        JustMoreCakes.commonSetup();
    }

    void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);
    }

    void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CAKE_STAND.get(), CakeStandRenderer::new);
    }

    void missingMappings(MissingMappingsEvent event) {
        handleMissingMappings(event, ForgeRegistries.ITEMS, this::missingItems);
        handleMissingMappings(event, ForgeRegistries.BLOCKS, this::missingBlock);
    }

    @Nullable
    private Item missingItems(String name) {
        if (name.equals("cheese")) {
            return ModItems.CREAM_CHEESE.get();
        }
        else {
            Block block = missingBlock(name);
            return block == null ? null : block.asItem();
        }
    }

    @Nullable
    private Block missingBlock(String name) {
        String[] removedCakes = {"sprinkle_cake", "christmas_cake"};
        for (String cake : removedCakes) {
            if (name.equals(cake)) {
                return Blocks.CAKE;
            }
            else if (name.equals("candle_" + cake)) {
                return Blocks.CANDLE_CAKE;
            }
            else {
                for (DyeColor color : DyeColor.values()) {
                    if (name.equals(color + "_candle_" + cake)) {
                        return Util.getBlock(mcLoc(color + "_candle_cake"));
                    }
                }
            }
        }

        return switch (name) {
            case "cheese_cake" -> ModBlocks.CHEESECAKE_FAMILY.getBaseCake().get();
            case "triple_decker_cake" -> ModBlocks.VANILLA_CAKE_FAMILY.getThreeTieredCake().get();
            case "birthday_cake" -> Blocks.CAKE;
            default -> null;
        };
    }

    private <T> void handleMissingMappings(MissingMappingsEvent event, IForgeRegistry<T> registry, Function<String, T> function) {
        List<MissingMappingsEvent.Mapping<T>> mappings = event.getMappings(registry.getRegistryKey(), MOD_ID);

        for (MissingMappingsEvent.Mapping<T> mapping : mappings) {
            T value = function.apply(mapping.getKey().getPath());
            if (value != null) {
                mapping.remap(value);
            }
            else {
                LOGGER.info("Failed to remap (" + mapping.getKey().toString() + ") of registry (" + registry.getRegistryName() + ")");
                mapping.fail();
            }
        }
    }

    void onEntityJump(final LivingEvent.LivingJumpEvent event) {
        Util.livingEntityJump(event.getEntity());
    }

    void onEntityTick(final LivingEvent.LivingTickEvent event) {
        Util.livingEntityTick(event.getEntity().level(), event.getEntity());
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
}