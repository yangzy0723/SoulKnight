package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MediaWin {
    public static Texture winnerPicture;
    public static Music backgroundMusic;
    public static void loadAssets(){
        winnerPicture = new Texture("ui/figs/win.jpg");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/winBGM.mp3"));
    }
    public static void disposeAssets(){
        winnerPicture.dispose();
        backgroundMusic.dispose();
    }
}
