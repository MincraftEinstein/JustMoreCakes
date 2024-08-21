package einstein.jmc.platform;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        return FMLEnvironment.dist.isClient() ? PhysicalSide.CLIENT : PhysicalSide.SERVER;
    }

    @Override
    public String getVersion() {
        return ModList.get().getModContainerById(JustMoreCakes.MOD_ID).orElseThrow().getModInfo().getVersion().toString();
    }

    @Override
    public Path getRootPath() {
        return ModList.get().getModFileById(JustMoreCakes.MOD_ID).getFile().findResource("", "");
    }
}
