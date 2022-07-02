package com.company.Items.Ammunition;

import com.company.Items.Item;
import com.company.Tools;

import static com.company.Main.actualRoom;

public class Armor extends Item {
    public int defence;

    public Armor() {
        this(Tools.lvl());
    }

    public Armor(int lvl) {
        super(lvl);
        name = Tools.rare() + " " + Tools.typeArmor();
        workName = "Armor " + name;
        int teorHp = Item.Generate.teorHp(lvl);
        defence = Generate.defence(teorHp);
        cost = (int) (Item.Generate.cost(lvl)* Generate.costCoeff * defence / Generate.coeff);
    }

    @Override
    public void show() {
        super.show();
        actualRoom.add("defence:" + defence);
    }

    @Override
    public void show(int id) {
        super.show(id);
        actualRoom.add("defence:" + defence);
    }

    public static class Generate {
        public static final double coeff = 0.125;
        public static final double costCoeff = 1;

        public static int defence(int teorHp) {
            return (int) Math.round(teorHp * coeff * (rnd.nextFloat() * 0.3 + 0.7));
        }
    }
}
