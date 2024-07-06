package einstein.jmc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public record BowlContents(ResourceLocation texture) {

    public static final ResourceKey<Registry<BowlContents>> REGISTRY_KEY = ResourceKey.createRegistryKey(JustMoreCakes.loc("bowl_contents"));
    public static final Codec<BowlContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(BowlContents::texture)
    ).apply(instance, BowlContents::new));

    public static final ResourceKey<BowlContents> EMPTY_KEY = ResourceKey.create(REGISTRY_KEY, JustMoreCakes.loc("empty"));
    public static final EmptyBowlContentsSupplier EMPTY = new EmptyBowlContentsSupplier();

    public static Holder<BowlContents> getHolder(@Nullable Level level, ResourceLocation id) {
        Optional<Holder.Reference<BowlContents>> holder = getRegistry(level).getHolder(ResourceKey.create(REGISTRY_KEY, id));
        return holder.isPresent() ? holder.get() : EMPTY.getHolder();
    }

    public static Registry<BowlContents> getRegistry(@Nullable Level level) {
        RegistryAccess access = level != null ? level.registryAccess() : Util.getRegistryAccess();
        try {
            return access.registryOrThrow(REGISTRY_KEY);
        }
        catch (Exception e) {
            return Util.getRegistryAccess().registryOrThrow(REGISTRY_KEY);
        }
    }

    public static class EmptyBowlContentsSupplier implements Supplier<BowlContents> {

        @Nullable
        private Holder<BowlContents> value;

        @Override
        public BowlContents get() {
            return getHolder().value();
        }

        public Holder<BowlContents> getHolder() {
            if (value == null) {
                value = getRegistry(null).getHolderOrThrow(EMPTY_KEY);
            }
            return value;
        }

        public void clear() {
            value = null;
        }
    }
}
