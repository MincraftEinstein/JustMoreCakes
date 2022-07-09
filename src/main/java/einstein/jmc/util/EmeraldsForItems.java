package einstein.jmc.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

// Variables are named from the prospective of the villager
public class EmeraldsForItems implements VillagerTrades.ItemListing {

    private final ItemStack receivedStack;
    private final int receivedCount;
    private final int givenCount;
    private final int maxTrades;
    private final int givenExp;
    private final float priceMultiplier;

    public EmeraldsForItems(final ItemLike receivedItem, final int receivedCount, final int givenCount, final int givenExp) {
        this(new ItemStack(receivedItem.asItem()), receivedCount, givenCount, 12, givenExp);
    }

    public EmeraldsForItems(final ItemLike receivedItem, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp) {
        this(new ItemStack(receivedItem.asItem()), receivedCount, givenCount, maxTrades, givenExp);
    }

    public EmeraldsForItems(final ItemStack receivedStack, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp) {
        this(receivedStack, receivedCount, givenCount, maxTrades, givenExp, 0.05F);
    }

    public EmeraldsForItems(final ItemStack receivedStack, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp, final float priceMultiplier) {
        this.receivedStack = receivedStack;
        this.receivedCount = receivedCount;
        this.givenCount = givenCount;
        this.maxTrades = maxTrades;
        this.givenExp = givenExp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(final Entity entity, final RandomSource random) {
        return new MerchantOffer(new ItemStack(receivedStack.getItem(), receivedCount), new ItemStack(Items.EMERALD, givenCount), maxTrades, givenExp, priceMultiplier);
    }
}