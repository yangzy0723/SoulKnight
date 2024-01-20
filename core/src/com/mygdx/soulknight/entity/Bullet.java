package com.mygdx.soulknight.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.TypeBullet;
import com.mygdx.soulknight.abstractclass.Creature;
import com.mygdx.soulknight.abstractclass.Entity;

public class Bullet extends Entity implements Json.Serializable{
    public TypeBullet typeBullet;
    public int attack;
    public float speedX;
    public float speedY;
    public Bullet(){};//给json重构子弹实例用的！不能删掉！！！
    public Bullet(float x, float y, int width, int height, TypeBullet typeBullet, float speedX, float speedY, int attack, Texture image){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.typeBullet = typeBullet;
        this.speedX = speedX;
        this.speedY = speedY;
        this.attack = attack;
        this.image = image;
    }
    public void update(){
        this.x = this.x + speedX * Gdx.graphics.getDeltaTime();
        this.y = this.y + speedY * Gdx.graphics.getDeltaTime();
    }

    /**
     * boolean返回被子弹伤害者是否死亡，若死亡，返回true，反之返回false
     */
    public boolean attackCreature(Creature c){
        c.beAttack(this.attack);
        return c.health <= 0;
    }

    public int getAttack() {
        return attack;
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);
        json.writeValue("typeBullet", this.typeBullet);
        json.writeValue("speedX", this.speedX);
        json.writeValue("speedY", this.speedY);
        json.writeValue("attack", this.attack);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.x = json.readValue("x", float.class, jsonData);
        this.y = json.readValue("y", float.class, jsonData);
        this.width = json.readValue("width", int.class, jsonData);
        this.height = json.readValue("height", int.class, jsonData);
        this.typeBullet = json.readValue("typeBullet", TypeBullet.class, jsonData);
        this.speedX = json.readValue("speedX", float.class, jsonData);
        this.speedY = json.readValue("speedY", float.class, jsonData);
        this.attack = json.readValue("attack", int.class, jsonData);
    }
}
