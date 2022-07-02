package com.company;

import com.company.GridObjects.Objects.GridObject;

import java.io.IOException;

import static com.company.Main.subDisplay;

public class Room {

    public int widthX;
    public int widthY;
    public GridObject[][] fieldObjects;

    public Room(int widthX, int widthY) {
        this.widthX = widthX;
        this.widthY = widthY;
        fieldObjects = new GridObject[widthX][widthY];
    }

    public Room() {
        this(30, 30);
    }

    public synchronized void print() {
        Main.monitor.print();
    }


    public synchronized void add(Object... obj) {
        subDisplay.add(obj);
    }

    public synchronized void setObject(GridObject obj, int x, int y) {
        fieldObjects[x][y] = obj;
    }

    public synchronized void setObject(GridObject obj, Tools.Coor coor) {
        fieldObjects[coor.X][coor.Y] = obj;
    }

    public GridObject getObject(int x, int y) {
        if (x < 0 || x + 1 > widthX || y < 0 || y + 1 > widthY)
            return null;
        return fieldObjects[x][y];
    }

    public GridObject getObject(Tools.Coor coor) {
        if (coor.X < 0 || coor.X + 1 > widthX || coor.Y < 0 || coor.Y + 1 > widthY)
            return null;
        return fieldObjects[coor.X][coor.Y];
    }

    public boolean isPassingObject(int x, int y, GridObject whoWantGo) {
        if (getObject(x, y) == null)
            return false;
        return getObject(x, y).isPass(whoWantGo);
    }
}
