package com.mygdx.soulknight.character.netplayers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeBullet;
import com.mygdx.soulknight.Enum.TypeNetPlayer;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.abstractclass.NetPlayer;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Bullet;

import java.util.List;

public class NetSnowman extends NetPlayer implements Json.Serializable{
    public NetSnowman(){};
    public NetSnowman(float startX, float startY, int id) {
        this.x = startX;
        this.y = startY;
        this.width = Variable.SNOWMAN_WIDTH;
        this.height = Variable.SNOWMAN_HEIGHT;

        this.image = MediaGamePlaying.snowmanLeft;

        this.health = Variable.SNOWMAN_HEALTH;
        this.defense = Variable.SNOWMAN_DEFENSE;
        this.attack = Variable.SNOWMAN_ATTACK;
        this.speed = Variable.SNOWMAN_SPEED;

        this.id = id;
        this.typeNetPlayer = TypeNetPlayer.Snowman;
        this.bodyState = BodyState.LEFT;
        this.old_x = x;
        this.old_y = y;
        this.shootTimer = 0;
    }

    public void react(String input, List<Monster> monsters, List<Bullet> bullets) {
        shootTimer += Gdx.graphics.getDeltaTime();
        if (input.contains("A")) {
            image = MediaGamePlaying.snowmanLeft;
            bodyState = BodyState.LEFT;
            x -= speed * Gdx.graphics.getDeltaTime();
        }
        if (input.contains("D")) {
            image = MediaGamePlaying.snowmanRight;
            bodyState = BodyState.RIGHT;
            x += speed * Gdx.graphics.getDeltaTime();
        }
        if (input.contains("W"))
            y += speed * Gdx.graphics.getDeltaTime();
        if (input.contains("S"))
            y -= speed * Gdx.graphics.getDeltaTime();
        if (input.contains("space") && shootTimer >= Variable.KNIGHT_SHOOT_INTERVAL) {
            Bullet bullet;
            if(!monsters.isEmpty()) {
                Monster target = monsters.get(MathUtils.random(0, monsters.size() - 1));
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
            bullets.add(bullet);
            MediaGamePlaying.shootSound.play();
            shootTimer = 0;
        }
    }
}

