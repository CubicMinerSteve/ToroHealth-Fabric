package net.torocraft.torohealth.bars;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.torocraft.torohealth.ToroHealth;

public class DamageParticle extends Particle {

	public int damage;

	public DamageParticle(ClientWorld world, Vec3d pos, Vec3d velocity) {
		super(world, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
	}

	public void setDamageNumber(@NotNull int dmg) {
		this.damage = dmg;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		int i = Math.abs(Math.round(damage));
		if (i == 0) {
			return;
		}
		String s = Integer.toString(i);

		MinecraftClient client = MinecraftClient.getInstance();
		float scaleToGui = 0.025f;
		Vec3d cameraLocation = client.gameRenderer.getCamera().getPos();

		float particleX = (float) (MathHelper.lerp(tickDelta, prevPosX, x) - cameraLocation.x);
		float particleY = (float) (MathHelper.lerp(tickDelta, prevPosY, y) - cameraLocation.y);
		float particleZ = (float) (MathHelper.lerp(tickDelta, prevPosZ, z) - cameraLocation.z);
		
		this.gravityStrength = 0.25f;

		Matrix4f matrix = new Matrix4f();
		matrix = matrix.translation(particleX, particleY, particleZ);
		matrix = matrix.rotate(camera.getRotation());
		matrix = matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

		TextRenderer textRenderer = client.textRenderer;
		Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		int textColor = damage < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor;
		textRenderer.draw(s, 0, 0, textColor, false, matrix, vertexConsumerProvider, TextLayerType.NORMAL, 0, 15728880, textRenderer.isRightToLeft());
		vertexConsumerProvider.draw();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}
}
