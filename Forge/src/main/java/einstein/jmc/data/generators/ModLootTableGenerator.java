package einstein.jmc.data.generators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableGenerator extends LootTableProvider {

	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTables = ImmutableList.of(Pair.of(BlockLootTableGenerator::new, LootContextParamSets.BLOCK));
	
	public ModLootTableGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return lootTables;
	}
	
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
		map.forEach((loc, table) -> LootTables.validate(validationContext, loc, table));
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes loot tables";
	}
}