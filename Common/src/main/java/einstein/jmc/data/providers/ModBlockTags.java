package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> CAKE_SPATULA_USABLE = create("cake_spatula_usable");

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, JustMoreCakes.loc(string));
    }
}
