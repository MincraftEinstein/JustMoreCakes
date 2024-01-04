package einstein.jmc.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class VanillaCakeFamily extends CakeFamily {

    public VanillaCakeFamily() {
        super("cake");
        baseBuilder = null;
        baseCake = null;
        twoTieredCake = twoTieredBuilder.build();
        threeTieredCake = threeTieredBuilder.build();

        REGISTERED_CAKE_FAMILIES.add(this);
    }

    @Override
    public Supplier<? extends Block> getBaseCake() {
        return () -> Blocks.CAKE;
    }
}
