package com.mygdx.soulknight.abstractclass;

import com.mygdx.soulknight.Enum.BodyState;
import com.mygdx.soulknight.Enum.TypeMonster;
import com.mygdx.soulknight.entity.Bullet;
import com.mygdx.soulknight.thread.MonsterThread;

import java.util.List;

public abstract class Monster extends Creature {
    public float shootTimer;
    public float moveTimer;
    public float speedX, speedY;
    public TypeMonster typeMonster;
    public BodyState bodyState;
    public MonsterThread monsterThread;
    //FOR Knight or NetKnight abstract!
    public void move(int randomNum, List<? extends Creature> moveGoal){}
    public void attackAct(List<Bullet> bullets, List<? extends Creature> attackGoal){}
}