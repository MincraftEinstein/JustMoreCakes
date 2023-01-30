package einstein.jmc.init;

import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.blocks.*;
import einstein.jmc.platform.Services;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {

    public static final Map<Block, ResourceLocation> SUPPORTED_CANDLES = new HashMap<>();

    public static final Supplier<BaseCakeBlock> CHOCOLATE_CAKE = new CakeBuilder("chocolate_cake", true).build();
    public static final Supplier<BaseCakeBlock> CARROT_CAKE = new CakeBuilder("carrot_cake", true).build();
    public static final Supplier<BaseCakeBlock> PUMPKIN_CAKE = new CakeBuilder("pumpkin_cake", true).build();
    public static final Supplier<BaseCakeBlock> MELON_CAKE = new CakeBuilder("melon_cake", true).build();
    public static final Supplier<BaseCakeBlock> APPLE_CAKE = new CakeBuilder("apple_cake", true).build();
    public static final Supplier<BaseCakeBlock> POISON_CAKE = new CakeBuilder("poison_cake", true, true, true).build();
    public static final Supplier<BaseCakeBlock> COOKIE_CAKE = new CakeBuilder("cookie_cake", true).build();
    public static final Supplier<BaseCakeBlock> TNT_CAKE = new CakeBuilder("tnt_cake", true, true, true)
            .setCakeClass(TNTCakeBlock::new)
            .setCandleCakeClass((originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, TNTCakeBlockEntity::new))
            .build();
    public static final Supplier<BaseCakeBlock> GOLDEN_APPLE_CAKE = new CakeBuilder("golden_apple_cake", true).build();
    public static final Supplier<BaseCakeBlock> RED_MUSHROOM_CAKE = new CakeBuilder("red_mushroom_cake", false, true).build();
    public static final Supplier<BaseCakeBlock> FIREY_CAKE = new CakeBuilder("firey_cake", true).build();
    public static final Supplier<BaseCakeBlock> REDSTONE_CAKE = new CakeBuilder("redstone_cake", true)
            .setCakeClass(RedstoneCakeBlock::new)
            .setCandleCakeClass(RedstoneCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> ENDER_CAKE = new CakeBuilder("ender_cake", true)
            .setCakeClass(EnderCakeBlock::new)
            .setCandleCakeClass(EnderCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> CHEESECAKE = new CakeBuilder("cheesecake", true).build();
    public static final Supplier<BaseCakeBlock> THREE_TIERED_CAKE = new CakeBuilder("three_tiered_cake", true, true)
            .setCakeClass(ThreeTieredCakeBlock::new)
            .setCandleCakeClass(ThreeTieredCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> SLIME_CAKE = new CakeBuilder("slime_cake", true)
            .setCakeProperties(cakeProperties().sound(SoundType.SLIME_BLOCK))
            .setCandleCakeProperties(candleCakeProperties().sound(SoundType.SLIME_BLOCK))
            .setCakeClass(SlimeCakeBlock::new)
            .setCandleCakeClass(SlimeCandleCakeBlock::new)
            .build();
    public static final Supplier<BaseCakeBlock> BEETROOT_CAKE = new CakeBuilder("beetroot_cake", true).build();
    public static final Supplier<BaseCakeBlock> LAVA_CAKE = new CakeBuilder("lava_cake", true)
            .setCakeClass(LavaCakeBlock::new)
            .setCandleCakeClass(LavaCandleCakeBlock::new)
            .setCakeProperties(cakeProperties().lightLevel(state -> 9))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 9))
            .build();
    public static final Supplier<BaseCakeBlock> CREEPER_CAKE = new CakeBuilder("creeper_cake", true).build();
    public static final Supplier<BaseCakeBlock> SEED_CAKE = new CakeBuilder("seed_cake", true).build();
    public static final Supplier<Block> CUPCAKE = Services.REGISTRY.registerBlockNoItem("cupcake", () -> new CupcakeBlock(cakeProperties())); // Don't replace with CakeBuilder so a custom BlockItem cake be used
    public static final Supplier<BaseCakeBlock> BROWN_MUSHROOM_CAKE = new CakeBuilder("brown_mushroom_cake", false, true).build();
    public static final Supplier<BaseCakeBlock> ICE_CAKE = new CakeBuilder("ice_cake", true).build();
    public static final Supplier<BaseCakeBlock> CHORUS_CAKE = new CakeBuilder("chorus_cake", false, true).build();
    public static final Supplier<BaseCakeBlock> SWEET_BERRY_CAKE = new CakeBuilder("sweet_berry_cake", true).build();
    public static final Supplier<BaseCakeBlock> HONEY_CAKE = new CakeBuilder("honey_cake", true).build();
    public static final Supplier<BaseCakeBlock> GLOWSTONE_CAKE = new CakeBuilder("glowstone_cake", true)
            .setCakeProperties(cakeProperties().lightLevel(state -> 12))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 12))
            .setCakeClass(GlowstoneCakeBlock::new)
            .setCandleCakeClass((originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, GlowstoneCakeBlockEntity::new))
            .build();
    public static final Supplier<BaseCakeBlock> CRIMSON_FUNGUS_CAKE = new CakeBuilder("crimson_fungus_cake", false, true).build();
    public static final Supplier<BaseCakeBlock> WARPED_FUNGUS_CAKE = new CakeBuilder("warped_fungus_cake", true).build();
    public static final Supplier<BaseCakeBlock> RED_VELVET_CAKE = new CakeBuilder("red_velvet_cake", true).build();
    public static final Supplier<BaseCakeBlock> GLOW_BERRY_CAKE = new CakeBuilder("glow_berry_cake", true)
            .setCakeProperties(cakeProperties().lightLevel(state -> 7))
            .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 7))
            .build();
    public static final Supplier<BaseCakeBlock> OBSIDIAN_CAKE = new CakeBuilder("obsidian_cake", true)
            .setCakeClass(ObsidianCakeBlock::new)
            .setCakeProperties(cakeProperties().sound(SoundType.STONE).strength(12.5F, 300))
            .setCandleCakeProperties(candleCakeProperties().sound(SoundType.STONE).strength(12.5F, 300))
            .build();

    public static final Supplier<Block> ENCASING_ICE = Services.REGISTRY.registerBlock("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final Supplier<Block> CAKE_OVEN = Services.REGISTRY.registerBlock("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))));

    public static BlockBehaviour.Properties cakeProperties() {
        return BlockBehaviour.Properties.copy(Blocks.CAKE);
    }

    public static BlockBehaviour.Properties candleCakeProperties() {
        return BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE);
    }

    public static void init() {}
}
