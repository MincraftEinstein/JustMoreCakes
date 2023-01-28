package einstein.jmc.data.providers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> CHEESE = create("cheese");
    public static final TagKey<Item> CHEESES = create("cheeses");
    public static final TagKey<Item> CHEESE_CAKES = create("cheese_cakes");
    public static final TagKey<Item> CHEESECAKES = create("cheesecakes");
    public static final TagKey<Item> RED_DYE = create("red_dye");
    public static final TagKey<Item> RED_DYES = create("red_dyes");
    public static final TagKey<Item> DYE_RED = create("dye_red");
    public static final TagKey<Item> SEEDS = create("seeds");
    public static final TagKey<Item> SLIME_BALLS = create("slime_balls");

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", string));
    }
}
