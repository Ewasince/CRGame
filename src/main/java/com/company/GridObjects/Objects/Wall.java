package com.company.GridObjects.Objects;
import com.company.Room;
import com.company.Tools.Coor;


public class Wall extends GridObject {
    {
        idType = ID.WALL;
    }

    public Wall(Coor coor, Room room) {
        super(coor, room);
    }

    @Override
    public void show() {
        room.add("There is the wall");
    }
}
