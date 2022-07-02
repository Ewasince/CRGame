package com.company.Spells;

import java.util.ArrayList;
import java.util.Random;

public class SpellBook<A extends Magic,B extends Magic,C extends Magic>{
    static Random rnd = new Random();
    private ArrayList<Magic> elements = new ArrayList<>();
    A spell1;
    B spell2;
    C spell3;
    SpellBook(int num1, int num2,int num3) {
        elements.add(new Fire());
        elements.add(new Water());
        elements.add(new Earth());
        elements.add(new Air());
        elements.add(new Frost());
        elements.add(new Ephire());



        spell1 = (A) elements.get(num1);
        spell2 = (B) elements.get(num2);
        spell3 = (C) elements.get(num3);
    }
    public int getDamage()  { return getFromThree().getDamage(); }
    public String getSpell()  { return getFromThree().getNameSpell(); }
    public int getNumOfSpell() { return getFromThree().getNumOfSpell(); }
    public Magic getFromThree()
    {
        switch(rnd.nextInt(3))
        {
            case 0:
                return spell1;
            case 1:
                return spell2;
            case 2:
                return spell3;
        }
        return spell1;
    }
}
