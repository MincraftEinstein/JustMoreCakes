package einstein.jmc.init;

import java.util.List;

import com.google.common.collect.ImmutableSet;

import einstein.einsteins_library.util.VillagerTradesUtils.EmeraldsForItemsTrade;
import einstein.einsteins_library.util.VillagerTradesUtils.ItemsForEmeraldsTrade;
import einstein.jmc.JustMoreCakes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = JustMoreCakes.MODID)
public class ModVillagers {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, JustMoreCakes.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, JustMoreCakes.MODID);
	
	public static final RegistryObject<PoiType> CAKE_BAKER_POI = POIS.register("cake_baker", () -> new PoiType("cake_baker", PoiType.getBlockStates(ModBlocks.CAKE_OVEN.get()), 1, 1));
	public static final RegistryObject<VillagerProfession> CAKE_BAKER = PROFESSIONS.register("cake_baker", () -> new VillagerProfession("cake_baker", CAKE_BAKER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER));
	
	@SubscribeEvent
	public static void onVillagerTradesEvent(final VillagerTradesEvent event) {
        final List<ItemListing> novice = event.getTrades().get(1); // Stone tier
        final List<ItemListing> apprentice = event.getTrades().get(2); // Iron tier
        final List<ItemListing> journeyman = event.getTrades().get(3); // Gold tier
        final List<ItemListing> expert = event.getTrades().get(4); // Emerald tier
        final List<ItemListing> master = event.getTrades().get(5); // Diamond tier
		if (event.getType() == ModVillagers.CAKE_BAKER.get()) {
			novice.add(new EmeraldsForItemsTrade(Items.WHEAT, 20, 1, 16, 2));
			novice.add(new EmeraldsForItemsTrade(Items.EGG, 5, 1, 16, 2));
			novice.add(new ItemsForEmeraldsTrade(Items.SUGAR, 2, 4, 12, 1));
			novice.add(new ItemsForEmeraldsTrade(Items.MILK_BUCKET, 2, 1, 12, 2));
			
			apprentice.add(new ItemsForEmeraldsTrade(Blocks.CAKE.asItem(), 1, 1, 12, 10));
			apprentice.add(new ItemsForEmeraldsTrade(Items.COCOA_BEANS, 3, 1, 12, 5));
			apprentice.add(new ItemsForEmeraldsTrade(ModBlocks.CARROT_CAKE.get().asItem(), 1, 1, 12, 10));
			
			journeyman.add(new EmeraldsForItemsTrade(Items.COAL, 15, 1, 16, 10));
			journeyman.add(new EmeraldsForItemsTrade(Items.CARROT, 22, 1, 16, 20));
			journeyman.add(new EmeraldsForItemsTrade(Items.SUGAR_CANE, 2, 1, 12, 10));
			
			expert.add(new EmeraldsForItemsTrade(ModItems.CHEESE.get(), 1, 6, 12, 30));
			expert.add(new ItemsForEmeraldsTrade(ModItems.CUPCAKE.get(), 4, 1, 16, 15));
			
			master.add(new ItemsForEmeraldsTrade(ModBlocks.THREE_TIERED_CAKE.get().asItem(), 15, 1, 12, 30));
			master.add(new ItemsForEmeraldsTrade(ModBlocks.CREEPER_CAKE.get().asItem(), 20, 1, 12, 30));
		}
	}
	
	@SubscribeEvent
	public static void onWanderingTradesEvent(final WandererTradesEvent event) {
		event.getGenericTrades().add(new ItemsForEmeraldsTrade(ModBlocks.SEED_CAKE.get().asItem(), 2, 1, 12));
	}
}