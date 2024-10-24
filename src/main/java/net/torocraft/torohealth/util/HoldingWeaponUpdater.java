package net.torocraft.torohealth.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config.Mode;

public class HoldingWeaponUpdater {

	public static void update() {
		if (Mode.NONE.equals(ToroHealth.CONFIG.inWorld.mode))
			return;
		MinecraftClient minecraft = MinecraftClient.getInstance();
		PlayerEntity player = minecraft.player;
		if (player == null) {
			ToroHealth.IS_HOLDING_WEAPON = false;
			return;
		}
		ToroHealth.IS_HOLDING_WEAPON = isWeapon(player.getMainHandStack()) || isWeapon(player.getOffHandStack());
	}

	private static boolean isWeapon(ItemStack item) {
		return ItemGroups.getGroups().get(8).contains(item);
	}

}