package com.mygdx.soulknight.thread;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.soulknight.Enum.TypeMonster;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.character.Knight;
import com.mygdx.soulknight.character.monsters.Collider;
import com.mygdx.soulknight.character.monsters.Master;
import com.mygdx.soulknight.character.monsters.Raider;
import com.mygdx.soulknight.entity.Wall;
import com.mygdx.soulknight.screen.GamePlayingScreen;

public class MonsterManager {
    int which;
    public MonsterManager(String level){
        if(level.contains("1"))
            Variable.MONSTER_NUMBER = 3;
        else if(level.contains("2"))
            Variable.MONSTER_NUMBER = 4;
        which = 0;
    }
    public void createMonster(){
        for(int i = 0; i < Variable.MONSTER_NUMBER; i++) {
            float x = 0;
            float y = 0;
            TypeMonster nextMonster = getMonster();
            if(nextMonster == TypeMonster.Master){
                Master master = null;
                do{
                    x = MathUtils.random(0, 800);
                    y = MathUtils.random(0, 800);
                    master = new Master(x, y, Variable.MASTER_WIDTH, Variable.MASTER_HEIGHT, 100, 0, 1, Variable.MASTER_SPEED);
                } while(checkCollision(master));
                GamePlayingScreen.monsters.add(master);
            }
            else if(nextMonster == TypeMonster.Collider){
                Collider collider = null;
                do{
                    x = MathUtils.random(0, 800);
                    y = MathUtils.random(0, 800);
                    collider = new Collider(x, y, Variable.COLLIDER_WIDTH, Variable.COLLIDER_HEIGHT, 100, 0, 1, Variable.COLLIDER_SPEED);
                } while (checkCollision(collider));
                GamePlayingScreen.monsters.add(collider);
            }
            else if(nextMonster == TypeMonster.Raider){
                Raider raider = null;
                do{
                    x = MathUtils.random(0, 800);
                    y = MathUtils.random(0, 800);
                    raider = new Raider(x, y, Variable.RAIDER_WIDTH, Variable.RAIDER_HEIGHT, 100, 0, 1, Variable.RAIDER_SPEED);
                } while (checkCollision(raider));
                GamePlayingScreen.monsters.add(raider);
            }
        }
        for(Monster monster : GamePlayingScreen.monsters){
            MonsterThread newThread = new MonsterThread(monster, GamePlayingScreen.monstersBullets, GamePlayingScreen.knights, GamePlayingScreen.knights);
            monster.monsterThread = newThread;
            newThread.start();
        }
    }

    private TypeMonster getMonster(){
        TypeMonster nextMonster = null;
        if(which == 0)
            nextMonster =  TypeMonster.Master;
        else if(which == 1)
            nextMonster =  TypeMonster.Collider;
        else if(which == 2)
            nextMonster = TypeMonster.Raider;
        which++;
        if(which > 2)
            which = 0;
        return nextMonster;
    }

    private boolean checkCollision(Monster mon){
        for(Wall wall : GamePlayingScreen.walls)
            if(wall.overlaps(mon))
                return true;
        for(Knight knight : GamePlayingScreen.knights)
            if(knight.overlaps(mon))
                return true;
        for(Monster monster : GamePlayingScreen.monsters)
            if(monster.overlaps(mon))
                return true;
        return false;
    }
}
