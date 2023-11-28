package einstein.jmc.compat.jade;

import einstein.jmc.block.CakeOvenBlock;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.entity.CakeOvenBlockEntity;
import einstein.jmc.compat.jade.providers.*;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import snownee.jade.api.*;
import snownee.jade.api.config.IWailaConfig;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {

    public static final CakeNourishmentProvider CAKE_NOURISHMENT_COMPONENT_PROVIDER = new CakeNourishmentProvider();
    public static final CakeEffectsProvider CAKE_EFFECT_PROVIDER = new CakeEffectsProvider();
    public static final CakeOvenProvider CAKE_OVEN_PROVIDER = new CakeOvenProvider();
    public static final ItemStorageRemoverProvider ITEM_STORAGE_REMOVER_PROVIDER = new ItemStorageRemoverProvider();
    public static final CandleTypeProvider CANDLE_TYPE_PROVIDER = new CandleTypeProvider();

    // Features
    public static final ResourceLocation CAKE_NOURISHMENT = loc("cake_nourishment");
    public static final ResourceLocation CAKE_EFFECTS = loc("cake_effects");
    public static final ResourceLocation CAKE_OVEN = loc("cake_oven");
    public static final ResourceLocation ITEM_STORAGE_REMOVER = loc("cake_oven.item_storage_remover");
    public static final ResourceLocation CANDLE_TYPE = loc("candle_type");

    // Configs
    public static final ResourceLocation DISPLAY_TYPE = loc("cake_nourishment.display_type");
    public static final ResourceLocation FOOD_ICONS_PER_LINE = loc("cake_nourishment.food_icons_per_line");
    public static final ResourceLocation SHOW_NUTRITION = loc("cake_nourishment.show_nutrition");
    public static final ResourceLocation SHOW_SATURATION = loc("cake_nourishment.show_saturation");
    public static final ResourceLocation HIDE_TRAPPED_CAKES = loc("hide_trapped_cakes");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CAKE_OVEN_PROVIDER, CakeOvenBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(DISPLAY_TYPE, CakeInfoDisplayType.TOTAL);
        registration.addConfig(FOOD_ICONS_PER_LINE, 10, 5, 30, false);
        registration.addConfig(SHOW_NUTRITION, true);
        registration.addConfig(SHOW_SATURATION, true);
        registration.addConfig(HIDE_TRAPPED_CAKES, false);

        registration.registerBlockComponent(CAKE_NOURISHMENT_COMPONENT_PROVIDER, BaseCakeBlock.class);
        registration.registerBlockComponent(CAKE_NOURISHMENT_COMPONENT_PROVIDER, BaseCandleCakeBlock.class);
        registration.registerBlockComponent(CAKE_NOURISHMENT_COMPONENT_PROVIDER, CakeBlock.class);
        registration.registerBlockComponent(CAKE_NOURISHMENT_COMPONENT_PROVIDER, CandleCakeBlock.class);

        registration.registerBlockComponent(CANDLE_TYPE_PROVIDER, BaseCandleCakeBlock.class);
        registration.registerBlockComponent(CANDLE_TYPE_PROVIDER, CandleCakeBlock.class);

        registration.registerBlockComponent(CAKE_EFFECT_PROVIDER, BaseCakeBlock.class);
        registration.registerBlockComponent(CAKE_EFFECT_PROVIDER, BaseCandleCakeBlock.class);
        registration.registerBlockComponent(CAKE_EFFECT_PROVIDER, CakeBlock.class);
        registration.registerBlockComponent(CAKE_EFFECT_PROVIDER, CandleCakeBlock.class);

        registration.registerBlockComponent(CAKE_OVEN_PROVIDER, CakeOvenBlock.class);
        registration.registerBlockComponent(ITEM_STORAGE_REMOVER_PROVIDER, CakeOvenBlock.class);

        registration.addRayTraceCallback((hitResult, accessor, unmodifiedAccessor) -> {
            if (IWailaConfig.get().getPlugin().get(HIDE_TRAPPED_CAKES)) {
                if (accessor instanceof BlockAccessor blockAccessor) {
                    Block block = blockAccessor.getBlock();
                    if (block.equals(ModBlocks.TNT_CAKE.get()) || block.equals(ModBlocks.POISON_CAKE.get())) {
                        return registration.blockAccessor()
                                .from(blockAccessor)
                                .blockEntity(() -> null) // Required for TNT cake to be displayed (idk why)
                                .blockState(Blocks.CAKE.defaultBlockState())
                                .build();
                    }
                    else if (isTrappedCandleCake(block, ModBlocks.TNT_CAKE.get()) || isTrappedCandleCake(block, ModBlocks.POISON_CAKE.get())) {
                        return registration.blockAccessor()
                                .from(blockAccessor)
                                .blockEntity(() -> null)
                                .blockState(Util.VANILLA_CANDLE_CAKES_BY_CANDLE.get(((BaseCandleCakeBlock) block).getCandle()).defaultBlockState())
                                .build();
                    }
                }
            }
            return accessor;
        });

        registration.markAsClientFeature(CAKE_NOURISHMENT);
        registration.markAsClientFeature(DISPLAY_TYPE);
        registration.markAsClientFeature(FOOD_ICONS_PER_LINE);
        registration.markAsClientFeature(SHOW_NUTRITION);
        registration.markAsClientFeature(SHOW_SATURATION);
        registration.markAsClientFeature(HIDE_TRAPPED_CAKES);
        registration.markAsClientFeature(CANDLE_TYPE);
    }

    private static boolean isTrappedCandleCake(Block block, BaseCakeBlock cake) {
        if (block instanceof BaseCandleCakeBlock candleCake) {
            return cake.getBuilder().getCandleCakeByCandle().values().stream().map(Supplier::get).toList().contains(candleCake);
        }
        return false;
    }

    public enum CakeInfoDisplayType {
        TOTAL,
        PER_SLICE
    }
}
