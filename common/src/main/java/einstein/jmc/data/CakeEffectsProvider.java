package einstein.jmc.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.registration.family.CakeFamily;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class CakeEffectsProvider implements DataProvider {

    private final PackOutput output;
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final Map<String, CakeEffects> toSerialize = new HashMap<>();

    public CakeEffectsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.modId = modId;
        this.lookupProvider = lookupProvider;
    }

    protected abstract void generate();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return lookupProvider.thenCompose((provider) -> {
            generate();

            Path folderPath = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(modId).resolve(CakeEffectsManager.EFFECTS_DIRECTORY);
            ImmutableList.Builder<CompletableFuture<?>> builder = new ImmutableList.Builder<>();
            RegistryOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);

            toSerialize.forEach((path, cakeEffects) -> {
                JsonElement element = CakeEffects.CODEC.encodeStart(ops, cakeEffects).getOrThrow();
                Path filePath = folderPath.resolve(path + ".json");
                builder.add(DataProvider.saveStable(cache, element, filePath));
            });

            return CompletableFuture.allOf(builder.build().toArray(CompletableFuture[]::new));
        });
    }

    public <T extends Block & CakeEffectsHolder> void add(Supplier<T> block, MobEffectInstance... instances) {
        add("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), new CakeEffects(block, instances));
    }

    public void add(CakeFamily family, MobEffectInstance... instances) {
        add("family/" + family.getRegistryKey().getPath(), new CakeEffects(family, instances));
    }

    public void add(String path, CakeEffects cakeEffects) {
        toSerialize.put(path, cakeEffects);
    }

    @Override
    public final String getName() {
        return "Cake Effects";
    }
}
