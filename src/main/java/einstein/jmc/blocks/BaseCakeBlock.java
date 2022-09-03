package einstein.jmc.blocks;

import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseCakeBlock extends Block {

	public static final IntegerProperty BITES = BlockStateProperties.BITES;
	protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)
	};
	
	public BaseCakeBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
	}
	
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_BITE[state.getValue(BITES)];
	}
	
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		String name = Util.getBlockRegistryName(asBlock()).getPath();
		if (!name.contains("red_mushroom_cake") && !name.contains("brown_mushroom_cake") && !name.contains("chorus_cake") && !name.contains("crimson_fungus_cake")) {
			if (itemstack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
				Block block = Block.byItem(item);
				if (block instanceof CandleBlock) {
					if (!player.isCreative()) {
						itemstack.shrink(1);
					}

					level.playSound((Player) null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
					String candle = Util.getBlockRegistryName(block).getPath();
					Block candleBlock = ModBlocks.getBlock(ModBlocks.loc(candle + "_" + name));
					level.setBlockAndUpdate(pos, candleBlock.defaultBlockState());
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					player.awardStat(Stats.ITEM_USED.get(item));
					return InteractionResult.SUCCESS;
				}
			}
		}
		
		if (level.isClientSide) {
			if (eat(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
			
			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		
		return eat(level, pos, state, player);
	}
	
	public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			((BaseCakeBlock) state.getBlock()).eatActions(player);
			String name = Util.getBlockRegistryName(asBlock()).getPath();
			if (name.contains("poison_cake")) {
				player.addEffect(new MobEffectInstance(MobEffects.POISON, ModServerConfigs.POISON_CAKE_POISON_DUR.get(), ModServerConfigs.POISON_CAKE_POISON_STRENGTH.get()));
			}
			else if (name.contains("golden_apple_cake")) {
		        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.GAPPLE_CAKE_REGEN_DUR.get(), ModServerConfigs.GAPPLE_CAKE_REGEN_STRENGTH.get()));
		        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ModServerConfigs.GAPPLE_CAKE_RES_DUR.get(), ModServerConfigs.GAPPLE_CAKE_RES_STRENGTH.get()));
		        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ModServerConfigs.GAPPLE_CAKE_ABSORPTION_DUR.get(), ModServerConfigs.GAPPLE_CAKE_ABSORPTION_STRENGTH.get()));
			}
			else if (name.contains("firey_cake")) {
		        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ModServerConfigs.FIREY_CAKE_FIRE_RES_DUR.get(), ModServerConfigs.FIREY_CAKE_FIRE_RES_STRENGTH.get()));
		        player.setSecondsOnFire(ModServerConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
			}
			else if (name.contains("beetroot_cake")) {
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.BEETROOT_CAKE_REGEN_DUR.get(), ModServerConfigs.BEETROOT_CAKE_REGEN_STRENGTH.get()));
			}
			else if (name.contains("ice_cake")) {
		        player.clearFire();
		        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, ModServerConfigs.ICE_CAKE_NIGHT_VISION_DUR.get(), ModServerConfigs.ICE_CAKE_NIGHT_VISION_STRENGTH.get()));
		        player.addEffect(new MobEffectInstance(ModPotions.FREEZING_EFFECT.get()));
		        FreezingEffect.freezeEntity(player);
			}
			else if (name.contains("glow_berry_cake")) {
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200));
				player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1800));
			}
			int i = state.getValue(BITES); 
			accessor.gameEvent(player, GameEvent.EAT, pos);
			if (i < 6) { // Number must be same as BITES
				accessor.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
			} else {
				accessor.removeBlock(pos, false);
				accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
			}
			return InteractionResult.SUCCESS;
		}
	}
	
	public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
	}
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction direction, BlockState p_51215_, LevelAccessor accessor, BlockPos pos, BlockPos p_51218_) {
		return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, p_51215_, accessor, pos, p_51218_);
	}
	
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).getMaterial().isSolid();
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}
	
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return (7 - state.getValue(BITES)) * 2;
	}
	
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computation) {
		return false;
	}
}