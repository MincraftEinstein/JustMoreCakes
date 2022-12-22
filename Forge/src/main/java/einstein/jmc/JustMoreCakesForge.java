package einstein.jmc;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.data.CakeEffectsManager;
import einstein.jmc.init.*;
import einstein.jmc.platform.ForgeRegistryHelper;
import einstein.jmc.platform.Services;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import einstein.jmc.util.Util;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

import static einstein.jmc.JustMoreCakes.*;

@Mod(MOD_ID)
public class JustMoreCakesForge {

    private static CakeEffectsManager CAKE_EFFECTS_MANAGER;

    public JustMoreCakesForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        JustMoreCakes.init();
        modEventBus.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onParallelDispatch);
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
        MinecraftForge.EVENT_BUS.addListener(this::missingMappings);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJump);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onVillagerTradesEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onWanderingTradesEvent);
        if (Services.PLATFORM.isModLoaded("cakechomps")) {
            MinecraftForge.EVENT_BUS.addListener(this::onBlockRightClicked);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC);
    }

    /**
     * Copied from {@link com.illusivesoulworks.cakechomps.CakeChompsForgeMod}
     * and slightly changed to work with JustMoreCakes
     */
    void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        BlockHitResult result = event.getHitVec();
        Util.useCake(player, pos, hand, () -> {
            UseOnContext useoncontext = new UseOnContext(player, hand, result);
            ItemStack stack = player.getItemInHand(hand);

            if (event.getUseItem() != Event.Result.DENY) {
                InteractionResult interact = stack.onItemUseFirst(useoncontext);

                if (interact != InteractionResult.PASS) {
                    return false;
                }
            }

            boolean flag = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
            boolean flag1 = (player.isSecondaryUseActive() && flag) &&
                    !(player.getMainHandItem().doesSneakBypassUse(player.getLevel(), pos, player) &&
                            player.getOffhandItem().doesSneakBypassUse(player.getLevel(), pos, player));

            return event.getUseBlock() == Event.Result.ALLOW || (event.getUseBlock() != Event.Result.DENY && !flag1);
        });
    }

    void onAddReloadListeners(AddReloadListenerEvent event) {
        CAKE_EFFECTS_MANAGER = new CakeEffectsManager();
        event.addListener(CAKE_EFFECTS_MANAGER);
    }

    public static void addCakeEffects(ResourceLocation id, BaseCakeBlock cake) {
        if (CAKE_EFFECTS_MANAGER != null) {
            CAKE_EFFECTS_MANAGER.getRegisteredCakeEffects().forEach((name, cakeEffects) -> {
                if (name.equals(id.getPath())) {
                    cake.addCakeEffects(cakeEffects);
                }
            });
        }
        else {
            throw new IllegalStateException("Can't retrieve CakeEffectsManager until resources have loaded");
        }
    }

    void onServerStarted(ServerStartedEvent event) {
        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> addCakeEffects(Util.getBlockId(cake.get()), cake.get()));
    }

    void commonSetup(final FMLCommonSetupEvent event) {
        GiveGiftToHero.GIFTS.put(ModVillagers.CAKE_BAKER.get(), loc("gameplay/hero_of_the_village/cake_baker_gift"));
        JustMoreCakes.commonSetup();
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
                        return Util.getBlock(mcLoc( color + "_candle_cake"));
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
        Util.livingEntityJump(event.getEntity());
    }

    void onEntityTick(final LivingEvent.LivingTickEvent event) {
        Util.livingEntityTick(event.getEntity().getLevel(), event.getEntity());
    }

    void onVillagerTradesEvent(final VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.CAKE_BAKER.get()) {
            final List<VillagerTrades.ItemListing> novice = event.getTrades().get(1); // Stone tier
            final List<VillagerTrades.ItemListing> apprentice = event.getTrades().get(2); // Iron tier
            final List<VillagerTrades.ItemListing> journeyman = event.getTrades().get(3); // Gold tier
            final List<VillagerTrades.ItemListing> expert = event.getTrades().get(4); // Emerald tier
            final List<VillagerTrades.ItemListing> master = event.getTrades().get(5); // Diamond tier

            novice.add(new EmeraldsForItems(Items.WHEAT, 20, 1, 16, 2));
            novice.add(new EmeraldsForItems(Items.EGG, 5, 1, 16, 2));
            novice.add(new ItemsForEmeralds(Items.SUGAR, 2, 4, 1));
            novice.add(new ItemsForEmeralds(Items.MILK_BUCKET, 2, 1, 2));

            apprentice.add(new ItemsForEmeralds(Blocks.CAKE.asItem(), 1, 1, 10));
            apprentice.add(new ItemsForEmeralds(Items.COCOA_BEANS, 3, 1, 5));
            apprentice.add(new ItemsForEmeralds(ModBlocks.CARROT_CAKE.get().asItem(), 1, 1,10));

            journeyman.add(new EmeraldsForItems(Items.COAL, 15, 1, 16, 10));
            journeyman.add(new EmeraldsForItems(Items.CARROT, 22, 1, 16, 20));
            journeyman.add(new EmeraldsForItems(Items.SUGAR_CANE, 2, 1, 10));

            expert.add(new EmeraldsForItems(ModItems.CREAM_CHEESE.get(), 1, 6, 30));
            expert.add(new ItemsForEmeralds(ModItems.CUPCAKE.get(), 4, 1, 16, 15));

            master.add(new ItemsForEmeralds(ModBlocks.THREE_TIERED_CAKE.get().asItem(), 15, 1, 30));
            master.add(new ItemsForEmeralds(ModBlocks.CREEPER_CAKE.get().asItem(), 20, 1,  30));
        }
    }

    void onWanderingTradesEvent(final WandererTradesEvent event) {
        event.getGenericTrades().add(new ItemsForEmeralds(ModBlocks.SEED_CAKE.get().asItem(), 2, 1, 12));
    }
}