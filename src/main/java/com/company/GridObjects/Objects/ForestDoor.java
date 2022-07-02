package com.company.GridObjects.Objects;

import com.company.GameLogic;
import com.company.Room;
import com.company.Main.LibraryOfMaps;
import com.company.Tools.Coor;

import static com.company.Main.*;
import static com.company.Main.player;

public class ForestDoor extends Door{
    {
        idType = ID.FORESTDOOR;
    }
    public ForestDoor(Coor coor, Room room) {
        super(coor, room);
    }

    @Override
    public void enter(){
        if (nextDoor == null) {
            Room nextRoom = new Room(rnd.nextInt(15)+10,rnd.nextInt(15)+10);
            nextDoor = GameLogic.genRoom(nextRoom, sideOfDoor, LibraryOfMaps.forest.params());
        }

        if (lvlDoor > player.lvl)
            return;
        room.setObject(this, player.coor);
        actualRoom = nextDoor.room;
        player.lastObject = nextDoor;
        nextDoor.room.setObject(player, nextDoor.coor);
        player.coor = new Coor(nextDoor.coor);
        player.room = nextDoor.room;
        nextDoor.nextDoor = this;
    }

}
