package net.torocraft.torohealth;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;
import net.torocraft.torohealth.hud.HudConfigScreen;
import net.torocraft.torohealth.hud.HudTorohealth;
import net.torocraft.torohealth.util.RayTrace;

public class ToroHealth implements ModInitializer {

	public static final String MOD_ID = "torohealth";

	public static Config CONFIG = new Config();
	public static HudTorohealth HUD = new HudTorohealth();
	public static RayTrace RAYTRACE = new RayTrace();
	public static boolean IS_HOLDING_WEAPON = false;
	public static Random RAND = new Random();

	public static ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(new Config(),
			ToroHealth.MOD_ID + ".json", config -> ToroHealth.CONFIG = config);

	private KeyBinding keySettings;

	@Override
	public void onInitialize() {
		CONFIG_LOADER.load();

		keySettings = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.torohealth.settings",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.torohealth.title"));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (keySettings.wasPressed()) {
			client.setScreenAndRender(HudConfigScreen.buildConfigScreen(client.currentScreen));
		}
	}
}
