package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.packs.ModItemTags;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagsProvider extends ItemTagsProvider {

    public static final TagKey<Item> F_CHEESE = ItemTags.create(new ResourceLocation("forge", "cheese"));
    public static final TagKey<Item> F_CAKES = ItemTags.create(new ResourceLocation("forge", "cakes"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<Supplier<BaseCakeBlock>, CakeBuilder> sortedCakes = Util.createValueSortedMap(CakeBuilder.BUILDER_BY_CAKE, Comparator.comparing(CakeBuilder::getCakeName));

        sortedCakes.forEach((cake, cakeBuilder) -> {
            if (!cake.get().asItem().equals(Items.AIR)) {
                tag(ModItemTags.CAKES).add(cake.get().asItem());
            }
        });

        tag(F_CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESE_CAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseCake().get().asItem(), ModBlocks.CHEESECAKE_FAMILY.getTwoTieredCake().get().asItem(), ModBlocks.CHEESECAKE_FAMILY.getThreeTieredCake().get().asItem());
        tag(ModItemTags.CHEESECAKES).add(ModBlocks.CHEESECAKE_FAMILY.getBaseCake().get().asItem(), ModBlocks.CHEESECAKE_FAMILY.getTwoTieredCake().get().asItem(), ModBlocks.CHEESECAKE_FAMILY.getThreeTieredCake().get().asItem());
        tag(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED.location())
                .addOptionalTag(ModItemTags.RED_DYES.location())
                .addOptionalTag(Tags.Items.DYES_RED.location());
        tag(ModItemTags.SEEDS).addTag(Tags.Items.SEEDS);
        tag(ModItemTags.SLIME_BALLS).addTag(Tags.Items.SLIMEBALLS);
        tag(ModItemTags.C_CAKES).addTag(ModItemTags.CAKES);
        tag(F_CAKES).addTag(ModItemTags.CAKES);
    }
}
