package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.torocraft.torohealth.ToroHealth;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Inject(method = "render", at = @At("RETURN"))
	private void render(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			ToroHealth.HUD.draw(drawContext, ToroHealth.CONFIG);
		}
	}

}
