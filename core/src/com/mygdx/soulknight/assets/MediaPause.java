package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MediaPause {
    public static Texture pauseMenu;
    public static Sound buttonSound;
    public static void loadAssets() {
        pauseMenu = new Texture("ui/figs/pause.png");
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("music/button.mp3"));
    }
    public static void disposeAssets(){
        pauseMenu.dispose();
        buttonSound.dispose();
    }

}
