package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.Enum.TypeFloor;
import com.mygdx.soulknight.abstractclass.Entity;

public class Floor extends Entity implements Json.Serializable{
    public TypeFloor typeFloor;
    public Floor(int x, int y, int width, int height, Texture image, TypeFloor typeFloor){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = image;

        this.typeFloor = typeFloor;
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);
        json.writeValue("typeFloor", this.typeFloor);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.x = json.readValue("x", float.class, jsonData);
        this.y = json.readValue("y", float.class, jsonData);
        this.width = json.readValue("width", int.class, jsonData);
        this.height = json.readValue("height", int.class, jsonData);
        this.typeFloor = json.readValue("typeFloor", TypeFloor.class, jsonData);
    }
}
