package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.soulknight.Enum.GameState;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.assets.MediaPause;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.io.SaveGameDataTool;
import com.mygdx.soulknight.thread.ThreadLock;

import java.io.File;

public class PauseScreen implements Screen {
    final SoulKnightGame game;
    final GamePlayingScreen gamePlayingScreen;
    public final Stage stage;

    TextButton buttonBackPause;
    TextButton buttonBackGame;
    TextButton buttonMenu;
    TextButton buttonSave;
    public PauseScreen(final SoulKnightGame game, final GamePlayingScreen gamePlayingScreen){
        this.game = game;
        this.gamePlayingScreen = gamePlayingScreen;
        this.stage = new Stage(new FillViewport(Variable.WINDOWS_SIZE, Variable.WINDOWS_SIZE));
        MediaPause.loadAssets();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        initButtons();
    }
    @Override
    public void render(float delta) {
        gamePlayingScreen.draw();
        stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(MediaPause.pauseMenu, 200,250);
        stage.getBatch().end();
        stage.draw();
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
        MediaPause.disposeAssets();
        ThreadLock.pauselock.unlock();
    }
    private void initButtons() {
        buttonBackPause = new TextButton("Back", Variable.skin, "default");
        buttonBackPause.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 120);
        buttonBackPause.setSize(Variable.BUTTON_WIDTH - 80, (float) Variable.BUTTON_HEIGHT - 20);
        buttonBackPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MediaPause.buttonSound.play();
                stage.clear();
                stage.addActor(buttonBackGame);
                stage.addActor(buttonSave);
                stage.addActor(buttonMenu);
            }
        });

        buttonBackGame = new TextButton("Back to Game", Variable.skin, "default");
        buttonBackGame.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 20);
        buttonBackGame.setSize(Variable.BUTTON_WIDTH - 80, (float) Variable.BUTTON_HEIGHT - 20);
        buttonBackGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gamePlayingScreen.gameState = GameState.RUNNING;
                game.setScreen(gamePlayingScreen);
                dispose();
            }
        });

        buttonMenu = new TextButton("Back to Menu", Variable.skin, "default");
        buttonMenu.setPosition((float) Variable.WINDOWS_SIZE /2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 70);
        buttonMenu.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                gamePlayingScreen.dispose();
                dispose();
            }
        });

        buttonSave = new TextButton("Save the Game", Variable.skin, "default");
        buttonSave.setPosition((float) Variable.WINDOWS_SIZE /2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 120);
        buttonSave.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
        buttonSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                MediaPause.buttonSound.play();
                stage.clear();
                if(new File(Variable.GAME_DATA_1).exists()){
                    final TextButton buttonData;
                    buttonData = new TextButton("Save 1 :(", Variable.skin, "default");
                    buttonData.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 20);
                    buttonData.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
                    buttonData.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            SaveGameDataTool saveTool = new SaveGameDataTool(gamePlayingScreen);
                            saveTool.saveToFile(Variable.GAME_DATA_1);
                            buttonData.setText("Save 1 :)");
                            MediaPause.buttonSound.play();
                        }
                    });
                    stage.addActor(buttonData);
                }
                else{
                    final TextButton buttonEmpty;
                    buttonEmpty = new TextButton("Empty :)", Variable.skin, "default");
                    buttonEmpty.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 20);
                    buttonEmpty.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
                    buttonEmpty.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            SaveGameDataTool saveTool = new SaveGameDataTool(gamePlayingScreen);
                            saveTool.saveToFile(Variable.GAME_DATA_1);
                            buttonEmpty.setText("Save 1 :)");
                            MediaPause.buttonSound.play();
                        }
                    });
                    stage.addActor(buttonEmpty);
                }
                if(Gdx.files.internal(Variable.GAME_DATA_2).exists()){
                    final TextButton buttonData;
                    buttonData = new TextButton("Save 2 :(", Variable.skin, "default");
                    buttonData.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 70);
                    buttonData.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
                    buttonData.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            SaveGameDataTool saveTool = new SaveGameDataTool(gamePlayingScreen);
                            saveTool.saveToFile(Variable.GAME_DATA_2);
                            buttonData.setText("Save 2 :)");
                            MediaPause.buttonSound.play();
                        }
                    });
                    stage.addActor(buttonData);
                }
                else{
                    final TextButton buttonEmpty;
                    buttonEmpty = new TextButton("Empty :)", Variable.skin, "default");
                    buttonEmpty.setPosition((float) Variable.WINDOWS_SIZE / 2 - 100, (float) Variable.WINDOWS_SIZE / 2 - 70);
                    buttonEmpty.setSize(Variable.BUTTON_WIDTH - 80, Variable.BUTTON_HEIGHT - 20);
                    buttonEmpty.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            SaveGameDataTool saveTool = new SaveGameDataTool(gamePlayingScreen);
                            saveTool.saveToFile(Variable.GAME_DATA_2);
                            buttonEmpty.setText("Save 2 :)");
                            MediaPause.buttonSound.play();
                        }
                    });
                    stage.addActor(buttonEmpty);
                }
                stage.addActor(buttonBackPause);
            }
        });

        stage.addActor(buttonBackGame);
        stage.addActor(buttonMenu);
        stage.addActor(buttonSave);
    }
}
