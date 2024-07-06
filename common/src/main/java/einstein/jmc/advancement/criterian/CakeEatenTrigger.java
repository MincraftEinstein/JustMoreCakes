package einstein.jmc.advancement.criterian;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.util.Util;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class CakeEatenTrigger extends SimpleCriterionTrigger<CakeEatenTrigger.TriggerInstance> {

    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::predicate),
            ResourceLocation.CODEC.fieldOf("cake").forGetter(TriggerInstance::cake)
    ).apply(instance, TriggerInstance::new));

    public void trigger(ServerPlayer player, ResourceLocation cake) {
        super.trigger(player, trigger -> trigger.matches(cake));
    }

    public void trigger(ServerPlayer player, Block block) {
        trigger(player, Util.getBlockId(block));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> predicate, ResourceLocation cake) implements SimpleInstance {

        public static Criterion<TriggerInstance> cakeEaten(ResourceLocation cake) {
            return ModTriggerTypes.CAKE_EATEN.get().createCriterion(new TriggerInstance(Optional.empty(), cake));
        }

        public boolean matches(ResourceLocation cake) {
            return this.cake.equals(cake);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return predicate;
        }
    }
}
