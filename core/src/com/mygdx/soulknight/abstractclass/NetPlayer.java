package com.mygdx.soulknight.abstractclass;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeNetPlayer;
import com.mygdx.soulknight.entity.Bullet;

import java.util.List;

public class NetPlayer extends Creature implements Json.Serializable{
    public int id;
    public TypeNetPlayer typeNetPlayer;
    public BodyState bodyState;
    public float old_x, old_y;
    public float shootTimer;
    public void react(String input, List<Monster> monsters, List<Bullet> bullets){};
    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);

        json.writeValue("id", this.id);
        json.writeValue("bodyState", this.bodyState);
        json.writeValue("typeNetPlayer", this.typeNetPlayer);
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

        this.id = json.readValue("id", int.class, jsonData);
        this.bodyState = json.readValue("bodyState", BodyState.class, jsonData);
        this.typeNetPlayer = json.readValue("typeNetPlayer", TypeNetPlayer.class, jsonData);
        this.old_x = json.readValue("old_x", float.class, jsonData);
        this.old_y = json.readValue("old_y", float.class, jsonData);
        this.shootTimer = json.readValue("shootTimer", float.class, jsonData);

        this.health = json.readValue("health", int.class, jsonData);
        this.defense = json.readValue("defense", int.class, jsonData);
        this.attack = json.readValue("attack", int.class, jsonData);
        this.speed = json.readValue("speed", int.class, jsonData);
    }
}
