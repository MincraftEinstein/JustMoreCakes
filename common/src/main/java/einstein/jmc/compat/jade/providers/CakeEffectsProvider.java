package einstein.jmc.compat.jade.providers;

import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.compat.jade.ModJadePlugin;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.CakeUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        if (!ModServerConfigs.ALLOW_DISPLAYING_CAKE_EFFECTS.get()) {
            return;
        }

        Block block = accessor.getBlock();
        CakeEffects cakeEffects = null;

        if (block instanceof CakeEffectsHolder holder) {
            cakeEffects = holder.justMoreCakes$getCakeEffects();
        }
        else if (block instanceof BaseCandleCakeBlock candleCake) {
            cakeEffects = candleCake.getParentCake().justMoreCakes$getCakeEffects();
        }
        else if (CakeUtil.getVanillaCandleCakes().contains(block)) {
            cakeEffects = ((CakeEffectsHolder) Blocks.CAKE).justMoreCakes$getCakeEffects();
        }

        if (cakeEffects != null) {
            IElementHelper helper = IElementHelper.get();
            IThemeHelper theme = IThemeHelper.get();
            ITooltip box = helper.tooltip();

            for (MobEffectInstance effectInstance : cakeEffects.mobEffects()) {
                Holder<MobEffect> effectHolder = effectInstance.getEffect();
                MobEffect effect = effectHolder.value();
                MobEffectInstance instance = new MobEffectInstance(effectHolder, effectInstance.getDuration(), effectInstance.getAmplifier());
                Component name = StatusEffectsProvider.getEffectName(instance);
                String duration = instance.isInfiniteDuration() ? I18n.get("effect.duration.infinite") : StringUtil.formatTickDuration(instance.getDuration(), accessor.tickRate());
                Component info = effect.isInstantenous() ? name : Component.translatable("jade.potion", name, duration);

                box.add(effect.getCategory() != MobEffectCategory.HARMFUL ? theme.success(info) : theme.danger(info));
            }
            tooltip.add(helper.box(box, BoxStyle.getNestedBox()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModJadePlugin.CAKE_EFFECTS;
    }
}
