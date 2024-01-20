package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MediaGamePlaying {
    public static Texture floor1;
    public static Texture floor2;
    public static Texture wall;
    public static Texture[] doorFrames = new Texture[4];
    public static Texture yellowBullet;
    public static Texture blueBullet;
    public static Texture greenBullet;
    public static Texture redBullet;
    public static Texture knightRight;
    public static Texture knightLeft;
    public static Texture snowmanRight;
    public static Texture snowmanLeft;
    public static Texture warriorRight;
    public static Texture warriorLeft;
    public static Texture colliderRight;
    public static Texture colliderLeft;
    public static Texture raiderRight;
    public static Texture raiderLeft;
    public static Texture master;
    public static Sound doorSound;
    public static Sound shootSound;
    public static Sound beHurtSound;
    public static Music backgroundMusic;
    public static void loadAssets(){
        floor1 = new Texture("basic/building/floor1.png");
        floor2 = new Texture("basic/building/floor2.png");
        wall = new Texture("basic/building/wall.png");
        for(int i = 1; i <= 4; i++)
            doorFrames[i-1] = new Texture("basic/doors/door" + i + ".png");


        yellowBullet = new Texture("basic/bullets/yellowBullet.png");
        blueBullet = new Texture("basic/bullets/blueBullet.png");
        greenBullet = new Texture("basic/bullets/greenBullet.png");
        redBullet = new Texture("basic/bullets/redBullet.png");

        knightRight = new Texture("basic/roles/knightRight.png");
        knightLeft = new Texture("basic/roles/knightLeft.png");
        snowmanRight = new Texture("basic/roles/snowmanRight.png");
        snowmanLeft = new Texture("basic/roles/snowmanLeft.png");
        warriorRight = new Texture("basic/roles/warriorRight.png");
        warriorLeft = new Texture("basic/roles/warriorLeft.png");
        colliderRight = new Texture("basic/roles/colliderRight.png");
        colliderLeft = new Texture("basic/roles/colliderLeft.png");
        raiderRight = new Texture("basic/roles/raiderRight.png");
        raiderLeft = new Texture("basic/roles/raiderLeft.png");
        master = new Texture("basic/roles/masterLeft.png");

        doorSound = Gdx.audio.newSound(Gdx.files.internal("music/doorSound.mp3"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("music/shootSound.mp3"));
        beHurtSound = Gdx.audio.newSound(Gdx.files.internal("music/beHurt.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/playingBGM.mp3"));
    }
    public static void disposeAssets(){
        floor1.dispose();
        floor2.dispose();
        wall.dispose();
        for(int i = 0; i < 4; i++)
            doorFrames[i].dispose();

        yellowBullet.dispose();
        blueBullet.dispose();
        greenBullet.dispose();
        redBullet.dispose();

        knightRight.dispose();
        knightLeft.dispose();
        snowmanRight.dispose();
        snowmanLeft.dispose();
        warriorRight.dispose();
        warriorLeft.dispose();
        colliderRight.dispose();
        colliderLeft.dispose();
        raiderRight.dispose();
        raiderLeft.dispose();
        master.dispose();

        doorSound.dispose();
        shootSound.dispose();
        beHurtSound.dispose();
        backgroundMusic.dispose();
    }
}
