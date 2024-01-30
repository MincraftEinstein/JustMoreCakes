package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeOvenBlock;
import einstein.jmc.block.CakeStandBlock;
import einstein.jmc.block.EncasingIceBlock;
import einstein.jmc.block.cake.*;
import einstein.jmc.block.cake.candle.*;
import einstein.jmc.platform.Services;
import einstein.jmc.util.*;
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

    public static final VanillaCakeFamily VANILLA_CAKE_FAMILY = new VanillaCakeFamily();
    public static final DefaultCakeFamily CHOCOLATE_CAKE_FAMILY = CakeFamily.create("chocolate").build();
    public static final DefaultCakeFamily CARROT_CAKE_FAMILY = CakeFamily.create("carrot").build();
    public static final DefaultCakeFamily PUMPKIN_CAKE_FAMILY = CakeFamily.create("pumpkin").build();
    public static final DefaultCakeFamily MELON_CAKE_FAMILY = CakeFamily.create("melon")
            .nutrition(1)
            .saturationModifier(0.4F)
            .build();
    public static final DefaultCakeFamily APPLE_CAKE_FAMILY = CakeFamily.create("apple").build();
    public static final DefaultCakeFamily COOKIE_CAKE_FAMILY = CakeFamily.create("cookie").build();
    public static final DefaultCakeFamily GOLDEN_APPLE_CAKE_FAMILY = CakeFamily.create("golden_apple")
            .nutrition(4)
            .saturationModifier(0.5F)
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily RED_MUSHROOM_CAKE_FAMILY = CakeFamily.create("red_mushroom")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/red_mushroom")))
            .build();
    public static final DefaultCakeFamily FIREY_CAKE_FAMILY = CakeFamily.create("firey").build();
    public static final DefaultCakeFamily REDSTONE_CAKE_FAMILY = CakeFamily.create("redstone")
            .modifyBaseBuilder(builder -> builder.setBothClasses(RedstoneCakeBlock::new, RedstoneCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.setBothClasses(RedstoneTwoTieredCakeBlock::new, RedstoneCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.setBothClasses(RedstoneThreeTieredCakeBlock::new, RedstoneCandleThreeTieredCakeBlock::new))
            .build();
    public static final DefaultCakeFamily ENDER_CAKE_FAMILY = CakeFamily.create("ender")
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily CHEESECAKE_FAMILY = CakeFamily.create("cheesecake", true).build();
    public static final DefaultCakeFamily SLIME_CAKE_FAMILY = CakeFamily.create("slime")
            .modifyBaseBuilder(builder -> builder.setBothClasses(SlimeCakeBlock::new, SlimeCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.setBothClasses(SlimeTwoTieredCakeBlock::new, SlimeCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.setBothClasses(SlimeThreeTieredCakeBlock::new, SlimeCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().sound(SoundType.SLIME_BLOCK))
            .candleCakeProperties(candleCakeProperties().sound(SoundType.SLIME_BLOCK))
            .build();
    public static final DefaultCakeFamily BEETROOT_CAKE_FAMILY = CakeFamily.create("beetroot")
            .nutrition(3)
            .saturationModifier(0.4F)
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily LAVA_CAKE_FAMILY = CakeFamily.create("lava")
            .modifyBaseBuilder(builder -> builder.setBothClasses(LavaCakeBlock::new, LavaCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.setBothClasses(LavaTwoTieredCakeBlock::new, LavaCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.setBothClasses(LavaThreeTieredCakeBlock::new, LavaCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().lightLevel(state -> 9))
            .candleCakeProperties(candleCakeProperties().lightLevel(state -> 9))
            .build();
    public static final DefaultCakeFamily CREEPER_CAKE_FAMILY = CakeFamily.create("creeper")
            .modifyBaseBuilder(builder -> builder.setCakeClass(CreeperCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.setCakeClass(CreeperTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.setCakeClass(CreeperThreeTieredCakeBlock::new))
            .build();
    public static final DefaultCakeFamily SEED_CAKE_FAMILY = CakeFamily.create("seed").build();
    public static final DefaultCakeFamily BROWN_MUSHROOM_CAKE_FAMILY = CakeFamily.create("brown_mushroom")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/brown_mushroom")))
            .build();
    public static final DefaultCakeFamily ICE_CAKE_FAMILY = CakeFamily.create("ice").build();
    public static final DefaultCakeFamily CHORUS_CAKE = CakeFamily.create("chorus")
            .model(new CakeModel.CrossCakeModel(JustMoreCakes.loc("block/chorus_cake_flower")))
            .alwaysEat()
            .build();
    public static final DefaultCakeFamily SWEET_BERRY_CAKE_FAMILY = CakeFamily.create("sweet_berry")
            .nutrition(1)
            .saturationModifier(0.5F)
            .build();
    public static final DefaultCakeFamily HONEY_CAKE = CakeFamily.create("honey")
            .saturationModifier(0.4F)
            .build();
    public static final DefaultCakeFamily GLOWSTONE_CAKE = CakeFamily.create("glowstone")
            .modifyBaseBuilder(builder -> builder.setBothClasses(GlowstoneCakeBlock::new, GlowstoneCandleCakeBlock::new))
            .modifyTwoTieredBuilder(builder -> builder.setBothClasses(GlowstoneTwoTieredCakeBlock::new, GlowstoneCandleTwoTieredCakeBlock::new))
            .modifyThreeTieredBuilder(builder -> builder.setBothClasses(GlowstoneThreeTieredCakeBlock::new, GlowstoneCandleThreeTieredCakeBlock::new))
            .cakeProperties(cakeProperties().lightLevel(state -> 12))
            .candleCakeProperties(candleCakeProperties().lightLevel(state -> 12))
            .build();
    public static final DefaultCakeFamily CRIMSON_FUNGUS_CAKE = CakeFamily.create("crimson_fungus")
            .model(new CakeModel.CrossCakeModel(mcLoc("block/crimson_fungus")))
            .build();
    public static final Supplier<BaseCakeBlock> POISON_CAKE = new CakeBuilder("poison_cake")
            .models(CakeModel.FROM_VANILLA, CakeModel.FROM_VANILLA)
            .customItemModel()
            .build();
    public static final Supplier<BaseCakeBlock> TNT_CAKE = new CakeBuilder("tnt_cake")
            .setBothClasses(TNTCakeBlock::new, TNTCandleCakeBlock::new)
            .models(CakeModel.FROM_VANILLA, CakeModel.FROM_VANILLA)
            .customItemModel()
            .build();
    public static final Supplier<BaseCakeBlock> CUPCAKE = new CakeBuilder("cupcake")
            .setCakeClass(CupcakeBlock::new)
            .model(CakeModel.CUSTOM)
            .disallowCandles()
            .noItem()
            .nutrition(1)
            .saturationModifier(0.3F)
            .build();
    public static final Supplier<BaseCakeBlock> WARPED_FUNGUS_CAKE = new CakeBuilder("warped_fungus_cake").build();
    public static final Supplier<BaseCakeBlock> RED_VELVET_CAKE = new CakeBuilder("red_velvet_cake").build();
    public static final Supplier<BaseCakeBlock> GLOW_BERRY_CAKE = new CakeBuilder("glow_berry_cake")
            .setCakeProperties(cakeProperties().lightLevel(state -> 7))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 7))
            .build();
    public static final Supplier<BaseCakeBlock> OBSIDIAN_CAKE = new CakeBuilder("obsidian_cake")
            .setCakeClass(ObsidianCakeBlock::new)
            .setCakeProperties(cakeProperties().sound(SoundType.STONE).strength(12.5F, 300))
            .setCandleCakeProperties(candleCakeProperties().sound(SoundType.STONE).strength(12.5F, 300))
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> SCULK_CAKE = new CakeBuilder("sculk_cake")
            .setCakeClass(SculkCakeBlock::new)
            .setCakeProperties(cakeProperties().lightLevel(state -> 1)
                    .emissiveRendering((state, getter, pos) -> SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE))
            .models(CakeModel.CUSTOM, CakeModel.DEFAULT)
            .alwaysEat()
            .build();

    public static final Supplier<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).friction(0.98F).randomTicks().strength(0.5F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).pushReaction(PushReaction.NORMAL)), true);
    public static final Supplier<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))), true);
    public static final Supplier<Block> CAKE_STAND = register("cake_stand", () -> new CakeStandBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(0.8F).noOcclusion()), true);

    public static <T extends Block> Supplier<T> register(String name, Supplier<T> block, boolean hasItem) {
        if (hasItem) {
            return Services.REGISTRY.registerBlock(name, block);
        }
        return Services.REGISTRY.registerBlockNoItem(name, block);
    }

    public static BlockBehaviour.Properties cakeProperties() {
        return BlockBehaviour.Properties.copy(Blocks.CAKE);
    }

    public static BlockBehaviour.Properties candleCakeProperties() {
        return BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE);
    }

    public static void init() {
    }
}
