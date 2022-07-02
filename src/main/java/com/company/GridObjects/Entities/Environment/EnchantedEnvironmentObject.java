package com.company.GridObjects.Entities.Environment;

import com.company.GridObjects.Objects.Loot;
import com.company.Items.Ammunition.Armor;
import com.company.Items.Ammunition.Weapon;
import com.company.Items.Item;
import com.company.Room;
import com.company.Tools;
import com.company.Tools.Coor;

import java.util.ArrayList;

public abstract class EnchantedEnvironmentObject extends EnvironmentObject {
    public ArrayList<Item> loot = new ArrayList<>(2);

    public EnchantedEnvironmentObject(Coor coor, Room room) {
        super(coor, room);
        if (rnd.nextBoolean())
            loot.add(new Weapon(Math.max(Tools.lvl() - 1, 1)));
        else
            loot.add(new Armor(Math.max(Tools.lvl() - 1, 1)));
    }

    @Override
    public void kill() {
        super.kill();
        new Loot(this, room);
    }

}
