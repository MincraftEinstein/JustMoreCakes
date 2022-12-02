package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ItemAssetsGenerator extends ItemModelProvider {
	
	public ItemAssetsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, JustMoreCakes.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void registerModels() {
		generatedItem(getItemName(ModItems.CUPCAKE), modLoc("item/" + getItemName(ModItems.CUPCAKE)));
		generatedItem(getItemName(ModItems.CREAM_CHEESE), modLoc("item/" + getItemName(ModItems.CREAM_CHEESE)));
		generatedItem(ModBlocks.POISON_CAKE.getId().getPath(), mcLoc("item/cake"));
		generatedItem(ModBlocks.TNT_CAKE.getId().getPath(), mcLoc("item/cake"));

		getBuilder("encasing_ice").parent(getExistingFile(mcLoc("block/ice")));
		getBuilder("cake_oven").parent(getExistingFile(modLoc("block/cake_oven")));

		for (RegistryObject<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
			if (!cake.get().getBuilder().hasCustomItemModel()) {
				generatedItem(getItemName(cake), modLoc("item/" + cake.getId().getPath()));
			}
		}
	}

	private ItemModelBuilder generatedItem(String name, ResourceLocation... layers) {
		ItemModelBuilder model = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			model = model.texture("layer" + i, layers[i]);
		}
		return model;
	}
	
	private String getItemName(RegistryObject<? extends ItemLike> item) {
		return item.getId().getPath();
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes item assets";
	}
}
