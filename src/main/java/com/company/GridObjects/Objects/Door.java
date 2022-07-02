package com.company.GridObjects.Objects;

import com.company.GameLogic;
import com.company.Tools;
import com.company.Tools.Coor;

import com.company.GridObjects.Creatures.Player;

import static com.company.GridObjects.Objects.GridObject.ID.*;

import com.company.Room;
import static com.company.Main.*;
import static com.company.Tools.Direction.*;
import static com.company.Tools.getDirection;

public class Door extends GridObject {
    Door nextDoor;
    Tools.Direction sideOfDoor;
    public int lvlDoor = 1;

    {
        idType = ID.DOOR;
    }

    public Door(Coor coor, Room room) {
        super(coor, room);
        getSideOfDoor();
//        this.currentRoom = room;
////        numOfDoor = num;
//        map.doors.put(num, this);
//        map.doors.putIfAbsent(numOfDoor, this);
    }

    public void enter() {
        if (nextDoor == null) {
            Room nextRoom = new Room(rnd.nextInt(15) + 15, rnd.nextInt(15) + 15);
            nextDoor = GameLogic.genRoom(nextRoom, sideOfDoor);
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

    void getSideOfDoor() {
        try {
//            room.techPrint();
            Coor up = new Coor(coor);
            getDirection(up, UP);
            GridObject upObj = room.getObject(up);
            Coor down = new Coor(coor);
            getDirection(down, DOWN);
            GridObject downObj = room.getObject(down);
            Coor left = new Coor(coor);
            getDirection(left, LEFT);
            GridObject leftObj = room.getObject(left);
            Coor right = new Coor(coor);
            getDirection(right, RIGHT);
            GridObject rightObj = room.getObject(right);
            if (upObj != null && upObj.idType == WALL || downObj != null && downObj.idType == WALL)
                if (leftObj == null || leftObj.idType == null)
                    sideOfDoor = LEFT;
                else if (rightObj == null || rightObj.idType == null)
                    sideOfDoor = RIGHT;
                else throw new Exception("Wrong generation of door");
            else if (leftObj != null && leftObj.idType == WALL || rightObj != null && rightObj.idType == WALL)
                if (upObj == null || upObj.idType == null)
                    sideOfDoor = UP;
                else if (downObj == null || downObj.idType == null)
                    sideOfDoor = DOWN;
                else throw new Exception("Wrong generation of door");
            else throw new Exception("Wrong generation of door");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPass(GridObject obj) {
        return obj instanceof Player;
    }

    public static Door generate(Coor coor, Room room) {
        if (rnd.nextInt(100) < 20)
            return new ForestDoor(coor, room);
        else
            return new Door(coor, room);
    }

}
