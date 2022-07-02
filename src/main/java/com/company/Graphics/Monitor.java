package com.company.Graphics;

import com.company.Main;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

public class Monitor {
    public Terminal terminal;
    public Screen screen;
    ArrayList<Panel> panels = new ArrayList<>(5);
    protected static TextGraphics textGraph;

    {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            textGraph = screen.newTextGraphics();
            screen.startScreen();
            screen.setCursorPosition(null);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    synchronized public void print() {
        for (Panel panel : panels) {
            panel.print();
        }
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
        screen.doResizeIfNecessary();
    }

    public void setCharacter(int posX, int posY, char ch) {
        textGraph.setCharacter(posX, posY, ch);
    }

    public void setCharacter(int posX, int posY, TextCharacter textCh) {
        textGraph.setCharacter(posX, posY, textCh);
    }

    public void drawLine(int posX1, int posY1, int posX2, int posY2, char ch) {
        textGraph.drawLine(posX1, posY1, posX2, posY2, ch);
    }

    public void fillRectangle(int posX1, int posY1, int posX2, int posY2, char ch) {
        textGraph.fillRectangle(new TerminalPosition(posX1, posY1), new TerminalSize(posX2, posY2), ch);
    }

    public void putString(int posX, int posY, String str) {
        textGraph.putString(posX, posY, str);
    }
}


//    public void display(String[] string) throws IOException {
//        for (int i = 0; i < string.length; i++) {
//            tg.putString(1, i, string[i]);
////            System.out.println(string[i]);
//        }
//        screen.refresh();
//    }

