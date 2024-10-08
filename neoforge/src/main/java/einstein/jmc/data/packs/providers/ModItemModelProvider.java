package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generatedItem(getItemName(ModItems.CUPCAKE), modLoc("item/" + getItemName(ModItems.CUPCAKE)));
        generatedItem(getItemName(ModItems.CREAM_CHEESE), modLoc("item/" + getItemName(ModItems.CREAM_CHEESE)));
        layeredItem(getItemName(ModItems.WHISK), mcLoc("handheld"), modLoc("item/" + getItemName(ModItems.WHISK)));
        generatedItem(getItemName(ModItems.CAKE_DOUGH), modLoc("item/" + getItemName(ModItems.CAKE_DOUGH)));
        generatedItem(getItemName(ModItems.CUPCAKE_DOUGH), modLoc("item/" + getItemName(ModItems.CUPCAKE_DOUGH)));
        generatedItem(getItemName(ModItems.CAKE_FROSTING), modLoc("item/" + getItemName(ModItems.CAKE_FROSTING)));
        generatedItem(getItemName(ModItems.CUPCAKE_FROSTING), modLoc("item/" + getItemName(ModItems.CUPCAKE_FROSTING)));
        generatedItem("poison_cake", mcLoc("item/cake"));
        generatedItem("tnt_cake", mcLoc("item/cake"));

        getBuilder("encasing_ice").parent(getExistingFile(mcLoc("block/ice")));
        getBuilder("cake_oven").parent(getExistingFile(modLoc("block/cake_oven")));
        getBuilder("cake_stand").parent(getExistingFile(modLoc("block/cake_stand")));
        getBuilder("ceramic_bowl").parent(getExistingFile(modLoc("block/ceramic_bowl")));

        CakeVariant.VARIANT_BY_CAKE.forEach((cake, variant) -> {
            if (variant.hasItem() && !variant.hasCustomItemModel()) {
                generatedItem(variant.getCakeName(), modLoc("item/" + variant.getCakeName()));
            }

            if (cake.get().isBaseVariant()) {
                Supplier<Item> sliceItem = variant.getSliceItem();
                if (sliceItem.get() != null && sliceItem.get() != Items.AIR && !sliceItem.get().equals(ModItems.CAKE_SLICE.get())) {
                    String sliceItemName = getItemName(sliceItem);
                    generatedItem(sliceItemName, modLoc("item/" + sliceItemName));
                }
            }
        });
    }

    private void generatedItem(String name, ResourceLocation... layers) {
        layeredItem(name, mcLoc("item/generated"), layers);
    }

    private void layeredItem(String name, ResourceLocation parent, ResourceLocation... layers) {
        ItemModelBuilder model = withExistingParent(name, parent);
        for (int i = 0; i < layers.length; i++) {
            model = model.texture("layer" + i, layers[i]);
        }
    }

    private String getItemName(Supplier<? extends ItemLike> item) {
        return Util.getItemId(item.get().asItem()).getPath();
    }
}
