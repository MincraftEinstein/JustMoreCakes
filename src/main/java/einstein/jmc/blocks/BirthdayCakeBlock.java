package einstein.jmc.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BirthdayCakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 8);
    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
    		Block.box(1, 0, 1, 15, 8, 15), //0 uneaten lit
    		Block.box(1, 0, 1, 15, 8, 15), //1 uneaten unlit
    		Shapes.or(Block.box(14, 0, 1, 15, 8, 2),
    				Block.box(1, 0, 1, 8, 8, 15),
    				Block.box(8, 0, 7, 15, 8, 15),
    				Block.box(11, 0, 4, 15, 8, 7),
    				Block.box(9, 0, 6, 11, 8, 7),
    				Block.box(13, 0, 2, 15, 8, 4),
    				Block.box(10, 0, 5, 11, 8, 6),
    				Block.box(12, 0, 3, 13, 8, 4)),
    		Shapes.or(Block.box(1, 0, 1, 8, 8, 15), //3
    				Block.box(8, 0, 8, 15, 8, 15)),
    		Shapes.or(Block.box(14, 0, 14, 15, 8, 15), //4
					Block.box(1, 0, 1, 8, 8, 15),
					Block.box(8, 0, 11, 12, 8, 15),
					Block.box(12, 0, 13, 14, 8, 15),
					Block.box(8, 0, 9, 10, 8, 11),
					Block.box(8, 0, 8, 9, 8, 9),
					Block.box(10, 0, 10, 11, 8, 11),
					Block.box(12, 0, 12, 13, 8, 13)),
    		Block.box(1, 0, 1, 8, 8, 15), //5
    		Shapes.or(Block.box(5, 0, 10, 6, 8, 11), //6
					Block.box(1, 0, 1, 8, 8, 9),
					Block.box(1, 0, 9, 5, 8, 12),
					Block.box(1, 0, 12, 3, 8, 14),
					Block.box(5, 0, 9, 7, 8, 10),
					Block.box(1, 0, 14, 2, 8, 15),
					Block.box(3, 0, 12, 4, 8, 13)),
    		Block.box(1.0, 0.0, 1.0, 8.0, 8.0, 8.0), //7
    		Shapes.or(Block.box(7, 0, 7, 8, 8, 8), //8
					Block.box(4, 0, 1, 8, 8, 5),
					Block.box(2, 0, 1, 4, 8, 3),
					Block.box(6, 0, 5, 8, 8, 7),
					Block.box(1, 0, 1, 2, 8, 2),
					Block.box(3, 0, 3, 4, 8, 4),
					Block.box(5, 0, 5, 6, 8, 6))
    };
    
    public BirthdayCakeBlock(Properties properties) {
        super(properties, false, 8);
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
    public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
    	int i = state.getValue(BITES);

		if (i == 0) {
			accessor.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1F, 1);
		}
		else {
			if (!player.canEat(false)) {
	            return InteractionResult.PASS;
	        }
	        player.awardStat(Stats.EAT_CAKE_SLICE);
			eatActions(player, pos, state);
			accessor.gameEvent(player, GameEvent.EAT, pos);
		}

		if (i < getBiteCount()) {
			accessor.setBlock(pos, state.setValue(BITES, i + 1), 3);
		} else {
			accessor.removeBlock(pos, false);
			accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(getBites()) == 0) {
            double x = pos.getX() + 0.5F;
            double y = pos.getY() + 0.95F;
            double z = pos.getZ() + 0.5F;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
