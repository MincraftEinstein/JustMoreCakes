package einstein.jmc.client.gui.screens.inventory;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CakeOvenScreen extends AbstractContainerScreen<CakeOvenMenu> {

    private static final ResourceLocation TEXTURE = JustMoreCakes.loc("textures/gui/container/cake_oven.png");

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
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        // 'leftPos' and 'topPos' are the calculated edges of the left and top sides of the image

        // guiGraphics.blit(texture, placmentX, placmentY, imageX, imageY, width, height);

        // Updates the flame animation
        if (menu.isLit()) {
            // 12 is the height of the burn flame - 2
            guiGraphics.blit(TEXTURE, leftPos + 19, topPos + 25 + 12 - menu.getLitProgress(), 176, 12 - menu.getLitProgress(), 14, menu.getLitProgress() + 1);
        }

        // If the cake oven is lit update the progress arrow
        guiGraphics.blit(TEXTURE, leftPos + 93, topPos + 34, 176, 14, menu.getBurnProgress() + 1, 16);
    }
}
