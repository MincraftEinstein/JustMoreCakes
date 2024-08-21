package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.services.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? PhysicalSide.CLIENT : PhysicalSide.SERVER;
    }

    @Override
    public String getVersion() {
        return getModContainer().getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public Path getRootPath() {
        return getModContainer().getRootPaths().getFirst();
    }

    private static ModContainer getModContainer() {
        return FabricLoader.getInstance().getModContainer(JustMoreCakes.MOD_ID).orElseThrow();
    }
}
