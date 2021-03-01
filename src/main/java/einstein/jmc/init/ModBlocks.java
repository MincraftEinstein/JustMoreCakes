package einstein.jmc.init;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.JustMoreCakes.JMCItemGroup;
import einstein.jmc.blocks.BeetrootCakeBlock;
import einstein.jmc.blocks.BirthdayCakeBlock;
import einstein.jmc.blocks.ChorusCakeBlock;
import einstein.jmc.blocks.CupcakeBlock;
import einstein.jmc.blocks.EnderCakeBlock;
import einstein.jmc.blocks.FireyCakeBlock;
import einstein.jmc.blocks.EncasingIceBlock;
import einstein.jmc.blocks.GlowstoneCakeBlock;
import einstein.jmc.blocks.GoldenAppleCakeBlock;
import einstein.jmc.blocks.IceCake;
import einstein.jmc.blocks.LavaCakeBlock;
import einstein.jmc.blocks.PoisonCakeBlock;
import einstein.jmc.blocks.RedstoneCake;
import einstein.jmc.blocks.SlimeCakeBlock;
import einstein.jmc.blocks.TNTCakeBlock;
import einstein.jmc.blocks.TripleDeckerCakeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks
{
    public static final Block CHOCOLATE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "chocolate_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block CARROT_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "carrot_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block PUMPKIN_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "pumpkin_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block MELON_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "melon_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block APPLE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "apple_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block POISON_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "poison_cake", new PoisonCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block COOKIE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "cookie_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block TNT_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "tnt_cake", new TNTCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block GOLDEN_APPLE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "golden_apple_cake", new GoldenAppleCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block RED_MUSHROOM_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "red_mushroom_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block FIREY_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "firey_cake", new FireyCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block REDSTONE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "redstone_cake", new RedstoneCake(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block ENDER_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "ender_cake", new EnderCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block CHEESECAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "cheesecake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block TRIPLE_DECKER_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "triple_decker_cake", new TripleDeckerCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block SLIME_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "slime_cake", new SlimeCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.SLIME).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block BIRTHDAY_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "birthday_cake", new BirthdayCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F).setLightLevel((state) -> {
    	final BlockState blockState = state;
    	final int i = blockState.get(BirthdayCakeBlock.BITES);
    	if (i < 1) {
    		return 9;
    	}
    	else {
    		return 0;
    	}
    })), JMCItemGroup.instance);
    public static final Block BEETROOT_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "beetroot_cake", new BeetrootCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block LAVA_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "lava_cake", new LavaCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F).setLightLevel((state) -> {
        return 9;
    })), JMCItemGroup.instance);
    public static final Block CREEPER_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "creeper_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block SEED_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "seed_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block CUPCAKE = RegistryHandler.registerBlockNoItem(JustMoreCakes.MODID, "cupcake", new CupcakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)));
    public static final Block BROWN_MUSHROOM_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "brown_mushroom_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block ICE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "ice_cake", new IceCake(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block CHORUS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "chorus_cake", new ChorusCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block CHRISTMAS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "christmas_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block SPRINKLE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "sprinkle_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block SWEET_BERRY_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "sweet_berry_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block HONEY_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "honey_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block GLOWSTONE_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "glowstone_cake", new GlowstoneCakeBlock(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F).setLightLevel((state) -> {
    	return 12;
    })), JMCItemGroup.instance);
    public static final Block CRIMSON_FUNGUS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "crimson_fungus_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block WARPED_FUNGUS_CAKE = RegistryHandler.registerBlock(JustMoreCakes.MODID, "warped_fungus_cake", new CakeBlockBase(Block.Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F)), JMCItemGroup.instance);
    public static final Block ENCASING_ICE = RegistryHandler.registerBlockNoItem(JustMoreCakes.MODID, "encasing_ice", new EncasingIceBlock(Block.Properties.create(Material.ICE).slipperiness(0.98F).tickRandomly().hardnessAndResistance(2.5F, 5.0F).sound(SoundType.GLASS).noDrops().notSolid().setAllowsSpawn(Blocks::neverAllowSpawn).setOpaque(Blocks::isntSolid).setSuffocates(Blocks::isntSolid).setBlocksVision(Blocks::isntSolid)));
    
}