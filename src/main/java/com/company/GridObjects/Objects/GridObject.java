package com.company.GridObjects.Objects;

import com.company.Room;
import com.company.Tools.Coor;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.TextCharacter;

import java.util.Random;

public abstract class GridObject {
    static public Random rnd = new Random();
    static int publicID = 0;
    public int id = publicID++;
    protected ID idType;
    public Coor coor;
    public Room room;

    public enum ID {
        VOID(' ', ANSI.WHITE, ANSI.BLACK),
        PLAYER('&', ANSI.GREEN_BRIGHT, ANSI.BLACK),
        ENEMY('E', ANSI.RED_BRIGHT, ANSI.BLACK),
        WALL('W', ANSI.WHITE, ANSI.BLACK),
        LOOT('$', ANSI.YELLOW_BRIGHT, ANSI.BLACK),
        TREE('%', ANSI.GREEN_BRIGHT, ANSI.BLACK),
        ETREE('%', ANSI.GREEN, ANSI.BLACK),
        ROCK('0', ANSI.BLACK_BRIGHT, ANSI.BLACK),
        EROCK('0', ANSI.BLACK_BRIGHT, ANSI.BLACK),
        DOOR('8', ANSI.WHITE_BRIGHT, ANSI.BLACK),
        FORESTDOOR('8', ANSI.GREEN, ANSI.BLACK),
        BASE('#', ANSI.WHITE_BRIGHT, ANSI.BLACK);

        public char skin;
        public TextColor frontColor;
        public TextColor backColor;
//        public TextColor frontcolor;

        ID(char skin, ANSI frontColor, ANSI backColor) {
            this.skin = skin;
            this.frontColor = frontColor;
            this.backColor = backColor;
        }
    }
    public TextCharacter getChar() {
        TextCharacter[] txtCh = TextCharacter.fromCharacter(idType.skin, idType.frontColor, idType.backColor);
        return txtCh[0];
    }

    public GridObject(Coor coor, Room room) {
        this.room = room;
        this.coor = new Coor(coor.X, coor.Y);
        room.setObject(this, this.coor);
    }

    public void kill() {
        room.setObject(new Void(coor, room), coor.X, coor.Y);
    }

    public void show() {
        room.add("There is unknown object");
    }

    public boolean isPass(GridObject obj) {
        return false;
    }

//    enum ColorGrid {
//        NONE(""),
//        WHITE("\u001b[0m"),
//        BLACK("\u001b[30m"),
//        RED("\u001b[31m"),
//        GREEN("\u001b[38;5;64m"),
//        LGREEN("\u001b[32m"),
//        YELLOW("\u001b[33m"),
//        BLUE("\u001b[34m"),
//        PURPLE("\u001b[35m"),
//        GRAY("\u001b[38;5;243m"),
//        LGRAY("\u001b[38;5;244m"),
//        BROWN("\u001b[38;5;124m"),
//        CYAN("\u001b[36m");
//        public String color;
//
//        ColorGrid(String color) {
//            this.color = color;
//        }
//    }
}

