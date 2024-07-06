package einstein.jmc.data.packs;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> CAKES = create("cakes");
    public static final TagKey<Block> BASE_CAKES = create("base_cakes");
    public static final TagKey<Block> TWO_TIERED_CAKES = create("two_tiered_cakes");
    public static final TagKey<Block> THREE_TIERED_CAKES = create("three_tiered_cakes");
    public static final TagKey<Block> CANDLE_CAKES = create("candle_cakes");
    public static final TagKey<Block> BASE_CANDLE_CAKES = create("base_candle_cakes");
    public static final TagKey<Block> TWO_TIERED_CANDLE_CAKES = create("two_tiered_candle_cakes");
    public static final TagKey<Block> THREE_TIERED_CANDLE_CAKES = create("three_tiered_candle_cakes");

    public static final TagKey<Block> C_CAKES = createC("cakes");
    public static final TagKey<Block> C_CANDLE_CAKES = createC("candle_cakes");
    public static final TagKey<Block> CAKE_SPATULA_USABLE = create("cake_spatula_usable");
    public static final TagKey<Block> CAKE_STAND_STORABLES = create("cake_stand_storables");

    private static TagKey<Block> createC(String string) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", string));
    }

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, JustMoreCakes.loc(string));
    }
}
