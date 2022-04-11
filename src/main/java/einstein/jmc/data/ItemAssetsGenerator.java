package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.CakeTypes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemAssetsGenerator extends ItemModelProvider {

	public ItemAssetsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, JustMoreCakes.MODID, existingFileHelper);
	}
	
	@Override
	protected void registerModels() {
		for (int i = 0; i < CakeTypes.values().length; i++) {
			String name = CakeTypes.byId(i).getName();
			if (name == "cheese") {
				name += "cake";
			}
			else {
				name += "_cake";
			}
			
			if (name.contains("poison")) {
				generatedItem(name, mcLoc("item/cake"));
			}
			else {
				generatedItem(name, modLoc("item/" + name));
			}
		}
		generatedItem(getObjectName(ModBlocks.TNT_CAKE.get()), mcLoc("item/cake"));
		generatedItem(getObjectName(ModBlocks.RED_MUSHROOM_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.RED_MUSHROOM_CAKE.get())));
		generatedItem(getObjectName(ModBlocks.BROWN_MUSHROOM_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.BROWN_MUSHROOM_CAKE.get())));
		generatedItem(getObjectName(ModBlocks.BIRTHDAY_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.BIRTHDAY_CAKE.get())));
		generatedItem(getObjectName(ModBlocks.CUPCAKE.get()), modLoc("item/" + getObjectName(ModBlocks.CUPCAKE.get())));
		generatedItem(getObjectName(ModBlocks.CHORUS_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.CHORUS_CAKE.get())));
		generatedItem(getObjectName(ModBlocks.GLOWSTONE_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.GLOWSTONE_CAKE.get())));
		generatedItem(getObjectName(ModBlocks.CRIMSON_FUNGUS_CAKE.get()), modLoc("item/" + getObjectName(ModBlocks.CRIMSON_FUNGUS_CAKE.get())));
		generatedItem(getObjectName(ModItems.CUPCAKE.get()), modLoc("item/" + getObjectName(ModItems.CUPCAKE.get())));
		generatedItem(getObjectName(ModItems.CHEESE.get()), modLoc("item/" + getObjectName(ModItems.CHEESE.get())));
		blockItemModel(Blocks.ICE, "encasing_ice");
		blockItemModel(ModBlocks.CAKE_OVEN.get(), "cake_oven");
	}
	
	private void blockItemModel(Block block, String fileName) {
		String name = getObjectName(block);
		String modid = block.getRegistryName().getNamespace();
		getBuilder(fileName).parent(getExistingFile(new ResourceLocation(modid, "block/" + name)));
	}
	
	private ItemModelBuilder generatedItem(String name, ResourceLocation... layers) {
		ItemModelBuilder model = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			model = model.texture("layer" + i, layers[i]);
		}
		return model;
	}
	
	private String getObjectName(ItemLike object) {
		return object.asItem().getRegistryName().getPath();
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes item assets";
	}
}
