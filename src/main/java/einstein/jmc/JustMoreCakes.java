package einstein.jmc;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.*;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.CakeChompsEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

@Mod(JustMoreCakes.MOD_ID)
public class JustMoreCakes {

    public static final String MOD_ID = "jmc";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final JMCTab JMC_TAB = new JMCTab(CreativeModeTab.TABS.length, "jmc_tab");
    private static CakeEffectsManager CAKE_EFFECTS_MANAGER;

    public JustMoreCakes() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onParallelDispatch);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModVillagers.POIS.register(modEventBus);
        ModVillagers.PROFESSIONS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener(this::missingMappings);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJump);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        if (ModList.get().isLoaded("cakechomps")) {
            MinecraftForge.EVENT_BUS.addListener(CakeChompsEvents::onCakeEaten);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC);
    }

    void onAddReloadListeners(AddReloadListenerEvent event) {
        CAKE_EFFECTS_MANAGER = new CakeEffectsManager();
        event.addListener(CAKE_EFFECTS_MANAGER);
    }

    public static CakeEffectsManager getCakeEffectsManager() {
        if (CAKE_EFFECTS_MANAGER == null) {
            throw new IllegalStateException("Can't retrieve CakeEffectsManager until resources have loaded");
        }

        return CAKE_EFFECTS_MANAGER;
    }

    public static void addCakeEffects(ResourceLocation id, BaseCakeBlock cake) {
        CakeEffectsManager manager = getCakeEffectsManager();
        manager.getRegisteredCakeEffects().forEach((location, cakeEffects) -> {
            if (location.getPath().equals(id.getPath())) {
                cake.setCakeEffects(cakeEffects);
            }
        });
    }

    void onServerStarted(ServerStartedEvent event) {
        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> addCakeEffects(cake.getId(), cake.get()));
    }

    void commonSetup(final FMLCommonSetupEvent event) {
        registerVillageBuilding("plains", "bakery_1");
        registerVillageBuilding("plains", "bakery_2");
        registerVillageBuilding("desert", "bakery_1");
        registerVillageBuilding("savanna", "bakery_1");
        registerVillageBuilding("snowy", "bakery_1");
        registerVillageBuilding("snowy", "bakery_2");
        registerVillageBuilding("taiga", "bakery_1");
        GiveGiftToHero.GIFTS.put(ModVillagers.CAKE_BAKER.get(), new ResourceLocation(MOD_ID, "gameplay/hero_of_the_village/cake_baker_gift"));
        Items.CAKE.maxStackSize = 64;
    }

    @SuppressWarnings("unchecked")
    void registerVillageBuilding(String biome, String name) {
        PlainVillagePools.bootstrap();
        DesertVillagePools.bootstrap();
        SavannaVillagePools.bootstrap();
        SnowyVillagePools.bootstrap();
        TaigaVillagePools.bootstrap();

        final StructureTemplatePool templatePool = BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("minecraft:village/" + biome + "/houses"));

        if (templatePool == null) {
            LOGGER.warn("Failed to register " + biome + " village building");
            return;
        }

        final StructurePoolElement structure = StructurePoolElement.legacy(MOD_ID + ":village/" + biome + "/houses/" + biome + "_" + name, ProcessorLists.MOSSIFY_10_PERCENT).apply(StructureTemplatePool.Projection.RIGID);

        try {
            final Field templatesField = StructureTemplatePool.class.getDeclaredField(ASMAPI.mapField("f_210560_"));
            templatesField.setAccessible(true);
            final ObjectArrayList<StructurePoolElement> templates = (ObjectArrayList<StructurePoolElement>) templatesField.get(templatePool);
            for (int i = 0; i < ModCommonConfigs.CAKE_BAKERY_GENERATION_WEIGHT.get(); i++) {
                templates.add(structure);
            }

            final Field rawTemplatesField = StructureTemplatePool.class.getDeclaredField(ASMAPI.mapField("f_210559_"));
            rawTemplatesField.setAccessible(true);
            final List<Pair<StructurePoolElement, Integer>> rawTemplates = (List<Pair<StructurePoolElement, Integer>>) rawTemplatesField.get(templatePool);
            rawTemplates.add(Pair.of(structure, ModCommonConfigs.CAKE_BAKERY_GENERATION_WEIGHT.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onParallelDispatch(final ParallelDispatchEvent event) {
        event.enqueueWork(ModPotions::registerPotionRecipes);
    }

    void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);
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
            return missingBlock(name).asItem();
        }
    }

    @Nullable
    private Block missingBlock(String name) {
        String[] removedCakes = { "sprinkle_cake", "christmas_cake" };
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
                        return ModBlocks.getBlock(new ResourceLocation("minecraft", color + "_candle_cake"));
                    }
                }
            }
        }

        return switch (name) {
            case "cheese_cake" -> ModBlocks.CHEESECAKE.get();
            case "triple_decker_cake" -> ModBlocks.THREE_TIERED_CAKE.get();
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
            } else {
                LOGGER.info("Failed to remap (" + mapping.getKey().toString() + ") of registry (" + registry.getRegistryName() + ")");
                mapping.fail();
            }
        }
    }

    void onEntityJump(final LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
                player.push(0, 0.15F, 0);
            }
        }
    }

    void onEntityTick(final LivingEvent.LivingTickEvent event) {
        Level level = event.getEntity().getCommandSenderWorld();
        LivingEntity entity = event.getEntity();
        RandomSource random = entity.getRandom();

        if (entity.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
            if (entity.verticalCollision && entity.isOnGround() && !entity.isSleeping()) {
                float f0 = 0.65F;

                if (entity.hasEffect(MobEffects.JUMP)) {
                    f0 += 0.1F * (entity.getEffect(MobEffects.JUMP).getAmplifier() + 1);
                }

                entity.push(0, f0, 0);
                entity.playSound(SoundEvents.SLIME_SQUISH, 1, 1);

                if (level.isClientSide) {
                    for (int i = 0; i < 8; ++i) {
                        float f1 = random.nextFloat() * ((float)Math.PI * 2F);
                        float f2 = random.nextFloat() * 0.5F + 0.5F;
                        float f3 = Mth.sin(f1) * 1 * 0.5F * f2;
                        float f4 = Mth.cos(f1) * 1 * 0.5F * f2;
                        level.addParticle(ParticleTypes.ITEM_SLIME, entity.getX() + f3, entity.getY(), entity.getZ() + f4, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static void AddSupportedCandles() {
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.CANDLE, ModBlocks.mcLoc(""));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.WHITE_CANDLE, ModBlocks.mcLoc("white_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.ORANGE_CANDLE, ModBlocks.mcLoc("orange_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.MAGENTA_CANDLE, ModBlocks.mcLoc("magenta_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.LIGHT_BLUE_CANDLE, ModBlocks.mcLoc("light_blue_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.YELLOW_CANDLE, ModBlocks.mcLoc("yellow_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.LIME_CANDLE, ModBlocks.mcLoc("lime_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.PINK_CANDLE, ModBlocks.mcLoc("pink_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.GRAY_CANDLE, ModBlocks.mcLoc("gray_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.LIGHT_GRAY_CANDLE, ModBlocks.mcLoc("light_gray_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.CYAN_CANDLE, ModBlocks.mcLoc("cyan_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.PURPLE_CANDLE, ModBlocks.mcLoc("purple_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.BLUE_CANDLE, ModBlocks.mcLoc("blue_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.BROWN_CANDLE, ModBlocks.mcLoc("brown_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.GREEN_CANDLE, ModBlocks.mcLoc("green_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.RED_CANDLE, ModBlocks.mcLoc("red_"));
        ModBlocks.SUPPORTED_CANDLES.put(Blocks.BLACK_CANDLE, ModBlocks.mcLoc("black_"));
    }
}
