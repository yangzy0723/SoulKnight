package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.assets.MediaWin;
import com.mygdx.soulknight.assets.Variable;

public class WinnerScreen implements Screen {
    final SoulKnightGame game;
    private final Stage stage;
    public WinnerScreen(final SoulKnightGame game){
        this.game = game;
        this.stage = new Stage(new FillViewport(Variable.WINDOWS_SIZE, Variable.WINDOWS_SIZE));
        MediaWin.loadAssets();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        MediaWin.backgroundMusic.setLooping(true);
        MediaWin.backgroundMusic.play();

        initButtons();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);

        stage.getBatch().begin();
        stage.getBatch().draw(MediaWin.winnerPicture, 0,0);
        stage.getBatch().end();
        stage.draw();
        if(GamePlayingScreen.recorder != null)
            GamePlayingScreen.recorder.update();//为了录制欸
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
        MediaWin.disposeAssets();
    }
    private void initButtons(){
        TextButton buttonBack = new TextButton("Back to Menu", Variable.skin, "default");
        buttonBack.setPosition((float) Variable.WINDOWS_SIZE / 2 + 100, (float) Variable.WINDOWS_SIZE / 2 - 390);
        buttonBack.setSize(Variable.BUTTON_WIDTH, Variable.BUTTON_HEIGHT);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });
        stage.addActor(buttonBack);
    }
}
