package einstein.jmc.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Advancement.class)
public interface AdvancementAccessor {

    @Mutable
    @Accessor("criteria")
    void setCriteria(Map<String, Criterion<?>> criteria);
}
