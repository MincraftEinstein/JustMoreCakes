package einstein.jmc.blocks;

import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeTieredCakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
    		Shapes.or(Block.box(3, 15, 3, 13, 21, 13), //0 uneaten
    				Block.box(1, 0, 1, 15, 8, 15),
    				Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(5, 15, 3, 13, 21, 13), //1
    				Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(7, 15, 3, 13, 21, 13), //2
    				Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(9, 15, 3, 13, 21, 13), //3
    				Block.box(1, 0, 1, 15, 8, 15),
    				Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(11, 15, 3, 13, 21, 13), //4
					Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(2, 8, 2, 14, 15, 14), //5
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(4, 8, 2, 14, 15, 14), //6
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(6, 8, 2, 14, 15, 14), //7
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(8, 8, 2, 14, 15, 14), //8
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(10, 8, 2, 14, 15, 14), //9
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Block.box(1, 0, 1, 15, 8, 15), //10
    		Block.box(4, 0, 1, 15, 8, 15), //11
    		Block.box(5, 0, 1, 15, 8, 15), //12
    		Block.box(7, 0, 1, 15, 8, 15), //13
    		Block.box(9, 0, 1, 15, 8, 15), //14
    		Block.box(11, 0, 1, 15, 8, 15) //15
    };
    
    public ThreeTieredCakeBlock(CakeBuilder builder) {
        super(builder, 15);
    }

	@Override
	public IntegerProperty getBites() {
		return BITES;
	}

	@Override
	public VoxelShape[] getShapeByBite() {
		return SHAPE_BY_BITE;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return ((getBiteCount() + 1) - state.getValue(getBites()));
	}
}
