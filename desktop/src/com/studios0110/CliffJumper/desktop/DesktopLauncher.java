package com.studios0110.CliffJumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.studios0110.CliffJumper.Splash.Splash;
import com.studios0110.CliffJumper.Splash.Starter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Splash.screenW;
		config.height = Splash.screenH;
		new LwjglApplication(new Starter(), config);
	}
}
