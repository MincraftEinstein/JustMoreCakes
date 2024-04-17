package einstein.jmc.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.data.effects.CakeEffectsManager;
import einstein.jmc.util.CakeFamily;
import einstein.jmc.util.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class CakeEffectsProvider implements DataProvider {

    private final PackOutput output;
    private final String modId;
    private final Map<String, JsonElement> map = new HashMap<>();

    public CakeEffectsProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    protected abstract void addCakeEffects();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addCakeEffects();

        Path folderPath = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(modId).resolve(CakeEffectsManager.EFFECTS_DIRECTORY);
        ImmutableList.Builder<CompletableFuture<?>> builder = new ImmutableList.Builder<>();

        map.forEach((path, element) -> {
            Path filePath = folderPath.resolve(path + ".json");
            builder.add(DataProvider.saveStable(cache, element, filePath));
        });

        return CompletableFuture.allOf(builder.build().toArray(CompletableFuture[]::new));
    }

    public void add(Supplier<BaseCakeBlock> cake, SerializableMobEffectInstance... effectInstances) {
        JsonElement element = CakeEffects.BLOCK_CODEC.encodeStart(JsonOps.INSTANCE, new CakeEffects(cake.get(), List.of(effectInstances))).getOrThrow(false, error -> {
        });
        map.put("blocks/" + Util.getBlockId(cake.get()).getPath(), element);
    }

    public void add(CakeFamily family, SerializableMobEffectInstance... effectInstances) {
        JsonElement element = CakeEffects.FAMILY_CODEC.encodeStart(JsonOps.INSTANCE, new CakeEffects(family, List.of(effectInstances))).getOrThrow(false, error -> {
        });
        map.put("families/" + family.getRegistryKey().getPath(), element);
    }

    @Override
    public String getName() {
        return "Cake Effects";
    }
}
