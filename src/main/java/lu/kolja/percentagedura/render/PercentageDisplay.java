package lu.kolja.percentagedura.render;

import lu.kolja.percentagedura.KeyMappings;
import lu.kolja.percentagedura.NumberUtil;
import lu.kolja.percentagedura.xmod.XMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class PercentageDisplay implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
        if (stack.isEmpty() || !stack.isBarVisible()) return false;
        switch (KeyMappings.state) {
            case DISABLED -> renderItemBar(stack, xPosition, yPosition, guiGraphics);
            case ENABLED_PERCENTAGE -> {
                for (var supplier : XMod.COMPATIBILITY) {
                    var compatibility = supplier.compatibility(stack);
                    if (compatibility != null && !compatibility.isEmpty()) {
                        for (int i = 0; i < compatibility.size(); i++) { //no enhanced for loop since we need to add energy and durability bar in some cases
                            var comp = compatibility.get(i);
                            if (comp.isActive()) {
                                double percentage = comp.percentage();
                                renderText(guiGraphics, font, NumberUtil.formatPercentage(percentage), xPosition, yPosition - i * 5, comp.color());
                            }
                        }
                        return true;
                    }
                }
            }
            case ENABLED_NUMBER -> {
                for (var supplier : XMod.COMPATIBILITY) {
                    var compatibility = supplier.compatibility(stack);
                    if (compatibility != null && !compatibility.isEmpty()) {
                        for (int i = 0; i < compatibility.size(); i++) { //no enhanced for loop since we need to add energy and durability bar in some cases
                            var comp = compatibility.get(i);
                            if (comp.isActive()) {
                                double amount = comp.amount();
                                renderText(guiGraphics, font, NumberUtil.formatNumber(amount), xPosition, yPosition - i * 5, comp.color());
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public void renderText(GuiGraphics guiGraphics, Font font, String text, int xPosition, int yPosition, int color) {
        PoseStack poseStack = guiGraphics.pose();
        int stringWidth = font.width(text);
        int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
        int y = (yPosition * 2) + 22;
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0D, 0.0D, 500.0D);
        MultiBufferSource.BufferSource buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        font.drawInBatch(text, x, y, color, true, poseStack.last().pose(), buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
        buffersource.endBatch();
        poseStack.popPose();
    }

    private void renderItemBar(ItemStack stack, int xPosition, int yPosition, GuiGraphics guiGraphics) {
        int l = stack.getBarWidth();
        int i = stack.getBarColor();
        int j = xPosition + 2;
        int k = yPosition + 13;
        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
    }
}
