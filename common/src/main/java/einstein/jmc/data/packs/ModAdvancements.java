package einstein.jmc.data.packs;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.advancement.criterian.CakeEatenTrigger;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModAdvancements {

    private static final AdvancementHolder PLANT_SEEDS_DUMMY = new AdvancementHolder(JustMoreCakes.mcLoc("husbandry/plant_seed"), null);

    public static void generate(Consumer<AdvancementHolder> consumer) {
        AdvancementHolder craftCake = addCakes(Advancement.Builder.advancement()
                .parent(PLANT_SEEDS_DUMMY)
                .requirements(AdvancementRequirements.Strategy.OR)
                .display(Blocks.CAKE.asItem(), translatable("craft_cake.title"), translatable("craft_cake.description"), null, FrameType.TASK, true, true, false)
        ).save(consumer, JustMoreCakes.loc("husbandry/craft_cake").toString());

        addCakes(Advancement.Builder.advancement()
                .parent(craftCake)
                .display(ModBlocks.CHOCOLATE_CAKE.get(), translatable("craft_all_cakes.title"), translatable("craft_all_cakes.description"), null, FrameType.CHALLENGE, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
        ).save(consumer, JustMoreCakes.loc("husbandry/craft_all_cakes").toString());

        Advancement.Builder.advancement()
                .parent(craftCake)
                .display(ModBlocks.OBSIDIAN_CAKE.get(), translatable("eat_obsidian_cake.title"), translatable("eat_obsidian_cake.description"), null, FrameType.TASK, true, true, false)
                .addCriterion("obsidian_cake_eaten", CakeEatenTrigger.TriggerInstance.cakeEaten(Util.getBlockId(ModBlocks.OBSIDIAN_CAKE.get())))
                .save(consumer, JustMoreCakes.loc("husbandry/eat_obsidian_cake").toString());
    }

    private static Advancement.Builder addCakes(Advancement.Builder advancement) {
        advancement.addCriterion("cake", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Blocks.CAKE).build()));

        TreeSet<Supplier<BaseCakeBlock>> set = new TreeSet<>(Comparator.comparing(o -> o.get().getBuilder().getCakeName()));
        set.addAll(CakeBuilder.BUILDER_BY_CAKE.keySet());

        for (Supplier<BaseCakeBlock> cake : set) {
            if (cake != ModBlocks.TWO_TIERED_CAKE && cake != ModBlocks.THREE_TIERED_CAKE) {
                Item cakeItem = ((ItemLike) cake.get()).asItem();
                advancement.addCriterion(Util.getBlockId(cake.get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(cakeItem).build()));
            }
        }

        return advancement;
    }

    private static Component translatable(String name) {
        return Component.translatable("advancements.husbandry." + JustMoreCakes.MOD_ID + "." + name);
    }
}
