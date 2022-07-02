package com.company.GridObjects.Creatures;

import com.company.Main;
import com.company.Properties;
import com.company.Room;
import com.company.Tools;
import com.company.Items.Ammunition.Armor;
import com.company.Items.Ammunition.Weapon;
import com.company.Tools.Coor;

import java.util.Locale;

public class Enemy extends Creature implements Runnable {
    Thread thread;

    {
        idType = ID.ENEMY;
    }

    public Enemy(Coor coor, Room room) {
        super(coor, room);
        lvl = Tools.lvl();
        xp = Generate.xp(lvl);
        name = spellsPool[0].getNameSpell() + " " + genName();
        maxHp = Generate.maxHp(lvl);
        hp = maxHp;
        weapon = new Weapon(lvl);
        armor = new Armor(lvl);
        defaultDamage = Generate.defaultDamage(maxHp);
        thread = new Thread(this, Integer.toString(id));
        thread.start();
    }



    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep( 1000 + rnd.nextInt(9000));
                if (isBusy)
                    continue;
                if (rnd.nextBoolean())
                    continue;
                int dir = rnd.nextInt(4);
                move(dir);
                if (Properties.doCheckEnemyMoving)
                    room.add("%s has come to %d", name, dir);
                if (Properties.doUpdateScreenEnemyMoving)
                    room.print();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move(String dir) {
        super.move(dir);
        lookAround();
    }

    public void lookAround(){
        for (int i = 0; i < 4; i++) {
            Coor coor = new Coor(this.coor);
            Tools.getDirection(coor, i);
            if (!(room.getObject(coor) instanceof Player player))
                continue;
            if (player.isBusy)
                return;
            Main.Console.attack(this);
            isBusy = true;
            player.isBusy = true;
        }
    }


    static String genName() {
        StringBuilder name = new StringBuilder();
        String[] vov = {"e", "y", "u", "i", "o", "a"};
        String[] cons = {"q", "w", "r", "t", "p", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
        if (rnd.nextInt(100) < 30) name.append(vov[rnd.nextInt(vov.length)]);
        int ln = rnd.nextInt(3) + 2;
        for (int i = 0; i < ln; i++) {
            name.append(cons[rnd.nextInt(cons.length)]);
            name.append(vov[rnd.nextInt(vov.length)]);
        }
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    @Override
    public void show() {
        super.show();
        room.add("reward: " + xp + " xp");
    }

    @Override
    public void kill() {
        super.kill();
        thread.interrupt();
    }

    public static void generate(Room room) {
        Coor coor = new Coor();
        if (Tools.getFreeCoor(coor, room))
            new Enemy(coor, room);
    }

    public static class Generate {
        public static int maxHp(int lvl) {
            return (int) Math.round(lvl * 50 * (rnd.nextFloat() * 0.7 + 0.3));
        }

        public static int xp(int lvl) {
            return (int) (Math.pow(lvl, 2) * 50 * 0.3 * (rnd.nextFloat() * 0.5 + 0.5));
        }

        public static int defaultDamage(int maxHp) {
            return (int) Math.ceil(maxHp * 0.1 * (rnd.nextFloat() * 0.3 + 0.7));
        }


    }
}
