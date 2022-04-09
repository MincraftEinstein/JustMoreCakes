package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModBlocks
{
    public static final Block TNT_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "tnt_cake", new TNTCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
    public static final Block RED_MUSHROOM_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "red_mushroom_cake", new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
    public static final Block BIRTHDAY_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "birthday_cake", new BirthdayCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	final int i = state.getValue(BirthdayCakeBlock.BITES);
    	if (i < 1) {
    		return 9;
    	}
    	else {
    		return 0;
    	}
    })), JustMoreCakes.JMC_TAB);
    public static final Block CUPCAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "cupcake", new CupcakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final Block BROWN_MUSHROOM_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "brown_mushroom_cake", new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
    public static final Block CHORUS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "chorus_cake", new ChorusCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
    public static final Block GLOWSTONE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "glowstone_cake", new GlowstoneCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
    	return 12;
    })), JustMoreCakes.JMC_TAB);
    public static final Block CRIMSON_FUNGUS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "crimson_fungus_cake", new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
    public static final Block ENCASING_ICE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "encasing_ice", new EncasingIceBlock(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(2.5F, 5.0F).sound(SoundType.GLASS).noDrops().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));
    public static final Block CAKE_OVEN = RegistryHandler.registerBlock(JustMoreCakes.MODID, "cake_oven", new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(Blocks.litBlockEmission(13))), JustMoreCakes.JMC_TAB);
    
    @SubscribeEvent
    public static void onBlocksRegistered(RegistryEvent.Register<Block> event) {
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
				BaseCakeBlock redstoneCake = RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new RedstoneCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new RedstoneCandleCakeBlock(Blocks.CANDLE, redstoneCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new RedstoneCandleCakeBlock(getBlock(MCRL(color + "_candle")), redstoneCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else if (type == "ender") {
				BaseCakeBlock enderCake = RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new EnderCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new EnderCandleCakeBlock(Blocks.CANDLE, enderCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new EnderCandleCakeBlock(getBlock(MCRL(color + "_candle")), enderCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else if (type == "lava") {
				BaseCakeBlock lavaCake = RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new LavaCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel((state) -> {
					return 9;
				})), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new LavaCandleCakeBlock(Blocks.CANDLE, lavaCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
					return 9;
				})));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new LavaCandleCakeBlock(getBlock(MCRL(color + "_candle")), lavaCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).lightLevel((state) -> {
						return 9;
					})));
				}
			}
			else if (type == "slime") {
				BaseCakeBlock slimeCake =  RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new SlimeCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).sound(SoundType.SLIME_BLOCK)), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new SlimeCandleCakeBlock(Blocks.CANDLE, slimeCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new SlimeCandleCakeBlock(getBlock(MCRL(color + "_candle")), slimeCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE).sound(SoundType.SLIME_BLOCK)));
				}
			}
			else if (type == "three_tiered") {
				ThreeTieredCakeBlock threeTieredCake = RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new ThreeTieredCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new ThreeTieredCandleCakeBlock(Blocks.CANDLE, threeTieredCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new ThreeTieredCandleCakeBlock(getBlock(MCRL(color + "_candle")), threeTieredCake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
			else {
				BaseCakeBlock cake = RegistryHandler.registerBlock(JustMoreCakes.MODID, name, new BaseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)), JustMoreCakes.JMC_TAB);
				RegistryHandler.registerBlock(JustMoreCakes.MODID, "candle_" + name, new BaseCandleCakeBlock(Blocks.CANDLE, cake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					RegistryHandler.registerBlock(JustMoreCakes.MODID, color + "_candle_" + name, new BaseCandleCakeBlock(getBlock(MCRL(color + "_candle")), cake, BlockBehaviour.Properties.copy(Blocks.CANDLE_CAKE)));
				}
			}
    	}
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