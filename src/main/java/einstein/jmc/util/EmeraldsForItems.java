package einstein.jmc.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class EmeraldsForItems implements VillagerTrades.ItemListing {

    private final ItemStack itemstack;
    private final int stackSize;
    private final int receivedSize;
    private final int maxUses;
    private final int givenExp;
    private final float priceMultiplier;

    public EmeraldsForItems(final ItemLike item, final int stackSize, final int receivedSize, final int givenExp) {
        this(new ItemStack(item.asItem()), stackSize, receivedSize, 12, givenExp);
    }

    public EmeraldsForItems(final ItemLike item, final int stackSize, final int receivedSize, final int maxUses, final int givenExp) {
        this(new ItemStack(item.asItem()), stackSize, receivedSize, maxUses, givenExp);
    }

    public EmeraldsForItems(final ItemStack stack, final int stackSize, final int receivedSize, final int maxUses, final int givenExp) {
        this(stack, stackSize, receivedSize, maxUses, givenExp, 0.05F);
    }

    public EmeraldsForItems(final ItemStack stack, final int stackSize, final int receivedSize, final int maxUses, final int givenExp, final float priceMultiplier) {
        this.itemstack = stack;
        this.stackSize = stackSize;
        this.receivedSize = receivedSize;
        this.maxUses = maxUses;
        this.givenExp = givenExp;
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(final Entity entity, final RandomSource random) {
        return new MerchantOffer(new ItemStack(itemstack.getItem(), stackSize), new ItemStack(Items.EMERALD, receivedSize), maxUses, givenExp, priceMultiplier);
    }
}