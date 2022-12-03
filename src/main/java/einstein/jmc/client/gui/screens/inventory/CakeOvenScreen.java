package einstein.jmc.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
	public void render(PoseStack pose, int par1, int par2, float par3) {
		renderBackground(pose);
		super.render(pose, par1, par2, par3);
		renderTooltip(pose, par1, par2);
	}
	
	@Override
	protected void renderBg(PoseStack pose, float par1, int par2, int par3) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		// 'leftPos' and 'topPos' are the calculated edges of the left and top sides of the image
		
		// blit(pose, placmentX, placmentY, imageX, imageY, width, height);
		
		// Updates the flame animation
		if (menu.isLit()) {
			// 12 is the height of the burn flame - 2
			blit(pose, leftPos + 19, topPos + 25 + 12 - menu.getLitProgress(), 176, 12 - menu.getLitProgress(), 14, menu.getLitProgress() + 1);
		}
		
		// If the cake oven is lit update the progress arrow
		blit(pose, leftPos + 93, topPos + 34, 176, 14, menu.getBurnProgress() + 1, 16);
	}
}
