package einstein.jmc.compat.rei;

import einstein.jmc.init.ModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CakeOvenRecipeCategory implements DisplayCategory<CakeOvenDisplay> {

    @Override
    public CategoryIdentifier<? extends CakeOvenDisplay> getCategoryIdentifier() {
        return ModREICommonPlugin.CAKE_OVEN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.rei.jmc.cake_oven");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.CAKE_OVEN.get());
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public DisplayRenderer getDisplayRenderer(CakeOvenDisplay display) {
        return SimpleDisplayRenderer.from(display.getInputEntries(), display.getOutputEntries());
    }

    @Override
    public List<Widget> setupDisplay(CakeOvenDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getY() + 10);
        int cookingTime = display.getCookingTime();
        DecimalFormat formatter = new DecimalFormat("###.##");
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.getX() + 78, startPoint.getY() + 14)));
        widgets.add(Widgets.createBurningFire(new Point(startPoint.getX() - 20, startPoint.getY() + 25)).animationDurationMS(10000));
        Component text = Component.translatable("category.rei.jmc.cake_oven.time&xp", formatter.format(display.getExperience()), formatter.format(cookingTime / 20));
        widgets.add(Widgets.createLabel(new Point(bounds.getX() + Minecraft.getInstance().font.width(text) + 5, bounds.getY() + 5), text).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.getX() + 42, startPoint.getY() + 13)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 78, startPoint.getY() + 14)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createShapelessIcon(bounds));

        int slotIndex = 0;
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                widgets.add(Widgets.createSlot(new Point(startPoint.getX() + (x * 18), startPoint.getY() + 5 + (y * 18))).entries(display.getInputEntries().get(slotIndex++)).markInput());
            }
        }

        return widgets;
    }
}
