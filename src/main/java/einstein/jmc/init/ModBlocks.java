package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.blocks.*;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JustMoreCakes.MOD_ID);

	public static final RegistryObject<BaseCakeBlock> CHOCOLATE_CAKE = new CakeBuilder("chocolate_cake", true).build();
	public static final RegistryObject<BaseCakeBlock> CARROT_CAKE = new CakeBuilder("carrot_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> PUMPKIN_CAKE = new CakeBuilder("pumpkin_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> MELON_CAKE = new CakeBuilder("melon_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> APPLE_CAKE = new CakeBuilder("apple_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> POISON_CAKE = new CakeBuilder("poison_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> COOKIE_CAKE = new CakeBuilder("cookie_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> TNT_CAKE = new CakeBuilder("tnt_cake", true)
        .setCakeClass((properties, allowsCandles) -> new TNTCakeBlock(properties))
        .setCandleCakeClass((originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, TNTCakeBlockEntity::new))
        .build();
    public static final RegistryObject<BaseCakeBlock> GOLDEN_APPLE_CAKE = new CakeBuilder("golden_apple_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> RED_MUSHROOM_CAKE = new CakeBuilder("red_mushroom_cake", false).build();
    public static final RegistryObject<BaseCakeBlock> FIREY_CAKE = new CakeBuilder("firey_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> REDSTONE_CAKE = new CakeBuilder("redstone_cake", true)
        .setCakeClass((properties, allowsCandles) -> new RedstoneCakeBlock(properties))
        .setCandleCakeClass(RedstoneCandleCakeBlock::new)
        .build();
    public static final RegistryObject<BaseCakeBlock> ENDER_CAKE = new CakeBuilder("ender_cake", true)
        .setCakeClass((properties, allowsCandles) -> new EnderCakeBlock(properties))
        .setCandleCakeClass(EnderCandleCakeBlock::new)
        .build();
    public static final RegistryObject<BaseCakeBlock> CHEESECAKE = new CakeBuilder("cheesecake", true).build();
    public static final RegistryObject<BaseCakeBlock> THREE_TIERED_CAKE = new CakeBuilder("three_tiered_cake", true)
        .setCakeClass((properties, allowsCandles) -> new ThreeTieredCakeBlock(properties))
        .setCandleCakeClass(ThreeTieredCandleCakeBlock::new)
        .build();
    public static final RegistryObject<BaseCakeBlock> SLIME_CAKE = new CakeBuilder("slime_cake", true)
            .setCakeProperties(cakeProperties().sound(SoundType.SLIME_BLOCK))
            .setCandleCakeProperties(candleCakeProperties().sound(SoundType.SLIME_BLOCK))
            .setCakeClass((properties, allowsCandles) -> new SlimeCakeBlock(properties))
            .setCandleCakeClass(SlimeCandleCakeBlock::new)
            .build();
    public static final RegistryObject<BaseCakeBlock> BEETROOT_CAKE = new CakeBuilder("beetroot_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> LAVA_CAKE = new CakeBuilder("lava_cake", true)
        .setCakeClass((properties, allowsCandles) -> new LavaCakeBlock(properties))
        .setCandleCakeClass(LavaCandleCakeBlock::new)
        .setCakeProperties(cakeProperties().lightLevel(state -> 9))
        .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 9))
        .build();
    public static final RegistryObject<BaseCakeBlock> CREEPER_CAKE = new CakeBuilder("creeper_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> SEED_CAKE = new CakeBuilder("seed_cake", true).build();
    public static final RegistryObject<Block> CUPCAKE = registerNoItem("cupcake", () -> new CupcakeBlock(cakeProperties())); // Don't replace with CakeBuilder so a custom BlockItem cake be used
    public static final RegistryObject<BaseCakeBlock> BROWN_MUSHROOM_CAKE = new CakeBuilder("brown_mushroom_cake", false).build();
    public static final RegistryObject<BaseCakeBlock> ICE_CAKE = new CakeBuilder("ice_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> CHORUS_CAKE = new CakeBuilder("chorus_cake", false)
        .setCakeClass((properties, allowsCandles) -> new ChorusCakeBlock(properties))
        .build();
    public static final RegistryObject<BaseCakeBlock> SWEET_BERRY_CAKE = new CakeBuilder("sweet_berry_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> HONEY_CAKE = new CakeBuilder("honey_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> GLOWSTONE_CAKE = new CakeBuilder("glowstone_cake", true)
        .setCakeProperties(cakeProperties().lightLevel(state -> 12))
        .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 12))
        .setCakeClass((properties, allowsCandles) -> new GlowstoneCakeBlock(properties))
        .setCandleCakeClass((originalCake, properties) -> new BaseEntityCandleCakeBlock<>(originalCake, properties, GlowstoneCakeBlockEntity::new))
        .build();
    public static final RegistryObject<BaseCakeBlock> CRIMSON_FUNGUS_CAKE = new CakeBuilder("crimson_fungus_cake", false).build();
    public static final RegistryObject<BaseCakeBlock> WARPED_FUNGUS_CAKE = new CakeBuilder("warped_fungus_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> RED_VELVET_CAKE = new CakeBuilder("red_velvet_cake", true).build();
    public static final RegistryObject<BaseCakeBlock> GLOW_BERRY_CAKE = new CakeBuilder("glow_berry_cake", true)
        .setCakeProperties(cakeProperties().lightLevel(state -> 7))
        .setCandleCakeProperties(candleCakeProperties().lightLevel(state -> 7))
        .build();

    public static final RegistryObject<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final RegistryObject<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))));

    public static <T extends Block> RegistryObject<T> register(final String name, final Supplier<T> block) {
        final RegistryObject<T> instance = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(instance.get(), new Item.Properties().tab(JustMoreCakes.JMC_TAB)));
        return instance;
    }

    public static <T extends Block> RegistryObject<T> registerNoItem(final String name, final Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static BlockBehaviour.Properties cakeProperties() {
        return Properties.copy(Blocks.CAKE);
    }

    public static BlockBehaviour.Properties candleCakeProperties() {
        return Properties.copy(Blocks.CANDLE_CAKE);
    }

    public static Block getBlock(ResourceLocation location) {
        Block block = ForgeRegistries.BLOCKS.getValue(location);
        if (block != Blocks.AIR) {
            return block;
        }
        else {
            throw new NullPointerException("Could not find block: " + location.toString());
        }
    }

    public static ResourceLocation loc(String string) {
        return new ResourceLocation(JustMoreCakes.MOD_ID, string);
    }

    public static ResourceLocation mcLoc(String string) {
        return new ResourceLocation("minecraft", string);
    }
}