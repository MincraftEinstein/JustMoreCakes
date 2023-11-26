package einstein.jmc.compat.jade.elements;

import einstein.jmc.compat.jade.ModJadePlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IDisplayHelper;

import static einstein.jmc.JustMoreCakes.mcLoc;

public class FoodPointsSpriteElement extends Element {

    private static final ResourceLocation FOOD_EMPTY_SPRITE = mcLoc("hud/food_empty");
    private static final ResourceLocation FOOD_HALF_SPRITE = mcLoc("hud/food_half");
    private static final ResourceLocation FOOD_FULL_SPRITE = mcLoc("hud/food_full");
    private static final int SPRITE_SIZE = 9;
    private static final int SPACING = 10;

    private final IPluginConfig config;
    private final ModJadePlugin.CakeInfoDisplayType displayType;
    private final int slices;
    private final int nutrition;
    private final Component sliceText;
    private final Font font = Minecraft.getInstance().font;

    public FoodPointsSpriteElement(IPluginConfig config, int slices, int nutrition) {
        displayType = config.getEnum(ModJadePlugin.DISPLAY_TYPE);
        sliceText = Component.translatable("jade.plugin_jmc.count", slices);
        this.config = config;
        this.slices = (displayType == ModJadePlugin.CakeInfoDisplayType.PER_SLICE ? 1 : slices);
        this.nutrition = nutrition;
    }

    @Override
    public Vec2 getSize() {
        int nutritionPerSlice = nutrition * slices;
        boolean isEven = nutritionPerSlice % 2 == 0;
        int maxIconsPerLine = config.getInt(ModJadePlugin.FOOD_ICONS_PER_LINE);
        int totalFoodPoints = (isEven ? nutritionPerSlice : nutritionPerSlice + 1) / 2;
        int foodPointsPerLine = Math.min(maxIconsPerLine, totalFoodPoints);
        int lineCount = (int) Math.ceil((float) totalFoodPoints / maxIconsPerLine);
        int width = SPRITE_SIZE * foodPointsPerLine;

        if (displayType == ModJadePlugin.CakeInfoDisplayType.PER_SLICE) {
            width += font.width(sliceText);
        }

        return new Vec2(width, SPACING * lineCount);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        IDisplayHelper helper = IDisplayHelper.get();
        int nutritionPerSlice = nutrition * slices;
        boolean isEven = nutritionPerSlice % 2 == 0;
        int maxIconsPerLine = config.getInt(ModJadePlugin.FOOD_ICONS_PER_LINE);
        int totalFoodPoints = (isEven ? nutritionPerSlice : nutritionPerSlice + 1) / 2;
        int foodPointsPerLine = Math.min(maxIconsPerLine, totalFoodPoints);
        int xOffset = 0;
        int yOffset = 0;
        int i = 1;

        if (!isEven) {
            blitFoodPoint(guiGraphics, helper, FOOD_HALF_SPRITE, x + xOffset, y + yOffset);
            xOffset = SPRITE_SIZE;
            i++;
        }

        for (; i <= totalFoodPoints; ++i) {
            blitFoodPoint(guiGraphics, helper, FOOD_FULL_SPRITE, x + xOffset, y + yOffset);

            xOffset += SPRITE_SIZE;
            if (i % foodPointsPerLine == 0) {
                yOffset += SPACING;
                xOffset = 0;
            }
        }

        if (displayType == ModJadePlugin.CakeInfoDisplayType.PER_SLICE) {
            helper.drawText(guiGraphics, sliceText, x + ((i - 2) * SPRITE_SIZE) + SPACING, y, IThemeHelper.get().getNormalColor());
        }
    }

    private static void blitFoodPoint(GuiGraphics guiGraphics, IDisplayHelper helper, ResourceLocation overlaySprite, float x, float y) {
        helper.blitSprite(guiGraphics, FOOD_EMPTY_SPRITE, (int) x, (int) y, SPRITE_SIZE, SPRITE_SIZE);
        helper.blitSprite(guiGraphics, overlaySprite, (int) x, (int) y, SPRITE_SIZE, SPRITE_SIZE);
    }

    @Override
    public @Nullable String getMessage() {
        return I18n.get("narration.jade.jmc.nutrition", nutrition);
    }
}
