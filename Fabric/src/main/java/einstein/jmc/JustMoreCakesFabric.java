package einstein.jmc;

import einstein.jmc.blocks.CakeEffectsHolder;
import einstein.jmc.data.CakeEffects;
import einstein.jmc.init.*;
import einstein.jmc.platform.Services;
import einstein.jmc.platform.services.RegistryHelper;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import einstein.jmc.util.Util;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

import static einstein.jmc.JustMoreCakes.LOGGER;
import static einstein.jmc.JustMoreCakes.loc;

public class JustMoreCakesFabric implements ModInitializer {

    private static Map<ResourceLocation, CakeEffects> CAKE_EFFECTS;

    @Override
    public void onInitialize() {
        JustMoreCakes.init();
        onAddReloadListeners();
        onServerStarting();
        onServerStarted();
        addVillagerTrades();
        ModPotions.registerPotionRecipes();

        if (Services.PLATFORM.isModLoaded("cakechomps")) {
            onBlockRightClicked();
        }

        ForgeConfigRegistry.INSTANCE.register(JustMoreCakes.MOD_ID, ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
        ForgeConfigRegistry.INSTANCE.register(JustMoreCakes.MOD_ID, ModConfig.Type.COMMON, ModCommonConfigs.SPEC);

        VillagerInteractionRegistries.registerGiftLootTable(ModVillagers.CAKE_BAKER.get(), loc("gameplay/hero_of_the_village/cake_baker_gift"));
        JustMoreCakes.commonSetup();

        FabricItemGroup.builder(JustMoreCakes.loc("jmc_tab")).icon(() -> new ItemStack(ModBlocks.CHOCOLATE_CAKE.get())).displayItems((flags, output, hasPermission) ->
                RegistryHelper.CREATIVE_TAB_ITEMS.forEach(supplier -> output.accept(supplier.get()))).build();
    }

    /**
     * Copied from {@link com.illusivesoulworks.cakechomps.CakeChompsFabricMod}
     * and slightly changed to work with JustMoreCakes
     */
    void onBlockRightClicked() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            Util.useCake(player, hitResult.getBlockPos(), hand, () -> {
                boolean bl = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
                return !(player.isSecondaryUseActive() && bl);
            });
            return InteractionResult.PASS;
        });
    }

    void onAddReloadListeners() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {

            @Override
            public ResourceLocation getFabricId() {
                return JustMoreCakes.loc("cake_effects");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                CAKE_EFFECTS = Util.deserializeCakeEffects(manager);
            }
        });
    }

    void onServerStarting() {
        ServerLifecycleEvents.SERVER_STARTING.register(JustMoreCakes::onServerStarting);
    }

    void onServerStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> addCakeEffects());
    }

    public static void addCakeEffects() {
        if (CAKE_EFFECTS != null) {
            CAKE_EFFECTS.forEach((location, cakeEffects) -> {
                if (cakeEffects.cake() instanceof CakeEffectsHolder holder) {
                    holder.addCakeEffects(cakeEffects);
                }
                else {
                    LOGGER.error("Failed to load cake effect for block {} as it is not valid cake effect holder", cakeEffects.cake());
                }
            });
        }
        else {
            throw new IllegalStateException("Can't retrieve CakeEffectsManager until resources have loaded");
        }
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
            trades.add(new ItemsForEmeralds(ModBlocks.CARROT_CAKE.get().asItem(), 1, 1,10));
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
            trades.add(new ItemsForEmeralds(ModBlocks.THREE_TIERED_CAKE.get().asItem(), 15, 1, 30));
            trades.add(new ItemsForEmeralds(ModBlocks.CREEPER_CAKE.get().asItem(), 20, 1,  30));
        });

        // Wandering trader
        TradeOfferHelper.registerWanderingTraderOffers(1, trades ->
                trades.add(new ItemsForEmeralds(ModBlocks.SEED_CAKE.get().asItem(), 2, 1, 12)));
    }
}
