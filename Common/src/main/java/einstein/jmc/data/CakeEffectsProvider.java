package einstein.jmc.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.util.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class CakeEffectsProvider implements DataProvider {

    private final DataGenerator.PathProvider pathProvider;
    private final String modid;
    private final Map<String, JsonElement> map = new HashMap<>();

    public CakeEffectsProvider(DataGenerator generator, String modid) {
        pathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "cake_effects");
        this.modid = modid;
    }

    protected abstract void addCakeEffects();

    @Override
    public void run(CachedOutput cache) {
        addCakeEffects();

        map.forEach((name, element) -> {
            try {
                DataProvider.saveStable(cache, element, pathProvider.json(new ResourceLocation(modid, name)));
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to save CakeEffect with name: " + name, e);
            }
        });
    }

    public void add(Supplier<BaseCakeBlock> cake, CakeEffects.MobEffectHolder... mobEffects) {
        List<CakeEffects.MobEffectHolder> holders = List.of(mobEffects);

        JsonElement element = CakeEffects.CODEC.encodeStart(JsonOps.INSTANCE, new CakeEffects(holders)).getOrThrow(false, s -> {});
        map.put(Util.getBlockId(cake.get()).getPath(), element);
    }

    @Override
    public String getName() {
        return "Cake Effects";
    }
}
