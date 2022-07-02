package com.company.Items;

import com.company.Main;
import com.company.Tools;

import java.util.Random;

public class Item {
    public static Random rnd = new Random();

    public String name = "";
    public String workName = "";
    public int cost;
    public int lvl;

    public Item() {
        this(Tools.lvl());
    }

    public Item(int lvl) {
        this.lvl = lvl;
    }


    public void show() {
        Main.actualRoom.add("name: %s", workName);
        Main.actualRoom.add("cost:" + cost);
        Main.actualRoom.add("lvl:" + lvl);
    }

    public void show(int id) {
        Main.actualRoom.add("%d name: %s", id, workName);
        Main.actualRoom.add("cost:" + cost);
        Main.actualRoom.add("lvl:" + lvl);
    }

    public static class Generate{
        public static int cost(int lvl){
            return (int) (Math.pow(1.75, lvl) * (rnd.nextFloat() * 0.4 + 0.6));
        }
        public static int teorHp(int lvl){
            return (int) Math.round(lvl * 50 * (rnd.nextFloat() * 0.4 + 0.8));
        }
    }
}
