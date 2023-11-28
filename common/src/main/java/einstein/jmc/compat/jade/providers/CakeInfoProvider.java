package einstein.jmc.compat.jade.providers;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.compat.jade.elements.FoodPointsSpriteElement;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.overlay.DisplayHelper;

import java.text.DecimalFormat;

public class CakeInfoProvider implements IBlockComponentProvider {

    private static final DecimalFormat FORMATTER = new DecimalFormat("0.##");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        if (block instanceof BaseCakeBlock cake) {
            Pair<Integer, Float> nourishment = cake.getNourishment();
            int nutrition = nourishment.getFirst();
            int slices = cake.getBiteCount() + 1;

            addNutrition(tooltip, config, slices, nutrition);
            addSaturation(tooltip, config, slices, nutrition, nourishment.getSecond());
        }
        else if (block instanceof BaseCandleCakeBlock candleCake) {
            BaseCakeBlock originalCake = candleCake.getOriginalCake();
            Pair<Integer, Float> nourishment = originalCake.getNourishment();
            int nutrition = nourishment.getFirst();
            int slices = originalCake.getBiteCount() + 1;

            addNutrition(tooltip, config, slices, nutrition);
            addSaturation(tooltip, config, slices, originalCake.getNourishment().getFirst(), originalCake.getNourishment().getSecond());
        }
        else if (block == Blocks.CAKE || Util.getVanillaCandleCakes().contains(block)) {
            addNutrition(tooltip, config, 7, 2);
            addSaturation(tooltip, config, 7, 2, 0.1F);
        }
    }

    private static void addNutrition(ITooltip tooltip, IPluginConfig config, int slices, int nutrition) {
        if (config.get(ModJadePlugin.SHOW_NUTRITION)) {
            if (nutrition > 0) {
                tooltip.add(new FoodPointsSpriteElement(config, slices, nutrition));
            }
        }
    }

    private static void addSaturation(ITooltip tooltip, IPluginConfig config, int slices, int nutrition, float saturationModifier) {
        if (config.get(ModJadePlugin.SHOW_SATURATION)) {
            if (saturationModifier > 0 && nutrition > 0) {
                if (config.getEnum(ModJadePlugin.DISPLAY_TYPE) == ModJadePlugin.CakeInfoDisplayType.PER_SLICE) {
                    saturationModifier /= slices;
                }
                tooltip.add(IElementHelper.get().text(Component.translatable("jade.plugin_jmc.saturation", FORMATTER.format(nutrition * saturationModifier * 2))));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_INFO;
    }
}
