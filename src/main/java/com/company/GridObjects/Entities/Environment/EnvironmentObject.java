package com.company.GridObjects.Entities.Environment;

import com.company.GameLogic;
import com.company.GridObjects.Entities.Entity;
import com.company.Room;
import com.company.Spells.SpellEquipped;
import com.company.Tools.Coor;

import java.util.Random;

import static com.company.Main.actualRoom;

public abstract class EnvironmentObject extends Entity {
    static Random rnd = new Random();


    public EnvironmentObject(Coor coor, Room room) {
        super(coor, room);
    }

    @Override
    public void show() {
        if (hp < maxHp)
            room.add("There is the %s with %d/%d hp", name, hp, maxHp);
        else
            room.add("There is the %s", name);
    }

    @Override
    public void getAttacked(double rate, SpellEquipped<?> spell) {
        int damage = GameLogic.dps(rate, spell.getDamage());
        if (spell.getType() == 0)
            damage = hp;
        damage = Math.max(damage, 1);
        if (hp - damage > 0)
            hp -= damage;
        else {
            hp = 0;
            isAlive = false;
        }
        room.add(name + " был атакован и получил " + damage + " " + spell.getNameSpell() + " урона");
    }


    public static void generate(Room room){
        actualRoom.add("Ёбаные баги, хуле не оверлодится?");

    }
}
