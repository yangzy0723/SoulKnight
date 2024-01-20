package com.mygdx.soulknight.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeBullet;
import com.mygdx.soulknight.abstractclass.Creature;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.screen.GamePlayingScreen;

public class Knight extends Creature implements Json.Serializable{
    public BodyState bodyState;
    public float old_x, old_y;
    private float shootTimer;
    public Knight(){};
    public Knight(float startX, float startY, int width, int height, int health, int defense, int attack, float speed) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;

        this.image = MediaGamePlaying.knightLeft;

        this.health = health;
        this.defense = defense;
        this.attack = attack;
        this.speed = speed;

        this.bodyState = BodyState.LEFT;
        this.old_x = x;
        this.old_y = y;
        this.shootTimer = 0;
    }

    public void update() {
        shootTimer += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            image = MediaGamePlaying.knightLeft;
            bodyState = BodyState.LEFT;
            x -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            image = MediaGamePlaying.knightRight;
            bodyState = BodyState.RIGHT;
            x += speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= Variable.KNIGHT_SHOOT_INTERVAL) {
            Bullet bullet;
            if(!GamePlayingScreen.monsters.isEmpty()) {
                Monster target = GamePlayingScreen.monsters.get(MathUtils.random(0, GamePlayingScreen.monsters.size() - 1));
                float delta_x = target.x - this.x;
                float delta_y = target.y - this.y;
                float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
                bullet = new Bullet(x, y, Variable.YELLOW_BULLET_SIZE, Variable.YELLOW_BULLET_SIZE, TypeBullet.Yellow,
                        Variable.KNIGHT_BULLET_SPEED * delta_x / distance, Variable.KNIGHT_BULLET_SPEED * delta_y / distance, attack, MediaGamePlaying.yellowBullet);
            }
            else {
                bullet = new Bullet(x, y, Variable.YELLOW_BULLET_SIZE, Variable.YELLOW_BULLET_SIZE, TypeBullet.Yellow,
                        Variable.KNIGHT_BULLET_SPEED, 0, attack, MediaGamePlaying.yellowBullet);
            }
            GamePlayingScreen.knightsBullets.add(bullet);
            MediaGamePlaying.shootSound.play();
            shootTimer = 0;
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);

        json.writeValue("bodyState", this.bodyState);
        json.writeValue("old_x", this.old_x);
        json.writeValue("old_y", this.old_y);
        json.writeValue("shootTimer", this.shootTimer);

        json.writeValue("health", this.health);
        json.writeValue("defense", this.defense);
        json.writeValue("attack", this.attack);
        json.writeValue("speed", this.speed);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.x = json.readValue("x", float.class, jsonData);
        this.y = json.readValue("y", float.class, jsonData);
        this.width = json.readValue("width", int.class, jsonData);
        this.height = json.readValue("height", int.class, jsonData);

        this.bodyState = json.readValue("bodyState", BodyState.class, jsonData);
        this.old_x = json.readValue("old_x", float.class, jsonData);
        this.old_y = json.readValue("old_y", float.class, jsonData);
        this.shootTimer = json.readValue("shootTimer", float.class, jsonData);

        this.health = json.readValue("health", int.class, jsonData);
        this.defense = json.readValue("defense", int.class, jsonData);
        this.attack = json.readValue("attack", int.class, jsonData);
        this.speed = json.readValue("speed", int.class, jsonData);
    }
}

