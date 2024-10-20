package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarStates;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void tick(CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			if (!this.world.isClient) {
				return;
			}
			ToroHealth.HUD.setEntity(ToroHealth.RAYTRACE.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
			HealthBarStates.tick();
			HoldingWeaponUpdater.update();
			ToroHealth.HUD.tick();	
		}
	}

}
