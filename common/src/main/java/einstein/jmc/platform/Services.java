package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.services.CommonHooks;
import einstein.jmc.platform.services.IPlatformHelper;
import einstein.jmc.platform.services.RegistryHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final RegistryHelper REGISTRY = load(RegistryHelper.class);
    public static final CommonHooks HOOKS = load(CommonHooks.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));

        JustMoreCakes.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
