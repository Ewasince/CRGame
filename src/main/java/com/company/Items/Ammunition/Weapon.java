package com.company.Items.Ammunition;

import com.company.Items.Item;
import com.company.Tools;

import static com.company.Main.actualRoom;

public class Weapon extends Item {
    public int damage;


    public Weapon() {
        this(Tools.lvl());
    }

    public Weapon(int lvl) {
        super(lvl);
        name = Tools.rare() + " " + Tools.typeWeapon();
        workName = "Weapon " + name;
        int teorHp = Item.Generate.teorHp(lvl);
        damage = Generate.damage(teorHp);
        cost = (int) (Item.Generate.cost(lvl) * Armor.Generate.costCoeff * damage / Armor.Generate.coeff);
    }

    @Override
    public void show() {
        super.show();
        actualRoom.add("power:" + damage);
    }

    @Override
    public void show(int id) {
        super.show(id);
        actualRoom.add("power:" + damage);

    }

    public static class Generate {
        public static final double coeff = 0.3;
        public static final double costCoeff = 1;

        public static int damage(int teorHp) {
            return (int) Math.round(teorHp * coeff * (rnd.nextFloat() * 0.3 + 0.7));
        }
    }
}
