package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleCakeBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Map;

public class CandleTypeProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();

        if (block instanceof BaseCandleCakeBlock candleCake) {
            ModJadePlugin.addItemToTooltip(tooltip, candleCake.getCandle());
        }
        else if (block instanceof CandleCakeBlock candleCake) {
            Util.VANILLA_CANDLE_CAKES_BY_CANDLE.entrySet().stream()
                    .filter(entry -> candleCake.equals(entry.getValue())).map(Map.Entry::getKey).findFirst()
                    .ifPresent(value -> ModJadePlugin.addItemToTooltip(tooltip, value));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CANDLE_TYPE;
    }
}
