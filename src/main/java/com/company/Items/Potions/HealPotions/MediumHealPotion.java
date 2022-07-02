package com.company.Items.Potions.HealPotions;

import com.company.GridObjects.Creatures.Enemy;
import com.company.Items.Item;

public class MediumHealPotion extends HealPotion{
    {
        name = "Medium Heal Potion";
        healingCoeff = 0.5;
    }
    MediumHealPotion(int lvl){
        super(lvl);
        maxResource = (int) (Enemy.Generate.maxHp(lvl) * globalHealingCoeff * healingCoeff);
        resource = maxResource;
        workName = name;
        cost = (int) (Item.Generate.cost(lvl) * Generate.costCoeff * resource);
    }
}
