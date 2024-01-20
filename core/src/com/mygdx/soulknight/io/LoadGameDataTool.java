package com.mygdx.soulknight.io;

import com.badlogic.gdx.utils.Json;
import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeBullet;
import com.mygdx.soulknight.Enum.TypeMonster;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.character.Knight;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.entity.Door;
import com.mygdx.soulknight.map.Map;
import com.mygdx.soulknight.screen.GamePlayingScreen;
import com.mygdx.soulknight.thread.MonsterThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoadGameDataTool {
    private GamePlayingScreen gamePlayingScreen;
    public LoadGameDataTool(SoulKnightGame game){
        gamePlayingScreen = new GamePlayingScreen(game);
    }

    public GamePlayingScreen getGamePlayingScreen() {
        return gamePlayingScreen;
    }

    public void LoadFromFile(String dataPath) throws IOException {
        try {
            File file = new File(dataPath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                Json json = new Json();
                if(count == 1)
                    gamePlayingScreen.map =  json.fromJson(Map.class, line);
                else if(count == 2)
                    GamePlayingScreen.door = json.fromJson(Door.class, line);
                else if (count == 3)
                    GamePlayingScreen.knightsBullets = Collections.synchronizedList(json.fromJson(ArrayList.class, Bullet.class, line));
                else if (count == 4)
                    GamePlayingScreen.monstersBullets = Collections.synchronizedList(json.fromJson(ArrayList.class, Bullet.class, line));
                else if(count == 5)
                    GamePlayingScreen.knights = Collections.synchronizedList(json.fromJson(ArrayList.class, Knight.class, line));
                else if(count == 6)
                    GamePlayingScreen.monsters = Collections.synchronizedList(json.fromJson(ArrayList.class, Monster.class, line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gamePlayingScreen.map.generateMap();
        GamePlayingScreen.door.image = MediaGamePlaying.doorFrames[GamePlayingScreen.door.currentFrame];
        for(Bullet bullet : GamePlayingScreen.knightsBullets){
            if(bullet.typeBullet == TypeBullet.Yellow)
                bullet.image = MediaGamePlaying.yellowBullet;
        }
        for(Bullet bullet : GamePlayingScreen.monstersBullets) {
            if (bullet.typeBullet == TypeBullet.Red)
                bullet.image = MediaGamePlaying.redBullet;
            else if(bullet.typeBullet == TypeBullet.Blue)
                bullet.image = MediaGamePlaying.blueBullet;
            else if(bullet.typeBullet == TypeBullet.Green)
                bullet.image = MediaGamePlaying.greenBullet;
        }
        for(Knight knight : GamePlayingScreen.knights){
            if(knight.bodyState == BodyState.LEFT)
                knight.image = MediaGamePlaying.knightLeft;
            else if(knight.bodyState == BodyState.RIGHT)
                knight.image = MediaGamePlaying.knightRight;
        }
        for (Monster monster : GamePlayingScreen.monsters) {
            if(monster.typeMonster == TypeMonster.Master)
                monster.image = MediaGamePlaying.master;
            else if(monster.typeMonster == TypeMonster.Collider){
                if(monster.bodyState == BodyState.LEFT)
                    monster.image = MediaGamePlaying.colliderLeft;
                else
                    monster.image = MediaGamePlaying.colliderRight;
            }
            else if(monster.typeMonster == TypeMonster.Raider){
                if(monster.bodyState == BodyState.LEFT)
                    monster.image = MediaGamePlaying.raiderLeft;
                else
                    monster.image = MediaGamePlaying.raiderRight;
            }
            monster.monsterThread = new MonsterThread(monster, GamePlayingScreen.monstersBullets, GamePlayingScreen.knights, GamePlayingScreen.knights);
            monster.monsterThread.start();
        }
    }
}
