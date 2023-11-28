package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleCakeBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.Map;
import java.util.Optional;

public class CandleTypeProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        IElementHelper helper = IElementHelper.get();
        if (block instanceof BaseCandleCakeBlock candleCake) {
            Block candle = candleCake.getCandle();
            tooltip.add(helper.smallItem(new ItemStack(candle)).clearCachedMessage());
            tooltip.append(helper.text(candle.getName()));
        }
        else if (block instanceof CandleCakeBlock candleCake) {
            Optional<Block> candle = Util.VANILLA_CANDLE_CAKES_BY_CANDLE.entrySet().stream()
                    .filter(entry -> candleCake.equals(entry.getValue())).map(Map.Entry::getKey).findFirst();
            if (candle.isPresent()) {
                tooltip.add(helper.smallItem(new ItemStack(candle.get())).clearCachedMessage());
                tooltip.append(helper.text(candle.get().getName()));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CANDLE_TYPE;
    }
}
