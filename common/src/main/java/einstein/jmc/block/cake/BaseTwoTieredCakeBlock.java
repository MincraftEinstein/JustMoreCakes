package einstein.jmc.block.cake;

import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CakeFamily family = getFamily();

        if (family != null) {
            if (stack.is(family.getBaseCake().get().asItem())) {
                if (CakeUtil.convertToThreeTiered(family, state, pos, level, player, stack, false).consumesAction()) {
                    return ItemInteractionResult.SUCCESS;
                }
                else if (state.getValue(getBites()) == 5) {
                    if (CakeUtil.convertToTwoTiered(family, state, pos, level, player, stack, true).consumesAction()) {
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
