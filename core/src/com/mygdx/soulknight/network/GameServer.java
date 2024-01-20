package com.mygdx.soulknight.network;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.soulknight.Enum.TypeFloor;
import com.mygdx.soulknight.Enum.TypeMonster;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.abstractclass.NetPlayer;
import com.mygdx.soulknight.assets.MediaGamePlaying;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.character.monsters.Collider;
import com.mygdx.soulknight.character.monsters.Master;
import com.mygdx.soulknight.character.monsters.Raider;
import com.mygdx.soulknight.character.netplayers.NetKnight;
import com.mygdx.soulknight.character.netplayers.NetSnowman;
import com.mygdx.soulknight.character.netplayers.NetWarrior;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.entity.Door;
import com.mygdx.soulknight.entity.Floor;
import com.mygdx.soulknight.entity.Wall;
import com.mygdx.soulknight.map.Map;
import com.mygdx.soulknight.thread.MonsterThread;
import com.mygdx.soulknight.thread.ThreadLock;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer extends ApplicationAdapter {
    private SpriteBatch batch;
    private int monsterNum;
    public Map map;
    public List<Wall> walls;
    public List<Floor> floors;
    public Door door;
    public List<NetPlayer> players;
    public List<Bullet> playersBullets;
    public List<Monster> monsters;
    public List<Bullet> monstersBullets;
    ExecutorService executorService;
    private AtomicInteger connectionCount;
    @Override
    public void create() {
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

        batch = new SpriteBatch();
        MediaGamePlaying.loadAssets();
        monsterNum = 0;
        map = new Map(Variable.LEVEL_ONLINE);
        walls = Collections.synchronizedList(new ArrayList<Wall>());
        floors = Collections.synchronizedList(new ArrayList<Floor>());
        door = new Door(Variable.DOOR_X, Variable.DOOR_Y, Variable.DOOR_WIDTH, Variable.DOOR_HEIGHT);
        players = Collections.synchronizedList(new ArrayList<NetPlayer>());
        playersBullets = Collections.synchronizedList(new ArrayList<Bullet>());
        monsters = Collections.synchronizedList(new ArrayList<Monster>());
        monstersBullets = Collections.synchronizedList(new ArrayList<Bullet>());
        generateMap(Variable.LEVEL_ONLINE);
    }

    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        ThreadLock.updateLock.lock();
        update();
        ThreadLock.updateLock.unlock();

        ThreadLock.updateLock.lock();
        draw();
        ThreadLock.updateLock.unlock();
    }
    public void generateMap(String level){
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
            for(int i = 1; i <= mapArray.length; i++)
                for(int j = 0; j < mapArray[0].length; j++) {
                    if (mapArray[i - 1][j] == 0)
                        walls.add(new Wall(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.wall));
                    else if(mapArray[i-1][j] == 1)
                        floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor1, TypeFloor.FLOOR1));
                    else if(mapArray[i-1][j] == 2)
                        floors.add(new Floor(40 * j, 800 - 40 * i, Variable.TILE_SIZE, Variable.TILE_SIZE, MediaGamePlaying.floor2, TypeFloor.FLOOR2));
                }
        }
    }
    public void draw(){
        batch.begin();
        for(Wall wall : walls)
            batch.draw(wall.image, wall.x, wall.y);
        for(Floor floor : floors)
            batch.draw(floor.image, floor.x, floor.y);
        if(monsters.isEmpty())
            door.draw(batch);
        for(Bullet bullet : playersBullets)
            batch.draw(bullet.image, bullet.x, bullet.y);
        for(Bullet bullet : monstersBullets)
            batch.draw(bullet.image, bullet.x, bullet.y);
        for(NetPlayer player : players){
            batch.draw(player.image, player.x, player.y);
            player.old_x = player.x;
            player.old_y = player.y;
        }
        for(Monster monster : monsters) {
            batch.draw(monster.image, monster.x, monster.y);
            monster.old_x = monster.x;
            monster.old_y = monster.y;
        }
        batch.end();
    }
    public void update(){
        for(Bullet bullet : playersBullets)
            bullet.update();
        for(Bullet bullet : monstersBullets)
            bullet.update();

        for (NetPlayer player : players)
            for (Wall wall : walls) {
                if (player.overlaps(wall)) {
                    player.x = player.old_x;
                    player.y = player.old_y;
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
        for(NetPlayer player : players)
            for(Monster monster : monsters) {
                if (player.overlaps(monster)){
                    player.x = player.old_x;
                    monster.x = monster.old_x;
                    player.y = player.old_y;
                    monster.y = monster.old_y;
                    break;
                }
            }
        for(NetPlayer player1 : players)
            for(NetPlayer player2 : players){
                if(player1.hashCode() != player2.hashCode() && player1.overlaps(player2)){
                    player1.x = player1.old_x;
                    player2.x = player2.old_x;
                    player1.y = player1.old_y;
                    player2.y = player2.old_y;
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
        for (Iterator<Bullet> bulletIterator = playersBullets.iterator(); bulletIterator.hasNext(); ) {
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
            for(Iterator<Bullet> bulletIterator = playersBullets.iterator(); bulletIterator.hasNext();){
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
        for(Iterator<NetPlayer> playerIterator = players.iterator(); playerIterator.hasNext();) {
            NetPlayer player = playerIterator.next();
            for(Iterator<Bullet> bulletIterator = monstersBullets.iterator(); bulletIterator.hasNext();){
                Bullet bullet = bulletIterator.next();
                if(player.overlaps(bullet)){
                    MediaGamePlaying.beHurtSound.play();
                    bulletIterator.remove();
                    if(bullet.attackCreature(player)) {
                        playerIterator.remove();
                        break;
                    }
                }
            }
        }
    }
    class MonsterManager{
        private int which = 0;
        public void createMonster(int number){
            for(int i = 0; i < number; i++) {
                float x = 0;
                float y = 0;
                TypeMonster nextMonster = getMonster();
                if(nextMonster == TypeMonster.Master){
                    Master master = null;
                    do{
                        x = MathUtils.random(0, 800);
                        y = MathUtils.random(0, 800);
                        master = new Master(x, y, Variable.MASTER_WIDTH, Variable.MASTER_HEIGHT, 100, 0, 1, Variable.MASTER_SPEED);
                    } while(checkCollision(master));
                    monsters.add(master);
                }
                else if(nextMonster == TypeMonster.Collider){
                    Collider collider = null;
                    do{
                        x = MathUtils.random(0, 800);
                        y = MathUtils.random(0, 800);
                        collider = new Collider(x, y, Variable.COLLIDER_WIDTH, Variable.COLLIDER_HEIGHT, 100, 0, 1, Variable.COLLIDER_SPEED);
                    } while (checkCollision(collider));
                    monsters.add(collider);
                }
                else if(nextMonster == TypeMonster.Raider){
                    Raider raider = null;
                    do{
                        x = MathUtils.random(0, 800);
                        y = MathUtils.random(0, 800);
                        raider = new Raider(x, y, Variable.RAIDER_WIDTH, Variable.RAIDER_HEIGHT, 100, 0, 1, Variable.RAIDER_SPEED);
                    } while (checkCollision(raider));
                    monsters.add(raider);
                }
            }
            for(Monster monster : monsters){
                MonsterThread newThread = new MonsterThread(monster, monstersBullets, players, players);
                monster.monsterThread = newThread;
                newThread.start();
            }
        }
        private TypeMonster getMonster() {
            TypeMonster nextMonster = null;
            if (which == 0)
                nextMonster = TypeMonster.Master;
            else if (which == 1)
                nextMonster = TypeMonster.Collider;
            else if (which == 2)
                nextMonster = TypeMonster.Raider;
            which++;
            if (which > 2)
                which = 0;
            return nextMonster;
        }
        private boolean checkCollision(Monster mon){
            for(Wall wall : walls)
                if(wall.overlaps(mon))
                    return true;
            for(NetPlayer player : players)
                if(player.overlaps(mon))
                    return true;
            for(Monster monster : monsters)
                if(monster.overlaps(mon))
                    return true;
            return false;
        }
    }



    /****** 服务器通信有关代码  *****/
    private void initNet(){
        this.connectionCount = new AtomicInteger(0);
    }
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(Variable.BUF_LENGTH));
                            //8个字节的前缀，用于记录消息的字节长度，最长可记录2^4 * 2^16 = 1024 * 1024字节长度的消息
                            pipeline.addLast(new LengthFieldPrepender(8));
                            //用于设置一次（单帧）能发送字节的最大长度，用于解决发送消息超长导致拆成多条消息分次发送的问题
                            //LengthFieldBasedFrameDecoder是一种解码器，通过消息中长度字段的值动态地分割接收到的ByteBufs
                            //当解码具有整数标头字段的二进制消息时，它特别有用，该字段表示消息正文或整个消息的长度
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
                            pipeline.addLast(new StringEncoder(), new GameServerHandler());
                        }
                    });
            System.out.println("Server started on port " + Variable.PORT);
            ChannelFuture future = bootstrap.bind(Variable.PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    class GameServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            int count = connectionCount.incrementAndGet();
            String message = "id" + count;
            ctx.writeAndFlush(message);
            if(count % 3 == 1) {
                NetKnight netKnight = new NetKnight(Variable.KNIGHT_X, Variable.KNIGHT_Y, count);
                players.add(netKnight);
            }
            else if(count % 3 == 2) {
                NetSnowman netSnowman = new NetSnowman(Variable.SNOWMAN_X, Variable.SNOWMAN_Y, count);
                players.add(netSnowman);
            }
            else if(count % 3 == 0) {
                NetWarrior netWarrior = new NetWarrior(Variable.WARRIOR_X, Variable.WARRIOR_Y, count);
                players.add(netWarrior);
            }
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;
            String message = byteBuf.toString(CharsetUtil.US_ASCII);
            System.out.println(message);
            if(Objects.equals(message, "GET")){
                Json json = new Json();
                String jsonData;
                jsonData = "Map" + json.toJson(map) + "\n";
                jsonData = jsonData + "Door" + json.toJson(door) + "\n";
                jsonData = jsonData + "BulletsPlayers" + json.toJson(playersBullets) + "\n";
                jsonData = jsonData + "BulletsMonsters" + json.toJson(monstersBullets) + "\n";
                jsonData = jsonData + "Monsters" + json.toJson(monsters) + "\n";
                jsonData = jsonData + "Players" + json.toJson(players) + "\n";
                ctx.writeAndFlush(jsonData);
            }
            else if(message.startsWith("input")){
                String[] input = message.split(" ");
                int id =  Integer.parseInt(input[1]);
                if(!Objects.equals(input[2], "enter")) {
                    for (NetPlayer player : players)
                        if (player.id == id) {
                            player.react(input[2], monsters, playersBullets);
                            break;
                        }
                }
                else{
                    for(NetPlayer player : players){
                        if (door.contains(player) && id == player.id && monsters.isEmpty()) {
                            monsterNum++;
                            (new MonsterManager()).createMonster(monsterNum);
                            break;
                        }
                    }
                }
            }
            else if(message.startsWith("close")){
                String[] input = message.split(" ");
                int id =  Integer.parseInt(input[1]);
                for(Iterator<NetPlayer> playerIterator = players.iterator(); playerIterator.hasNext();) {
                    NetPlayer player = playerIterator.next();
                    if(player.id == id){
                        playerIterator.remove();
                        ctx.close();
                    }
                }
            }
            byteBuf.release();
        }
    }
}