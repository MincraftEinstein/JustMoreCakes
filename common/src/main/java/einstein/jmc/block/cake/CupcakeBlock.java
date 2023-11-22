package einstein.jmc.block.cake;

import einstein.jmc.util.CakeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CupcakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 1);
    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
            Shapes.or(Block.box(6, 0, 6, 10, 5, 10),
                    Block.box(6, 3, 5, 10, 4, 6),
                    Block.box(6, 3, 10, 10, 4, 11),
                    Block.box(10, 3, 6, 11, 4, 10),
                    Block.box(5, 3, 6, 6, 4, 10),
                    Block.box(7, 5, 7, 9, 6, 9)),
            Shapes.or(Block.box(8, 0, 6, 10, 5, 10),
                    Block.box(8, 3, 5, 10, 4, 6),
                    Block.box(8, 3, 10, 10, 4, 11),
                    Block.box(10, 3, 6, 11, 4, 10),
                    Block.box(8, 5, 7, 9, 6, 9))
    };

    public CupcakeBlock(CakeBuilder builder) {
        super(builder, 1);
    }

    @Override
    public IntegerProperty getBites() {
        return BITES;
    }

    @Override
    public VoxelShape[] getShapeByBite() {
        return SHAPE_BY_BITE;
    }
}
