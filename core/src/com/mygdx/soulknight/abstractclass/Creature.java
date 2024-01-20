package com.mygdx.soulknight.abstractclass;

public class Creature extends Entity {
    public float old_x;
    public float old_y;
    public int health;
    public int defense;
    public int attack;
    public float speed;
    public void beAttack(int damage){
        if(this.defense > damage)
            damage = 0;
        damage = damage - this.defense;
        this.health -= damage;
    };
}
