package einstein.jmc.mixin;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DataGenerator.PackGenerator.class)
public interface PackGeneratorAccessor {

    @Mutable
    @Accessor("providerPrefix")
    void setProviderPrefix(String providerPrefix);

    @Mutable
    @Accessor("output")
    void setOutput(PackOutput output);
}
