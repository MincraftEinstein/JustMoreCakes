package einstein.jmc.init;

import einstein.jmc.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ModGameEvents {

    public static final Supplier<Holder<GameEvent>> SCULK_CAKE_EATEN = register("sculk_cake_eaten");

    private static Supplier<Holder<GameEvent>> register(String name) {
        return Services.REGISTRY.registerGameEvent(name, () -> new GameEvent(GameEvent.DEFAULT_NOTIFICATION_RADIUS));
    }

    public static void init() {
    }
}
