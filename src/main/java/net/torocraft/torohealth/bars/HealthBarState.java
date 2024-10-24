package net.torocraft.torohealth.bars;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.ValueUtils;

public class HealthBarState {

	public final LivingEntity entity;

	public float health;
	public float previousHealth;
	public float previousHealthDisplay;
	public float previousHealthDelay;

	public int lastDmg;
	public int lastDmgCumulative;

	public float lastHealth;
	public float lastDmgDelay;

	private float animationSpeed = 0;

	private static final float HEALTH_INDICATOR_DELAY = 10;

	public HealthBarState(LivingEntity entity) {
		this.entity = entity;
	}

	public void tick() {
		health = (float) Math.min(entity.getHealth(),
				ValueUtils.bypassLimit(entity, EntityAttributes.GENERIC_MAX_HEALTH));
		incrementTimers();

		if (lastHealth < 0.1) {
			reset();
		} else if (lastHealth != health) {
			handleHealthChange();
		} else if (lastDmgDelay == 0.0F) {
			reset();
		}

		updateAnimations();
	}

	private void reset() {
		lastHealth = health;
		lastDmg = 0;
		lastDmgCumulative = 0;
	}

	private void incrementTimers() {
		if (this.lastDmgDelay > 0) {
			this.lastDmgDelay--;
		}
		if (this.previousHealthDelay > 0) {
			this.previousHealthDelay--;
		}
	}

	private void handleHealthChange() {
		lastDmg = MathHelper.ceil(lastHealth) - MathHelper.ceil(health);
		lastDmgCumulative += lastDmg;

		lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
		lastHealth = health;
		if (ToroHealth.CONFIG.particle.show) {
			DamageParticleRenderer.renderParticle(entity, lastDmg);
		}
	}

	private void updateAnimations() {
		if (previousHealthDelay > 0) {
			float diff = previousHealthDisplay - health;
			if (diff > 0) {
				animationSpeed = diff / 10f;
			}
		} else if (previousHealthDelay < 1 && previousHealthDisplay > health) {
			previousHealthDisplay -= animationSpeed;
		} else {
			previousHealthDisplay = health;
			previousHealth = health;
			previousHealthDelay = HEALTH_INDICATOR_DELAY;
		}
	}

}
