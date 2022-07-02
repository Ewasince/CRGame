package com.company;

import com.company.Graphics.Monitor;
import com.company.Graphics.GraphicPanel;
import com.company.Graphics.TextPanel;
import com.company.GridObjects.Entities.Entity;
import com.company.GridObjects.Entities.Environment.Rock;
import com.company.GridObjects.Entities.Environment.Tree;
import com.company.GridObjects.Objects.*;
import com.company.GridObjects.Creatures.Enemy;
import com.company.GridObjects.Creatures.Player;
import com.company.Items.Ammunition.Armor;
import com.company.Items.Ammunition.Weapon;
import com.company.Items.Item;
import com.company.Items.Potions.HealPotions.HealPotion;
import com.company.Tools.Coor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;


public class Main {
    static Random rnd = new Random();
    static Scanner in = new Scanner(System.in);
    public static Player player;
    public static Room actualRoom;
    public static Monitor monitor;
    public static TextPanel subDisplay;

    static {
        monitor = new Monitor();
        subDisplay = new TextPanel(new Coor(41, 0), new Coor(38, 23), monitor);
    }


    public static void main(String[] args) {
        GraphicPanel graphicPanel = new GraphicPanel(new Coor(0, 0), new Coor(40, 23), monitor);

        actualRoom = new Room(30, 30);
        GameLogic.createRoom(actualRoom, LibraryOfMaps.standart);
        player.show();
        monitor.print();
        while (Console.run) {

            switch (Console.mod) {
                case STANDARD -> Console.standard();
                case BATTLE -> Console.battle();
                case DEATH -> Console.run = false;
                case BASE -> Console.base();
            }
        }
        try {
            monitor.screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Console {
        static boolean run = true;
        public static ConsoleMod mod = ConsoleMod.STANDARD;

        static void standard() {
            Consumer<KeyType> fromKey = (k) -> {
                switch (k) {
                    case ArrowUp -> Main.Console.interaction("w");
                    case ArrowDown -> Main.Console.interaction("s");
                    case ArrowLeft -> Main.Console.interaction("a");
                    case ArrowRight -> Main.Console.interaction("d");
                    case Escape -> run = false;
                }
            };
            Consumer<Character> fromChar = (k) -> {
                switch (k) {
                    case 'w', 's', 'a', 'd' -> player.move(Character.toString(k));
                    case 'q' -> Console.playerMenu();
                }
            };
            exFromKeyBoard(fromKey, fromChar);
            monitor.print();


//
//
//
//            switch (str[0]) {
//                case "w", "s", "a", "d" -> {
//                    player.move(str[0]);
//                    if (Console.mod == ConsoleMod.BASE)
//                        return;
//                }
//                case "e" -> {
//                    Console.interaction(str);
//                    if (mod == ConsoleMod.BATTLE)
//                        return;
//                }
//                case "q" -> Console.playerMenu();
//                case "exit" -> run = false;
//                case "g", "generate" -> Console.generate(str);
//                case "checkstats" -> Properties.doCheckStats = Objects.equals(str[1], "true");
//                case "calibrate" -> Console.calibrate();
//                case "help" -> {
//                    actualRoom.add("up|down|left|right(u|d|l|r) движение по направлению");
//                    actualRoom.add("exit\t\t");
//                    actualRoom.add("checkstats\t\t{true|false}\t\t показывать статистику после каждого хода в бою");
//                    actualRoom.add("show(s)\t\tпроказать");
//                    actualRoom.add("\t\tplayer(p)\t\tхарактеристики игрока");
//                    actualRoom.add("\t\tweapon(w)|armor(a)\t\tхарактеристики надетых вещей");
//                    actualRoom.add("\t\tinventory\t\tинфентарь");
//                    actualRoom.add("\t\t{u|d|l|r}\t\tхарактеристики врага или предмета поблизости");
//                    actualRoom.add("\t\t\t\t{weapon(w)|armor(a)\t\tнадетых вещей (у врагов)");
//                    actualRoom.add("attack(a)\t\t{u|d|l|r}\t\tатаковать врага по направлению");
//                    actualRoom.add("take(t)\t\t{u|d|l|r}\t\tподнять лут по направлению\t\t");
//                    actualRoom.add("equip\t\t{num}\t\tэкипировать предмет из инвентаря с индексом num");
//                    actualRoom.add("generate(g)\t\tenemy(en)\t\tсоздать врага на случайной позиции");
//                    actualRoom.add("\t\t\t\t");
//                }
//                default -> {
//                    actualRoom.add("Unknown command");
//                    actualRoom.widthY--;
//                    actualRoom.print();
//                    actualRoom.widthY++;
//                    return;
//                }

            actualRoom.print();
        }

        static void battle() {
            String[] message = {
                    "space\tattack",
                    "esc\trun away"
            };
            Consumer<KeyType> fromKey = (k) -> {
                if (k == KeyType.Escape) {
                    if (player.enemy instanceof Enemy enemy) {
                        enemy.isBusy = false;
                        enemy.enemy = null;
                    }
                    player.isBusy = false;
                    player.enemy = null;
                    mod = ConsoleMod.STANDARD;
                    actualRoom.print();
                }
            };
            Consumer<Character> fromChar = (k) -> {
                if (k == ' ') {
                    Battle.next();
                }
            };
            exFromKeyBoard(message, fromKey, fromChar);
        }

        static void base() {
            String[] message = {
                    "1\topen shop",
                    "2\tsell ammunition",
                    "3\tsell by id"
            };

            Consumer<KeyType> fromKey = (k) -> {
                switch (k) {
                    case ArrowUp -> Main.Console.interaction("w");
                    case ArrowDown -> Main.Console.interaction("s");
                    case ArrowLeft -> Main.Console.interaction("a");
                    case ArrowRight -> Main.Console.interaction("d");
                    case Escape -> run = false;
                }
            };
            Consumer<Character> fromChar = (k) -> {
                switch (k) {
                    case '1' -> Base.shop();
                    case '2' -> Base.sell();
                    case '3' -> Base.sellById();
                    case 'w', 's', 'a', 'd' -> player.move(k);
                    case 'q' -> Console.playerMenu();
                }
                monitor.print();
            };
            exFromKeyBoard(message, fromKey, fromChar);
        }

        public static void interaction(String dir) {
            Coor coor = new Coor(player.coor);
            if (!Tools.getDirection(coor, dir))
                return;
            if (actualRoom.getObject(coor.X, coor.Y) instanceof Entity entity) {
                entity.isBusy = true;
                String[] message = {
                        "1\t\tshow stats",
                        "space\tattack enemy"
//                        "esc\texit"
                };
                Consumer<KeyType> fromKey = (k) -> {
                    switch (k) {
                        case Escape -> entity.isBusy = false;
                    }
                };
                Consumer<Character> fromChar = (k) -> {
                    switch (k) {
                        case ' ' -> {
                            Console.attack(entity);
                            return;
                        }
                        case '1' -> {
                            entity.isBusy = false;
                            entity.show();
                        }
                        case 'w', 's', 'a', 'd' -> {
                            entity.isBusy = false;
                            player.move(k);
                        }

                    }
                    monitor.print();
                };
                exFromKeyBoard(message, fromKey, fromChar);
            } else if (actualRoom.getObject(coor.X, coor.Y) instanceof Loot loot) {
                String[] message = {
                        "1\t\tshow loot",
                        "space\ttake",
//                        "esc\texit"
                };
                Consumer<KeyType> fromKey = (k) -> {
//                    switch (k) {
//                        case Escape -> entity.isBusy = false;
//                    }
                };
                Consumer<Character> fromChar = (k) -> {
                    switch (k) {
                        case '1' -> actualRoom.getObject(coor.X, coor.Y).show();
                        case ' ' -> Console.take(loot);
                    }
                };
                exFromKeyBoard(message, fromKey, fromChar);
            } else
                actualRoom.add("There are no special");
        }

        static void playerMenu() {
            String[] message = {
                    "1\tshow stats",
                    "2\tinventory"
            };
            Consumer<KeyType> fromKey = (k) -> {
            };
            Consumer<Character> fromChar = (k) -> {
                switch (k) {
                    case '1' -> showPlayer();
                    case '2' -> {
                        player.showInventory();
//                        if (action[0].equals("e") && action.length == 2) {
//                            Console.equip(action[1]);
//                        } else
//                            Console.standard(action);
                    }
                }
                actualRoom.print();
            };
            exFromKeyBoard(message, fromKey, fromChar);


        }

        static void generate(String[] str) {
            switch (str[1]) {
                case "en":
                case "enemy":
                    Enemy.generate(actualRoom);
                case "tr":
                case "tree":
                    Tree.generate(actualRoom);
                    break;
                default:
                    actualRoom.add("Unknown command");
            }
        }

        public static void attack(Entity entity) {
            mod = ConsoleMod.BATTLE;
            player.enemy = entity;
            player.isBusy = true;
            if (entity instanceof Enemy enemy)
                enemy.enemy = player;
            Battle.next();
        }

        static void showPlayer() {
            player.show();
            Tools.printLine();
            player.weapon.show();
            Tools.printLine();
            player.armor.show();
        }

        static void take(Loot loot) {
//            Coor coor = new Coor(player.coor.X, player.coor.Y);
//            if (str.length != 2)
//                return;
//            if (!getDirection(coor, str[1]))
//                return;
//            if (!(Screen.getObject(coor.X, coor.Y) instanceof Loot loot))
//                return;
            for (Item item : loot.loot) {
                if (!player.getItem(item)) {
                    actualRoom.add("Not enough space in inventory");
                    return;
                }
                actualRoom.add("You've put " + item.name);
            }
            actualRoom.setObject(null, loot.coor);
        }

        static void equip(String str) {
//            if (str.length != 2)
//                return;
            Item tempItem = player.inventory.get(Integer.parseInt(str));

            if (!player.equipItem(Integer.parseInt(str))) {
                actualRoom.add("Failed to equip");
                return;
            }
            actualRoom.add(String.format("You've equiped %s", tempItem.name));
        }

        static void calibrate() {
            Properties.screenSize = 0;
            do {
                System.out.print(++Properties.screenSize);

            } while (!in.nextLine().equals("stop"));
            Properties.screenSize--;
        }

        static void exFromKeyBoard(String[] message, Consumer<KeyType> fromKey, Consumer<Character> fromChar) {
            for (String str : message) {
                actualRoom.add(str);
            }
            actualRoom.print();

            exFromKeyBoard(fromKey, fromChar);
        }

        static void exFromKeyBoard(Consumer<KeyType> fromKey, Consumer<Character> fromChar) {
            KeyStroke keyPressed = null;
            try {
                while (keyPressed == null)
                    keyPressed = monitor.terminal.pollInput();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (keyPressed.isCtrlDown()) {
                System.out.println("Debug");
                switch (keyPressed.getKeyType()) {
                    case ArrowUp -> subDisplay.leas(-1);
                    case ArrowDown -> subDisplay.leas(1);
                }
                monitor.print();
                return;
            }

            if (keyPressed.getKeyType() == KeyType.Character) {
                fromChar.accept(keyPressed.getCharacter());
            } else {
                fromKey.accept(keyPressed.getKeyType());
            }


        }
    }

    static class Battle {
        static void next() {
            if (player.enemy instanceof Enemy enemy) {
                player.attack();
                if (checkBattle())
                    return;
                enemy.attack();
                if (checkBattle())
                    return;
                if (Properties.doCheckStats)
                    checkStats(enemy);
            } else {
                player.attack();
                checkBattle();
            }
        }

        static public void checkStats(Enemy enemy) {
            player.show();
            Tools.printLine();
            enemy.show();
        }

        static public boolean checkBattle() {
            if (!(player.enemy.isAlive && player.isAlive)) {
                StringBuilder strToSub = new StringBuilder("Бой окончен, победил ");
                if (player.isAlive) {
                    player.isBusy = false;
                    strToSub.append(player.name);
                    player.getExperience(player.enemy.xp);
                    player.enemy.kill();
                    player.enemy = null;
                    Console.mod = ConsoleMod.STANDARD;
                } else if (player.enemy.isAlive) {
                    strToSub.append(player.enemy.name);
                    if (player.enemy instanceof Enemy enemy)
                        enemy.enemy = null;
                    player.kill();
                    Console.mod = ConsoleMod.DEATH;
                } else {
                    strToSub.append("никто");
                    if (player.enemy instanceof Enemy enemy)
                        enemy.enemy = null;
                    player.enemy = null;
                    Console.mod = ConsoleMod.DEATH;
                }
                actualRoom.add(strToSub.toString());
                actualRoom.print();
                Console.mod = ConsoleMod.STANDARD;
                return true;
            }
            return false;
        }

    }

    static class Base {
        static final int chanceAmmunition = 10;

        static void shop() {
            int countProducts = Math.min(player.lvl + 2, 10);
            Item[] shop = new Item[countProducts];
            shop[0] = genProduct();
            shop[0].show(0);
            for (int i = 1; i < countProducts; i++) {
                Tools.printLine();
                shop[i] = genProduct();
                shop[i].show(i);
            }
            actualRoom.print();


            KeyStroke keyPressed = null;
            try {
                while (keyPressed == null)
                    keyPressed = monitor.terminal.pollInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (keyPressed.getKeyType() != KeyType.Character)
                return;
            int id = keyPressed.getCharacter();
            if (!(id >= '0' && id <= '9'))
                return;
            id -= '0';
            if (id >= shop.length)
                return;
            if (!player.removeMoney(shop[id].cost)) {
                actualRoom.add("You don't have enough money");
                return;
            }
            if (!player.getItem(shop[id])) {
                player.addMoney(shop[id].cost);
                actualRoom.add("You don't have space in inventory");
                return;
            }
            actualRoom.add("You've bought %s", shop[id].name);
            Tools.printLine();
        }

        static Item genProduct() {
            if (rnd.nextInt(100) < chanceAmmunition)
                return rnd.nextBoolean() ? new Weapon() : new Armor();
            else
                return HealPotion.getRandom();
        }

        static void sell() {
            Iterator<Item> iter = player.inventory.iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item instanceof Weapon || item instanceof Armor) {
                    Item item_ = item;
                    if (!player.sellItem(item, iter)) {
                        actualRoom.add("Wrong iteration");
                        continue;
                    }
                    actualRoom.add("You've sell %s", item_.name);
                }
            }
            Tools.printLine();
        }

        static void sellById() {
            player.showInventory();
            actualRoom.print();
            KeyStroke keyPressed = null;
            try {
                while (keyPressed == null)
                    keyPressed = monitor.terminal.pollInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (keyPressed.getKeyType() != KeyType.Character)
                return;
            int id = keyPressed.getCharacter();
            if (!(id >= '0' && id <= '9'))
                return;
            id -= '0';
            if (id >= player.inventory.size())
                return;


            Item item_ = player.inventory.get(id);
            if (!player.sellItem(player.inventory.get(id))) {
                actualRoom.add("Wrong id");
                return;
            }
            actualRoom.add("You've sell %s", item_.name);
            Tools.printLine();
        }
    }

    public static class LibraryOfMaps {
        public static SavedMap standart = new SavedMap(new String[]{
                "WWWWWWWWWWWWWWW",
                "W#............W",
                "8.............W",
                "W.............W",
                "W............WWWWW",
                "W................W",
                "W.....&..........WWWWWWWWWWWWW",
                "WWWWWW8WWWW..................W",
                "          W..................W",
                "          WWWWWW...%E%.......8",
                "               W..%%.%%......W",
                "               W..E...E......W",
                "               W..%%.%%......W",
                "               W...%E%.......W",
                "               WWWWWWWWWWWWWWW"
        },
                new int[]{5, 15, 7},
                new Coor(0, 2));
        public static SavedMap forest = new SavedMap(new String[]{
                "WWWWWWWWWWWWWWW",
                "W.............W",
                "W.............W",
                "W.............WWWWWWW",
                "W..................WWWWW",
                "W......................WW",
                "W.......................WWWWWW",
                "WWWW.........................W",
                "  WW......................%%%W",
                "   WW..............%%........8",
                "    W.............%%.%%...%%%W",
                "    W........................W",
                "    WWWWWWWWWWWW..%%.%%......W",
                "               W...%%........W",
                "               WWWWWWWWWWWWWWW"},
                new int[]{0, 30, 7},
                new Coor(29, 10));


        static ArrayList<Consumer<Room>> generators = new ArrayList<>(5);

        static {
            generators.add(Enemy::generate);
            generators.add(Tree::generate);
            generators.add(Rock::generate);
        }

        public record SavedMap(String[] StringMap, int[] params, Coor coorFrstDoor) {
        }
    }

    public enum ConsoleMod {
        STANDARD,
        BATTLE,
        BASE,
        DEATH
    }

    enum Find {
        WIDTH,
        HEIGHT
    }


}









