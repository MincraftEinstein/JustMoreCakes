package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
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
		for (int i = 0; i < JustMoreCakes.CAKE_TYPES.size(); i++) {
			String name = JustMoreCakes.CAKE_TYPES.get(i);

			if (name.equals("poison_cake") || name.equals("tnt_cake")) {
				generatedItem(name, mcLoc("item/cake"));
			}
			else {
				generatedItem(name, modLoc("item/" + name));
			}
		}
		
		generatedItem(getItemName(ModBlocks.RED_MUSHROOM_CAKE), modLoc("item/" + getItemName(ModBlocks.RED_MUSHROOM_CAKE)));
		generatedItem(getItemName(ModBlocks.BROWN_MUSHROOM_CAKE), modLoc("item/" + getItemName(ModBlocks.BROWN_MUSHROOM_CAKE)));
		generatedItem(getItemName(ModBlocks.BIRTHDAY_CAKE), modLoc("item/" + getItemName(ModBlocks.BIRTHDAY_CAKE)));
		generatedItem(getItemName(ModBlocks.CUPCAKE), modLoc("item/" + getItemName(ModBlocks.CUPCAKE)));
		generatedItem(getItemName(ModBlocks.CHORUS_CAKE), modLoc("item/" + getItemName(ModBlocks.CHORUS_CAKE)));
		generatedItem(getItemName(ModBlocks.GLOWSTONE_CAKE), modLoc("item/" + getItemName(ModBlocks.GLOWSTONE_CAKE)));
		generatedItem(getItemName(ModBlocks.CRIMSON_FUNGUS_CAKE), modLoc("item/" + getItemName(ModBlocks.CRIMSON_FUNGUS_CAKE)));
		generatedItem(getItemName(ModItems.CUPCAKE), modLoc("item/" + getItemName(ModItems.CUPCAKE)));
		generatedItem(getItemName(ModItems.CHEESE), modLoc("item/" + getItemName(ModItems.CHEESE)));

		getBuilder("encasing_ice").parent(getExistingFile(mcLoc("block/ice")));
		getBuilder("cake_oven").parent(getExistingFile(modLoc("block/cake_oven")));
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
