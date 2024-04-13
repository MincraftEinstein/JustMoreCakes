package einstein.jmc.compat.rei;

import einstein.jmc.init.ModItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MixingRecipeCategory implements DisplayCategory<MixingDisplay> {

    @Override
    public CategoryIdentifier<? extends MixingDisplay> getCategoryIdentifier() {
        return ModREICommonPlugin.MIXING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.rei.jmc.mixing");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.WHISK.get());
    }

    @Override
    public int getDisplayHeight() {
        return 70;
    }

    @Override
    public List<Widget> setupDisplay(MixingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 50, bounds.getY() + 10);
        int mixingTime = display.getMixingTime();
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.getX() + 78, startPoint.getY() + 14)));
        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 78, startPoint.getY() + 14)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createArrow(new Point(startPoint.getX() + 42, startPoint.getY() + 13)));
        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 44, startPoint.getY() + 36)).entry(EntryStacks.of(ModItems.WHISK.get())));
        Component text = Component.translatable("category.rei.jmc.mixing.uses", mixingTime);
        widgets.add(Widgets.createLabel(new Point(startPoint.getX() + 68, startPoint.getY() + 41), text).noShadow().leftAligned().color(0xFF404040, 0xFFBBBBBB));
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
