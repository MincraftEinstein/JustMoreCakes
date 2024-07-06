package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.compat.jade.ModJadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public class CeramicBowlProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        IElementHelper helper = IElementHelper.get();
        CompoundTag tag = accessor.getServerData();

        if (tag.contains("MixingTime")) {
            int mixingProgress = tag.getInt("MixingProgress");
            int mixingTime = tag.getInt("MixingTime");
            tooltip.add(helper.text(Component.translatable("jade.plugin_jmc.progress", mixingProgress, mixingTime)));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        CeramicBowlBlockEntity blockEntity = (CeramicBowlBlockEntity) accessor.getBlockEntity();
        int mixingProgress = blockEntity.getMixingProgress();

        if (mixingProgress > 0) {
            tag.putInt("MixingProgress", mixingProgress);
            blockEntity.getMatchingRecipe().ifPresent(holder -> tag.putInt("MixingTime", holder.value().getMixingTime()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CERAMIC_BOWL;
    }
}
