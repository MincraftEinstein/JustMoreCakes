package einstein.jmc.data.packs.providers;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static einstein.jmc.data.packs.ModItemTags.*;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagProvider blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<Supplier<BaseCakeBlock>, CakeVariant> sortedCakes = Util.createValueSortedMap(CakeVariant.VARIANT_BY_CAKE, Comparator.comparing(CakeVariant::getCakeName));

        sortedCakes.forEach((cake, variant) -> {
            if (!cake.get().asItem().equals(Items.AIR)) {
                getOrCreateTagBuilder(CAKES).add(cake.get().asItem());
            }
        });

        getOrCreateTagBuilder(CHEESE_CAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        getOrCreateTagBuilder(CHEESECAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        getOrCreateTagBuilder(CHEESE).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(CHEESES).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(RED_DYE).addOptionalTag(DYE_RED)
                .addOptionalTag(ConventionalItemTags.RED_DYES);
        getOrCreateTagBuilder(SEEDS).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);
        getOrCreateTagBuilder(SLIME_BALLS).add(Items.SLIME_BALL);
        getOrCreateTagBuilder(C_CAKES).addTag(CAKES);
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.CAKE_SPATULA.get(), ModItems.WHISK.get());
        getOrCreateTagBuilder(C_MILKS).add(Items.MILK_BUCKET);
        getOrCreateTagBuilder(C_FOOD_MILK).addTag(C_MILKS);
        getOrCreateTagBuilder(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).addTag(C_CAKES);
    }
}
