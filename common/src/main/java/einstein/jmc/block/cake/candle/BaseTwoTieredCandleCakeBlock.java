package einstein.jmc.block.cake.candle;

import com.google.common.collect.ImmutableList;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseTwoTieredCandleCakeBlock extends BaseCandleCakeBlock {

    protected static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 1, 15, 8, 15), // Lower
            box(2, 8, 2, 14, 15, 14), // Top
            box(7, 15, 7, 9, 21, 9) // Candle
    );

    public BaseTwoTieredCandleCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(parentCake, candle, properties);
    }

    @Override
    protected double getCandleHeight() {
        return 0.9375;
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return ImmutableList.of(new Vec3(0.5D, 1.4375D, 0.5D));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BaseTwoTieredCakeBlock getParentCake() {
        return (BaseTwoTieredCakeBlock) super.getParentCake();
    }
}
