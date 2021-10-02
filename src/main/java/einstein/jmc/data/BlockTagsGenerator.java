package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGenerator extends BlockTagsProvider {

	public BlockTagsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, JustMoreCakes.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
	}
}
