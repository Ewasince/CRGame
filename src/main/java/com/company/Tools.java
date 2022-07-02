package com.company;

import java.util.Objects;
import java.util.Random;

import com.company.GridObjects.Objects.Void;

import static com.company.Main.actualRoom;
//import com.company.Main.Screen;

public class Tools {
    static Random rnd = new Random();

    public static int lvl() {
        int lvl;
        if (rnd.nextBoolean())
            lvl = Main.player.lvl;
        else if (rnd.nextInt(100) < 30)
            lvl = Main.player.lvl + 1;
        else
            lvl = Main.player.lvl - 1;
        return Math.max(lvl, 1);
    }

    public static String rare() {
        String[] classw = {"Legendary", "Epic", "Rare", "Common"};
        int chance = rnd.nextInt(1000);
        if (chance == 0) return classw[0];
        if (chance < 10) return classw[1];
        if (chance < 100) return classw[2];
        return classw[3];
    }

    public static String typeMaterial() {
        String[] type = {"Magic", "Wood", "Iron", "Copper", "Obsidian"};
        return type[rnd.nextInt(type.length)];
    }

    public static String typeWeapon() {
        String[] classw = {"Sword", "Rapira", "Hummer", "Axe", "Katana", "Pan"};
        return typeMaterial() + " " + classw[rnd.nextInt(classw.length)];
    }

    public static String typeArmor() {
        String[] classw = {"Cuirass", "Cloak", "Cape"};
        return typeMaterial() + " " + classw[rnd.nextInt(classw.length)];
    }

    public static void printLine() {
        actualRoom.add("——————————");
    }

    public static void printLine(int times) {
        for (int i = 0; i < times; i++) actualRoom.add("——————————");
    }

    public static class Coor {
        public int X;
        public int Y;

        public Coor(int x, int y) {
            X = x;
            Y = y;
        }

        public Coor(Coor coor) {
            X = coor.X;
            Y = coor.Y;
        }

        public Coor() {
            this(0, 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coor coor = (Coor) o;
            return X == coor.X && Y == coor.Y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(X, Y);
        }
    }

    public static boolean getDirection(Coor coor, String direction) {
        Direction dir;
        switch (direction) {
            case "0", "w" -> dir = Direction.UP;
            case "1", "s" -> dir = Direction.DOWN;
            case "2", "a" -> dir = Direction.LEFT;
            case "3", "d" -> dir = Direction.RIGHT;
            case "ur", "ru", "up-right", "right-up" -> dir = Direction.UPRRIGHT;
            case "rd", "dr", "down-right", "right-down" -> dir = Direction.DOWNRIGHT;
            case "ul", "lu", "up-left", "left-up" -> dir = Direction.UPLEFT;
            case "dl", "ld", "down-left", "left-down" -> dir = Direction.DOWNLEFT;
            default -> {
                actualRoom.add("Unknown direction");
                return false;
            }
        }
        coor.X += dir.x;
        coor.Y += dir.y;
        return true;
    }

    public static boolean getDirection(Coor coor, int direction) {
        return getDirection(coor, String.valueOf(direction));
    }

    public static boolean getDirection(Coor coor, Direction dir) {
        coor.X += dir.x;
        coor.Y += dir.y;
        return true;
    }

//    public static boolean getFreeCoor(Coor coor) {
//        return getFreeCoor(coor, CurrentMap);
//    }

    public static boolean getFreeCoor(Coor coor, Room currentRoom) {
        for (int i = 0; i < 5; i++) {
            coor.X = rnd.nextInt(currentRoom.widthX);
            coor.Y = rnd.nextInt(currentRoom.widthY);
            if (currentRoom.getObject(coor.X, coor.Y) instanceof Void)
                return true;
        }
        int startX = rnd.nextInt(currentRoom.widthX);
        int startY = rnd.nextInt(currentRoom.widthY);
        coor.X = startX;
        coor.Y = startY;
        while (!(currentRoom.getObject(coor.X, coor.Y) instanceof Void)) {
            if (++coor.X >= currentRoom.widthX) {
                coor.X = 0;
                if (++coor.Y >= currentRoom.widthY)
                    coor.Y = 0;
            }
            if (coor.X == startX && coor.Y == startY) {
                currentRoom.add("There are no free area");
                return false;
            }
        }
        return true;
    }

    public enum Direction {
        UP(0, -1),
        UPRRIGHT(1, -1),
        UPLEFT(-1, -1),
        DOWN(0, 1),
        DOWNRIGHT(1, 1),
        DOWNLEFT(-1, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);
        int x;
        int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
