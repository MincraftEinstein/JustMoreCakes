package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static einstein.jmc.data.packs.ModItemTags.*;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<Supplier<BaseCakeBlock>, CakeVariant> sortedCakes = Util.createValueSortedMap(CakeVariant.VARIANT_BY_CAKE, Comparator.comparing(CakeVariant::getCakeName));

        sortedCakes.forEach((cake, variant) -> {
            if (!cake.get().asItem().equals(Items.AIR)) {
                tag(CAKES).add(cake.get().asItem());
            }
        });

        tag(CHEESE_CAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        tag(CHEESECAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        tag(CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(CHEESES).add(ModItems.CREAM_CHEESE.get());
        tag(RED_DYE).addOptionalTag(DYE_RED.location())
                .addOptionalTag(RED_DYES.location())
                .addOptionalTag(Tags.Items.DYES_RED.location());
        tag(C_CAKES).addTag(CAKES);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.CAKE_SPATULA.get(), ModItems.WHISK.get());
        tag(C_MILKS).add(Items.MILK_BUCKET);
        tag(C_FOODS_MILK).addTag(C_MILKS);
        tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).addTag(C_CAKES);
    }
}
