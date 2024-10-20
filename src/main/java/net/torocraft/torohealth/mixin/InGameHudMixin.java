package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.torocraft.torohealth.ToroHealth;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Inject(method = "render", at = @At("RETURN"))
	private void render(MatrixStack matrixStack, float partial, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			ToroHealth.HUD.draw(matrixStack, ToroHealth.CONFIG);
		}
	}

}
