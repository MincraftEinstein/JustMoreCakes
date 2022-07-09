package einstein.jmc.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

public class ItemsForEmeralds implements VillagerTrades.ItemListing {

    private final ItemStack stack;
    private final int stackSize;
    private final int receivedSize;
    private final int maxUses;
    private final int givenExp;
    private final float priceMultiplier;

    public ItemsForEmeralds(final ItemLike item, final int stackSize, final int receivedSize, final int givenExp) {
        this(new ItemStack(item), stackSize, receivedSize, 12, givenExp);
    }

    public ItemsForEmeralds(final ItemLike item, final int stackSize, final int receivedSize, final int maxUses, final int givenExp) {
        this(new ItemStack(item), stackSize, receivedSize, maxUses, givenExp);
    }

    public ItemsForEmeralds(final ItemStack stack, final int stackSize, final int receivedSize, final int maxUses, final int givenExp) {
        this(stack, stackSize, receivedSize, maxUses, givenExp, 0.05F);
    }

    public ItemsForEmeralds(final ItemStack stack, final int stackSize, final int receivedSize, final int maxUses, final int givenExp, final float priceMultiplier) {
        this.stack = stack;
        this.stackSize = stackSize;
        this.receivedSize = receivedSize;
        this.maxUses = maxUses;
        this.givenExp = givenExp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(final Entity entity, final RandomSource random) {
        return new MerchantOffer(new ItemStack(Items.EMERALD, stackSize), new ItemStack(stack.getItem(), receivedSize), maxUses, givenExp, priceMultiplier);
    }
}