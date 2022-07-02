package com.company.GridObjects.Objects;
import com.company.Room;
import com.company.Tools.Coor;

import com.company.GridObjects.Creatures.Player;

public class Base extends GridObject{
    {
        idType = ID.BASE;
    }

    public Base(Coor coor, Room room) {
        super(coor, room);
    }

    @Override
    public boolean isPass(GridObject obj) {
        return obj instanceof Player;
    }
}
