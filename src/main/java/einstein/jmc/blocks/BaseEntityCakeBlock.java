package einstein.jmc.blocks;

import einstein.jmc.util.CakeBuilder;
import net.minecraft.world.level.block.EntityBlock;

public abstract class BaseEntityCakeBlock extends BaseCakeBlock implements EntityBlock {

	public BaseEntityCakeBlock(Properties properties, CakeBuilder builder) {
		super(properties, builder);
	}
}