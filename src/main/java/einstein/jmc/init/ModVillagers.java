package einstein.jmc.init;

import com.google.common.collect.ImmutableSet;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@EventBusSubscriber(modid = JustMoreCakes.MODID)
public class ModVillagers {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, JustMoreCakes.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, JustMoreCakes.MODID);

	public static final RegistryObject<PoiType> CAKE_BAKER_POI = POIS.register("cake_baker", () -> new PoiType(PoiTypes.getBlockStates(ModBlocks.CAKE_OVEN.get()), 1, 1));
	public static final RegistryObject<VillagerProfession> CAKE_BAKER = PROFESSIONS.register("cake_baker", () -> new VillagerProfession("cake_baker", (holder) -> holder.is(CAKE_BAKER_POI.getId()), (holder) -> holder.is(CAKE_BAKER_POI.getId()), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER));
	
	@SubscribeEvent
	public static void onVillagerTradesEvent(final VillagerTradesEvent event) {
		if (event.getType() == ModVillagers.CAKE_BAKER.get()) {
			final List<VillagerTrades.ItemListing> novice = event.getTrades().get(1); // Stone tier
			final List<VillagerTrades.ItemListing> apprentice = event.getTrades().get(2); // Iron tier
			final List<VillagerTrades.ItemListing> journeyman = event.getTrades().get(3); // Gold tier
			final List<VillagerTrades.ItemListing> expert = event.getTrades().get(4); // Emerald tier
			final List<VillagerTrades.ItemListing> master = event.getTrades().get(5); // Diamond tier

			novice.add(new ItemsForEmeralds(Items.WHEAT, 20, 1, 16, 2));
			novice.add(new ItemsForEmeralds(Items.EGG, 5, 1, 16, 2));
			novice.add(new ItemsForEmeralds(Items.SUGAR, 2, 4, 1));
			novice.add(new ItemsForEmeralds(Items.MILK_BUCKET, 2, 1, 2));

			apprentice.add(new ItemsForEmeralds(Blocks.CAKE.asItem(), 1, 1, 10));
			apprentice.add(new ItemsForEmeralds(Items.COCOA_BEANS, 3, 1, 5));
			apprentice.add(new ItemsForEmeralds(ModBlocks.CARROT_CAKE.get().asItem(), 1, 1,10));

			journeyman.add(new EmeraldsForItems(Items.COAL, 15, 1, 16, 10));
			journeyman.add(new EmeraldsForItems(Items.CARROT, 22, 1, 16, 20));
			journeyman.add(new EmeraldsForItems(Items.SUGAR_CANE, 2, 1, 10));

			expert.add(new EmeraldsForItems(ModItems.CHEESE.get(), 1, 6, 30));
			expert.add(new ItemsForEmeralds(ModItems.CUPCAKE.get(), 4, 1, 16, 15));

			master.add(new ItemsForEmeralds(ModBlocks.THREE_TIERED_CAKE.get().asItem(), 15, 1, 30));
			master.add(new ItemsForEmeralds(ModBlocks.CREEPER_CAKE.get().asItem(), 20, 1,  30));
		}
	}

	@SubscribeEvent
	public static void onWanderingTradesEvent(final WandererTradesEvent event) {
		event.getGenericTrades().add(new ItemsForEmeralds(ModBlocks.SEED_CAKE.get().asItem(), 2, 1, 12));
	}
}