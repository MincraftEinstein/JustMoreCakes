package einstein.jmc.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class PoisonCakeBlock extends BaseCakeBlock
{
    public PoisonCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
//    @Override
//    public void eatActions(Player player) {
//		player.getFoodData().eat(2, 0.1F);
//		player.addEffect(new MobEffectInstance(MobEffects.POISON, ModServerConfigs.POISON_CAKE_POISON_DUR.get(), ModServerConfigs.POISON_CAKE_POISON_STRENGTH.get()));
//	}
}
