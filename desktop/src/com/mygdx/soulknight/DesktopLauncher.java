package com.mygdx.soulknight;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.soulknight.assets.Variable;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(Variable.WINDOWS_SIZE,Variable.WINDOWS_SIZE);
		config.setTitle("SoulKnight");
		new Lwjgl3Application(new SoulKnightGame(), config);
	}
}
