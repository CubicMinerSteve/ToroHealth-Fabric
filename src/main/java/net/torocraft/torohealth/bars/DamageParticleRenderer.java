package net.torocraft.torohealth.bars;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.torocraft.torohealth.ToroHealth;

public class DamageParticleRenderer {

	public static void renderParticle(Entity entity, int lastDamage) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;

		if (world == null || world != entity.getWorld()) {
			return;
		}

		Camera camera = client.gameRenderer.getCamera();
		Vec3d entityLocation = entity.getPos().add(0, entity.getHeight() / 2, 0);

		double distanceSquared = camera.getPos().squaredDistanceTo(entityLocation.getX(), entityLocation.getY(),
				entityLocation.getZ());
		if (distanceSquared > ToroHealth.CONFIG.particle.distanceSquared) {
			return;
		}

		double offsetBy = entity.getWidth();
		Vec3d offset = camera.getPos().subtract(entityLocation).normalize().multiply(offsetBy);
		Vec3d particlePos = entityLocation.add(offset);

		Vec3d particleVelocity = entity.getVelocity();
		double velocityX = ToroHealth.RAND.nextGaussian() * 0.04;
		double velocityY = 0.10 + (ToroHealth.RAND.nextGaussian() * 0.05);
		double velocityZ = ToroHealth.RAND.nextGaussian() * 0.04;

		particleVelocity = particleVelocity.add(velocityX, velocityY, velocityZ);

		DamageParticle particle = new DamageParticle(world, particlePos.x, particlePos.y, particlePos.z,
				particleVelocity.x, particleVelocity.y, particleVelocity.z);
		particle.setDamageNumber(lastDamage);
		particle.setMaxAge(50);
		client.particleManager.addParticle(particle);
	}
}
