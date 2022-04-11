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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks
{
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JustMoreCakes.MODID);
	
    public static final RegistryObject<Block> TNT_CAKE = register("tnt_cake", () -> new TNTCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> RED_MUSHROOM_CAKE = register("red_mushroom_cake", () -> new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> BIRTHDAY_CAKE = register("birthday_cake", () -> new BirthdayCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	final int i = state.getValue(BirthdayCakeBlock.BITES);
    	if (i < 1) {
    		return 9;
    	}
    	else {
    		return 0;
    	}
    })));
    public static final RegistryObject<Block> CUPCAKE = BLOCKS.register("cupcake", () -> new CupcakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> BROWN_MUSHROOM_CAKE = register("brown_mushroom_cake", () -> new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> CHORUS_CAKE = register("chorus_cake", () -> new ChorusCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> GLOWSTONE_CAKE = register("glowstone_cake", () -> new GlowstoneCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	return 12;
    })));
    public static final RegistryObject<Block> CRIMSON_FUNGUS_CAKE = register("crimson_fungus_cake", () -> new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> ENCASING_ICE = register("encasing_ice", () -> new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noDrops().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final RegistryObject<Block> CAKE_OVEN = register("cake_oven", () -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))));
    
    public static void init(IEventBus modEventBus) {
    	BLOCKS.register(modEventBus);
    	for (int i = 0; i < CakeTypes.values().length; i++) {
    		String type = CakeTypes.byId(i).getName();
    		String name;
    		if (type == "cheese") {
    			name = type + "cake";
    		}
    		else {
    			name = type + "_cake";
    		}
    		
			if (type == "redstone") {
				RegistryObject<Block> redstoneCake = register(name, () -> new RedstoneCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
				registerNoItem("candle_" + name, () -> new RedstoneCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) redstoneCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new RedstoneCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) redstoneCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else if (type == "ender") {
				RegistryObject<Block> enderCake = register(name, () -> new EnderCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
				registerNoItem("candle_" + name, () -> new EnderCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) enderCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new EnderCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) enderCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else if (type == "lava") {
				RegistryObject<Block> lavaCake = register(name, () -> new LavaCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
					return 9;
				})));
				registerNoItem("candle_" + name, () -> new LavaCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) lavaCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
					return 9;
				})));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new LavaCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) lavaCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
						return 9;
					})));
				}
			}
			else if (type == "slime") {
				RegistryObject<Block> slimeCake = register(name, () -> new SlimeCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).sound(SoundType.SLIME_BLOCK)));
				registerNoItem("candle_" + name, () -> new SlimeCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) slimeCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new SlimeCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) slimeCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
				}
			}
			else if (type == "three_tiered") {
				RegistryObject<Block> threeTieredCake = register(name, () -> new ThreeTieredCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
				registerNoItem("candle_" + name, () -> new ThreeTieredCandleCakeBlock(Blocks.CANDLE, (ThreeTieredCakeBlock) threeTieredCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new ThreeTieredCandleCakeBlock(getBlock(MCRL(color + "_candle")), (ThreeTieredCakeBlock) threeTieredCake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else {
				RegistryObject<Block> cake = register(name, () -> new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
				registerNoItem("candle_" + name, () -> new BaseCandleCakeBlock(Blocks.CANDLE, (BaseCakeBlock) cake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					registerNoItem(color + "_candle_" + name, () -> new BaseCandleCakeBlock(getBlock(MCRL(color + "_candle")), (BaseCakeBlock) cake.get(), BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
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