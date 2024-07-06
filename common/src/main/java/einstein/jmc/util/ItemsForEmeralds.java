package einstein.jmc.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

// Variables are named from the prospective of the villager
public class ItemsForEmeralds implements VillagerTrades.ItemListing {

    private final ItemStack givenStack;
    private final int receivedCount;
    private final int givenCount;
    private final int maxTrades;
    private final int givenExp;
    private final float priceMultiplier;

    public ItemsForEmeralds(final ItemLike givenItem, final int receivedCount, final int givenCount, final int givenExp) {
        this(new ItemStack(givenItem), receivedCount, givenCount, 12, givenExp);
    }

    public ItemsForEmeralds(final ItemLike givenItem, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp) {
        this(new ItemStack(givenItem), receivedCount, givenCount, maxTrades, givenExp);
    }

    public ItemsForEmeralds(final ItemStack givenStack, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp) {
        this(givenStack, receivedCount, givenCount, maxTrades, givenExp, 0.05F);
    }

    public ItemsForEmeralds(final ItemStack givenStack, final int receivedCount, final int givenCount, final int maxTrades, final int givenExp, final float priceMultiplier) {
        this.givenStack = givenStack;
        this.receivedCount = receivedCount;
        this.givenCount = givenCount;
        this.maxTrades = maxTrades;
        this.givenExp = givenExp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(final Entity entity, final RandomSource random) {
        return new MerchantOffer(new ItemCost(Items.EMERALD, receivedCount), new ItemStack(givenStack.getItem(), givenCount), maxTrades, givenExp, priceMultiplier);
    }
}