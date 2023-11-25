package einstein.jmc.compat.jade;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {

    public static final CakeInfoProvider CAKE_INFO_COMPONENT_PROVIDER = new CakeInfoProvider();
    public static final ResourceLocation DISPLAY_TYPE = JustMoreCakes.loc("cake_info.display_type");
    public static final ResourceLocation FOOD_ICONS_PER_LINE = JustMoreCakes.loc("cake_info.food_icons_per_line");
    public static final ResourceLocation SHOW_SATURATION = JustMoreCakes.loc("cake_info.show_saturation");

    @Override
    public void register(IWailaCommonRegistration registration) {
        IWailaPlugin.super.register(registration);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(DISPLAY_TYPE, CakeInfoDisplayType.TOTAL);
        registration.addConfig(FOOD_ICONS_PER_LINE, 10, 5, 30, false);
        registration.addConfig(SHOW_SATURATION, true);

        registration.registerBlockComponent(CAKE_INFO_COMPONENT_PROVIDER, BaseCakeBlock.class);
        registration.registerBlockComponent(CAKE_INFO_COMPONENT_PROVIDER, BaseCandleCakeBlock.class);
        registration.registerBlockComponent(CAKE_INFO_COMPONENT_PROVIDER, CakeBlock.class);
        registration.registerBlockComponent(CAKE_INFO_COMPONENT_PROVIDER, CandleCakeBlock.class);
    }

    public enum CakeInfoDisplayType {
        TOTAL,
        PER_SLICE
    }
}
