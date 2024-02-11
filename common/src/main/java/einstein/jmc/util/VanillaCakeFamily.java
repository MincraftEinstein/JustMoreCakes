package einstein.jmc.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;

public class VanillaCakeFamily extends CakeFamily {

    private VanillaCakeFamily() {
        super(mcLoc("default"), "cake");
    }

    @Override
    public Supplier<? extends Block> getBaseCake() {
        return () -> Blocks.CAKE;
    }

    public static class Builder extends CakeFamily.Builder<VanillaCakeFamily> {

        public Builder() {
            super(new VanillaCakeFamily());
        }

        @Override
        public VanillaCakeFamily build() {
            family.baseCake = null;
            family.baseVariant = null;
            family.twoTieredVariant = twoTieredVariantBuilder.build();
            family.threeTieredVariant = threeTieredVariantBuilder.build();
            return super.build();
        }
    }
}
