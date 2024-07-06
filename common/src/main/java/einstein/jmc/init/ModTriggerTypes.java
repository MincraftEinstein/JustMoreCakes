package einstein.jmc.init;

import einstein.jmc.advancement.criterian.CakeEatenTrigger;
import einstein.jmc.platform.Services;

import java.util.function.Supplier;

public class ModTriggerTypes {

    public static final Supplier<CakeEatenTrigger> CAKE_EATEN = Services.REGISTRY.registerTriggerType("cake_eaten", CakeEatenTrigger::new);

    public static void init() {
    }
}
