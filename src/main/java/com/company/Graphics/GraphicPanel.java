package com.company.Graphics;

import com.company.GridObjects.Creatures.Player;
import com.company.GridObjects.Objects.GridObject;
import com.company.GridObjects.Objects.GridObject.ID;
import com.company.Main;
import com.company.Tools;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class GraphicPanel extends  Panel {

    public GraphicPanel(Tools.Coor pos, Tools.Coor width, Monitor monitor) {
        super(pos, width, monitor);
    }

    public void print() {
        super.print();

        int availableWidth = width - 1;
        int availableHeight = height - 1;
        GridObject[][] field = Main.actualRoom.fieldObjects;
        Player player = Main.player;

        int topGap = (availableHeight ) / 2;
        int bottomGap = (availableHeight) - topGap;
        int startHPos = player.coor.Y - topGap;
        int endHPos = player.coor.Y + bottomGap;
        if (availableHeight > field[0].length) {
            startHPos = 0;
            endHPos = field[0].length;
        } else if (startHPos < 0) {
            startHPos = 0;
            endHPos = availableHeight;
        } else if (endHPos >= field[0].length) {
            endHPos = field[0].length;
            startHPos = field[0].length - availableHeight;
        }

        int leftGap = (availableWidth ) / 2;
        int rightGap = (availableWidth ) - leftGap;
        int startWPos = player.coor.X - leftGap;
        int endWPos = player.coor.X + rightGap;
        if (availableWidth > field.length) {
            startWPos = 0;
            endWPos = field.length;
        } else if (startWPos < 0) {
            startWPos = 0;
            endWPos = availableWidth;
        } else if (endWPos >= field.length) {
            endWPos = field.length;
            startWPos = field.length - availableWidth;
        }

        for (int y = startHPos; y < endHPos; y++)
            for (int x = startWPos; x < endWPos; x++) {
                int relativityX = x - startWPos + posX + 1;
                int relativityY = y - startHPos + posY + 1;
//                    int internalX =
                TextCharacter tempCh;
                if (field[x][y] == null)
                    tempCh = TextCharacter.fromCharacter(ID.VOID.skin, ID.VOID.frontColor, ID.VOID.backColor)[0];
                else
                    tempCh = field[x][y].getChar();

                monitor.setCharacter(relativityX, relativityY, tempCh);
            }

        try {
            monitor.screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}