package com.company.GridObjects.Creatures;

import com.company.GridObjects.Entities.Entity;
import com.company.GridObjects.Objects.GridObject;
import com.company.GridObjects.Objects.Loot;
import com.company.GameLogic;
import com.company.GridObjects.Objects.Void;
import com.company.Room;
import com.company.Tools;
import com.company.Items.Ammunition.Armor;
import com.company.Items.Ammunition.Weapon;
import com.company.Spells.*;
import com.company.Tools.Coor;

public abstract class Creature extends Entity {

    public int lvl;
    public double rate;
    public int defaultDamage;
    public Weapon weapon;
    public Armor armor;
    public SpellEquipped<?>[] spellsPool = new SpellEquipped<?>[3];
    public Entity enemy;
    public GridObject lastObject;
    {
         lastObject = new Void(coor, room);
         room.setObject(this, coor);
    }

    Creature(Coor coor, Room room) {
        super(coor, room);
        rate = rnd.nextFloat() * 0.2 + 0.8;
        for (int i = 0; i < 3; i++) spellsPool[i] = getRandomEqSpell();
    }

    public void move(int x, int y) {
        if (x < 0 || x + 1 > room.widthX || y < 0 || y + 1 > room.widthY)//возможно исключение!!!!!!!!!!
            return;
        if (!room.isPassingObject(x, y, this))
            return;

        GridObject presentObject = room.getObject(x, y);
        room.setObject(this, x, y);
        room.setObject(lastObject, coor.X, coor.Y);
        lastObject = presentObject;
        coor.X = x;
        coor.Y = y;
    }

    public void move(String direction) {
        if (!isAlive)
            return;
        Tools.Coor newCoor = new Tools.Coor(coor);
        if (!Tools.getDirection(newCoor, direction))
            return;
        move(newCoor.X, newCoor.Y);
    }

    public void move(int direction) {
        move(Integer.toString(direction));
    }
    public void move(char direction) {
        move(Character.toString(direction));
    }


    public void attack() {
        enemy.getAttacked(rate, weapon);
    }

    @Override
    public void getAttacked(double rate, Weapon weapon) {
        int physDamage = GameLogic.dps(rate, weapon.damage);
        int damage = physDamage - armor.defence;
        damage = Math.max(damage, Math.max(Math.round(physDamage * 0.2f), 1));
        if (hp - damage > 0) hp -= damage;
        else {
            hp = 0;
            isAlive = false;
        }
        room.add("%s (%d/%d) был атакован и получил %d урона, заблокировав %d", name, hp, maxHp, damage, (physDamage - damage));
    }

    @Override
    public void getAttacked(double rate, SpellEquipped<?> spell) {
        int damage;
        int magicDamage = GameLogic.dps(rate, spell.getDamage());
        if (spell.getNumOfSpell() == this.spellsPool[0].getNumOfSpell())
            damage = (int) (magicDamage * 0.2);
        else
            damage = magicDamage;
        damage = Math.max(damage, Math.max((int) (magicDamage * 0.2), 1));
        if (hp - damage > 0) hp -= damage;
        else {
            hp = 0;
            isAlive = false;
        }
        room.add("%s (%d/%d) был атакован и получил %d %s урона, заблокировав %d", name, hp, maxHp, damage, spell.getNameSpell(), (magicDamage - damage));
    }

    @Override
    public void kill() {
        super.kill();
        new Loot(this, room);
        if (enemy == null)
            return;
        if (enemy instanceof Enemy enemy) {
            enemy.isBusy = false;
            enemy.enemy = null;
        }
    }

    public SpellEquipped<?> getRandomEqSpell() {
        SpellEquipped<?> spell;
        int numOfElement = rnd.nextInt(6);

        spell = switch (numOfElement) {
            case 0 -> new SpellEquipped<Fire>(0);
            case 1 -> new SpellEquipped<Water>(1);
            case 2 -> new SpellEquipped<Earth>(2);
            case 3 -> new SpellEquipped<Air>(3);
            case 4 -> new SpellEquipped<Frost>(4);
            case 5 -> new SpellEquipped<Ephire>(5);
            default -> null;
        };

        return spell;
    }

    public SpellEquipped<?> getSpell(int num) {
        return spellsPool[num];
    }

    @Override
    public void show() {
        room.add("%s %d lvl", name, lvl);
        room.add("hp: %d/%d", hp, maxHp);
        room.add("defence: %d", armor.defence);
        room.add("attack: %d", weapon.damage);
        room.add("default attack: %d", defaultDamage);
        room.add("rate: %.2f hits per second", rate);
        room.add("life: %b", isAlive);
    }
}
