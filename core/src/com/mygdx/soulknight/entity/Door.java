package com.mygdx.soulknight.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.soulknight.abstractclass.Entity;
import com.mygdx.soulknight.assets.MediaGamePlaying;

public class Door extends Entity implements Json.Serializable{
    public float stateTime = 0;
    public float frameDuration = 0.1f;
    public int currentFrame = 0;
    public Door(){};//给json重构子弹实例用的！不能删掉！！！
    public Door(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = MediaGamePlaying.doorFrames[0];
    }
    public void draw(SpriteBatch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        if(stateTime > frameDuration){
            stateTime = 0;
            currentFrame++;
            if(currentFrame >= 4)
                currentFrame = 0;
        }
        image = MediaGamePlaying.doorFrames[currentFrame];
        batch.draw(image, x, y); // x和y是传送门的位置
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", this.x);
        json.writeValue("y", this.y);
        json.writeValue("width", this.width);
        json.writeValue("height", this.height);

        json.writeValue("stateTime", this.stateTime);
        json.writeValue("frameDuration", this.frameDuration);
        json.writeValue("currentFrame", this.currentFrame);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.x = json.readValue("x", float.class, jsonData);
        this.y = json.readValue("y", float.class, jsonData);
        this.width = json.readValue("width", int.class, jsonData);
        this.height = json.readValue("height", int.class, jsonData);

        this.stateTime = json.readValue("stateTime", float.class, jsonData);
        this.frameDuration = json.readValue("frameDuration", float.class, jsonData);
        this.currentFrame = json.readValue("currentFrame", int.class, jsonData);
    }
}
