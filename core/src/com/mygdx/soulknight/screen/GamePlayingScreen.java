package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.soulknight.Enum.GameState;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.character.Knight;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.entity.Door;
import com.mygdx.soulknight.entity.Floor;
import com.mygdx.soulknight.entity.Wall;
import com.mygdx.soulknight.io.GifRecorder;
import com.mygdx.soulknight.map.Map;
import com.mygdx.soulknight.thread.MonsterManager;
import com.mygdx.soulknight.thread.ThreadLock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GamePlayingScreen implements Screen {
    final SoulKnightGame game;
    public Map map;
    public GameState gameState;
    public static GifRecorder recorder;
    public static List<Wall> walls;
    public static List<Floor> floors;
    public static Door door;
    public static List<Knight> knights;
    public static List<Bullet> knightsBullets;
    public static List<Monster> monsters;
    public static  List<Bullet> monstersBullets;
    public GamePlayingScreen(final SoulKnightGame game){
        MediaGamePlaying.loadAssets();
        this.game = game;
        gameState = GameState.RUNNING;
        if(Variable.RECORD_FUNCTION)
            recorder = new GifRecorder(game.batch);
        else
            recorder = null;
        walls = Collections.synchronizedList(new ArrayList<Wall>());
        floors = Collections.synchronizedList(new ArrayList<Floor>());
    }
    public GamePlayingScreen(final SoulKnightGame game, String level) {
        MediaGamePlaying.loadAssets();
        this.game = game;
        map = new Map(level);
        gameState = GameState.RUNNING;
        if(Variable.RECORD_FUNCTION)
            recorder = new GifRecorder(game.batch);
        else
            recorder = null;
        walls = Collections.synchronizedList(new ArrayList<Wall>());
        floors = Collections.synchronizedList(new ArrayList<Floor>());
        door = new Door(Variable.DOOR_X, Variable.DOOR_Y, Variable.DOOR_WIDTH, Variable.DOOR_HEIGHT);
        knights = Collections.synchronizedList(new ArrayList<Knight>());
        knightsBullets = Collections.synchronizedList(new ArrayList<Bullet>());
        monsters = Collections.synchronizedList(new ArrayList<Monster>());
        monstersBullets = Collections.synchronizedList(new ArrayList<Bullet>());

        createGameEntities(level);
    }
    @Override
    public void show() {
        MediaGamePlaying.backgroundMusic.setLooping(true);
        MediaGamePlaying.backgroundMusic.play();
    }
    @Override
    public void render(float delta){
        switch(gameState) {
            case RUNNING:
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                ThreadLock.updateLock.lock();
                update();
                ThreadLock.updateLock.unlock();

                ThreadLock.updateLock.lock();
                draw();
                ThreadLock.updateLock.unlock();

                if(recorder != null)
                    recorder.update();

                if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                    ThreadLock.pauselock.lock();
                    gameState = GameState.PAUSE;
                }
                break;

            case PAUSE:
                game.setScreen(new PauseScreen(game, this));
                break;
        }
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void hide() {
        MediaGamePlaying.backgroundMusic.pause();
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {
        for(Monster monster : monsters)
            monster.monsterThread.stopThread();
        walls.clear();
        floors.clear();
        knights.clear();
        knightsBullets.clear();
        monsters.clear();
        monstersBullets.clear();
        MediaGamePlaying.disposeAssets();
    }
    public void createGameEntities(String level){
        try {
            map.generateMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Knight knight = new Knight(Variable.KNIGHT_X, Variable.KNIGHT_Y, Variable.KNIGHT_WIDTH, Variable.KNIGHT_HEIGHT,
                Variable.KNIGHT_HEALTH, Variable.KNIGHT_DEFENSE, Variable.KNIGHT_ATTACK, Variable.KNIGHT_SPEED);
        knights.add(knight);

        MonsterManager monsterManager = new MonsterManager(level);
        monsterManager.createMonster();
    }
    public void update() {
        for(Bullet bullet : knightsBullets)
            bullet.update();
        for(Bullet bullet : monstersBullets)
            bullet.update();
        for(Knight knight : knights)
            knight.update();

        for (Knight knight : knights)
            for (Wall wall : walls) {
                if (knight.overlaps(wall)) {
                    knight.x = knight.old_x;
                    knight.y = knight.old_y;
                    break;
                }
            }
        for (Monster monster : monsters)
            for (Wall wall : walls) {
                if (monster.overlaps(wall)) {
                    monster.x = monster.old_x;
                    monster.y = monster.old_y;
                    break;
                }
            }
        for(Knight knight : knights)
            for(Monster monster : monsters) {
                if (knight.overlaps(monster)){
                    knight.x = knight.old_x;
                    monster.x = monster.old_x;
                    knight.y = knight.old_y;
                    monster.y = monster.old_y;
                    break;
                }
            }
        for(Monster monster1 : monsters)
            for(Monster monster2 : monsters){
                if(monster1.hashCode() != monster2.hashCode() && monster1.overlaps(monster2)){
                    monster1.x = monster1.old_x;
                    monster2.x = monster2.old_x;
                    monster1.y = monster1.old_y;
                    monster2.y = monster2.old_y;
                }
            }
        for (Iterator<Bullet> bulletIterator = knightsBullets.iterator(); bulletIterator.hasNext(); ) {
            Bullet bullet = bulletIterator.next();
            for (Wall wall : walls) {
                if(bullet.overlaps(wall)) {
                    bulletIterator.remove();
                    break;
                }
            }
        }
        for (Iterator<Bullet> bulletIterator = monstersBullets.iterator(); bulletIterator.hasNext(); ) {
            Bullet bullet = bulletIterator.next();
            for (Wall wall : walls) {
                if(bullet.overlaps(wall)) {
                    bulletIterator.remove();
                    break;
                }
            }
        }
        for(Iterator<Monster> monsterIterator = monsters.iterator(); monsterIterator.hasNext();) {
            Monster monster = monsterIterator.next();
            for(Iterator<Bullet> bulletIterator = knightsBullets.iterator(); bulletIterator.hasNext();){
                Bullet bullet = bulletIterator.next();
                if(monster.overlaps(bullet)){
                    bulletIterator.remove();
                    if(bullet.attackCreature(monster)) {
                        monster.monsterThread.stopThread();
                        monsterIterator.remove();
                        break;
                    }
                }
            }
        }
        for(Iterator<Knight> knightIterator = knights.iterator(); knightIterator.hasNext();) {
            Knight knight = knightIterator.next();
            for(Iterator<Bullet> bulletIterator = monstersBullets.iterator(); bulletIterator.hasNext();){
                Bullet bullet = bulletIterator.next();
                if(knight.overlaps(bullet)){
                    MediaGamePlaying.beHurtSound.play();
                    bulletIterator.remove();
                    if(bullet.attackCreature(knight)) {
                        knightIterator.remove();
                        break;
                    }
                }
            }
        }
        if(monsters.isEmpty()){
            if(door.contains(knights.get(0)) && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                MediaGamePlaying.backgroundMusic.stop();
                MediaGamePlaying.doorSound.play();
                if(recorder != null)
                    recorder.endRecord();
                //为了在这一帧卡一下，播放传送门的声音
                for(long i = 0; i <= 2000000000L; i++){}
                dispose();
                game.setScreen(new WinnerScreen(game));
                return;
            }
        }
        if(knights.isEmpty()){
            for(Monster monster : monsters)
                    monster.monsterThread.stopThread();
            if(recorder != null)
                recorder.endRecord();
            dispose();
            game.setScreen(new LoserScreen(game));
        }
    }
    public void draw(){
        this.game.batch.begin();
        for(Wall wall : walls)
            game.batch.draw(wall.image, wall.x, wall.y);
        for(Floor floor : floors)
            game.batch.draw(floor.image, floor.x, floor.y);
        if(monsters.isEmpty())
            door.draw(game.batch);
        for(Bullet bullet : knightsBullets)
            game.batch.draw(bullet.image, bullet.x, bullet.y);
        for(Bullet bullet : monstersBullets)
            game.batch.draw(bullet.image, bullet.x, bullet.y);
        for(Knight knight : knights) {
            game.batch.draw(knight.image, knight.x, knight.y);
            knight.old_x = knight.x;
            knight.old_y = knight.y;
        }
        for(Monster monster : monsters) {
            game.batch.draw(monster.image, monster.x, monster.y);
            monster.old_x = monster.x;
            monster.old_y = monster.y;
        }
        this.game.batch.end();
    }
}
