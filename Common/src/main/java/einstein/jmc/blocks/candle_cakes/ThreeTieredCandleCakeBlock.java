package einstein.jmc.blocks.candle_cakes;

import com.google.common.collect.ImmutableList;
import einstein.jmc.blocks.cakes.BaseCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeTieredCandleCakeBlock extends BaseCandleCakeBlock {

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(3, 15, 3, 13, 21, 13),
            Block.box(1, 0, 1, 15, 8, 15),
            Block.box(2, 8, 2, 14, 15, 14),
            Block.box(7, 21, 7, 9, 27, 9));

    public ThreeTieredCandleCakeBlock(BaseCakeBlock originalCake, Properties properties) {
        super(originalCake, properties);
    }

    @Override
    protected double getCandleHeight() {
        return 1.3125D;
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return ImmutableList.of(new Vec3(0.5D, 1.8125D, 0.5D));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 16;
    }
}