package com.max128.gravity;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Setup of LibGDX for Gravity
public class DesktopLauncher {

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(0);
		config.setIdleFPS(1);
		config.useVsync(true);
		config.setTitle("Gravity");
		config.setWindowedMode(Main.WIDTH, Main.HEIGHT);
		new Lwjgl3Application(new Main(), config);
	}

}
