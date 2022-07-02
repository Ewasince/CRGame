package com.company;

import com.company.GridObjects.Creatures.Enemy;
import com.company.GridObjects.Creatures.Player;
import com.company.GridObjects.Entities.Environment.Tree;
import com.company.GridObjects.Objects.Door;
import com.company.GridObjects.Objects.Void;
import com.company.GridObjects.Objects.Wall;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.company.GridObjects.Objects.GridObject.ID.*;
import static com.company.Main.Find.HEIGHT;
import static com.company.Main.Find.WIDTH;
import static com.company.Tools.Direction.*;

public class GameLogic {
    static public int dps (double rate, int damage)
    {
        return (int) (rate*damage);
    }

    static Door createRoom(Room genRoom, String[] startField, int[] params, Tools.Coor coorOfEnterDoor) {
        //  nOfDoors – order id's of doors. warning: count of num have to be equal or less count of doors
        int dotCounter = 0;
        for (String s : startField) {
            char[] tempchar = s.toCharArray();
            for (char c : tempchar)
                if (c == '.')
                    dotCounter++;
        }// подсчет свободного пространства
        Door EnterDoor = null;

        try {
            int iCicle = Math.min(genRoom.widthY, startField.length);
            for (int i = 0; i < iCicle; i++) {
                char[] tempChar = startField[i].toCharArray();
                int jCicle = Math.min(genRoom.widthX, tempChar.length);
                for (int j = 0; j < jCicle; j++) {
                    ///////////////////начало бурления говна
                    char ch = tempChar[j];
                    Tools.Coor coor = new Tools.Coor(j, i);
                    if (ENEMY.skin == ch)
                        new Enemy(coor, genRoom);
                    else if ('W' == ch)
                        new Wall(coor, genRoom);
                    else if (BASE.skin == ch)
                        new com.company.GridObjects.Objects.Base(coor, genRoom);
                    else if (TREE.skin == ch)
                        new Tree(coor, genRoom);
                    else if ('.' == ch)
                        new Void(coor, genRoom);
                    else if (DOOR.skin == ch) {
                        if (coor.equals(coorOfEnterDoor))
                            EnterDoor = Door.generate(coor, genRoom);
                        else
                            Door.generate(coor, genRoom);
                    } else if (PLAYER.skin == ch) {
                        Main.player = new Player(
                                "Vasya Pupkin",
                                coor,
                                5, genRoom
                        );
                    }
                }
            }
            if (Main.player == null) {
                Tools.Coor coor = new Tools.Coor(genRoom.widthX / 2, genRoom.widthY / 2);
                Tools.getFreeCoor(coor, genRoom);
                Main.player = new Player("Автоматически сгенерированный игрок", coor, 5, genRoom);
            }
            for (int i = 0; i < 3; i++)
                spawn(dotCounter, params[i], Main.LibraryOfMaps.generators.get(i), genRoom);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (EnterDoor == null)
                throw new Exception("Входная дверь не сгенерировалась!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnterDoor;
    }

    static Door createRoom(Room room, Main.LibraryOfMaps.SavedMap map) {
        return createRoom(room, map.StringMap(), map.params(), map.coorFrstDoor());
    }

    static void spawn(int countDots, int param, Consumer<Room> action, Room room) {
        for (int i = 0; i < countDots * param / 100; i++) {
            action.accept(room);
        }
    }

    public static Door genRoom(Room room, Tools.Direction sideOfMainDoor) {
        return genRoom(room, sideOfMainDoor, Main.LibraryOfMaps.standart.params());
    }

    public static Door genRoom(Room room, Tools.Direction sideOfMainDoor, int[] params) {
        switch (sideOfMainDoor) {
            case UP -> sideOfMainDoor = DOWN;
            case RIGHT -> sideOfMainDoor = Tools.Direction.LEFT;
            case DOWN -> sideOfMainDoor = UP;
            case LEFT -> sideOfMainDoor = Tools.Direction.RIGHT;
        }

        int width = room.widthX;
        int height = room.widthY;
        char[][] field = new char[width][height];//буферный массив чаров для текстовой генерации карты
        int cSqu = 4;//количество проходок генератора


        //генерация стен комнаты и "VOID" внутри них
        char[][] insertSquare = genSquare(1, width, height);
        int tmpX = Main.rnd.nextInt(width - insertSquare.length);
        int tmpY = Main.rnd.nextInt(height - insertSquare[0].length);
        for (int i = tmpX; i < insertSquare.length + tmpX; i++)
            for (int j = tmpY; j < insertSquare[0].length + tmpY; j++)
                field[i][j] = insertSquare[i - tmpX][j - tmpY];

        int countUnluckyAttempts = 0;
        for (int k = 2; k <= cSqu; k++) {
            insertSquare = genSquare(k, width, height);
            tmpX = Main.rnd.nextInt(width - insertSquare.length);
            tmpY = Main.rnd.nextInt(height - insertSquare[0].length);
            int flagW = 0;
            int flagDot = 0;
            char[][] backup = field.clone();
            for (int i = tmpX; i < insertSquare.length + tmpX; i++)
                for (int j = tmpY; j < insertSquare[0].length + tmpY; j++) {
                    int ti = i - tmpX;
                    int tj = j - tmpY;
                    if (field[i][j] == 'W' && insertSquare[ti][tj] == 'W')
                        flagW++;
                    else if (field[i][j] == '.' && insertSquare[ti][tj] == '.')
                        flagDot++;
                    else if ((field[i][j] == 'W' && insertSquare[ti][tj] == '.') || (field[i][j] == '.' && insertSquare[ti][tj] == 'W')) {
                        field[i][j] = '.';
                        flagDot++;
                    } else
                        field[i][j] = insertSquare[ti][tj];
                }
            if (flagW < 2 || flagDot < 7) {
                field = backup;
                if (countUnluckyAttempts >= 5) {
                    countUnluckyAttempts = 0;
                    continue;
                }
                k--;
                countUnluckyAttempts++;
            }
        }

        //генерация двери(ей)
        Tools.Coor coorEnterDoor = genDoor(field, sideOfMainDoor);

        ArrayList<Tools.Direction> dirs = new ArrayList<>(4);
        dirs.add(UP);
        dirs.add(DOWN);
        dirs.add(LEFT);
        dirs.add(RIGHT);
        dirs.remove(sideOfMainDoor);
        Tools.Direction extraDoorDir = dirs.get(Main.rnd.nextInt(dirs.size()));
        dirs.remove(extraDoorDir);
        genDoor(field, extraDoorDir);

        if (Main.rnd.nextInt(100) < 30)
            genDoor(field, dirs.get(Main.rnd.nextInt(dirs.size())));

        String[] stringField = new String[field[0].length];
        for (int i = 0; i < field[0].length; i++) {
            StringBuilder tempStr = new StringBuilder();
            for (int j = 0; j < field.length; j++) {
                tempStr.append(field[j][i]);
            }
            stringField[i] = tempStr.toString();
        }

        return createRoom(room, stringField, params, coorEnterDoor);
    }

    static Tools.Coor genDoor(char[][] field, Tools.Direction sideOfDoor) {
        int width = field.length;
        int height = field[0].length;
        int strtDot;
        int endDot;
        Tools.Coor rangeMainDoor = new Tools.Coor(0, 0);
        Tools.Coor coorMDor = null;
        if (sideOfDoor == UP || sideOfDoor == DOWN) {
            findLineDoor(field, rangeMainDoor, WIDTH);
            strtDot = rangeMainDoor.X + 1;
            endDot = rangeMainDoor.Y - 1;

            boolean notGenerated = true;
            while (notGenerated) {
                int lineOfDoor = Main.rnd.nextInt(endDot - strtDot) + strtDot;
                if (sideOfDoor == UP)
                    for (int i = 0; i < height; i++)
                        if (field[lineOfDoor][i] == 'W') {
                            if (!(i + 1 < height && field[lineOfDoor][i + 1] != 'W'))
                                break;
                            field[lineOfDoor][i] = '8';
                            notGenerated = false;
                            coorMDor = new Tools.Coor(lineOfDoor, i);
                            break;
                        }
                if (sideOfDoor == DOWN)
                    for (int i = height - 1; i >= 0; i--)
                        if (field[lineOfDoor][i] == 'W') {
                            if (!(i - 1 >= 0 && field[lineOfDoor][i - 1] != 'W'))
                                break;
                            field[lineOfDoor][i] = '8';
                            notGenerated = false;
                            coorMDor = new Tools.Coor(lineOfDoor, i);
                            break;
                        }
            }
        } else {
            findLineDoor(field, rangeMainDoor, HEIGHT);
            strtDot = rangeMainDoor.X;
            endDot = rangeMainDoor.Y;

            boolean notGenerated = true;
            while (notGenerated) {
                int lineOfDoor = Main.rnd.nextInt(endDot - strtDot - 1) + strtDot + 1;
                if (sideOfDoor == LEFT)
                    for (int i = 0; i < width; i++)
                        if (field[i][lineOfDoor] == 'W') {
                            if (!(i + 1 < field[0].length && field[i + 1][lineOfDoor] != 'W'))
                                break;
                            field[i][lineOfDoor] = '8';
                            notGenerated = false;
                            coorMDor = new Tools.Coor(i, lineOfDoor);
                            break;
                        }
                if (sideOfDoor == RIGHT)
                    for (int i = width - 1; i >= 0; i--)
                        if (field[i][lineOfDoor] == 'W') {
                            if (!(i - 1 >= 0 && field[i - 1][lineOfDoor] != 'W'))
                                break;
                            field[i][lineOfDoor] = '8';
                            notGenerated = false;
                            coorMDor = new Tools.Coor(i, lineOfDoor);
                            break;
                        }
            }
        }
        return coorMDor;
    }

    static void findLineDoor(char[][] startField, Tools.Coor coor, Main.Find type) {
        //coor.X – startDor
        //coor.Y - endDot
        char[][] field;
        if (type == Main.Find.WIDTH)
            field = startField.clone();
        else if (type == HEIGHT) {
            char[][] tempField = new char[startField[0].length][startField.length];
            for (int i = 0; i < startField.length; i++)
                for (int j = 0; j < startField[0].length; j++)
                    tempField[j][i] = startField[i][j];
            field = tempField;

        } else {
            try {
                throw new Exception("Неверное направление генерации двери");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        int width = field.length;
        int height = field[0].length;
        mainloop:
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (field[i][j] != '\u0000') {
                    coor.X = i;
                    break mainloop;
                }
        mainloop:
        for (int i = width - 1; i >= 0; i--)
            for (int j = 0; j < height; j++)
                if (field[i][j] != '\u0000') {
                    coor.Y = i;
                    break mainloop;
                }
    }

    static char[][] genSquare(int primaryCoeff, int width, int height) {
        int coeff = (int) Math.max(Math.pow(primaryCoeff, 0.5), 1);
        int tWidth = width * (50 / coeff + 10 + Main.rnd.nextInt(20)) / 100;
        int tHeight = height * (50 / coeff + 10 + Main.rnd.nextInt(20)) / 100;
        char[][] tFld = new char[tWidth][tHeight];
        for (int i = 0; i < tWidth; i++)
            for (int j = 0; j < tHeight; j++)
                if (i == 0 || i == tWidth - 1 || j == 0 || j == tHeight - 1)
                    tFld[i][j] = 'W';
                else
                    tFld[i][j] = '.';
        return tFld;
    }
}
