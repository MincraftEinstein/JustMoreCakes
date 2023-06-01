package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
	
	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, JustMoreCakes.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void registerModels() {
		generatedItem(getItemName(ModItems.CUPCAKE), modLoc("item/" + getItemName(ModItems.CUPCAKE)));
		generatedItem(getItemName(ModItems.CREAM_CHEESE), modLoc("item/" + getItemName(ModItems.CREAM_CHEESE)));
		generatedItem(getItemName(ModItems.CAKE_SPATULA), modLoc("item/" + getItemName(ModItems.CAKE_SPATULA)));
		generatedItem("poison_cake", mcLoc("item/cake"));
		generatedItem("tnt_cake", mcLoc("item/cake"));

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generatedItem(getItemName(ModItems.CUPCAKE), modLoc("item/" + getItemName(ModItems.CUPCAKE)));
        generatedItem(getItemName(ModItems.CREAM_CHEESE), modLoc("item/" + getItemName(ModItems.CREAM_CHEESE)));
        generatedItem("poison_cake", mcLoc("item/cake"));
        generatedItem("tnt_cake", mcLoc("item/cake"));

        getBuilder("encasing_ice").parent(getExistingFile(mcLoc("block/ice")));
        getBuilder("cake_oven").parent(getExistingFile(modLoc("block/cake_oven")));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            if (!cake.get().getBuilder().hasCustomItemModel()) {
                generatedItem(getItemName(cake), modLoc("item/" + Util.getItemId(cake.get().asItem()).getPath()));
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

    private String getItemName(Supplier<? extends ItemLike> item) {
        return Util.getItemId(item.get().asItem()).getPath();
    }
}
