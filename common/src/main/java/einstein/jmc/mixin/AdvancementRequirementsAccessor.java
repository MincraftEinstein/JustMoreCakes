package einstein.jmc.mixin;

import net.minecraft.advancements.AdvancementRequirements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AdvancementRequirements.class)
public interface AdvancementRequirementsAccessor {

    @Mutable
    @Accessor("requirements")
    void setRequirements(List<List<String>> requirements);
}
