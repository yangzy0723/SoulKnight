package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MediaMenu {
    public static Texture menu;
    public static Sound buttonSound;
    public static Music backgroundMusic;
    public static void loadAssets(){
        menu = new Texture("ui/figs/menu.jpg");
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("music/button.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/winBGM.mp3"));
    }
    public static void disposeAssets(){
        menu.dispose();
        buttonSound.dispose();
        backgroundMusic.dispose();
    }

}
