package einstein.jmc.item;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.effects.CakeEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CupcakeItem extends ItemNameBlockItem {

    public CupcakeItem(BaseCakeBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            BaseCakeBlock cake = ((BaseCakeBlock) getBlock());
            CakeEffects effects = cake.justMoreCakes$getCakeEffects();

            if (effects != null) {
                cake.applyEffects(player, effects);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
