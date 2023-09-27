package einstein.jmc.advancement.criterian;

import com.google.gson.JsonObject;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.util.Util;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class CakeEatenTrigger extends SimpleCriterionTrigger<CakeEatenTrigger.TriggerInstance> {

    public void trigger(ServerPlayer player, ResourceLocation cake) {
        super.trigger(player, trigger -> trigger.test(cake));
    }

    public void trigger(ServerPlayer player, Block block) {
        trigger(player, Util.getBlockId(block));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context) {
        return new TriggerInstance(predicate, new ResourceLocation(json.get("cake").getAsString()));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation cake;

        public TriggerInstance(Optional<ContextAwarePredicate> predicate, ResourceLocation cake) {
            super(predicate);
            this.cake = cake;
        }

        public static Criterion<TriggerInstance> cakeEaten(ResourceLocation cake) {
            return JustMoreCakes.CAKE_EATEN_TRIGGER.createCriterion(new TriggerInstance(Optional.empty(), cake));
        }

        public boolean test(ResourceLocation cake) {
            return this.cake.equals(cake);
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject json = super.serializeToJson();
            json.addProperty("cake", cake.toString());
            return json;
        }
    }
}
