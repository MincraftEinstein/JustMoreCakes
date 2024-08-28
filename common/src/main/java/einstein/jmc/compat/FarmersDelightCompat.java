package einstein.jmc.compat;

import com.google.common.base.Suppliers;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.packs.providers.farmersdelight.FDSupportRecipeProvider;
import einstein.jmc.init.ModFeatureFlags;
import einstein.jmc.mixin.PackGeneratorAccessor;
import einstein.jmc.platform.Services;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FarmersDelightCompat {

    public static final String FARMERS_DELIGHT_MOD_ID = "farmersdelight";
    public static final String FD_SUPPORT_ID = "jmc_fd_support";
    public static final Component FD_SUPPORT_NAME = Component.translatable("datapack.jmc.fd_support.name");
    public static final Component FD_SUPPORT_DESCRIPTION = Component.translatable("datapack.jmc.fd_support.description");
    public static final Supplier<Boolean> IS_ENABLED = Suppliers.memoize(() -> Services.PLATFORM.isModLoaded(FARMERS_DELIGHT_MOD_ID));

    public static void init() {
    }

    public static void createFDSupportPack(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator.PackGenerator pack) {
        Path path = generator.vanillaPackOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(JustMoreCakes.MOD_ID).resolve("datapacks").resolve(FD_SUPPORT_ID);
        PackGeneratorAccessor accessor = (PackGeneratorAccessor) pack;
        accessor.setProviderPrefix(FD_SUPPORT_ID);
        accessor.setOutput(new PackOutput(path));
        pack.addProvider(packOutput -> new FDSupportRecipeProvider(packOutput, lookupProvider));
        pack.addProvider(packOutput -> PackMetadataGenerator.forFeaturePack(packOutput, FD_SUPPORT_DESCRIPTION, FeatureFlagSet.of(ModFeatureFlags.FD_SUPPORT)));
    }

    public static ResourceLocation fdLoc(String string) {
        return ResourceLocation.fromNamespaceAndPath(FARMERS_DELIGHT_MOD_ID, string);
    }
}
