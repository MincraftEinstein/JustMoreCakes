package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.*;
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

import java.util.function.Supplier;

public class ModBlocks {

	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JustMoreCakes.MOD_ID);

	public static final RegistryObject<Block> CHOCOLATE_CAKE = registerCandleCake("chocolate_cake");
	public static final RegistryObject<Block> CARROT_CAKE = registerCandleCake("carrot_cake");
	public static final RegistryObject<Block> PUMPKIN_CAKE = registerCandleCake("pumpkin_cake");
	public static final RegistryObject<Block> MELON_CAKE = registerCandleCake("melon_cake");
	public static final RegistryObject<Block> APPLE_CAKE = registerCandleCake("apple_cake");
	public static final RegistryObject<Block> POISON_CAKE = registerCandleCake("poison_cake");
	public static final RegistryObject<Block> COOKIE_CAKE = registerCandleCake("cookie_cake");
    public static final RegistryObject<Block> TNT_CAKE = register("tnt_cake", () -> new TNTCakeBlock(cakeProperties()));
    public static final RegistryObject<Block> GOLDEN_APPLE_CAKE = registerCandleCake("golden_apple_cake");
    public static final RegistryObject<Block> RED_MUSHROOM_CAKE = register("red_mushroom_cake", () -> new BaseCakeBlock(cakeProperties(), false));
    public static final RegistryObject<Block> FIREY_CAKE = registerCandleCake("firey_cake");
    public static final RegistryObject<Block> REDSTONE_CAKE = register("redstone_cake", () -> new RedstoneCakeBlock(cakeProperties()));
    public static final RegistryObject<Block> ENDER_CAKE = register("ender_cake", () -> new EnderCakeBlock(cakeProperties()));
    public static final RegistryObject<Block> CHEESECAKE = registerCandleCake("cheesecake");
    public static final RegistryObject<Block> THREE_TIERED_CAKE = register("three_tiered_cake", () -> new ThreeTieredCakeBlock(cakeProperties()));
    public static final RegistryObject<Block> SLIME_CAKE = register("slime_cake", () -> new SlimeCakeBlock(cakeProperties().sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Block> BIRTHDAY_CAKE = register("birthday_cake", () -> new BirthdayCakeBlock(cakeProperties().lightLevel(state -> {
    	if (state.getValue(BirthdayCakeBlock.BITES) < 1) {
    		return 9;
    	}
    	else {
    		return 0;
    	}
    })));
    public static final RegistryObject<Block> BEETROOT_CAKE = registerCandleCake("beetroot_cake");
    public static final RegistryObject<Block> LAVA_CAKE = register("lava_cake", () -> new LavaCakeBlock(cakeProperties().lightLevel(state -> 9)));
    public static final RegistryObject<Block> CREEPER_CAKE = registerCandleCake("creeper_cake");
    public static final RegistryObject<Block> SEED_CAKE = registerCandleCake("seed_cake");
    public static final RegistryObject<Block> CUPCAKE = registerNoItem("cupcake", () -> new CupcakeBlock(cakeProperties()));
    public static final RegistryObject<Block> BROWN_MUSHROOM_CAKE = register("brown_mushroom_cake", () -> new BaseCakeBlock(cakeProperties(), false));
    public static final RegistryObject<Block> ICE_CAKE = registerCandleCake("ice_cake");
    public static final RegistryObject<Block> CHORUS_CAKE = register("chorus_cake", () -> new ChorusCakeBlock(cakeProperties()));
    public static final RegistryObject<Block> CHIRSTMAS_CAKE = registerCandleCake("christmas_cake");
    public static final RegistryObject<Block> SPRINKLE_CAKE = registerCandleCake("sprinkle_cake");
    public static final RegistryObject<Block> SWEET_BERRY_CAKE = registerCandleCake("sweet_berry_cake");
    public static final RegistryObject<Block> HONEY_CAKE = registerCandleCake("honey_cake");
    public static final RegistryObject<Block> GLOWSTONE_CAKE = register("glowstone_cake", () -> new GlowstoneCakeBlock(cakeProperties().lightLevel(state -> 12)));
    public static final RegistryObject<Block> CRIMSON_FUNGUS_CAKE = register("crimson_fungus_cake", () -> new BaseCakeBlock(cakeProperties(), false));
    public static final RegistryObject<Block> WARPED_FUNGUS_CAKE = registerCandleCake("warped_fungus_cake");
	public static final RegistryObject<Block> RED_VELVET_CAKE = registerCandleCake("red_velvet_cake");
	public static final RegistryObject<Block> GLOW_BERRY_CAKE = registerCandleCake("glow_berry_cake");

    public static final RegistryObject<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final RegistryObject<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))));
    
    public static void init(IEventBus modEventBus) {
    	BLOCKS.register(modEventBus);
    	
    	ModDataGenerators.CAKE_TYPES.add("redstone_cake");
		registerNoItem("candle_redstone_cake", () -> new RedstoneCandleCakeBlock((BaseCakeBlock) REDSTONE_CAKE.get(), candleCakeProperties()));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_redstone_cake", () -> new RedstoneCandleCakeBlock((BaseCakeBlock) REDSTONE_CAKE.get(), candleCakeProperties()));
		}
		
		ModDataGenerators.CAKE_TYPES.add("ender_cake");
		registerNoItem("candle_ender_cake", () -> new EnderCandleCakeBlock((BaseCakeBlock) ENDER_CAKE.get(), candleCakeProperties()));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_ender_cake", () -> new EnderCandleCakeBlock((BaseCakeBlock) ENDER_CAKE.get(), candleCakeProperties()));
		}
		
		ModDataGenerators.CAKE_TYPES.add("three_tiered_cake");
		registerNoItem("candle_three_tiered_cake", () -> new ThreeTieredCandleCakeBlock((ThreeTieredCakeBlock) THREE_TIERED_CAKE.get(), candleCakeProperties()));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_three_tiered_cake", () -> new ThreeTieredCandleCakeBlock((ThreeTieredCakeBlock) THREE_TIERED_CAKE.get(), candleCakeProperties()));
		}
		
		ModDataGenerators.CAKE_TYPES.add("slime_cake");
		registerNoItem("candle_slime_cake", () -> new SlimeCandleCakeBlock((BaseCakeBlock) SLIME_CAKE.get(), candleCakeProperties().sound(SoundType.SLIME_BLOCK)));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_slime_cake", () -> new SlimeCandleCakeBlock((BaseCakeBlock) SLIME_CAKE.get(), candleCakeProperties().sound(SoundType.SLIME_BLOCK)));
		}
		
		ModDataGenerators.CAKE_TYPES.add("lava_cake");
		registerNoItem("candle_lava_cake", () -> new LavaCandleCakeBlock((BaseCakeBlock) LAVA_CAKE.get(), candleCakeProperties().lightLevel(state -> 9)));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_lava_cake", () -> new LavaCandleCakeBlock((BaseCakeBlock) LAVA_CAKE.get(), candleCakeProperties().lightLevel(state -> 9)));
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
		RegistryObject<Block> cake = register(name, () -> new BaseCakeBlock(cakeProperties()));
		registerNoItem("candle_" + name, () -> new BaseCandleCakeBlock((BaseCakeBlock) cake.get(), candleCakeProperties()));
		for (int i = 0; i < DyeColor.values().length; i++) {
			String color = DyeColor.byId(i).getName();
			registerNoItem(color + "_candle_" + name, () -> new BaseCandleCakeBlock((BaseCakeBlock) cake.get(), candleCakeProperties()));
		}

		ModDataGenerators.CAKE_TYPES.add(name);
    	return cake;
    }

	private static BlockBehaviour.Properties cakeProperties() {
		return Properties.copy(Blocks.CAKE);
	}
	
	private static BlockBehaviour.Properties candleCakeProperties() {
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