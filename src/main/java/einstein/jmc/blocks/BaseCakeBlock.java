package einstein.jmc.blocks;

import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
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

/**
 * Copied from {@link}net.minecraft.world.level.block.CakeBlock
 * for the purpose of making some futures accessible from other
 * classes.
 */
public class BaseCakeBlock extends Block {
	public static final int MAX_BITES = 6;
	public static final IntegerProperty BITES = BlockStateProperties.BITES;
	public static final int FULL_CAKE_SIGNAL = getOutputSignal(0);
	protected static final float AABB_OFFSET = 1.0F;
	protected static final float AABB_SIZE_PER_BITE = 2.0F;
	protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)
	};
	
	public BaseCakeBlock(BlockBehaviour.Properties p_51184_) {
		super(p_51184_);
		this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
	}
	
	public VoxelShape getShape(BlockState p_51222_, BlockGetter p_51223_, BlockPos p_51224_, CollisionContext p_51225_) {
		return SHAPE_BY_BITE[p_51222_.getValue(BITES)];
	}
	
	public InteractionResult use(BlockState p_51202_, Level p_51203_, BlockPos p_51204_, Player p_51205_, InteractionHand p_51206_, BlockHitResult p_51207_) {
		ItemStack itemstack = p_51205_.getItemInHand(p_51206_);
		Item item = itemstack.getItem();
		String name = getRegistryName().getPath();
		if (!name.contains("red_mushroom_cake") && !name.contains("brown_mushroom_cake") && !name.contains("chorus_cake") && !name.contains("crimson_fungus_cake")) {
			if (itemstack.is(ItemTags.CANDLES) && p_51202_.getValue(BITES) == 0) {
				Block block = Block.byItem(item);
				if (block instanceof CandleBlock) {
					if (!p_51205_.isCreative()) {
						itemstack.shrink(1);
					}

					p_51203_.playSound((Player) null, p_51204_, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
					String candle = block.getRegistryName().getPath();
					Block candleBlock = ModBlocks.getBlock(ModBlocks.RL(candle + "_" + name));
					((BaseCandleCakeBlock) candleBlock).setOriginalCake(this);
					p_51203_.setBlockAndUpdate(p_51204_, candleBlock.defaultBlockState());
					p_51203_.gameEvent(p_51205_, GameEvent.BLOCK_CHANGE, p_51204_);
					p_51205_.awardStat(Stats.ITEM_USED.get(item));
					return InteractionResult.SUCCESS;
				}
			}
		}
		
		if (p_51203_.isClientSide) {
			if (eat(p_51203_, p_51204_, p_51202_, p_51205_).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
			
			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		
		return eat(p_51203_, p_51204_, p_51202_, p_51205_);
	}
	
	public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			((BaseCakeBlock) state.getBlock()).eatActions(player);
			String name = getRegistryName().getPath();
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
		        player.addEffect(new MobEffectInstance(ModPotions.FREEZING_EFFECT));
		        FreezingEffect.freezeEntity(player);
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
	public BlockState updateShape(BlockState p_51213_, Direction p_51214_, BlockState p_51215_, LevelAccessor p_51216_, BlockPos p_51217_, BlockPos p_51218_) {
		return p_51214_ == Direction.DOWN && !p_51213_.canSurvive(p_51216_, p_51217_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_51213_, p_51214_, p_51215_, p_51216_, p_51217_, p_51218_);
	}
	
	public boolean canSurvive(BlockState p_51209_, LevelReader p_51210_, BlockPos p_51211_) {
		return p_51210_.getBlockState(p_51211_.below()).getMaterial().isSolid();
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51220_) {
		p_51220_.add(BITES);
	}
	
	public int getAnalogOutputSignal(BlockState p_51198_, Level p_51199_, BlockPos p_51200_) {
		return getOutputSignal(p_51198_.getValue(BITES));
	}
	
	public static int getOutputSignal(int p_152747_) {
		return (7 - p_152747_) * 2;
	}
	
	public boolean hasAnalogOutputSignal(BlockState p_51191_) {
		return true;
	}
	
	public boolean isPathfindable(BlockState p_51193_, BlockGetter p_51194_, BlockPos p_51195_, PathComputationType p_51196_) {
		return false;
	}
}