package einstein.jmc.client.renderers.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import einstein.jmc.block.CeramicBowlBlock;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CeramicBowlRenderer implements BlockEntityRenderer<CeramicBowlBlockEntity> {

    public static final float ITEM_SCALE = 0.375F;
    public static final float CENTER_X = (10 / 16F) / 2;
    private final ItemRenderer itemRenderer;

    public CeramicBowlRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CeramicBowlBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int fillLevel = blockEntity.getBlockState().getValue(CeramicBowlBlock.FILL_LEVEL);
        if (fillLevel > 0) {
            ResourceLocation texture = blockEntity.getContents().value().texture();
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);

            PoseStack.Pose last = poseStack.last();
            Matrix4f pose = last.pose();
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.solid());

            float minU = sprite.getU(0.1875F);
            float maxU = sprite.getU(0.8125F);
            float minV = sprite.getV(0.1875F);
            float maxV = sprite.getV(0.8125F);
            float y = 0.1875F + fillLevel * 0.0625F;

            vertexConsumer.addVertex(pose, -CENTER_X, y, -CENTER_X)
                    .setColor(0xFF, 0xFF, 0xFF, 1)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(last, 0, 1, 0);

            vertexConsumer.addVertex(pose, -CENTER_X, y, CENTER_X)
                    .setColor(0xFF, 0xFF, 0xFF, 1)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(last, 0, 1, 0);

            vertexConsumer.addVertex(pose, CENTER_X, y, CENTER_X)
                    .setColor(0xFF, 0xFF, 0xFF, 1)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(last, 0, 1, 0);

            vertexConsumer.addVertex(pose, CENTER_X, y, -CENTER_X)
                    .setColor(0xFF, 0xFF, 0xFF, 1)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(last, 0, 1, 0);

            poseStack.popPose();
        }

        if (!blockEntity.isEmpty()) {
            Level level = blockEntity.getLevel();
            int seed = (int) blockEntity.getBlockPos().asLong();

            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                if (i != CeramicBowlBlockEntity.RESULT_SLOT) {
                    ItemStack stack = blockEntity.getItem(i);

                    poseStack.pushPose();
                    poseStack.translate(0.5, i * 0.0001, 0.5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                    poseStack.translate(-0.125, 0, -0.125);
                    poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, level, seed + i);
                    poseStack.popPose();
                }
            }
        }
    }
}
