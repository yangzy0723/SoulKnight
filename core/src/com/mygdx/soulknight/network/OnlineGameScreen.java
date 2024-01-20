package com.mygdx.soulknight.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.soulknight.Enum.*;
import com.mygdx.soulknight.SoulKnightGame;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.abstractclass.NetPlayer;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.MediaMenu;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.entity.Door;
import com.mygdx.soulknight.entity.Floor;
import com.mygdx.soulknight.entity.Wall;
import com.mygdx.soulknight.io.GifRecorder;
import com.mygdx.soulknight.map.Map;
import com.mygdx.soulknight.screen.MenuScreen;
import com.mygdx.soulknight.thread.ThreadLock;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlineGameScreen implements Screen {
    final SoulKnightGame game;
    ExecutorService executorService;
    private int id;
    private Channel channel;
    public Map map;
    public Door door;
    public List<Wall> walls;
    public List<Floor> floors;
    public List<NetPlayer> players;
    public List<Bullet> playersBullets;
    public List<Monster> monsters;
    public List<Bullet> monstersBullets;
    public GifRecorder recorder;
    public OnlineGameScreen(final SoulKnightGame game){
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                initNet();
                try {
                    start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        MediaGamePlaying.loadAssets();
        this.game = game;
        walls = Collections.synchronizedList(new ArrayList<Wall>());
        floors = Collections.synchronizedList(new ArrayList<Floor>());
        players = Collections.synchronizedList(new ArrayList<NetPlayer>());
        playersBullets = Collections.synchronizedList(new ArrayList<Bullet>());
        monsters = Collections.synchronizedList(new ArrayList<Monster>());
        monstersBullets = Collections.synchronizedList(new ArrayList<Bullet>());
        if(Variable.RECORD_FUNCTION)
            recorder = new GifRecorder(game.batch);
        else
            recorder = null;
    }
    @Override
    public void show() {
        MediaGamePlaying.backgroundMusic.setLooping(true);
        MediaGamePlaying.backgroundMusic.play();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        channel.writeAndFlush(Unpooled.copiedBuffer("GET".getBytes(StandardCharsets.UTF_8)));
        if(walls.isEmpty() && map != null)
            generateMap(map.levelPath);

        ThreadLock.clientlock.lock();
        game.batch.begin();
        for(Wall wall : walls)
            game.batch.draw(wall.image, wall.x, wall.y);
        for(Floor floor : floors)
            game.batch.draw(floor.image, floor.x, floor.y);
        if(door != null && monsters.isEmpty())
            game.batch.draw(MediaGamePlaying.doorFrames[door.currentFrame], door.x, door.y);
        for(Bullet bullet : playersBullets)
            game.batch.draw(bullet.image, bullet.x, bullet.y);
        for(Bullet bullet : monstersBullets)
            game.batch.draw(bullet.image, bullet.x, bullet.y);
        for(NetPlayer player : players)
            game.batch.draw(player.image, player.x, player.y);
        for(Monster monster : monsters)
            game.batch.draw(monster.image, monster.x, monster.y);
        game.batch.end();
        ThreadLock.clientlock.unlock();

        // process user input
        String message = "input " + this.id + " ";
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            message += "W";
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            message += "S";
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            message += "A";
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            message += "D";
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
            message += "space";
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
            message += "enter";
        if(!message.equals("input " + this.id + " "))
            channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8)));

        if(recorder != null)
            recorder.update();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();//dispose要放在最前面，不知道为什么
            channel.writeAndFlush(Unpooled.copiedBuffer(("close " + this.id).getBytes(StandardCharsets.UTF_8)));
            channel.close();
            if(recorder != null)
                recorder.endRecord();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        MediaGamePlaying.disposeAssets();
    }

    private void generateMap(String level) {
        FileHandle fileHandle = Gdx.files.internal(level);
        String mapString = fileHandle.readString();

        int rowCount = 0;
        int[][] mapArray = null;
        String[] lines = mapString.split("\\n");
        for (String line : lines) {
            String[] values = line.trim().split(" ");
            if (mapArray == null)
                mapArray = new int[values.length][values.length];
            for (int i = 0; i < values.length; i++)
                mapArray[rowCount][i] = Integer.parseInt(values[i]);
            rowCount++;
        }
        if (mapArray != null) {
            for (int i = 1; i <= mapArray.length; i++)
                for (int j = 0; j < mapArray[0].length; j++) {
                    if (mapArray[i - 1][j] == 0)
                        walls.add(new Wall(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.wall));
                    else if (mapArray[i - 1][j] == 1)
                        floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor1, TypeFloor.FLOOR1));
                    else if (mapArray[i - 1][j] == 2)
                        floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor2, TypeFloor.FLOOR2));
                }

        }
    }

    private void initNet(){
        id = -1;
    }
    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new DefaultMaxBytesRecvByteBufAllocator(1024 * 1024, 1024 * 1024))
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(Variable.BUF_LENGTH));
                            ChannelPipeline pipeline = ch.pipeline();
                            //8个字节的前缀，用于记录消息的字节长度，最长可记录2^4 * 2^16 = 1024 * 1024字节长度的消息
                            pipeline.addLast(new LengthFieldPrepender(8));
                            //用于设置一次（单帧）能发送字节的最大长度，用于解决发送消息超长导致拆成多条消息分次发送的问题
                            //LengthFieldBasedFrameDecoder是一种解码器，通过消息中长度字段的值动态地分割接收到的ByteBufs
                            //当解码具有整数标头字段的二进制消息时，它特别有用，该字段表示消息正文或整个消息的长度
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
                            pipeline.addLast(new GameClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(Variable.SERVER_IP, Variable.PORT).sync();
            channel = future.channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    class GameClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;
            String message = byteBuf.toString(CharsetUtil.US_ASCII);
            if(message.startsWith("id"))
                id = Integer.parseInt(message.substring("id".length()));
            else {
                Json json = new Json();
                String[] jsonData = message.split("\n");

                ThreadLock.clientlock.lock();
                for(String json_data : jsonData)
                    if (json_data.startsWith("Map"))
                        map = json.fromJson(Map.class, json_data.substring("Map".length()));
                    else if(json_data.startsWith("Door"))
                        door = json.fromJson(Door.class, json_data.substring("Door".length()));
                    else if (json_data.startsWith("BulletsPlayers"))
                        playersBullets = Collections.synchronizedList(
                                json.fromJson(ArrayList.class, Bullet.class, json_data.substring("BulletsPlayers".length())));
                    else if (json_data.startsWith("BulletsMonsters"))
                        monstersBullets = Collections.synchronizedList(
                                json.fromJson(ArrayList.class, Bullet.class, json_data.substring("BulletsMonsters".length())));
                    else if(json_data.startsWith("Monsters"))
                        monsters = Collections.synchronizedList(
                                json.fromJson(ArrayList.class, Monster.class, json_data.substring("Monsters".length())));
                    else if(json_data.startsWith("Players"))
                        players = Collections.synchronizedList(
                                json.fromJson(ArrayList.class, NetPlayer.class, json_data.substring("Players".length())));
                for(Bullet bullet : playersBullets)
                    if(bullet.typeBullet == TypeBullet.Yellow)
                        bullet.image = MediaGamePlaying.yellowBullet;
                for(Bullet bullet : monstersBullets)
                    if (bullet.typeBullet == TypeBullet.Red)
                        bullet.image = MediaGamePlaying.redBullet;
                    else if(bullet.typeBullet == TypeBullet.Blue)
                        bullet.image = MediaGamePlaying.blueBullet;
                    else if(bullet.typeBullet == TypeBullet.Green)
                        bullet.image = MediaGamePlaying.greenBullet;
                for(NetPlayer player : players)
                    if(player.typeNetPlayer == TypeNetPlayer.Knight) {
                        if (player.bodyState == BodyState.LEFT)
                            player.image = MediaGamePlaying.knightLeft;
                        else if (player.bodyState == BodyState.RIGHT)
                            player.image = MediaGamePlaying.knightRight;
                    }
                    else if(player.typeNetPlayer == TypeNetPlayer.Snowman){
                            if (player.bodyState == BodyState.LEFT)
                                player.image = MediaGamePlaying.snowmanLeft;
                            else if (player.bodyState == BodyState.RIGHT)
                                player.image = MediaGamePlaying.snowmanRight;
                    }
                    else if(player.typeNetPlayer == TypeNetPlayer.Warrior){
                        if(player.bodyState == BodyState.LEFT)
                            player.image = MediaGamePlaying.warriorLeft;
                        else if(player.bodyState == BodyState.RIGHT)
                            player.image = MediaGamePlaying.warriorRight;
                    }
                for (Monster monster : monsters) {
                    if (monster.typeMonster == TypeMonster.Master)
                        monster.image = MediaGamePlaying.master;
                    else if (monster.typeMonster == TypeMonster.Collider) {
                        if (monster.bodyState == BodyState.LEFT)
                            monster.image = MediaGamePlaying.colliderLeft;
                        else
                            monster.image = MediaGamePlaying.colliderRight;
                    }
                    else if (monster.typeMonster == TypeMonster.Raider) {
                        if (monster.bodyState == BodyState.LEFT)
                            monster.image = MediaGamePlaying.raiderLeft;
                        else
                            monster.image = MediaGamePlaying.raiderRight;
                    }
                }
                ThreadLock.clientlock.unlock();
            }
            byteBuf.release();
        }
    }
}
