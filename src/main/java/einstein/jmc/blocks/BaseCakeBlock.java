package einstein.jmc.blocks;

import einstein.jmc.CakeEffects;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.CakeBuilder;
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
			Block.box(1, 0, 1, 15, 8, 15),
			Block.box(3, 0, 1, 15, 8, 15),
			Block.box(5, 0, 1, 15, 8, 15),
			Block.box(7, 0, 1, 15, 8, 15),
			Block.box(9, 0, 1, 15, 8, 15),
			Block.box(11, 0, 1, 15, 8, 15),
			Block.box(13, 0, 1, 15, 8, 15)
	};

	private final boolean allowsCandles;
	private final int biteCount;
	private CakeBuilder builder;
	private CakeEffects cakeEffects;

	protected BaseCakeBlock(CakeBuilder builder, int biteCount) {
		this(builder.getCakeProperties(), builder.allowsCandles(), biteCount);
		this.builder = builder;
	}

	public BaseCakeBlock(CakeBuilder builder) {
		this(builder, 6);
	}

	public BaseCakeBlock(Properties properties, boolean allowsCandles, int biteCount) {
		super(properties);
		this.allowsCandles = allowsCandles;
		this.biteCount = biteCount;
		registerDefaultState(stateDefinition.any().setValue(getBites(), 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return getShapeByBite()[state.getValue(getBites())];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		if (allowsCandles) {
			if (itemstack.is(ItemTags.CANDLES) && state.getValue(getBites()) == 0) {
				Block block = Block.byItem(item);
				if (block instanceof CandleBlock) {
					if (!player.isCreative()) {
						itemstack.shrink(1);
					}

					level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1, 1);
					Block candleCake = builder.getCandleCakeByCandle().get(block).get();
					level.setBlockAndUpdate(pos, candleCake.defaultBlockState());
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
		}
		else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			player.getFoodData().eat(2, 0.1F);
			eatActions(player, pos, state);

			if (cakeEffects != null) {
				for (CakeEffects.MobEffectHolder effect : cakeEffects.mobEffects()) {
					player.addEffect(new MobEffectInstance(effect.effect(), effect.duration().orElse(0), effect.amplifier().orElse(0)));
				}
			}

			int i = state.getValue(getBites()); 
			accessor.gameEvent(player, GameEvent.EAT, pos);

			if (i < getBiteCount()) {
				accessor.setBlock(pos, state.setValue(getBites(), i + 1), 3);
			}
			else {
				accessor.removeBlock(pos, false);
				accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
			}

			return InteractionResult.SUCCESS;
		}
	}

	public void eatActions(Player player, BlockPos pos, BlockState state) {
		if (equals(ModBlocks.FIREY_CAKE.get())) {
			player.setSecondsOnFire(ModServerConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
		}
		else if (equals(ModBlocks.ICE_CAKE.get())) {
			player.clearFire();
		}
		else if (equals(ModBlocks.CHORUS_CAKE.get())) {
			Util.teleportRandomly(player, ModServerConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
		}
		else if (equals(ModBlocks.ENDER_CAKE.get())) {
			Util.teleportRandomly(player, ModServerConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
			player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1, 1);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).getMaterial().isSolid();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getBites());
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return ((getBiteCount() + 1) - state.getValue(getBites())) * 2;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computation) {
		return false;
	}

	public IntegerProperty getBites() {
		return BITES;
	}

	public VoxelShape[] getShapeByBite() {
		return SHAPE_BY_BITE;
	}

	public int getBiteCount() {
		return biteCount;
	}

	public CakeBuilder getBuilder() {
		return builder;
	}

	public void setCakeEffects(CakeEffects cakeEffects) {
		this.cakeEffects = cakeEffects;
	}
}