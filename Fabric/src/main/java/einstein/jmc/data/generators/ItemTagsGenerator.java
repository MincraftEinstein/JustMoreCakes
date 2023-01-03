package einstein.jmc.data.generators;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemTagsGenerator extends FabricTagProvider<Item> {

    public ItemTagsGenerator(FabricDataGenerator generator) {
        super(generator, Registry.ITEM);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(ModItemTags.CHEESE_CAKES).add(ModBlocks.CHEESECAKE.get().asItem());
        getOrCreateTagBuilder(ModItemTags.CHEESECAKES).add(ModBlocks.CHEESECAKE.get().asItem());
        getOrCreateTagBuilder(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED)
                .addOptionalTag(ModItemTags.RED_DYES).add(Items.RED_DYE);
        getOrCreateTagBuilder(ModItemTags.SEEDS).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);
        getOrCreateTagBuilder(ModItemTags.SLIME_BALLS).add(Items.SLIME_BALL);
    }
}
