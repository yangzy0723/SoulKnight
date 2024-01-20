package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MediaLose {
    public static Texture LoserPicture;
    public static Music backgroundMusic;
    public static void loadAssets(){
        LoserPicture = new Texture("ui/figs/lose.jpg");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/loseBGM.mp3"));
    }
    public static void disposeAssets(){
        LoserPicture.dispose();
        backgroundMusic.dispose();
    }
}
