package com.company.GridObjects.Objects;
import com.company.Room;
import com.company.Tools.Coor;

public class Void extends GridObject{
    {
        idType = ID.VOID;
    }
    public Void(Coor coor, Room room) {
        super(coor, room);
    }
    @Override
    public boolean isPass(GridObject obj){
        return true;
    }
}
