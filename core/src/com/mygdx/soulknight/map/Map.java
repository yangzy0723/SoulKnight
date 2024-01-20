package com.mygdx.soulknight.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.TypeFloor;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Floor;
import com.mygdx.soulknight.entity.Wall;
import com.mygdx.soulknight.screen.GamePlayingScreen;

import java.io.IOException;

public class Map implements Json.Serializable{
    public String levelPath;
    public Map(){};//给json重构地图实例用的！不能删掉！！！
    public Map(String levelPath){
        this.levelPath = levelPath;
    }
    public void generateMap() throws IOException {
        FileHandle fileHandle = Gdx.files.internal(levelPath);
        String mapString = fileHandle.readString();

        int rowCount = 0;
        int[][] mapArray = null;
        String[] lines = mapString.split("\\n");
        for (String line : lines) {
            String[] values = line.trim().split(" ");
            if (mapArray == null)
                mapArray = new int[values.length][values.length];
            for (int i = 0; i < values.length; i++)
                mapArray[rowCount][i] = Integer.parseInt(values[i]);
            rowCount++;
        }
        if (mapArray != null) {
            for(int i = 1; i <= mapArray.length; i++)
                for(int j = 0; j < mapArray[0].length; j++) {
                    if (mapArray[i - 1][j] == 0)
                        GamePlayingScreen.walls.add(new Wall(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.wall));
                    else if(mapArray[i-1][j] == 1)
                        GamePlayingScreen.floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor1, TypeFloor.FLOOR1));
                    else if(mapArray[i-1][j] == 2)
                        GamePlayingScreen.floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor2, TypeFloor.FLOOR2));
                }

        }
    }
    @Override
    public void write(Json json) {
        json.writeValue("levelPath", this.levelPath);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.levelPath = json.readValue("levelPath", String.class,  jsonData);
    }
}
