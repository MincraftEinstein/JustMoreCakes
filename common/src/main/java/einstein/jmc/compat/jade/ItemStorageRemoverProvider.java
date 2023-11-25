package einstein.jmc.compat.jade;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public class ItemStorageRemoverProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        tooltip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.ITEM_STORAGE_REMOVER;
    }
}
