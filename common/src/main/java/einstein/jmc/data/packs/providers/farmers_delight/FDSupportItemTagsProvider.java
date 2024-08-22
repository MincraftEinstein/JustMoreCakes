package einstein.jmc.data.packs.providers.farmers_delight;

import einstein.jmc.data.packs.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class FDSupportItemTagsProvider extends ItemTagsProvider {

    public FDSupportItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItemTags.FD_MINEABLE_KNIVES).addTag(ModItemTags.C_CAKES);
    }
}
