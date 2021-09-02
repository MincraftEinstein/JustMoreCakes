package einstein.jmc.util;

import java.util.List;

import einstein.einsteins_library.util.VillagerTradesUtils;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModVillagers;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = JustMoreCakes.MODID)
public class ModVillagerTrades {
	
	@SubscribeEvent
	public static void onVillagerTradesEvent(final VillagerTradesEvent event) {
        final List<ItemListing> novice = event.getTrades().get(1);
        final List<ItemListing> apprentice = event.getTrades().get(2);
        final List<ItemListing> journeyman = event.getTrades().get(3);
        final List<ItemListing> expert = event.getTrades().get(4);
        final List<ItemListing> master = event.getTrades().get(5);
		if (event.getType() == ModVillagers.CAKE_BAKER) {
			novice.add(new VillagerTradesUtils.EmeraldsForItemsTrade(Items.SUGAR, 4, 1, 16, 2));
			novice.add(new VillagerTradesUtils.ItemsForEmeraldsTrade(Items.EGG, 1, 1, 8, 2));
			apprentice.add(new VillagerTradesUtils.EmeraldsForItemsTrade(Items.WHEAT, 16, 3, 3, 5));
			apprentice.add(new VillagerTradesUtils.ItemsForEmeraldsTrade(Items.COCOA_BEANS, 6, 2, 5, 7));
			journeyman.add(new VillagerTradesUtils.ItemsForEmeraldsTrade(Blocks.CAKE, 5, 1, 4, 10));
			expert.add(new VillagerTradesUtils.EmeraldsForItemsTrade(Items.MILK_BUCKET, 1, 2, 5, 15));
			expert.add(new VillagerTradesUtils.ItemsForEmeraldsTrade(Items.MILK_BUCKET, 2, 1, 5, 15));
			master.add(new VillagerTradesUtils.ItemsForEmeraldsTrade(ModBlocks.CREEPER_CAKE, 20, 1, 1, 25));
		}
	}
}
