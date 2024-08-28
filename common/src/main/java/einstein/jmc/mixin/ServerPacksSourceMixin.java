package einstein.jmc.mixin;

import einstein.jmc.compat.FarmersDelightCompat;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static einstein.jmc.JustMoreCakes.*;
import static einstein.jmc.platform.Services.PLATFORM;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin {

    @ModifyArg(method = {
            "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;",
            "createVanillaTrustedRepository"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"))
    private static RepositorySource[] createPackRepo(RepositorySource[] sources) {
        List<RepositorySource> repositorySources = new ArrayList<>(List.of(sources));
        Path path = PLATFORM.getRootPath();
        String version = PLATFORM.getVersion();

        Pack pack = Pack.readMetaAndCreate(
                new PackLocationInfo(FarmersDelightCompat.FD_SUPPORT_ID, FarmersDelightCompat.FD_SUPPORT_NAME,
                        PackSource.FEATURE, Optional.of(
                        new KnownPack(PLATFORM.getPlatformName(), FarmersDelightCompat.FD_SUPPORT_ID, version)
                )),
                new PathPackResources.PathResourcesSupplier(path.resolve("data/jmc/datapacks/" + FarmersDelightCompat.FD_SUPPORT_ID)), PackType.SERVER_DATA,
                new PackSelectionConfig(FarmersDelightCompat.IS_ENABLED.get(), Pack.Position.TOP, false)
        );

        if (pack != null) {
            repositorySources.add(consumer -> consumer.accept(pack));
        }
        else {
            LOGGER.error("Failed to load datapack for Farmers Delight support");
        }

        return repositorySources.toArray(RepositorySource[]::new);
    }
}
