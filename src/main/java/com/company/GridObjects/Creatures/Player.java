package com.company.GridObjects.Creatures;

import com.company.GridObjects.Objects.Base;
import com.company.GridObjects.Objects.Door;
import com.company.GridObjects.Objects.GridObject;
import com.company.Items.Potions.HealPotions.HealPotion;
import com.company.Main;
import com.company.Room;
import com.company.Tools;
import com.company.Items.Ammunition.Armor;
import com.company.Items.Item;
import com.company.Items.Ammunition.Weapon;
import com.company.Tools.Coor;

import java.util.ArrayList;
import java.util.Iterator;

import static com.company.Main.actualRoom;


public class Player extends Creature {
    public ArrayList<Item> inventory = new ArrayList<>();
    public int sizeOfInventory = 10;
    public int wallet = 100;

    {
        idType = ID.PLAYER;
    }

    public Player(String name, Coor coor, int defaultDamage, Room room) {
        super(coor, room);
        this.name = name;
        this.hp = maxHp;
        xp = 0;
        lvl = 1;
        maxHp = lvl * 50 + 50;
        hp = maxHp;
        weapon = new Weapon(lvl);
        armor = new Armor(lvl);
        this.defaultDamage = defaultDamage;
        for (int i = 0; i < 2; i++) {
            getItem(HealPotion.getRandom(lvl));
        }
    }

    @Override
    public void move(String dir) {
        GridObject lastObject = this.lastObject;
        Tools.Coor coor = new Tools.Coor(this.coor);
        super.move(dir);
        if (!coor.equals(this.coor) && lastObject instanceof Base)
            Main.Console.mod = Main.ConsoleMod.STANDARD;
        if (this.lastObject instanceof Base)
            Main.Console.mod = Main.ConsoleMod.BASE;
        if (this.lastObject instanceof Door door) {
            door.enter();
//            currentMap.enter(door);
//            Screen.setObject(lastObject, this.coor);
        }
    }

    @Override
    public void getAttacked(double rate, Weapon weapon) {
        super.getAttacked(rate, weapon);
        if (hp >= maxHp * 0.7)
            return;
        if (inventory.size() == 0)
            return;
        for (Item item : inventory) {
            if (!(item instanceof HealPotion potion))
                continue;
            heal((HealPotion) item);
            if (hp >= maxHp * 0.7)
                break;
        }

    }

    public void getExperience(int xp) {
        actualRoom.add("You've got %d xp", xp);
        this.xp += xp;
        if (this.xp < Math.pow(lvl, 2) * 50)
            return;
        this.xp -= Math.pow(lvl, 2) * 50;
        lvl++;
        actualRoom.add("You've got a new level!");
        maxHp = lvl * 50 + 50;
        defaultDamage = (int) Math.ceil((maxHp - 50) * 0.1 * (rnd.nextFloat() * 0.3 + 0.7));
    }

    public boolean getItem(Item item) {
        if (item instanceof Weapon weapon) {
            if (weapon.damage > this.weapon.damage) {
                Weapon wep = this.weapon;
                this.weapon = weapon;
                item = wep;
            }
        } else if (item instanceof Armor armor) {
            if (armor.defence > this.armor.defence) {
                Armor arm = this.armor;
                this.armor = armor;
                item = arm;
            }
        }
        if (inventory.size() >= sizeOfInventory)
            return false;
        inventory.add(item);
        return true;
    }

    public boolean sellItem(Item item, Iterator<?> iter) {
        if (!inventory.contains(item))
            return false;
        wallet += item.cost;
        iter.remove();
        return true;
    }

    public boolean sellItem(Item item) {
        if (!inventory.contains(item))
            return false;
        wallet += item.cost;
        inventory.remove(item);
        return true;
    }

    public boolean equipItem(int num) {
        if (num >= inventory.size())
            return false;
        Item tempitem = inventory.get(num);
        inventory.remove(num);
        return equipItem(tempitem);
    }

    public boolean removeMoney(int sum) {
        if (wallet - sum > 0) {
            wallet -= sum;
            return true;
        }
        return false;
    }

    public void addMoney(int sum) {
        wallet += sum;
    }

    public void heal(HealPotion potion) {
        if (maxHp - hp >= potion.resource) {
            inventory.remove(potion);
            hp += potion.resource;
        } else {
            potion.resource -= maxHp - hp;
            hp = maxHp;
        }
    }

    @Override
    public void attack() {
        if (rnd.nextBoolean())
            super.attack();
        else
            enemy.getAttacked(rate, getSpell(rnd.nextInt(3)));
    }

    @Override
    public void show() {
        super.show();
        actualRoom.add("xp: %d next level in: %d", xp, (int) (Math.pow(lvl, 2) * 50 - xp));
        actualRoom.add("Has magic of enements: %s, %s and %s",
                getSpell(0).getNameSpell(),
                getSpell(1).getNameSpell(),
                getSpell(2).getNameSpell());
    }

    public void showInventory() {
        actualRoom.add("Equipped:");
        Tools.printLine();
        weapon.show();
        Tools.printLine();
        armor.show();
        Tools.printLine();
        if (inventory.size() == 0) {
            actualRoom.add("Inventory is empty");
            return;
        }
        actualRoom.add("Inventory:");
        int id = 0;
        for (Item item : inventory) {
            Tools.printLine();
            item.show(id++);
        }
    }

    public boolean equipItem(Item item) {
        Item tempItem = null;
        if (lvl < item.lvl)
            return false;
        if (item instanceof Weapon) {
            tempItem = weapon;
            weapon = (Weapon) item;
        } else if (item instanceof Armor) {
            tempItem = armor;
            armor = (Armor) item;
        } else
            return false;
        inventory.add(tempItem);
        return true;
    }

    public boolean checkWeaponLvl(int lvl) {
        return this.lvl >= lvl;
    }
}
