package einstein.jmc.blocks.cakes;

import einstein.jmc.util.CakeBuilder;
import net.minecraft.world.level.block.EntityBlock;

public abstract class BaseEntityCakeBlock extends BaseCakeBlock implements EntityBlock {

    public BaseEntityCakeBlock(CakeBuilder builder) {
        super(builder);
    }
}