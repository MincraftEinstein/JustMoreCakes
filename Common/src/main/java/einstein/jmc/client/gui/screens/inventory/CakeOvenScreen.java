package einstein.jmc.client.gui.screens.inventory;

import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static einstein.jmc.JustMoreCakes.loc;

public class CakeOvenScreen extends AbstractContainerScreen<CakeOvenMenu> {

    private static final ResourceLocation TEXTURE = loc("textures/gui/container/cake_oven.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = loc("container/cake_oven/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = loc("container/cake_oven/burn_progress");
    private static final int LIT_SIZE = 14;
    private static final int BURN_WIDTH = 24;
    private static final int BURN_HEIGHT = 16;

    public CakeOvenScreen(CakeOvenMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (menu.isLit()) {
            int litProgress = Mth.ceil(menu.getLitProgress() * 13) + 1;
            guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, LIT_SIZE, LIT_SIZE, 0, LIT_SIZE - litProgress, leftPos + 19, topPos + 25 + LIT_SIZE - litProgress, LIT_SIZE, litProgress);
        }

        int burnProgress = Mth.ceil(menu.getBurnProgress() * BURN_WIDTH);
        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, BURN_WIDTH, BURN_HEIGHT, 0, 0, leftPos + 93, topPos + 34, burnProgress, BURN_HEIGHT);
    }
}
