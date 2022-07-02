package com.company.GridObjects.Entities.Environment;

import com.company.Room;
import com.company.Tools.Coor;


import com.company.Tools;


public class Rock extends EnvironmentObject {

    {
        idType = ID.ROCK;
        name = "Rock";
    }

    public Rock(Coor coor, Room room) {
        super(coor, room);
        maxHp = rnd.nextInt(30) + 30;
        hp = maxHp;
    }

    public static void generate(Room room) {
        Tools.Coor coor = new Tools.Coor();
        if (Tools.getFreeCoor(coor, room))
            if (rnd.nextInt(100) < 5)
                new EnchantedRock(coor, room);
            else
                new Rock(coor, room);
    }
}
