package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.compat.jade.elements.FoodPointsSpriteElement;
import einstein.jmc.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.text.DecimalFormat;

public class CakeNourishmentProvider implements IBlockComponentProvider {

    private static final DecimalFormat FORMATTER = new DecimalFormat("0.##");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        if (block instanceof BaseCakeBlock cake) {
            addNourishmentInfo(tooltip, config, cake.getSlices() + 1, cake.getNutrition(), cake.getSaturationModifier());
        }
        else if (block instanceof BaseCandleCakeBlock candleCake) {
            BaseCakeBlock parentCake = candleCake.getParentCake();
            addNourishmentInfo(tooltip, config, parentCake.getSlices() + 1, parentCake.getNutrition(), parentCake.getSaturationModifier());
        }
        else if (block == Blocks.CAKE || Util.getVanillaCandleCakes().contains(block)) {
            addNourishmentInfo(tooltip, config, 7, 2, 0.1F);
        }
    }

    private static void addNourishmentInfo(ITooltip tooltip, IPluginConfig config, int slices, int nutrition, float saturationModifier) {
        if (config.get(ModJadePlugin.SHOW_NUTRITION)) {
            if (nutrition > 0) {
                tooltip.add(new FoodPointsSpriteElement(config, slices, nutrition));
            }
        }

        if (config.get(ModJadePlugin.SHOW_SATURATION)) {
            if (saturationModifier > 0 && nutrition > 0) {
                if (config.getEnum(ModJadePlugin.DISPLAY_TYPE) == ModJadePlugin.CakeInfoDisplayType.TOTAL) {
                    saturationModifier *= slices;
                }
                tooltip.add(IElementHelper.get().text(Component.translatable("jade.plugin_jmc.saturation", FORMATTER.format(nutrition * saturationModifier * 2))));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_NOURISHMENT;
    }
}
