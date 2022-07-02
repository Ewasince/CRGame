package com.company.GridObjects.Entities.Environment;
import com.company.Room;
import com.company.Tools.Coor;

import com.company.GridObjects.Objects.GridObject;
import com.company.Tools;

public class EnchantedTree extends EnchantedEnvironmentObject{

    {
        idType = GridObject.ID.ETREE;
        name = "Enchanted Tree";
    }

    public EnchantedTree(Coor coor, Room room) {
        super(coor, room);
        maxHp = rnd.nextInt(20) + 30;
        hp = maxHp;
    }

    public static void generate(Room room) {
        Tools.Coor coor = new Tools.Coor();
        if (Tools.getFreeCoor(coor, room))
            new EnchantedTree(coor, room);
    }
}
