package com.mygdx.soulknight.character.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeBullet;
import com.mygdx.soulknight.Enum.TypeMonster;
import com.mygdx.soulknight.abstractclass.Creature;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Bullet;

import java.util.Arrays;
import java.util.List;

public class Master extends Monster implements Json.Serializable{
    public Master(){};
    public Master(float x, float y, int width, int height, int health, int defense, int attack, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = MediaGamePlaying.master;

        this.old_x = x;
        this.old_y = y;
        this.health = health;
        this.defense = defense;
        this.attack = attack;
        this.speed = speed;

        this.typeMonster = TypeMonster.Master;
        this.bodyState = BodyState.LEFT;
        this.shootTimer = 0;
    }
    @Override
    public void move(int randomNum, List<? extends Creature> moveGoal){
        if(randomNum == 0) {
            x -= speed * Gdx.graphics.getDeltaTime();
        }
        else if (randomNum == 1) {
            x += speed * Gdx.graphics.getDeltaTime();
        }
        else if (randomNum == 2) {
            y += speed * Gdx.graphics.getDeltaTime();
        }
        else if (randomNum == 3) {
            y -= speed * Gdx.graphics.getDeltaTime();
        }
    }
    @Override
    public void attackAct(List<Bullet> bullets, List<? extends Creature> attackGoal){
        shootTimer += Gdx.graphics.getDeltaTime();
        if(shootTimer >= Variable.MASTER_SHOOT_INTERVAL) {
            Bullet[] bulletsTemp = new Bullet[8];
            bulletsTemp[0] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    -Variable.MONSTER_BULLET_SPEED/1.414f, Variable.MONSTER_BULLET_SPEED/1.414f, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[1] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    Variable.MONSTER_BULLET_SPEED/1.414f, -Variable.MONSTER_BULLET_SPEED/1.414f, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[2] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    Variable.MONSTER_BULLET_SPEED/1.414f, Variable.MONSTER_BULLET_SPEED/1.414f, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[3] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    -Variable.MONSTER_BULLET_SPEED/1.414f, -Variable.MONSTER_BULLET_SPEED/1.414f, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[4] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    0, Variable.MONSTER_BULLET_SPEED, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[5] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    Variable.MONSTER_BULLET_SPEED, 0, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[6] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    -Variable.MONSTER_BULLET_SPEED, 0, attack, MediaGamePlaying.blueBullet);
            bulletsTemp[7] = new Bullet(x, y, Variable.BlLU_BULLET_SIZE, Variable.BlLU_BULLET_SIZE, TypeBullet.Blue,
                    0, -Variable.MONSTER_BULLET_SPEED, attack, MediaGamePlaying.blueBullet);
            bullets.addAll(Arrays.asList(bulletsTemp));
            shootTimer = 0;
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);

        json.writeValue("old_x", this.old_x);
        json.writeValue("old_y", this.old_y);
        json.writeValue("health", this.health);
        json.writeValue("defense", this.defense);
        json.writeValue("attack", this.attack);
        json.writeValue("speed", this.speed);

        json.writeValue("typeMonster", this.typeMonster);
        json.writeValue("bodyState", this.bodyState);
        json.writeValue("shootTimer", this.shootTimer);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.x = json.readValue("x", float.class, jsonData);
        this.y = json.readValue("y", float.class, jsonData);
        this.width = json.readValue("width", int.class, jsonData);
        this.height = json.readValue("height", int.class, jsonData);

        this.old_x = json.readValue("old_x", float.class, jsonData);
        this.old_y = json.readValue("old_y", float.class, jsonData);
        this.health = json.readValue("health", int.class, jsonData);
        this.defense = json.readValue("defense", int.class, jsonData);
        this.attack = json.readValue("attack", int.class, jsonData);
        this.speed = json.readValue("speed", int.class, jsonData);

        this.typeMonster = json.readValue("typeMonster", TypeMonster.class, jsonData);
        this.bodyState = json.readValue("bodyState", BodyState.class, jsonData);
        this.shootTimer = json.readValue("shootTimer", float.class, jsonData);
    }
}
