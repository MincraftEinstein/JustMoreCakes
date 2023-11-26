package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.entity.CakeOvenBlockEntity;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public class CakeOvenProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor>, CakeOvenConstants {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag tag = accessor.getServerData();
        if (tag.contains("CookTime")) {
            int cookTime = tag.getInt("CookTime");
            int cookTimeTotal = tag.getInt("CookTimeTotal");
            NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
            IElementHelper helper = IElementHelper.get();

            ContainerHelper.loadAllItems(tag, items);
            tooltip.add(helper.item(items.get(0))); // First item is appended here so the 'add' method can be used

            // Starts at 1 to skip the first item, since it is already appended above
            for (int i = 1; i < INGREDIENT_SLOT_COUNT; i++) {
                tooltip.append(helper.item(items.get(i)));
            }

            tooltip.append(helper.item(items.get(FUEL_SLOT)));
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(helper.progress((float) cookTime / cookTimeTotal).translate(new Vec2(-2, 0)));
            tooltip.append(helper.item(items.get(RESULT_SLOT)));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        CakeOvenBlockEntity blockEntity = (CakeOvenBlockEntity) accessor.getBlockEntity();

        if (!blockEntity.isEmpty()) {
            ContainerHelper.saveAllItems(tag, blockEntity.getItems());
            CompoundTag savedTag = blockEntity.saveWithoutMetadata();
            tag.putInt("CookTime", savedTag.getInt("CookTime"));
            tag.putInt("CookTimeTotal", savedTag.getInt("CookTimeTotal"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_OVEN;
    }
}
