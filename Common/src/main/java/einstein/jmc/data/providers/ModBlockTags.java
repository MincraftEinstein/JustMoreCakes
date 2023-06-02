package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> CAKES = create("cakes");
    public static final TagKey<Block> CANDLE_CAKES = create("candle_cakes");
    public static final TagKey<Block> C_CAKES = createC("cakes");
    public static final TagKey<Block> C_CANDLE_CAKES = createC("candle_cakes");
    public static final TagKey<Block> CAKE_SPATULA_USABLE = create("cake_spatula_usable");

    private static TagKey<Block> createC(String string) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("c", string));
    }

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, JustMoreCakes.loc(string));
    }
}
