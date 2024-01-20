package com.mygdx.soulknight.thread;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.soulknight.abstractclass.Creature;
import com.mygdx.soulknight.abstractclass.Monster;
import com.mygdx.soulknight.assets.Variable;
import com.mygdx.soulknight.entity.Bullet;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonsterThread extends Thread{
    private final Monster monster;
    private final AtomicBoolean running;
    private final List<Bullet> monstersBullets;
    private final List<? extends Creature> attackGoal;
    private final List<? extends Creature> moveGoal;
    public MonsterThread(Monster monster, List<Bullet> bullets, List<? extends Creature> attackGoal, List<? extends Creature> moveGoal){
        this.monster = monster;
        this.running = new AtomicBoolean(true);
        this.monstersBullets = bullets;
        this.attackGoal = attackGoal;
        this.moveGoal = moveGoal;
    }
    public void stopThread(){
        running.set(false);
    }
    @Override
    public void run() {
        while (running.get()) {
            int randomNum = (int) (MathUtils.random(0, 3));

            ThreadLock.pauselock.lock();
            ThreadLock.updateLock.lock();
            monster.move(randomNum, moveGoal);
            monster.attackAct(monstersBullets, attackGoal);
            ThreadLock.updateLock.unlock();
            ThreadLock.pauselock.unlock();

            try {
                Thread.sleep((int)Variable.THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
