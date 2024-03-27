package einstein.jmc.client.renderers.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CeramicBowlRenderer implements BlockEntityRenderer<CeramicBowlBlockEntity> {

    public static final float ITEM_SCALE = 0.375F;
    public static final double OFFSET = 0.0625 * ITEM_SCALE;
    private final ItemRenderer itemRenderer;

    public CeramicBowlRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CeramicBowlBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!blockEntity.isEmpty()) {
            Level level = blockEntity.getLevel();
            int seed = (int) blockEntity.getBlockPos().asLong();

            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                ItemStack stack = blockEntity.getItem(i);

                for (int i2 = 0; i2 < getRenderedItemCount(stack); i2++) {
                    poseStack.pushPose();
                    poseStack.translate(0.5, (OFFSET * i2 * 2) + (i * 0.0001), 0.5);
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

    private int getRenderedItemCount(ItemStack stack) {
        int count = stack.getCount();
        return count > 48 ? 5 : count > 32 ? 4 : count > 16 ? 3 : count > 1 ? 2 : 1;
    }
}
