package einstein.jmc.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.CakeOvenMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CakeOvenScreen extends AbstractContainerScreen<CakeOvenMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(JustMoreCakes.MODID, "textures/gui/container/cake_oven.png");
	
	public CakeOvenScreen(CakeOvenMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}
	
	@Override
	protected void init() {
		super.init();
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}
	
	@Override
	public void render(PoseStack pose, int p_97796_, int p_97797_, float p_97798_) {
		renderBackground(pose);
		super.render(pose, p_97796_, p_97797_, p_97798_);
		renderTooltip(pose, p_97796_, p_97797_);
	}
	
	@Override
	protected void renderBg(PoseStack pose, float p_97788_, int p_97789_, int p_97790_) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		
		// Updates the flame animation
		if (menu.isLit()) {
			blit(pose, leftPos + 19, topPos + 25 + 12 - menu.getLitProgress(), 176, 12 - menu.getLitProgress(), 14, menu.getLitProgress() + 1);
		}
		
		// If the cake oven is lit update the progress bar
		blit(pose, leftPos + 93, topPos + 34, 176, 14, menu.getBurnProgress() + 1, 16);
	}
}
