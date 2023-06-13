package einstein.jmc.data.providers;

import einstein.jmc.advancement.criterian.CakeEatenTrigger;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Supplier;

public class ModAdvancements {

    public static Advancement.Builder craftCake() {
        return addCakes(Advancement.Builder.advancement().requirements(RequirementsStrategy.OR)
                .display(Blocks.CAKE.asItem(), translatable("craft_cake.title"), translatable("craft_cake.description"), null, FrameType.TASK, true, true, false));
    }

    public static Advancement.Builder craftAllCakes(Advancement parent) {
        return addCakes(Advancement.Builder.advancement().parent(parent))
                .display(ModBlocks.CHOCOLATE_CAKE.get(), translatable("craft_all_cakes.title"), translatable("craft_all_cakes.description"), null, FrameType.CHALLENGE, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100));
    }

    public static Advancement.Builder eatObsidianCake(Advancement parent) {
        return Advancement.Builder.advancement().parent(parent)
                .display(ModBlocks.OBSIDIAN_CAKE.get(), translatable("eat_obsidian_cake.title"), translatable("eat_obsidian_cake.description"), null, FrameType.TASK, true, true, false)
                .addCriterion("obsidian_cake_eaten", CakeEatenTrigger.TriggerInstance.cakeEaten(Util.getBlockId(ModBlocks.OBSIDIAN_CAKE.get())));
    }

    private static Advancement.Builder addCakes(Advancement.Builder advancement) {
        advancement.addCriterion("cake", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Blocks.CAKE).build()));
        advancement.addCriterion("cupcake", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItems.CUPCAKE.get()).build()));

        TreeSet<Supplier<BaseCakeBlock>> set = new TreeSet<>(Comparator.comparing(o -> o.get().getBuilder().getCakeName()));
        set.addAll(CakeBuilder.BUILDER_BY_CAKE.keySet());

        for (Supplier<BaseCakeBlock> cake : set) {
            Item cakeItem = ((ItemLike) cake.get()).asItem();
            advancement.addCriterion(Util.getBlockId(cake.get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(cakeItem).build()));
        }

        return advancement;
    }

    private static Component translatable(String string) {
        return Component.translatable("advancements.husbandry." + string);
    }
}
