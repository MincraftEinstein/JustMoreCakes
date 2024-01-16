package einstein.jmc.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;

public class VanillaCakeFamily extends CakeFamily {

    public VanillaCakeFamily() {
        super(mcLoc("default"), "cake");
        baseBuilder = null;
        baseCake = null;
        twoTieredCake = twoTieredBuilder.build();
        threeTieredCake = threeTieredBuilder.build();

        REGISTERED_CAKE_FAMILIES.put(getRegistryKey(), this);
    }

    @Override
    public Supplier<? extends Block> getBaseCake() {
        return () -> Blocks.CAKE;
    }
}
