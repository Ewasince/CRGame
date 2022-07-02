package com.company.Graphics;

import com.company.Main;
import com.company.Tools;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class TextPanel extends Panel {
    public ArrayList<String> textDisplay = new ArrayList<>();
//    public ArrayList<String> textDisplayBuffer = new ArrayList<>();

    public TextPanel(Tools.Coor pos, Tools.Coor width, Monitor monitor) {
        super(pos, width, monitor);
    }

    int page = 0;

    @Override
    public void print() {
        super.print();
        double avH = height - 1;
        Stream<String> strings = textDisplay.stream();
        AtomicInteger row = new AtomicInteger();
        strings.skip((long) avH * page).limit((long) avH * (page + 1)).forEach(s -> {
            monitor.putString(posX + 1, posY + 1 + row.get(), s);
            row.getAndIncrement();
        });
    }
//    public void bufferedPrint() {
//        textDisplayBuffer = (ArrayList<String>) textDisplay.clone();
//        textDisplay.clear();
//        bufferedPrint();
//    }

    public void leas(int dir) {
        double avH = height - 1;
        if (page + dir <= (int) Math.ceil(textDisplay.size() / avH) - 1 && page + dir >= 0)
            page += dir;
    }

    public synchronized void add(Object... obj) {

        double avH = height - 1;
        int maxPage = (int) Math.ceil(textDisplay.size() / avH) - 1;
        boolean jumpTo = page == maxPage;

        int availableWidth = width - 1;
        Object[] newObj = new Object[obj.length - 1];
        System.arraycopy(obj, 1, newObj, 0, newObj.length);
        String tempStr = " " + String.format((String) obj[0], newObj);

        while (tempStr.length() > availableWidth) {
            textDisplay.add(tempStr.substring(0, availableWidth));
            tempStr = tempStr.substring(availableWidth);
        }
        textDisplay.add(tempStr);

        page = (int) Math.ceil(textDisplay.size() / avH) - 1;
    }
}
