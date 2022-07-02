package com.company.GridObjects.Entities.Environment;

import com.company.Room;
import com.company.Tools.Coor;
import com.company.Tools;


public class Tree extends EnvironmentObject {

    {
        idType = ID.TREE;
        name = "Tree";
    }

    public Tree(Coor coor, Room room) {
        super(coor, room);
        maxHp = rnd.nextInt(20) + 30;
        hp = maxHp;
    }


    public static void generate(Room room) {
        Tools.Coor coor = new Tools.Coor();
        if (Tools.getFreeCoor(coor, room))
            if (rnd.nextInt(100) < 5)
                new EnchantedTree(coor, room);
            else
                new Tree(coor, room);
    }

}
