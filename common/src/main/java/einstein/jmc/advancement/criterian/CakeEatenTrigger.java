package einstein.jmc.advancement.criterian;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.registration.family.CakeFamily;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class CakeEatenTrigger extends SimpleCriterionTrigger<CakeEatenTrigger.TriggerInstance> {

    private static final Codec<TriggerInstance> BLOCK = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::predicate),
            CakeEffectsHolder.BLOCK_CODEC.fieldOf("block").forGetter(TriggerInstance::holder)
    ).apply(instance, TriggerInstance::new));

    private static final Codec<TriggerInstance> FAMILY = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::predicate),
            CakeFamily.CODEC.fieldOf("family").forGetter(trigger -> (CakeFamily) trigger.holder())
    ).apply(instance, TriggerInstance::new));

    public static final Codec<TriggerInstance> CODEC = Codec.xor(BLOCK, FAMILY).xmap(either ->
            either.map(trigger -> trigger, trigger -> trigger), trigger -> {
        if (trigger.holder() instanceof Block) {
            return Either.left(trigger);
        }
        else if (trigger.holder() instanceof CakeFamily) {
            return Either.right(trigger);
        }
        throw new UnsupportedOperationException("This is neither a block nor a family");
    });

    public void trigger(ServerPlayer player, CakeEffectsHolder holder) {
        super.trigger(player, trigger -> trigger.matches(holder));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> predicate,
                                  CakeEffectsHolder holder) implements SimpleInstance {

        public static Criterion<TriggerInstance> cakeEaten(CakeEffectsHolder holder) {
            return ModTriggerTypes.CAKE_EATEN.get().createCriterion(new TriggerInstance(Optional.empty(), holder));
        }

        public boolean matches(CakeEffectsHolder holder) {
            return holder().equals(holder);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return predicate;
        }
    }
}
