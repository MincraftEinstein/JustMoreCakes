package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.loot.AddItemLootModifier;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootModifiersProvider extends GlobalLootModifierProvider {

    public ModLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID);
    }

    @Override
    protected void start() {
        List<Block> cakes = new ArrayList<>(CakeUtil.getVanillaCandleCakes());
        cakes.add(Blocks.CAKE);
        for (Block cake : cakes) {
            add(Util.getBlockId(cake).getPath(), addCakeSpatulaDrop(cake, Blocks.CAKE));
        }
    }

    public static AddItemLootModifier addCakeSpatulaDrop(Block block) {
        return addCakeSpatulaDrop(block, block);
    }

    public static AddItemLootModifier addCakeSpatulaDrop(Block block, Block dropped) {
        return new AddItemLootModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(block.getLootTable().location()).build(),
                Util.HAS_CAKE_SPATULA.get().build()
        }, dropped);
    }
}
