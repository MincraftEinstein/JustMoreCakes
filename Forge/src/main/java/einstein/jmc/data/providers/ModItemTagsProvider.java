package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public static final TagKey<Item> CHEESE = ItemTags.create(new ResourceLocation("forge", "cheese"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
        tag(ModItemTags.CHEESE_CAKES).add(ModBlocks.CHEESECAKE.get().asItem());
        tag(ModItemTags.CHEESECAKES).add(ModBlocks.CHEESECAKE.get().asItem());
        tag(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED.location())
                .addOptionalTag(ModItemTags.RED_DYES.location())
                .addOptionalTag(Tags.Items.DYES_RED.location());
        tag(ModItemTags.SEEDS).addTag(Tags.Items.SEEDS);
        tag(ModItemTags.SLIME_BALLS).addTag(Tags.Items.SLIMEBALLS);
    }
}
