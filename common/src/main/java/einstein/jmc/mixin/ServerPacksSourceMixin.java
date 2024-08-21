package einstein.jmc.mixin;

import net.minecraft.network.chat.Component;
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

        repositorySources.add(consumer -> consumer.accept(
                Pack.readMetaAndCreate(
                        new PackLocationInfo(FD_SUPPORT_ID, Component.translatable("datapack.jmc.fd_support.name"),
                                PackSource.FEATURE, Optional.of(
                                new KnownPack(PLATFORM.getPlatformName(), FD_SUPPORT_ID, version)
                        )),
                        new PathPackResources.PathResourcesSupplier(path.resolve("data/jmc/datapacks/fd_support")), PackType.SERVER_DATA,
                        new PackSelectionConfig(PLATFORM.isModLoaded(FARMERS_DELIGHT_MOD_ID), Pack.Position.TOP, false)
                )
        ));
        return repositorySources.toArray(RepositorySource[]::new);
    }
}
