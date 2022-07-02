package com.company.Spells;

import java.util.Random;

public class SpellEquipped<A extends Magic> {
    A spell;
    static Random rnd = new Random();

    public SpellEquipped(int numOfSpell)
    {
        switch (numOfSpell)
        {
            case 0:
                spell = (A) new Fire();
                break;
            case 1:
                spell = (A) new Water();
                break;
            case 2:
                spell = (A) new Earth();
                break;
            case 3:
                spell = (A) new Air();
                break;
            case 4:
                spell = (A) new Frost();
                break;
            case 5:
                spell = (A) new Ephire();
                break;
            default: spell = (A) new Fire();
        }
    }



    public String getNameSpell()  { return spell.getNameSpell(); }

    public int getType()
    {
        return spell.getType();
    }
    public int getDamage()
    {
        return spell.getDamage();
    }

    public int getNumOfSpell()
    {
        return spell.getNumOfSpell();
    }
}
