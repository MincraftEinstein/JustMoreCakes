package einstein.jmc.item;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.compat.FarmersDelightCompat;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.registration.family.CakeFamily;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class CakeSliceItem extends Item {

    private final CakeFamily family;
    private Supplier<BaseCakeBlock> cakeBlock;

    public CakeSliceItem(Properties properties, CakeFamily family) {
        super(properties);
        this.family = family;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (!FarmersDelightCompat.IS_ENABLED.get()) {
            tooltip.add(Component.translatable("item.jmc.fd_support.cake_slice.info").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (cakeBlock == null) {
            cakeBlock = family.getBaseVariant().getCake();
        }

        if (entity instanceof Player player) {
            CakeEffects effects = cakeBlock.get().justMoreCakes$getCakeEffects();

            if (effects != null) {
                cakeBlock.get().applyEffects(player, effects);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
