package einstein.jmc.block.cake;

import einstein.jmc.util.CakeVariant;
import einstein.jmc.util.CakeFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BaseTwoTieredCakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 10);
    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
            Shapes.or(Block.box(2, 8, 2, 14, 15, 14), //0 uneaten
                    Block.box(1, 0, 1, 15, 8, 15)),
            Shapes.or(Block.box(4, 8, 2, 14, 15, 14), //1
                    Block.box(1, 0, 1, 15, 8, 15)),
            Shapes.or(Block.box(6, 8, 2, 14, 15, 14), //2
                    Block.box(1, 0, 1, 15, 8, 15)),
            Shapes.or(Block.box(8, 8, 2, 14, 15, 14), //3
                    Block.box(1, 0, 1, 15, 8, 15)),
            Shapes.or(Block.box(10, 8, 2, 14, 15, 14), //4
                    Block.box(1, 0, 1, 15, 8, 15)),
            Block.box(1, 0, 1, 15, 8, 15), //5
            Block.box(4, 0, 1, 15, 8, 15), //6
            Block.box(5, 0, 1, 15, 8, 15), //7
            Block.box(7, 0, 1, 15, 8, 15), //8
            Block.box(9, 0, 1, 15, 8, 15), //9
            Block.box(11, 0, 1, 15, 8, 15) //10
    };

    public BaseTwoTieredCakeBlock(CakeVariant builder) {
        super(builder, 10);
    }

    @Override
    public @Nullable IntegerProperty getBites() {
        return BITES;
    }

    @Override
    public VoxelShape[] getShapeByBite(BlockState state) {
        return SHAPE_BY_BITE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        CakeFamily family = getFamily();

        if (family != null) {
            if (stack.is(family.getBaseCake().get().asItem()) && isUneaten(state, pos, level)) {
                BlockPos abovePos = pos.above();
                if (level.getBlockState(abovePos).canBeReplaced()) {
                    BlockState newState = family.getThreeTieredCake().get().defaultBlockState();

                    level.setBlockAndUpdate(abovePos, newState);
                    level.setBlockAndUpdate(pos, BaseThreeTieredCakeBlock.createLowerState(newState.getBlock(), true));
                    Block.pushEntitiesUp(Blocks.AIR.defaultBlockState(), newState, level, abovePos);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, abovePos);
                    level.playSound(null, abovePos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, 1);
                    player.awardStat(Stats.ITEM_USED.get(Items.CAKE));

                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return ((getSlices() + 1) - state.getValue(getBites()));
    }
}
