package einstein.jmc.init;

import com.google.common.collect.ImmutableSet;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.Services;
import einstein.jmc.util.EmeraldsForItems;
import einstein.jmc.util.ItemsForEmeralds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Supplier;

public class ModVillagers {

    public static final Supplier<PoiType> CAKE_BAKER_POI = Services.REGISTRY.registerPOIType("cake_baker", () -> new PoiType(PoiTypes.getBlockStates(ModBlocks.CAKE_OVEN.get()), 1, 1));
    public static final Supplier<VillagerProfession> CAKE_BAKER = Services.REGISTRY.registerVillagerProfession("cake_baker", () -> new VillagerProfession("cake_baker", holder -> holder.is(JustMoreCakes.loc("cake_baker")), holder -> holder.is(JustMoreCakes.loc("cake_baker")), ImmutableSet.of(Items.SUGAR_CANE), ImmutableSet.of(Blocks.SUGAR_CANE), SoundEvents.VILLAGER_WORK_BUTCHER));

    public static void init() {
    }

    public static void noviceTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new EmeraldsForItems(Items.WHEAT, 20, 1, 16, 2));
        listings.add(new EmeraldsForItems(Items.EGG, 5, 1, 16, 2));
        listings.add(new ItemsForEmeralds(Items.SUGAR, 2, 4, 1));
        listings.add(new ItemsForEmeralds(Items.MILK_BUCKET, 2, 1, 2));
    }

    public static void apprenticeTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new ItemsForEmeralds(Blocks.CAKE.asItem(), 1, 1, 10));
        listings.add(new ItemsForEmeralds(Items.COCOA_BEANS, 3, 1, 5));
        listings.add(new ItemsForEmeralds(ModBlocks.CARROT_CAKE_FAMILY.getBaseItem().get(), 1, 1, 10));
    }

    public static void journeymanTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new EmeraldsForItems(Items.COAL, 15, 1, 16, 10));
        listings.add(new EmeraldsForItems(Items.CARROT, 22, 1, 16, 20));
        listings.add(new EmeraldsForItems(Items.SUGAR_CANE, 2, 1, 10));
    }

    public static void expertTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new EmeraldsForItems(ModItems.CREAM_CHEESE.get(), 1, 6, 30));
        listings.add(new ItemsForEmeralds(ModItems.CUPCAKE.get(), 4, 1, 16, 15));
    }

    public static void masterTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new ItemsForEmeralds(new ItemStack(ModItems.CAKE_SPATULA.get()), 6, 1, 3, 15, 0.2F));
        listings.add(new ItemsForEmeralds(ModBlocks.CREEPER_CAKE_FAMILY.getBaseItem().get(), 20, 1, 30));
    }

    public static void wanderingTraderTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(new ItemsForEmeralds(ModBlocks.SEED_CAKE_FAMILY.getBaseItem().get(), 2, 1, 12));
    }
}
