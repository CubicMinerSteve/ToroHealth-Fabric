package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.util.UnlimitedValue;

public class BarDisplay {

	private static final Identifier ICON_TEXTURES = new Identifier("textures/gui/icons.png");

	private final MinecraftClient mc;

	public BarDisplay(MinecraftClient mc) {
		this.mc = mc;
	}

	public void draw(DrawContext drawContext, LivingEntity entity) {

		// The starting offset of horizontal position.
		int xOffset = 0;
		TextRenderer textRenderer = mc.textRenderer;

		// Set render color to 0xFFFFFF and make it default 100% opaque white.
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		// Texture Settings.
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		// Enable OpenGL Blend to process colors.
		RenderSystem.enableBlend();

		MatrixStack matrix = drawContext.getMatrices();
		HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);

		String name = getEntityName(entity);
		int healthMax = MathHelper.ceil(UnlimitedValue.getWideRangedAttribute(entity, EntityAttributes.GENERIC_MAX_HEALTH));
		int healthCur = MathHelper.ceil(entity.getHealth());
		String healthText = healthCur + "/" + healthMax;

		drawContext.drawTextWithShadow(textRenderer, name, xOffset, 2, 0xffffff);
		xOffset += textRenderer.getWidth(name) + 5;

		renderHeartIcon(drawContext, xOffset, (int) 2);
		xOffset += 10;

		drawContext.drawTextWithShadow(textRenderer, healthText, xOffset, 2, 0xe0e0e0);
		xOffset += textRenderer.getWidth(healthText) + 5;

		int armor = entity.getArmor();
		if (armor > 0) {
			renderArmorIcon(drawContext, xOffset, (int) 2);
			xOffset += 10;
			drawContext.drawTextWithShadow(textRenderer, entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
		}
	}

	private String getEntityName(LivingEntity entity) {
		return entity.getDisplayName().getString();
	}

	private void renderArmorIcon(DrawContext drawContext, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		drawContext.drawTexture(ICON_TEXTURES, x, y, 34, 9, 9, 9);
	}

	private void renderHeartIcon(DrawContext drawContext, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		drawContext.drawTexture(ICON_TEXTURES, x, y, 16 + 36, 0, 9, 9);
	}

}
