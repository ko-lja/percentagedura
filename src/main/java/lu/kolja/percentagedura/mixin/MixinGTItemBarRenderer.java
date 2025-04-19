package lu.kolja.percentagedura.mixin;

import lu.kolja.percentagedura.KeyMappings;
import lu.kolja.percentagedura.render.DisplayState;
import com.gregtechceu.gtceu.client.renderer.item.decorator.GTToolBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

@Mixin(value = GTToolBarRenderer.class, remap = false)
public class MixinGTItemBarRenderer {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y, CallbackInfoReturnable<Boolean> cir) {
        if (KeyMappings.state != DisplayState.DISABLED) cir.cancel();
    }
}
