package net.torocraft.torohealth.bars;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.InWorld;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.util.EntityUtil;
import net.torocraft.torohealth.util.EntityUtil.Relation;
import net.torocraft.torohealth.util.ValueUtils;

public class HealthBarRenderer {

	private static final Identifier GUI_BARS_TEXTURES = Identifier.of(ToroHealth.MOD_ID + ":textures/gui/bars.png");
	private static final int DARK_GRAY = 0x808080;
	private static final float FULL_SIZE = 40;

	private static InWorld getConfig() {
		return ToroHealth.CONFIG.inWorld;
	}

	private static final List<LivingEntity> renderedEntities = new ArrayList<>();

	public static void prepareRenderInWorld(LivingEntity entity) {

		MinecraftClient client = MinecraftClient.getInstance();

		if (!EntityUtil.showHealthBar(entity, client)) {
			return;
		}
		if (entity.distanceTo(client.getCameraEntity()) > ToroHealth.CONFIG.inWorld.distance) {
			return;
		}

		HealthBarStates.getState(entity);

		if (Mode.WHEN_HOLDING_WEAPON.equals(getConfig().mode) && !ToroHealth.IS_HOLDING_WEAPON) {
			return;
		}
		if (Mode.NONE.equals(getConfig().mode)) {
			return;
		}
		if (ToroHealth.CONFIG.inWorld.onlyWhenLookingAt && ToroHealth.HUD.getEntity() != entity) {
			return;
		}
		if (ToroHealth.CONFIG.inWorld.onlyWhenHurt
				&& entity.getHealth() >= ValueUtils.bypassLimit(entity, EntityAttributes.GENERIC_MAX_HEALTH)) {
			return;
		}

		renderedEntities.add(entity);
	}

	public static void renderInWorld(Matrix4f positionMatrix, Camera camera) {

		MinecraftClient client = MinecraftClient.getInstance();

		if (camera == null) {
			camera = client.getEntityRenderDispatcher().camera;
		}
		if (camera == null) {
			renderedEntities.clear();
			return;
		}
		if (renderedEntities.isEmpty()) {
			return;
		}

		for (LivingEntity entity : renderedEntities) {
			float scaleToGui = 0.025f;
			boolean sneaking = entity.isInSneakingPose();
			float height = entity.getHeight() + 0.6F - (sneaking ? 0.25F : 0.0F);

			// Use 'false' boolean for safety. May change later.
			float tickDelta = client.getRenderTickCounter().getTickDelta(false);
			double x = MathHelper.lerp((double) tickDelta, entity.prevX, entity.getX());
			double y = MathHelper.lerp((double) tickDelta, entity.prevY, entity.getY());
			double z = MathHelper.lerp((double) tickDelta, entity.prevZ, entity.getZ());

			Vec3d camPos = camera.getPos();
			double camX = camPos.x;
			double camY = camPos.y;
			double camZ = camPos.z;

			Matrix4f matrix4f = new Matrix4f(positionMatrix);
			matrix4f.translate((float) (x - camX), (float) (y + height - camY), (float) (z - camZ));
			matrix4f.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
			matrix4f.rotate(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
			matrix4f.scale(-scaleToGui, -scaleToGui, scaleToGui);

			renderwithMatrix(matrix4f, entity, 0, 0, FULL_SIZE, true);
		}

		renderedEntities.clear();
	}

	public static void renderwithMatrix(Matrix4f matrix, LivingEntity entity, double x, double y, float width,
			boolean inWorld) {

		// Get entity relation to players.
		Relation relation = EntityUtil.determineRelation(entity);

		// Set First Color and Second Color according to the relation.
		int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor
				: ToroHealth.CONFIG.bar.foeColor;
		int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary
				: ToroHealth.CONFIG.bar.foeColorSecondary;

		HealthBarState state = HealthBarStates.getState(entity);

		double maxHealth = ValueUtils.bypassLimit(entity, EntityAttributes.GENERIC_MAX_HEALTH);
		float percent = (float) Math.min(1, Math.min(state.health, maxHealth) / maxHealth);
		float percent2 = (float) (Math.min(state.previousHealthDisplay, maxHealth) / maxHealth);

		// Vertical offset.
		int zOffset = 0;

		drawBar(matrix, x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
		drawBar(matrix, x, y, width, percent2, color2, zOffset++, inWorld);
		drawBar(matrix, x, y, width, percent, color, zOffset, inWorld);
	}

	public static void renderwithDrawContext(DrawContext drawContext, LivingEntity entity, double x, double y,
			float width, boolean inWorld) {

		MatrixStack matrix = drawContext.getMatrices();

		// Get entity relation to players.
		Relation relation = EntityUtil.determineRelation(entity);

		// Set First Color and Second Color according to the relation.
		int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor
				: ToroHealth.CONFIG.bar.foeColor;
		int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary
				: ToroHealth.CONFIG.bar.foeColorSecondary;

		HealthBarState state = HealthBarStates.getState(entity);

		double maxHealth = ValueUtils.bypassLimit(entity, EntityAttributes.GENERIC_MAX_HEALTH);
		float percent = (float) Math.min(1, Math.min(state.health, maxHealth) / maxHealth);
		float percent2 = (float) (Math.min(state.previousHealthDisplay, maxHealth) / maxHealth);

		// Vertical offset.
		int zOffset = 0;

		// Get the Position Matrix of the current DrawContext.
		Matrix4f m4f = matrix.peek().getPositionMatrix(); // .getModel();
		drawBar(m4f, x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
		drawBar(m4f, x, y, width, percent2, color2, zOffset++, inWorld);
		drawBar(m4f, x, y, width, percent, color, zOffset, inWorld);

		if (!inWorld) {
			if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
				drawDamageNumber(drawContext, state.lastDmgCumulative, x, y, width);
			} else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
				drawDamageNumber(drawContext, state.lastDmg, x, y, width);
			}
		}
	}

	public static void drawDamageNumber(DrawContext drawContext, int dmg, double x, double y, float width) {
		int i = Math.abs(Math.round(dmg));
		if (i == 0) {
			return;
		}
		String s = Integer.toString(i);
		MinecraftClient minecraft = MinecraftClient.getInstance();

		int sw = minecraft.textRenderer.getWidth(s);
		int color = dmg < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor;

		drawContext.drawTextWithShadow(minecraft.textRenderer, s, (int) (x + (width / 2) - sw), (int) y + 10, color);
	}

	private static void drawBar(Matrix4f matrix4f, double x, double y, float width, float percent, int color,
			int zOffset, boolean inWorld) {
		float c = 0.00390625F;

		// Horizontal starting point in texture files, which is u in uv format.
		int u = 0;

		// Vertical starting point in texture files, which is v in uv format.
		int v = 6 * 5 * 2 + 5;

		// Horizontal pixel amount. 92 is the actual width of the bar texture.
		int uw = MathHelper.ceil(92 * percent);

		// Bar texture's height is 5 in default.
		int vh = 5;

		double size = percent * width;
		double h = inWorld ? 4 : 6;

		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;

		RenderSystem.setShaderColor(r, g, b, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderTexture(0, GUI_BARS_TEXTURES);
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		float half = width / 2;

		float zOffsetAmount = inWorld ? -0.1F : 0.1F;

		Tessellator tessellator = Tessellator.getInstance();

		// Changed in Fabric 1.21. Just begin the buffer and then store it.
		BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

		// Changed in Fabric 1.21. No need for BufferBuilder.next() method!
		buffer.vertex(matrix4f, (float) (-half + x), (float) y, zOffset * zOffsetAmount).texture(u * c, v * c);
		buffer.vertex(matrix4f, (float) (-half + x), (float) (h + y), zOffset * zOffsetAmount).texture(u * c,
				(v + vh) * c);
		buffer.vertex(matrix4f, (float) (-half + size + x), (float) (h + y), zOffset * zOffsetAmount)
				.texture((u + uw) * c, (v + vh) * c);
		buffer.vertex(matrix4f, (float) (-half + size + x), (float) y, zOffset * zOffsetAmount).texture(((u + uw) * c),
				v * c);

		// Changed in Fabric 1.21. Use global program to draw.
		BufferRenderer.drawWithGlobalProgram(buffer.end());

		// IMPORTANT: RESET SHADER COLOR!!!!!!
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}

}
