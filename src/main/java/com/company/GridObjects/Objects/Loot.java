package com.company.GridObjects.Objects;

import com.company.GridObjects.Creatures.Player;
import com.company.GridObjects.Entities.Environment.EnchantedEnvironmentObject;
import com.company.Room;
import com.company.Tools;
import com.company.GridObjects.Creatures.Creature;

import java.util.ArrayList;

import com.company.Items.Item;


public class Loot extends GridObject {
    public ArrayList<Item> loot = new ArrayList<>();

    {
        idType = ID.LOOT;
    }

    public Loot(Creature creature, Room room) {
        super(creature.coor, room);
        loot.add(creature.weapon);
        loot.add(creature.armor);
    }

    public Loot(EnchantedEnvironmentObject obj, Room room) {
        super(obj.coor, room);
        loot.addAll(obj.loot);
    }

    @Override
    public void show() {
        loot.get(0).show();
        if (loot.size() == 1)
            return;
        for (int i = 1; i < loot.size(); i++) {
            Tools.printLine();
            loot.get(i).show();
        }
    }

    @Override
    public boolean isPass(GridObject obj) {
        return obj instanceof Player;
    }
}
