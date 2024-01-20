package com.mygdx.soulknight.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.soulknight.screen.GamePlayingScreen;

public class SaveGameDataTool {
    GamePlayingScreen gamePlayingScreen;
    public SaveGameDataTool(GamePlayingScreen gamePlayingScreen){
        this.gamePlayingScreen = gamePlayingScreen;
    }

    public void saveToFile(String dataPath){
        Json json = new Json();
        String jsonData;
        FileHandle fileHandle;

        fileHandle = Gdx.files.local(dataPath);

        jsonData = json.toJson(gamePlayingScreen.map);
        fileHandle.writeString(jsonData + "\n", false);

        jsonData = json.toJson(GamePlayingScreen.door);
        fileHandle.writeString(jsonData + "\n", true);

        jsonData = json.toJson(GamePlayingScreen.knightsBullets);
        fileHandle.writeString(jsonData + "\n", true);

        jsonData = json.toJson(GamePlayingScreen.monstersBullets);
        fileHandle.writeString(jsonData + "\n", true);

        jsonData = json.toJson(GamePlayingScreen.knights);
        fileHandle.writeString(jsonData + "\n", true);

        jsonData = json.toJson(GamePlayingScreen.monsters);
        fileHandle.writeString(jsonData + "\n", true);
    }
}
