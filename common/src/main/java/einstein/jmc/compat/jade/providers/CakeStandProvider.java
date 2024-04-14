package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.entity.CakeStandBlockEntity;
import einstein.jmc.compat.jade.ModJadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class CakeStandProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CakeStandBlockEntity blockEntity = (CakeStandBlockEntity) accessor.getBlockEntity();

        if (!blockEntity.isEmpty()) {
            IElementHelper helper = IElementHelper.get();
            Block block = blockEntity.getStoredBlock();
            ItemStack stack = new ItemStack(block.asItem());

            if (!stack.isEmpty()) {
                tooltip.add(helper.smallItem(stack).clearCachedMessage());
                tooltip.append(helper.text(Component.literal(" ").append(block.getName())));
                return;
            }

            tooltip.add(helper.text(block.getName()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_STAND;
    }
}
