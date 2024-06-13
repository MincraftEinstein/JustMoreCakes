package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.loot.AddItemLootModifier;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.util.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.ArrayList;
import java.util.List;

public class ModLootModifiersProvider extends GlobalLootModifierProvider {

    public ModLootModifiersProvider(PackOutput output) {
        super(output, JustMoreCakes.MOD_ID);
    }

    @Override
    protected void start() {
        List<Block> cakes = new ArrayList<>(CakeUtil.getVanillaCandleCakes());
        cakes.add(Blocks.CAKE);
        for (Block cake : cakes) {
            add(Util.getBlockId(cake).getPath(), new AddItemLootModifier(new LootItemCondition[]{
                    new LootTableIdCondition.Builder(cake.getLootTable()).build(),
                    Util.HAS_CAKE_SPATULA.get().build()
            }, Blocks.CAKE));
        }
    }
}
