package com.company.Spells;

public class Spell implements Magic {
    int type;
    public static String[] typeName = {"Fire", "Water", "Earth", "Air", "Ice", "Ephire"};
    public static int[] typeDamage = {20, 15, 20, 10, 17, 19};


//    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getNameSpell() {
        return typeName[type];
    }

    @Override
    public int getDamage() {
        return typeDamage[type];
    }

    @Override
    public int getNumOfSpell() {
        return type;
    }
//    static public Spell getRandomSpell(int num)
//    {
//
//        switch (num)
//        {
//            case 0:
//                spell = (A) new Fire();
//                break;
//            case 1:
//                spell = (A) new Water();
//                break;
//            case 2:
//                spell = (A) new Earth();
//                break;
//            case 3:
//                spell = (A) new Air();
//                break;
//            case 4:
//                spell = (A) new Frost();
//                break;
//            case 5:
//                spell = (A) new Ephire();
//                break;
//        }
//        return null;
//    }

}
