package einstein.jmc.block.cake;

import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.util.CakeVariant;
import einstein.jmc.data.SerializableMobEffectInstance;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CreeperTwoTieredCakeBlock extends BaseTwoTieredCakeBlock {

    public CreeperTwoTieredCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public void applyEffects(Player player, CakeEffects effects) {
        List<SerializableMobEffectInstance> effectInstances = effects.mobEffects();
        Util.applyEffectFromInstance(effectInstances.get(player.getRandom().nextInt(effectInstances.size())), player);
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        player.level().playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 1, 0.5F);
        return super.eatActions(player, pos, state);
    }
}
