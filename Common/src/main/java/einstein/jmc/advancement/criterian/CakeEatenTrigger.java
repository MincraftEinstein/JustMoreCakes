package einstein.jmc.advancement.criterian;

import com.google.gson.JsonObject;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.util.Util;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

public class CakeEatenTrigger extends SimpleCriterionTrigger<CakeEatenTrigger.TriggerInstance> {

    public static final ResourceLocation ID = JustMoreCakes.loc("cake_eaten");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, ResourceLocation cake) {
        super.trigger(player, trigger -> trigger.test(cake));
    }

    public void trigger(ServerPlayer player, BaseCakeBlock cake) {
        trigger(player, Util.getBlockId(cake));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context) {
        return new TriggerInstance(new ResourceLocation(json.get("cake").getAsString()));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation cake;

        public TriggerInstance(ResourceLocation cake) {
            super(ID, EntityPredicate.Composite.ANY);
            this.cake = cake;
        }

        public boolean test(ResourceLocation cake) {
            return this.cake.equals(cake);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("cake", cake.toString());
            return json;
        }
    }
}
