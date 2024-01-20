package com.mygdx.soulknight;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.network.GameServer;

public class GameServerLauncher {
    public static void main (String[] arg) throws Exception {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setWindowedMode(Variable.WINDOWS_SIZE, Variable.WINDOWS_SIZE);
        config.setTitle("SoulKnight-Server");
        new Lwjgl3Application(new GameServer(), config);
    }
}
