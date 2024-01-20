package com.mygdx.soulknight.character.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
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

import java.util.List;

public class Collider extends Monster implements Json.Serializable{
    public Collider(){};
    public Collider(float x, float y, int width, int height, int health, int defense, int attack, float speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = MediaGamePlaying.colliderLeft;

        this.old_x = x;
        this.old_y = y;
        this.health = health;
        this.defense = defense;
        this.attack = attack;
        this.speed = speed;

        this.typeMonster = TypeMonster.Collider;
        this.bodyState = BodyState.LEFT;
        this.shootTimer = 0;
        this.moveTimer = 0;
        this.speedX = 20;
        this.speedY = 20;
    }
    @Override
    public void move(int randomNum, List<? extends Creature> moveGoal){
        moveTimer += Gdx.graphics.getDeltaTime();
        if(moveTimer >= Variable.MONSTER_MOVE_INTERVAL){
            if(randomNum == 0) {
                speedX = 0;
                speedY = speed;
            }
            else if(randomNum == 1) {
                speedX = 0;
                speedY = -speed;
            }
            else if(randomNum == 2){
                speedX = -speed;
                speedY = 0;
                image = MediaGamePlaying.colliderLeft;
                bodyState = BodyState.LEFT;
            }
            else{
                speedX = speed;
                speedY = 0;
                image = MediaGamePlaying.colliderRight;
                bodyState = BodyState.RIGHT;
            }
            moveTimer = 0;
        }
        x += speedX * Gdx.graphics.getDeltaTime();
        y += speedY * Gdx.graphics.getDeltaTime();;
    }
    @Override
    public void attackAct(List<Bullet> bullets, List<? extends Creature> attackGoal){
        shootTimer += Gdx.graphics.getDeltaTime();
        if(shootTimer >= Variable.COLLIDER_SHOOT_INTERVAL) {
            int speedX = MathUtils.random(-100, 100);
            int speedY = MathUtils.random(-100, 100);
            if(speedX < 50 && speedX > 0)
                speedX += 50;
            else if(speedX < 0 && speedX > -50)
                speedX -= 50;
            if(speedY < 50 && speedY > 0)
                speedY += 50;
            else if(speedY < 0 && speedY > -50)
                speedY -= 50;
            Bullet bullet = new Bullet(x, y, Variable.GREEN_BULLET_SIZE, Variable.GREEN_BULLET_SIZE, TypeBullet.Green,
                    speedX, speedY, attack, MediaGamePlaying.greenBullet);
            bullets.add(bullet);
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
        json.writeValue("moveTimer", this.moveTimer);
        json.writeValue("speedX", this.speedX);
        json.writeValue("speedY", this.speedY);
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
        this.moveTimer = json.readValue("moveTimer", float.class, jsonData);
        this.speedX = json.readValue("speedX", float.class, jsonData);
        this.speedY = json.readValue("speedY", float.class, jsonData);
    }
}
