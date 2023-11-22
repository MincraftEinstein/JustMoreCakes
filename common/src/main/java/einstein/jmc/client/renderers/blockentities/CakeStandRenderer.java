package einstein.jmc.client.renderers.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.jmc.block.entity.CakeStandBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CakeStandRenderer implements BlockEntityRenderer<CakeStandBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public CakeStandRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(CakeStandBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!blockEntity.isEmpty()) {
            poseStack.pushPose();
            poseStack.scale(0.75F, 0.75F, 0.75F);
            poseStack.translate(0.169, 0.5, 0.169);
            blockRenderer.renderSingleBlock(blockEntity.getStoredBlock().defaultBlockState(), poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}
