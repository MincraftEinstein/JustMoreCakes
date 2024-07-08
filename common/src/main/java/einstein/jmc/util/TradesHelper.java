package einstein.jmc.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public class TradesHelper {

    public static final int MAX_USES = 12;
    public static final float PRICE_MULTIPLIER = 0.05F;

    public static BaseTrade forEmeralds(int emeraldCost, ItemStack stack, int villagerXp) {
        return forEmeralds(emeraldCost, stack, MAX_USES, villagerXp);
    }

    public static BaseTrade forEmeralds(int emeraldCost, ItemStack stack, int maxUses, int villagerXp) {
        return forEmeralds(emeraldCost, stack, maxUses, villagerXp, PRICE_MULTIPLIER);
    }

    public static BaseTrade forEmeralds(int emeraldCost, ItemStack stack, int maxUses, int villagerXp, float priceMultiplier) {
        return new BaseTrade(new ItemStack(Items.EMERALD, emeraldCost), stack, maxUses, villagerXp, priceMultiplier);
    }

    public static BaseTrade forItems(ItemStack stack, int emeraldCount, int villagerXp) {
        return forItems(stack, emeraldCount, MAX_USES, villagerXp);
    }

    public static BaseTrade forItems(ItemStack stack, int emeraldCount, int maxUses, int villagerXp) {
        return forItems(stack, emeraldCount, maxUses, villagerXp, PRICE_MULTIPLIER);
    }

    public static BaseTrade forItems(ItemStack stack, int emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BaseTrade(stack, new ItemStack(Items.EMERALD, emeraldCount), maxUses, villagerXp, priceMultiplier);
    }

    public record BaseTrade(ItemStack price, ItemStack stack, int maxUses, int villagerXp,
                            float priceMultiplier) implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemCost(price.getItem(), price.getCount()), stack, maxUses, villagerXp, priceMultiplier);
        }
    }
}
