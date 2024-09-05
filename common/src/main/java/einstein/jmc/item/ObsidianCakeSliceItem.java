package einstein.jmc.item;

import einstein.jmc.block.cake.ObsidianCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ObsidianCakeSliceItem extends CakeSliceItem {

    public ObsidianCakeSliceItem(Properties properties) {
        super(properties, ModBlocks.OBSIDIAN_CAKE_FAMILY);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ObsidianCakeBlock.damage(level, entity);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 10;
    }
}
