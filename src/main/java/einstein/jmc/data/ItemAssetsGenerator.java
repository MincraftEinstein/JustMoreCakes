package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.Util;
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
		super(generator, JustMoreCakes.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void registerModels() {
		for (int i = 0; i < ModDataGenerators.CAKE_TYPES.size(); i++) {
			String name = ModDataGenerators.CAKE_TYPES.get(i);

			if (name.equals("poison_cake") || name.equals("tnt_cake")) {
				generatedItem(name, mcLoc("item/cake"));
			}
			else {
				generatedItem(name, modLoc("item/" + name));
			}
		}
		
		generatedItem(getItemName(ModBlocks.TNT_CAKE.get()), mcLoc("item/cake"));
		generatedItem(getItemName(ModBlocks.RED_MUSHROOM_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.RED_MUSHROOM_CAKE.get())));
		generatedItem(getItemName(ModBlocks.BROWN_MUSHROOM_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.BROWN_MUSHROOM_CAKE.get())));
		generatedItem(getItemName(ModBlocks.BIRTHDAY_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.BIRTHDAY_CAKE.get())));
		generatedItem(getItemName(ModBlocks.CUPCAKE.get()), modLoc("item/" + getItemName(ModBlocks.CUPCAKE.get())));
		generatedItem(getItemName(ModBlocks.CHORUS_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.CHORUS_CAKE.get())));
		generatedItem(getItemName(ModBlocks.GLOWSTONE_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.GLOWSTONE_CAKE.get())));
		generatedItem(getItemName(ModBlocks.CRIMSON_FUNGUS_CAKE.get()), modLoc("item/" + getItemName(ModBlocks.CRIMSON_FUNGUS_CAKE.get())));
		generatedItem(getItemName(ModItems.CUPCAKE.get()), modLoc("item/" + getItemName(ModItems.CUPCAKE.get())));
		generatedItem(getItemName(ModItems.CHEESE.get()), modLoc("item/" + getItemName(ModItems.CHEESE.get())));
		blockItemModel(Blocks.ICE, "encasing_ice");
		blockItemModel(ModBlocks.CAKE_OVEN.get(), "cake_oven");
	}
	
	private void blockItemModel(Block block, String fileName) {
		String name = getItemName(block);
		String modid = Util.getBlockRegistryName(block).getNamespace();
		getBuilder(fileName).parent(getExistingFile(new ResourceLocation(modid, "block/" + name)));
	}
	
	private ItemModelBuilder generatedItem(String name, ResourceLocation... layers) {
		ItemModelBuilder model = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			model = model.texture("layer" + i, layers[i]);
		}
		return model;
	}
	
	private String getItemName(ItemLike item) {
		return Util.getItemRegistryName(item.asItem()).getPath();
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes item assets";
	}
}
