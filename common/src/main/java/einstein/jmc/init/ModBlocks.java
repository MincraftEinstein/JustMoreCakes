package einstein.jmc.init;

import einstein.jmc.blocks.cakes.*;
import einstein.jmc.blocks.candle_cakes.*;
import einstein.jmc.blocks.entities.GlowstoneCakeBlockEntity;
import einstein.jmc.blocks.entities.TNTCakeBlockEntity;
import einstein.jmc.blocks.*;
import einstein.jmc.platform.Services;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {

    public static final Supplier<BaseCakeBlock> CHOCOLATE_CAKE = new CakeBuilder("chocolate_cake").build();
    public static final Supplier<BaseCakeBlock> CARROT_CAKE = new CakeBuilder("carrot_cake").build();
    public static final Supplier<BaseCakeBlock> PUMPKIN_CAKE = new CakeBuilder("pumpkin_cake").build();
    public static final Supplier<BaseCakeBlock> MELON_CAKE = new CakeBuilder("melon_cake")
            .nutrition(1)
            .saturation(0.4F)
            .build();
    public static final Supplier<BaseCakeBlock> APPLE_CAKE = new CakeBuilder("apple_cake").build();
    public static final Supplier<BaseCakeBlock> POISON_CAKE = new CakeBuilder("poison_cake")
            .customBlockModel()
            .customCandleCakeBlockModels()
            .customItemModel()
            .build();
    public static final Supplier<BaseCakeBlock> COOKIE_CAKE = new CakeBuilder("cookie_cake").build();
    public static final Supplier<BaseCakeBlock> TNT_CAKE = new CakeBuilder("tnt_cake")
            .setBothClasses(TNTCakeBlock::new, (originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, TNTCakeBlockEntity::new))
            .customBlockModel()
            .customCandleCakeBlockModels()
            .customItemModel()
            .build();
    public static final Supplier<BaseCakeBlock> GOLDEN_APPLE_CAKE = new CakeBuilder("golden_apple_cake")
            .nutrition(4)
            .saturation(0.5F)
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> RED_MUSHROOM_CAKE = new CakeBuilder("red_mushroom_cake")
            .customBlockModel()
            .build();
    public static final Supplier<BaseCakeBlock> FIREY_CAKE = new CakeBuilder("firey_cake").build();
    public static final Supplier<BaseCakeBlock> REDSTONE_CAKE = new CakeBuilder("redstone_cake")
            .setBothClasses(RedstoneCakeBlock::new, RedstoneCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> ENDER_CAKE = new CakeBuilder("ender_cake")
            .setBothClasses(EnderCakeBlock::new, EnderCandleCakeBlock::new)
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> CHEESECAKE = new CakeBuilder("cheesecake").build();
    public static final Supplier<BaseCakeBlock> THREE_TIERED_CAKE = new CakeBuilder("three_tiered_cake")
            .setBothClasses(ThreeTieredCakeBlock::new, ThreeTieredCandleCakeBlock::new)
            .customBlockModel()
            .customCandleCakeBlockModels()
            .build();
    public static final Supplier<BaseCakeBlock> SLIME_CAKE = new CakeBuilder("slime_cake")
            .setCakeProperties(cakeProperties().sound(SoundType.SLIME_BLOCK))
            .setCandleCakeProperties(candleCakeProperties().sound(SoundType.SLIME_BLOCK))
            .setBothClasses(SlimeCakeBlock::new, SlimeCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> BEETROOT_CAKE = new CakeBuilder("beetroot_cake")
            .nutrition(3)
            .saturation(0.4F)
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> LAVA_CAKE = new CakeBuilder("lava_cake")
            .setBothClasses(LavaCakeBlock::new, LavaCandleCakeBlock::new)
            .setCakeProperties(cakeProperties().lightLevel(state -> 9))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 9))
            .build();
    public static final Supplier<BaseCakeBlock> CREEPER_CAKE = new CakeBuilder("creeper_cake")
            .setCakeClass(CreeperCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> SEED_CAKE = new CakeBuilder("seed_cake").build();
    public static final Supplier<BaseCakeBlock> CUPCAKE = new CakeBuilder("cupcake")
            .setCakeClass(CupcakeBlock::new)
            .customBlockModel()
            .disallowCandles()
            .noItem()
            .nutrition(1)
            .saturation(0.3F)
            .build();
    public static final Supplier<BaseCakeBlock> BROWN_MUSHROOM_CAKE = new CakeBuilder("brown_mushroom_cake")
            .customBlockModel()
            .build();
    public static final Supplier<BaseCakeBlock> ICE_CAKE = new CakeBuilder("ice_cake").build();
    public static final Supplier<BaseCakeBlock> CHORUS_CAKE = new CakeBuilder("chorus_cake")
            .customBlockModel()
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> SWEET_BERRY_CAKE = new CakeBuilder("sweet_berry_cake")
            .nutrition(1)
            .saturation(0.5F)
            .build();
    public static final Supplier<BaseCakeBlock> HONEY_CAKE = new CakeBuilder("honey_cake")
            .saturation(0.4F)
            .build();
    public static final Supplier<BaseCakeBlock> GLOWSTONE_CAKE = new CakeBuilder("glowstone_cake")
            .setCakeProperties(cakeProperties().lightLevel(state -> 12))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 12))
            .setBothClasses(GlowstoneCakeBlock::new, (originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, GlowstoneCakeBlockEntity::new))
            .build();
    public static final Supplier<BaseCakeBlock> CRIMSON_FUNGUS_CAKE = new CakeBuilder("crimson_fungus_cake")
            .customBlockModel()
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
            .saturation(0)
            .alwaysEat()
            .build();
    public static final Supplier<BaseCakeBlock> SCULK_CAKE = new CakeBuilder("sculk_cake")
            .setCakeClass(SculkCakeBlock::new)
            .setCakeProperties(cakeProperties().lightLevel(state -> 1)
                    .emissiveRendering((state, getter, pos) -> SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE))
            .customBlockModel()
            .customCandleCakeBlockModels()
            .alwaysEat()
            .build();

    public static final Supplier<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).pushReaction(PushReaction.NORMAL)), true);
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
