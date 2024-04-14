package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.entity.CakeStandBlockEntity;
import einstein.jmc.compat.jade.ModJadePlugin;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CakeStandProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CakeStandBlockEntity blockEntity = (CakeStandBlockEntity) accessor.getBlockEntity();

        if (!blockEntity.isEmpty()) {
            ModJadePlugin.addItemToTooltip(tooltip, blockEntity.getStoredBlock());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_STAND;
    }
}
