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

public class Panel {
    Monitor monitor;
    int posX;
    int posY;
    int width;
    int height;

    public Panel(Tools.Coor pos, Tools.Coor width, Monitor monitor) {
        posX = pos.X;
        posY = pos.Y;
        this.width = width.X;
        this.height = width.Y;
        this.monitor = monitor;
        monitor.panels.add(this);
    }

    public void print() {
        monitor.drawLine(posX, posY, posX + width, posY, '═');
        monitor.drawLine(posX, posY, posX, posY + height, '║');
        monitor.drawLine(posX + width, posY, posX + width, posY + height, '║');
        monitor.drawLine(posX, posY + height, posX + width, posY + height, '═');
        monitor.setCharacter(posX, posY, '╔');
        monitor.setCharacter(posX + width, posY, '╗');
        monitor.setCharacter(posX, posY + height, '╚');
        monitor.setCharacter(posX + width, posY + height, '╝');
        monitor.fillRectangle(posX + 1, posY + 1, width - 1, height - 1, ' ');
    }
}