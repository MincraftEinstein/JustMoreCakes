package einstein.jmc.tags;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGenerator extends BlockTagsProvider {

	public BlockTagsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, JustMoreCakes.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get());
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes block tags";
	}
}
