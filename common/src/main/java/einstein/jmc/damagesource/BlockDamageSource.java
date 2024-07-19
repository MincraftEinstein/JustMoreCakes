package einstein.jmc.damagesource;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BlockDamageSource extends DamageSource {

    private final Block block;

    public BlockDamageSource(RegistryAccess registryAccess, @Nullable Block block) {
        super(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(JustMoreCakes.OBSIDIAN_CAKE_DAMAGE_TYPE));
        this.block = block;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity killedEntity) {
        String msg = "death.attack." + getMsgId();
        return block != null ? Component.translatable(msg + ".block", killedEntity.getDisplayName(), block.getName()) : super.getLocalizedDeathMessage(killedEntity);
    }
}
