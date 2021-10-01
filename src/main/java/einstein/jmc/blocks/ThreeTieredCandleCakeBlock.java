package einstein.jmc.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import einstein.jmc.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeTieredCandleCakeBlock extends AbstractCandleBlock {
	public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
	protected static final float AABB_OFFSET = 1.0F;
	protected static final VoxelShape SHAPE = Shapes.or(
			Block.box(3, 15, 3, 13, 21, 13),
			Block.box(1, 0, 1, 15, 8, 15),
			Block.box(2, 8, 2, 14, 15, 14),
			Block.box(7, 21, 7, 9, 27, 9));
	private static final Map<Block, ThreeTieredCandleCakeBlock> BY_CANDLE = Maps.newHashMap();
	private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5D, 1.0D, 0.5D));
	protected ThreeTieredCakeBlock originalCake;

	public ThreeTieredCandleCakeBlock(Block p_152859_, BlockBehaviour.Properties p_152860_) {
		super(p_152860_);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
		BY_CANDLE.put(p_152859_, this);
	}
	
	protected Iterable<Vec3> getParticleOffsets(BlockState p_152868_) {
		return PARTICLE_OFFSETS;
	}
	
	public VoxelShape getShape(BlockState p_152875_, BlockGetter p_152876_, BlockPos p_152877_, CollisionContext p_152878_) {
		return SHAPE;
	}
	
	public void setOriginalCake(ThreeTieredCakeBlock cake) {
		originalCake = cake;
	}
	
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
			if (candleHit(hitResult) && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
				extinguish(player, state, level, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				InteractionResult interactionresult = originalCake.eat(level, pos, ModBlocks.getBlock(ModBlocks.RL(originalCake.getRegistryName().getPath())).defaultBlockState(), player);
				if (interactionresult.consumesAction()) {
					dropResources(state, level, pos);
				}
				return interactionresult;
			}
		}
		else {
			level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
			Block candleBlock = this;
			((ThreeTieredCandleCakeBlock) candleBlock).setOriginalCake(originalCake);
			level.setBlock(pos, candleBlock.defaultBlockState().setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
			level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
			itemstack.hurtAndBreak(1, player, (p_41303_) -> {
				p_41303_.broadcastBreakEvent(player.getUsedItemHand());
			});

			return InteractionResult.sidedSuccess(level.isClientSide());
		}
	}
	
	private static boolean candleHit(BlockHitResult p_152907_) {
		return p_152907_.getLocation().y - (double) p_152907_.getBlockPos().getY() > 0.5D;
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152905_) {
		p_152905_.add(LIT);
	}
	
	public ItemStack getCloneItemStack(BlockGetter p_152862_, BlockPos p_152863_, BlockState p_152864_) {
		return new ItemStack(originalCake.asItem());
	}
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState p_152898_, Direction p_152899_, BlockState p_152900_, LevelAccessor p_152901_, BlockPos p_152902_, BlockPos p_152903_) {
		return p_152899_ == Direction.DOWN && !p_152898_.canSurvive(p_152901_, p_152902_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152898_, p_152899_, p_152900_, p_152901_, p_152902_, p_152903_);
	}
	
	public boolean canSurvive(BlockState p_152891_, LevelReader p_152892_, BlockPos p_152893_) {
		return p_152892_.getBlockState(p_152893_.below()).getMaterial().isSolid();
	}
	
	public int getAnalogOutputSignal(BlockState p_152880_, Level p_152881_, BlockPos p_152882_) {
		return BaseCakeBlock.FULL_CAKE_SIGNAL;
	}
	
	public boolean hasAnalogOutputSignal(BlockState p_152909_) {
		return true;
	}
	
	public boolean isPathfindable(BlockState p_152870_, BlockGetter p_152871_, BlockPos p_152872_, PathComputationType p_152873_) {
		return false;
	}
}