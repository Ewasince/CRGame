package com.company.GridObjects.Entities;

import com.company.GameLogic;
import com.company.GridObjects.Objects.GridObject;
import com.company.Items.Ammunition.Weapon;
import com.company.Room;
import com.company.Spells.SpellEquipped;
import com.company.Tools.Coor;

import static com.company.Main.actualRoom;

public abstract class Entity extends GridObject {

    public Entity(Coor coor, Room room) {
        super(coor, room);
    }

    public String name;
    public int hp;
    public int maxHp;
    public boolean isAlive = true;
    public boolean isBusy = false;
    public int xp;

    public void getAttacked(double rate, Weapon weapon) {
        int damage = GameLogic.dps(rate, weapon.damage);
//        int damage = physDamage - defence;
//        damage = Math.max(damage, 1);
        if (hp - damage > 0)
            hp -= damage;
        else {
            hp = 0;
            isAlive = false;
        }
        actualRoom.add(name + " был атакован и получил " + damage + " урона");
    }

    public void getAttacked(double rate, SpellEquipped<?> spell) {
        int damage = GameLogic.dps(rate, spell.getDamage());
        damage = Math.max(damage, 1);
        if (hp - damage > 0)
            hp -= damage;
        else {
            hp = 0;
            isAlive = false;
        }
        actualRoom.add("%s был атакован и получил %d %s урона", name, damage, spell.getNameSpell());
    }
    public void getDamage(int startDamage, int damage){

    }

    @Override
    public void show() {
        actualRoom.add("There is unknown entity");
    }


}
