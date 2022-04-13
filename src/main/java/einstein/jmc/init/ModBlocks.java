package einstein.jmc.init;

import java.util.function.Supplier;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.blocks.BirthdayCakeBlock;
import einstein.jmc.blocks.CakeOvenBlock;
import einstein.jmc.blocks.ChorusCakeBlock;
import einstein.jmc.blocks.CupcakeBlock;
import einstein.jmc.blocks.EncasingIceBlock;
import einstein.jmc.blocks.EnderCakeBlock;
import einstein.jmc.blocks.EnderCandleCakeBlock;
import einstein.jmc.blocks.GlowstoneCakeBlock;
import einstein.jmc.blocks.LavaCakeBlock;
import einstein.jmc.blocks.LavaCandleCakeBlock;
import einstein.jmc.blocks.RedstoneCakeBlock;
import einstein.jmc.blocks.RedstoneCandleCakeBlock;
import einstein.jmc.blocks.SlimeCakeBlock;
import einstein.jmc.blocks.SlimeCandleCakeBlock;
import einstein.jmc.blocks.TNTCakeBlock;
import einstein.jmc.blocks.ThreeTieredCakeBlock;
import einstein.jmc.blocks.ThreeTieredCandleCakeBlock;
import einstein.jmc.data.ModDataGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks
{
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JustMoreCakes.MODID);
	private static final BlockBehaviour.Properties CAKE = Properties.copy(Blocks.CAKE);
	private static final BlockBehaviour.Properties CANDLE_CAKE = Properties.copy(Blocks.CANDLE_CAKE);
	
	public static final RegistryObject<Block> CHOCOLATE_CAKE = registerCandleCake("chocolate_cake");
	public static final RegistryObject<Block> CARROT_CAKE = registerCandleCake("carrot_cake");
	public static final RegistryObject<Block> PUMPKIN_CAKE = registerCandleCake("pumpkin_cake");
	public static final RegistryObject<Block> MELON_CAKE = registerCandleCake("melon_cake");
	public static final RegistryObject<Block> APPLE_CAKE = registerCandleCake("apple_cake");
	public static final RegistryObject<Block> POISON_CAKE = registerCandleCake("poison_cake");
	public static final RegistryObject<Block> COOKIE_CAKE = registerCandleCake("cookie_cake");
    public static final RegistryObject<Block> TNT_CAKE = register("tnt_cake", () -> new TNTCakeBlock(CAKE));
    public static final RegistryObject<Block> GOLDEN_APPLE_CAKE = registerCandleCake("golden_apple_cake");
    public static final RegistryObject<Block> RED_MUSHROOM_CAKE = register("red_mushroom_cake", () -> new BaseCakeBlock(CAKE));
    public static final RegistryObject<Block> FIREY_CAKE = registerCandleCake("firey_cake");
    public static final RegistryObject<Block> REDSTONE_CAKE = register("redstone_cake", () -> new RedstoneCakeBlock(CAKE));
    public static final RegistryObject<Block> ENDER_CAKE = register("ender_cake", () -> new EnderCakeBlock(CAKE));
    public static final RegistryObject<Block> CHEESECAKE = registerCandleCake("cheesecake");
    public static final RegistryObject<Block> THREE_TIERED_CAKE = register("three_tiered_cake", () -> new ThreeTieredCakeBlock(CAKE));
    public static final RegistryObject<Block> SLIME_CAKE = register("slime_cake", () -> new SlimeCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Block> BIRTHDAY_CAKE = register("birthday_cake", () -> new BirthdayCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	final int i = state.getValue(BirthdayCakeBlock.BITES);
    	if (i < 1) {
    		return 9;
    	}
    	else {
    		return 0;
    	}
    })));
    public static final RegistryObject<Block> BEETROOT_CAKE = registerCandleCake("beetroot_cake");
    public static final RegistryObject<Block> LAVA_CAKE = register("lava_cake", () -> new LavaCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
		return 9;
	})));
    public static final RegistryObject<Block> CREEPER_CAKE = registerCandleCake("creeper_cake");
    public static final RegistryObject<Block> SEED_CAKE = registerCandleCake("seed_cake");
    public static final RegistryObject<Block> CUPCAKE = registerNoItem("cupcake", () -> new CupcakeBlock(CAKE));
    public static final RegistryObject<Block> BROWN_MUSHROOM_CAKE = register("brown_mushroom_cake", () -> new BaseCakeBlock(CAKE));
    public static final RegistryObject<Block> ICE_CAKE = registerCandleCake("ice_cake");
    public static final RegistryObject<Block> CHORUS_CAKE = register("chorus_cake", () -> new ChorusCakeBlock(CAKE));
    public static final RegistryObject<Block> CHIRSTMAS_CAKE = registerCandleCake("christmas_cake");
    public static final RegistryObject<Block> SPRINKLE_CAKE = registerCandleCake("sprinkle_cake");
    public static final RegistryObject<Block> SWEET_BERRY_CAKE = registerCandleCake("sweet_berry_cake");
    public static final RegistryObject<Block> HONEY_CAKE = registerCandleCake("honey_cake");
    public static final RegistryObject<Block> GLOWSTONE_CAKE = register("glowstone_cake", () -> new GlowstoneCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	return 12;
    })));
    public static final RegistryObject<Block> CRIMSON_FUNGUS_CAKE = register("crimson_fungus_cake", () -> new BaseCakeBlock(CAKE));
    public static final RegistryObject<Block> WARPED_FUNGUS_CAKE = registerCandleCake("warped_fungus_cake");
    public static final RegistryObject<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noDrops().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final RegistryObject<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))));
    
    public static void init(IEventBus modEventBus) {
    	BLOCKS.register(modEventBus);
    	
    	ModDataGenerators.CAKE_TYPES.add("redstone_cake");
		registerNoItem("candle_redstone_cake", () -> new RedstoneCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) REDSTONE_CAKE.get(), CANDLE_CAKE));
		for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
			String color = DyeColor.byId(i2).getName();
			registerNoItem(color + "_candle_redstone_cake", () -> new RedstoneCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) REDSTONE_CAKE.get(), CANDLE_CAKE));
		}
		
		ModDataGenerators.CAKE_TYPES.add("ender_cake");
		registerNoItem("candle_ender_cake", () -> new EnderCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) ENDER_CAKE.get(), CANDLE_CAKE));
		for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
			String color = DyeColor.byId(i2).getName();
			registerNoItem(color + "_candle_ender_cake", () -> new EnderCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) ENDER_CAKE.get(), CANDLE_CAKE));
		}
		
		ModDataGenerators.CAKE_TYPES.add("three_tiered_cake");
		registerNoItem("candle_three_tiered_cake", () -> new ThreeTieredCandleCakeBlock(Blocks.CANDLE, (ThreeTieredCakeBlock) THREE_TIERED_CAKE.get(), CANDLE_CAKE));
		for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
			String color = DyeColor.byId(i2).getName();
			registerNoItem(color + "_candle_three_tiered_cake", () -> new ThreeTieredCandleCakeBlock(getBlock(MCRL(color + "_candle")), (ThreeTieredCakeBlock) THREE_TIERED_CAKE.get(), CANDLE_CAKE));
		}
		
		ModDataGenerators.CAKE_TYPES.add("slime_cake");
		registerNoItem("candle_slime_cake", () -> new SlimeCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) SLIME_CAKE.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
		for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
			String color = DyeColor.byId(i2).getName();
			registerNoItem(color + "_candle_slime_cake", () -> new SlimeCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) SLIME_CAKE.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
		}
		
		ModDataGenerators.CAKE_TYPES.add("lava_cake");
		registerNoItem("candle_lava_cake", () -> new LavaCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) LAVA_CAKE.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
			return 9;
		})));
		for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
			String color = DyeColor.byId(i2).getName();
			registerNoItem(color + "_candle_lava_cake", () -> new LavaCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) LAVA_CAKE.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
				return 9;
			})));
		}
    }
    
    public static <T extends Block> RegistryObject<Block> register(final String name, final Supplier<T> block) {
    	final RegistryObject<Block> instance = BLOCKS.register(name, block);
		ModItems.ITEMS.register(name, () -> new BlockItem(instance.get(), new Item.Properties().tab(JustMoreCakes.JMC_TAB)));
    	return instance;
    }
    
    public static <T extends Block> RegistryObject<Block> registerNoItem(final String name, final Supplier<T> block) {
    	return BLOCKS.register(name, block);
    }
    
    public static RegistryObject<Block> registerCandleCake(final String name) {
		RegistryObject<Block> cake = register(name, () -> new BaseCakeBlock(CAKE));
		registerNoItem("candle_" + name, () -> new BaseCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) cake.get(), CANDLE_CAKE));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_" + name, () -> new BaseCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) cake.get(), CANDLE_CAKE));
		}
		ModDataGenerators.CAKE_TYPES.add(name);
    	return cake;
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
	
	public static ResourceLocation RL(String string) {
		return new ResourceLocation(JustMoreCakes.MODID, string);
	}
	
	public static ResourceLocation MCRL(String string) {
		return new ResourceLocation("minecraft", string);
	}
}