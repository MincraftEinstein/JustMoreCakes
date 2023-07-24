package einstein.jmc.init;

import einstein.jmc.platform.Services;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ModGameEvents {

    public static final Supplier<GameEvent> SCULK_CAKE_EATEN = register("sculk_cake_eaten");

    private static Supplier<GameEvent> register(String name) {
        return Services.REGISTRY.registerGameEvent(name, () -> new GameEvent(name, GameEvent.DEFAULT_NOTIFICATION_RADIUS));
    }

    public static void init() {
    }
}
