package einstein.jmc.compat.jade;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.data.cakeeffect.CakeEffects;
import einstein.jmc.util.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import snownee.jade.addon.vanilla.StatusEffectsProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public class CakeEffectsProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        CakeEffects cakeEffects = null;

        if (block instanceof CakeEffectsHolder holder) {
            cakeEffects = holder.getCakeEffects();
        }
        else if (block instanceof BaseCandleCakeBlock candleCake) {
            cakeEffects = candleCake.getOriginalCake().getCakeEffects();
        }
        else if (Util.getVanillaCandleCakes().contains(block)) {
            cakeEffects = ((CakeEffectsHolder) Blocks.CAKE).getCakeEffects();
        }

        if (cakeEffects != null) {
            IElementHelper helper = IElementHelper.get();
            IThemeHelper theme = IThemeHelper.get();
            ITooltip box = helper.tooltip();

            for (CakeEffects.MobEffectHolder holder : cakeEffects.mobEffects()) {
                MobEffectInstance instance = new MobEffectInstance(holder.effect(), holder.duration().orElse(0), holder.amplifier().orElse(0));
                Component name = StatusEffectsProvider.getEffectName(instance);
                String duration;

                if (instance.isInfiniteDuration()) {
                    duration = I18n.get("effect.duration.infinite");
                }
                else {
                    duration = StringUtil.formatTickDuration(instance.getDuration());
                }

                Component info = Component.translatable("jade.potion", name, duration);
                box.add(holder.effect().getCategory() != MobEffectCategory.HARMFUL ? theme.success(info) : theme.danger(info));
            }
            tooltip.add(helper.box(box, BoxStyle.getNestedBox()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_EFFECTS;
    }
}
