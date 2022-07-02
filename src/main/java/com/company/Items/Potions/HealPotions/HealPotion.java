package com.company.Items.Potions.HealPotions;

import com.company.Items.Item;
import com.company.Tools;

import static com.company.Main.actualRoom;

public class HealPotion extends Item {
    public int resource;
    int maxResource;
    double healingCoeff;
    double globalHealingCoeff = 0.9;

    HealPotion() {
        this(Tools.lvl());
    }

    HealPotion(int lvl) {
        super(lvl);
    }

    @Override
    public void show() {
        super.show();
        actualRoom.add("resource: %d/%d", resource, maxResource);
    }

    @Override
    public void show(int id) {
        super.show(id);
        actualRoom.add("resource: %d/%d", resource, maxResource);
    }

    static final int chanceTiny = 30;
    static final int chanceMedium = 50;
    static final int chanceBig = 20;

    public static HealPotion getRandom(int lvl) {
        int rndRng = 100;
        if (rnd.nextInt(rndRng) < chanceTiny)
            return new TinyHealPotion(lvl);
        else
            rndRng -= chanceTiny;
        if (rnd.nextInt(rndRng) < chanceMedium) {
            return new MediumHealPotion(lvl);

        } else
//            rndRng -= chanceMedium;
//        rnd.nextInt(rndRng);
            return new BigHealPotion(lvl);
    }

    public static HealPotion getRandom() {
        return getRandom(Tools.lvl());
    }

    public static class Generate {
        public static final double costCoeff = 1;

    }
}
