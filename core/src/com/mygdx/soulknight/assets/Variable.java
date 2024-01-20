package com.mygdx.soulknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Variable {
    // 骑士
    public static int KNIGHT_X = 400;
    public static int KNIGHT_Y = 200;
    public static int KNIGHT_WIDTH = 50;
    public static int KNIGHT_HEIGHT = 59;
    public static int KNIGHT_HEALTH = 1;
    public static int KNIGHT_ATTACK = 20;
    public static int KNIGHT_DEFENSE = 10;
    public static float KNIGHT_SPEED = 200;
    public static float KNIGHT_SHOOT_INTERVAL = 0.5f;

    //雪人
    public static int SNOWMAN_X = 400;
    public static int SNOWMAN_Y = 600;
    public static int SNOWMAN_WIDTH = 40;
    public static int SNOWMAN_HEIGHT = 48;
    public static int SNOWMAN_HEALTH = 1;
    public static int SNOWMAN_ATTACK = 15;
    public static int SNOWMAN_DEFENSE = 10;
    public static float SNOWMAN_SPEED = 250;
    public static float SNOWMAN_SHOOT_INTERVAL = 0.5f;

    //图腾勇士
    public static int WARRIOR_X = 200;
    public static int WARRIOR_Y = 500;
    public static int WARRIOR_WIDTH = 54;
    public static int WARRIOR_HEIGHT = 58;
    public static int WARRIOR_HEALTH = 1;
    public static int WARRIOR_ATTACK = 30;
    public static int WARRIOR_DEFENSE = 10;
    public static float WARRIOR_SPEED = 150;
    public static float WARRIOR_SHOOT_INTERVAL = 0.5f;

    // 怪兽
    public static int MASTER_WIDTH = 67;
    public static int MASTER_HEIGHT = 72;
    public static int COLLIDER_WIDTH = 70;
    public static int COLLIDER_HEIGHT = 70;
    public static int RAIDER_WIDTH = 50;
    public static int RAIDER_HEIGHT = 50;
    public static int MASTER_SPEED = 200;
    public static int COLLIDER_SPEED = 200;
    public static int RAIDER_SPEED = 100;
    public static float MASTER_SHOOT_INTERVAL = 4f;
    public static float COLLIDER_SHOOT_INTERVAL = 5f;
    public static float RAIDER_SHOOT_INTERVAL = 3f;
    public static float MONSTER_MOVE_INTERVAL = 1f;
    public static int MONSTER_NUMBER = 3;

    // 子弹
    public static float MONSTER_BULLET_SPEED = 300f;
    public static float KNIGHT_BULLET_SPEED = 400f;
    public static int YELLOW_BULLET_SIZE = 10;
    public static int BlLU_BULLET_SIZE = 20;
    public static int GREEN_BULLET_SIZE = 35;
    public static int RED_BULLET_SIZE = 15;

    // 实体
    public static int DOOR_WIDTH = 80;
    public static int DOOR_HEIGHT = 100;
    public static float DOOR_X = 360;
    public static float DOOR_Y = 350;
    public static int TILE_SIZE = 40;

    // 按钮
    public static int BUTTON_WIDTH = 280;
    public static int BUTTON_HEIGHT = 60;

    //录制
    public static boolean RECORD_FUNCTION = false;
    public static int RECORD_KEY = Input.Keys.T;
    public static int RECORD_FPS = 10;
    public static int SPEED_MULTIPLIER = 1;

    //联网
    public static String SERVER_IP = "localhost";
    public static int PORT = 12345;
    public static int BUF_LENGTH = 4096;

    // 其他
    public static float THREAD_SLEEP_TIME = 16;
    public static int WINDOWS_SIZE = 800;
    public static String GAME_DATA_1 = "data/game_data1.json";
    public static String GAME_DATA_2 = "data/game_data2.json";
    public static String LEVEL_ONLINE = "maps/level0.txt";
    public static String LEVEL_1 = "maps/level1.txt";
    public static String LEVEL_2 = "maps/level2.txt";

    // 字体
    public static BitmapFont font24;
    public static Skin skin;
    public static void generateSkin(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.BLACK;
        font24 = generator.generateFont(params);

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiSkin.atlas")));
        skin.add("default-font", font24);
        skin.load(Gdx.files.internal("ui/uiSkin.json"));
    }
}
