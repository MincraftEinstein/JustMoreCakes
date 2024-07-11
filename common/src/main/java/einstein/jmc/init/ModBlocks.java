package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeOvenBlock;
import einstein.jmc.block.CakeStandBlock;
import einstein.jmc.block.CeramicBowlBlock;
import einstein.jmc.block.EncasingIceBlock;
import einstein.jmc.block.cake.*;
import einstein.jmc.block.cake.candle.*;
import einstein.jmc.platform.Services;
import einstein.jmc.data.CakeModel;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.registration.family.DefaultCakeFamily;
import einstein.jmc.registration.family.VanillaCakeFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;

public class ModBlocks {

    public static final Supplier<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).friction(0.98F).randomTicks().strength(0.5F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn((state, getter, pos, type) -> false).isRedstoneConductor((state, getter, pos) -> false).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).pushReaction(PushReaction.NORMAL)), true);
    public static final Supplier<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(3.5F).lightLevel(state -> state.getValue(CakeOvenBlock.LIT) ? 13 : 0)), true);
    public static final Supplier<Block> CAKE_STAND = register("cake_stand", () -> new CakeStandBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(0.8F).noOcclusion()), true);
    public static final Supplier<Block> CERAMIC_BOWL = register("ceramic_bowl", () -> new CeramicBowlBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).pushReaction(PushReaction.DESTROY).instabreak().noOcclusion()), true);

    public static final CakeVariant POISON_CAKE_VARIANT = CakeVariant.create("poison_cake")
            .models(CakeModel.FROM_VANILLA, CakeModel.FROM_VANILLA)
            .customItemModel()
            .build();
    public static final CakeVariant TNT_CAKE_VARIANT = CakeVariant.create("tnt_cake")
            .bothSuppliers(TNTCakeBlock::new, TNTCandleCakeBlock::new)
            .models(CakeModel.FROM_VANILLA, CakeModel.FROM_VANILLA)
            .customItemModel()
            .build();
    public static final CakeVariant CUPCAKE_VARIANT = CakeVariant.create("cupcake")
            .cakeSupplier(CupcakeBlock::new)
            .model(CakeModel.CUSTOM)
            .disallowCandles()
            .noItem()
            .nutrition(1)
            .saturationModifier(0.3F)
            .build();

    public static final VanillaCakeFamily VANILLA_CAKE_FAMILY = new VanillaCakeFamily.Builder().build();
    public static final DefaultCakeFamily CHOCOLATE_CAKE_FAMILY = DefaultCakeFamily.create("chocolate").build();
    public static final DefaultCakeFamily CARROT_CAKE_FAMILY = DefaultCakeFamily.create("carrot").build();
    public static final DefaultCakeFamily PUMPKIN_CAKE_FAMILY = DefaultCakeFamily.create("pumpkin").build();
    public static final DefaultCakeFamily MELON_CAKE_FAMILY = DefaultCakeFamily.create("melon")
            .nutrition(1)
            .saturationModifier(0.4F)
            .build();
    public static final DefaultCakeFamily APPLE_CAKE_FAMILY = DefaultCakeFamily.create("apple").build();
    public static final DefaultCakeFamily COOKIE_CAKE_FAMILY = DefaultCakeFamily.create("cookie").build();
    public static final DefaultCakeFamily GOLDEN_APPLE_CAKE_FAMILY = DefaultCakeFamily.create("golden_apple")
            .nutrition(4)
            .saturationModifier(0.5F)
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily RED_MUSHROOM_CAKE_FAMILY = DefaultCakeFamily.create("red_mushroom")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/red_mushroom")))
            .build();
    public static final DefaultCakeFamily FIREY_CAKE_FAMILY = DefaultCakeFamily.create("firey").build();
    public static final DefaultCakeFamily REDSTONE_CAKE_FAMILY = DefaultCakeFamily.create("redstone")
            .modifyBaseBuilder(builder -> builder.bothSuppliers(RedstoneCakeBlock::new, RedstoneCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.bothSuppliers(RedstoneTwoTieredCakeBlock::new, RedstoneCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.bothSuppliers(RedstoneThreeTieredCakeBlock::new, RedstoneCandleThreeTieredCakeBlock::new))
            .build();
    public static final DefaultCakeFamily ENDER_CAKE_FAMILY = DefaultCakeFamily.create("ender")
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily CHEESECAKE_FAMILY = DefaultCakeFamily.create("cheesecake", true).build();
    public static final DefaultCakeFamily SLIME_CAKE_FAMILY = DefaultCakeFamily.create("slime")
            .modifyBaseBuilder(builder -> builder.bothSuppliers(SlimeCakeBlock::new, SlimeCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.bothSuppliers(SlimeTwoTieredCakeBlock::new, SlimeCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.bothSuppliers(SlimeThreeTieredCakeBlock::new, SlimeCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().sound(SoundType.SLIME_BLOCK))
            .candleCakeProperties(candleCakeProperties().sound(SoundType.SLIME_BLOCK))
            .build();
    public static final DefaultCakeFamily BEETROOT_CAKE_FAMILY = DefaultCakeFamily.create("beetroot")
            .nutrition(3)
            .saturationModifier(0.4F)
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily LAVA_CAKE_FAMILY = DefaultCakeFamily.create("lava")
            .modifyBaseBuilder(builder -> builder.bothSuppliers(LavaCakeBlock::new, LavaCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.bothSuppliers(LavaTwoTieredCakeBlock::new, LavaCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.bothSuppliers(LavaThreeTieredCakeBlock::new, LavaCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().lightLevel(state -> 9))
            .candleCakeProperties(candleCakeProperties().lightLevel(state -> 9))
            .build();
    public static final DefaultCakeFamily CREEPER_CAKE_FAMILY = DefaultCakeFamily.create("creeper")
            .modifyBaseBuilder(builder -> builder.cakeSupplier(CreeperCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.cakeSupplier(CreeperTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.cakeSupplier(CreeperThreeTieredCakeBlock::new))
            .build();
    public static final DefaultCakeFamily SEED_CAKE_FAMILY = DefaultCakeFamily.create("seed").build();
    public static final DefaultCakeFamily BROWN_MUSHROOM_CAKE_FAMILY = DefaultCakeFamily.create("brown_mushroom")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/brown_mushroom")))
            .build();
    public static final DefaultCakeFamily ICE_CAKE_FAMILY = DefaultCakeFamily.create("ice").build();
    public static final DefaultCakeFamily CHORUS_CAKE_FAMILY = DefaultCakeFamily.create("chorus")
            .model(new CakeModel.CrossCakeModel(JustMoreCakes.loc("block/chorus_cake_flower")))
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily SWEET_BERRY_CAKE_FAMILY = DefaultCakeFamily.create("sweet_berry")
            .nutrition(1)
            .saturationModifier(0.5F)
            .build();
    public static final DefaultCakeFamily HONEY_CAKE_FAMILY = DefaultCakeFamily.create("honey")
            .saturationModifier(0.4F)
            .build();
    public static final DefaultCakeFamily GLOWSTONE_CAKE_FAMILY = DefaultCakeFamily.create("glowstone")
            .modifyBaseBuilder(builder -> builder.bothSuppliers(GlowstoneCakeBlock::new, GlowstoneCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.bothSuppliers(GlowstoneTwoTieredCakeBlock::new, GlowstoneCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.bothSuppliers(GlowstoneThreeTieredCakeBlock::new, GlowstoneCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().lightLevel(state -> 12))
            .candleCakeProperties(candleCakeProperties().lightLevel(state -> 12))
            .build();
    public static final DefaultCakeFamily CRIMSON_FUNGUS_CAKE_FAMILY = DefaultCakeFamily.create("crimson_fungus")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/crimson_fungus")))
            .build();
    public static final DefaultCakeFamily WARPED_FUNGUS_CAKE_FAMILY = DefaultCakeFamily.create("warped_fungus").build();
    public static final DefaultCakeFamily RED_VELVET_CAKE_FAMILY = DefaultCakeFamily.create("red_velvet").build();
    public static final DefaultCakeFamily GLOW_BERRY_CAKE_FAMILY = DefaultCakeFamily.create("glow_berry")
            .cakeProperties(cakeProperties().lightLevel(state -> 7))
            .candleCakeProperties(candleCakeProperties().lightLevel(state -> 7))
            .build();
    public static final DefaultCakeFamily OBSIDIAN_CAKE_FAMILY = DefaultCakeFamily.create("obsidian")
            .modifyBaseBuilder(builder -> builder.cakeSupplier(ObsidianCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.cakeSupplier(ObsidianTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.cakeSupplier(ObsidianThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().sound(SoundType.STONE).strength(12.5F, 300).pushReaction(PushReaction.BLOCK))
            .candleCakeProperties(candleCakeProperties().sound(SoundType.STONE).strength(12.5F, 300).pushReaction(PushReaction.BLOCK))
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily SCULK_CAKE_FAMILY = DefaultCakeFamily.create("sculk")
            .modifyBaseBuilder(builder -> builder.cakeSupplier(SculkCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.cakeSupplier(SculkTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.bothSuppliers(SculkThreeTieredCakeBlock::new, SculkCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().lightLevel(state -> 1)
                    .emissiveRendering((state, getter, pos) -> SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE))
            .models(CakeModel.CUSTOM, CakeModel.DEFAULT)
            .alwaysEat()
            .build();

    public static <T extends Block> Supplier<T> register(String name, Supplier<T> block, boolean hasItem) {
        if (hasItem) {
            return Services.REGISTRY.registerBlock(name, block);
        }
        return Services.REGISTRY.registerBlockNoItem(name, block);
    }

    public static BlockBehaviour.Properties cakeProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE);
    }

    public static BlockBehaviour.Properties candleCakeProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE);
    }

    public static void init() {
    }
}
