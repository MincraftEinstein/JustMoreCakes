package einstein.jmc.init;

import com.google.common.collect.ImmutableSet;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static einstein.jmc.util.TradesHelper.forEmeralds;
import static einstein.jmc.util.TradesHelper.forItems;

public class ModVillagers {

    public static final Supplier<PoiType> CAKE_BAKER_POI = Services.REGISTRY.registerPOIType("cake_baker", () ->
            new PoiType(ImmutableSet.copyOf(ModBlocks.CAKE_OVEN.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final Predicate<Holder<PoiType>> CAKE_BAKER_HOLDER_PREDICATE = holder -> holder.is(JustMoreCakes.loc("cake_baker"));
    public static final Supplier<VillagerProfession> CAKE_BAKER = Services.REGISTRY.registerVillagerProfession("cake_baker", () ->
            new VillagerProfession("cake_baker",
                    CAKE_BAKER_HOLDER_PREDICATE,
                    CAKE_BAKER_HOLDER_PREDICATE,
                    ImmutableSet.of(Items.SUGAR_CANE),
                    ImmutableSet.of(Blocks.SUGAR_CANE),
                    SoundEvents.VILLAGER_WORK_BUTCHER
            )
    );

    public static void init() {
    }

    public static void noviceTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forItems(new ItemStack(Items.WHEAT, 20), 1, 16, 2));
        listings.add(forItems(new ItemStack(Items.EGG, 5), 1, 16, 2));
        listings.add(forEmeralds(2, new ItemStack(Items.SUGAR, 4), 1));
        listings.add(forEmeralds(2, new ItemStack(Items.MILK_BUCKET, 1), 2));
    }

    public static void apprenticeTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forEmeralds(1, new ItemStack(Blocks.CAKE), 10));
        listings.add(forEmeralds(3, new ItemStack(Items.COCOA_BEANS), 5));
        listings.add(forEmeralds(1, new ItemStack(ModBlocks.CARROT_CAKE_FAMILY.getBaseItem().get()), 10));
    }

    public static void journeymanTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forItems(new ItemStack(Items.COAL, 15), 1, 16, 10));
        listings.add(forItems(new ItemStack(Items.CARROT, 22), 1, 16, 20));
        listings.add(forItems(new ItemStack(Items.SUGAR_CANE, 2), 1, 10));
    }

    public static void expertTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forItems(new ItemStack(ModItems.CREAM_CHEESE.get()), 6, 30));
        listings.add(forEmeralds(4, new ItemStack(ModItems.CUPCAKE.get()), 16, 15));
    }

    public static void masterTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forEmeralds(6, new ItemStack(ModItems.CAKE_SPATULA.get()), 3, 15, 0.2F));
        listings.add(forEmeralds(20, new ItemStack(ModBlocks.CREEPER_CAKE_FAMILY.getBaseItem().get()), 30));
    }

    public static void wanderingTraderTrades(List<VillagerTrades.ItemListing> listings) {
        listings.add(forEmeralds(2, new ItemStack(ModBlocks.SEED_CAKE_FAMILY.getBaseItem().get()), 12));
    }
}
