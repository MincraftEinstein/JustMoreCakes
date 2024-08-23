package einstein.jmc.data.packs;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> CHEESE = createC("cheese");
    public static final TagKey<Item> CHEESES = createC("cheeses");
    public static final TagKey<Item> CHEESE_CAKES = createC("cheese_cakes");
    public static final TagKey<Item> CHEESECAKES = createC("cheesecakes");
    public static final TagKey<Item> RED_DYE = createC("red_dye");
    public static final TagKey<Item> RED_DYES = createC("red_dyes");
    public static final TagKey<Item> DYE_RED = createC("dye_red");
    public static final TagKey<Item> SEEDS = createC("seeds");
    public static final TagKey<Item> SLIME_BALLS = createC("slime_balls");
    public static final TagKey<Item> CAKES = create("cakes");
    public static final TagKey<Item> C_CAKES = createC("cakes");
    public static final TagKey<Item> FD_KNIVES = create(JustMoreCakes.fdLoc("tools/knives"));
    public static final TagKey<Item> C_FOODS_MILK = createC("foods/milk");
    public static final TagKey<Item> C_MILKS = createC("milks");

    private static TagKey<Item> createC(String name) {
        return create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Item> create(String name) {
        return create(JustMoreCakes.loc(name));
    }

    private static TagKey<Item> create(ResourceLocation location) {
        return TagKey.create(Registries.ITEM, location);
    }
}
