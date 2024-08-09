package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.packs.ModItemTags;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<Supplier<BaseCakeBlock>, CakeVariant> sortedCakes = Util.createValueSortedMap(CakeVariant.VARIANT_BY_CAKE, Comparator.comparing(CakeVariant::getCakeName));

        sortedCakes.forEach((cake, variant) -> {
            if (!cake.get().asItem().equals(Items.AIR)) {
                tag(ModItemTags.CAKES).add(cake.get().asItem());
            }
        });

        tag(ModItemTags.CHEESE_CAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        tag(ModItemTags.CHEESECAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseItem().get());
        tag(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED.location())
                .addOptionalTag(ModItemTags.RED_DYES.location())
                .addOptionalTag(Tags.Items.DYES_RED.location());
        tag(ModItemTags.SLIME_BALLS).addTag(Tags.Items.SLIMEBALLS);
        tag(ModItemTags.C_CAKES).addTag(ModItemTags.CAKES);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.CAKE_SPATULA.get(), ModItems.WHISK.get());
    }
}
