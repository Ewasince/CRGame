package com.company.GridObjects.Entities.Environment;
import com.company.Room;
import com.company.Tools.Coor;

import com.company.GridObjects.Objects.GridObject;
import com.company.Tools;

public class EnchantedRock extends EnchantedEnvironmentObject{
    {
        idType = GridObject.ID.EROCK;
        name = "Enchanted Rock";
    }

    public EnchantedRock(Coor coor, Room room) {
        super(coor, room);
        maxHp = rnd.nextInt(30) + 30;
        hp = maxHp;
    }

    public static void generate(Room room) {
        Tools.Coor coor = new Tools.Coor();
        if (Tools.getFreeCoor(coor, room))
            new EnchantedRock(coor, room);
    }
}
