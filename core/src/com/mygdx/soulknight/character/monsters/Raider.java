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

public class Raider extends Monster implements Json.Serializable{
    public Raider(){};
    public Raider(float x, float y, int width, int height, int health, int defense, int attack, int speed){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = MediaGamePlaying.raiderLeft;

        this.old_x = x;
        this.old_y = y;
        this.health = health;
        this.defense = defense;
        this.attack = attack;
        this.speed = speed;

        this.typeMonster = TypeMonster.Raider;
        this.bodyState = BodyState.LEFT;
        this.shootTimer = 0;
    }
    @Override
    public void move(int randomNum, List<? extends Creature> moveGoal){
        if(!moveGoal.isEmpty()) {
            Creature goal = moveGoal.get(0);
            int choice = MathUtils.random(0, 1);
            if(choice == 0){
                if(x < goal.x) {
                    x += speed * Gdx.graphics.getDeltaTime();
                    image = MediaGamePlaying.raiderRight;
                    bodyState = BodyState.RIGHT;
                }
                else if(x > goal.x) {
                    x -= speed * Gdx.graphics.getDeltaTime();
                    image = MediaGamePlaying.raiderLeft;
                    bodyState = BodyState.LEFT;
                }
            }
            else{
                if(y < goal.y)
                    y += speed * Gdx.graphics.getDeltaTime();
                else if(y > goal.y)
                    y -= speed * Gdx.graphics.getDeltaTime();
            }
        }
    }
    @Override
    public void attackAct(List<Bullet> bullets, List<? extends Creature> attackGoal){
        shootTimer += Variable.THREAD_SLEEP_TIME / 1000;
        if(shootTimer >= Variable.RAIDER_SHOOT_INTERVAL && !attackGoal.isEmpty()){
            Creature target = attackGoal.get(MathUtils.random(0, attackGoal.size() - 1));
            float delta_x = target.x - this.x;
            float delta_y = target.y - this.y;
            float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
            Bullet bullet = new Bullet(x, y, Variable.RED_BULLET_SIZE, Variable.RED_BULLET_SIZE, TypeBullet.Red,
                    Variable.MONSTER_BULLET_SPEED * delta_x / distance, Variable.MONSTER_BULLET_SPEED * delta_y / distance, attack, MediaGamePlaying.redBullet);
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
