package ServerPackage;

//import static ServerPackage.Zombieworld.append;
import java.awt.Color;
import java.util.ArrayList;

/*
 * Player {...} class.
 * You need to have a player in the game in order
 * to play.
 */
public class Player {

    ArrayList<String> drawText;
    int hp, stamina, defense, height;
    boolean combat = false;
    Room location;
    String name;
    ArrayList<Item> inventory;
    Item.Armor head;
    Item.Armor chest;
    Item.Armor legs;
    Item.Armor hands;
    Item.Armor shoulders;
    Item.Armor shoes;
    Item.Armor arms;
    Item.Armor back;


    /*
     * Player(String name, Room start){...}
     * This constructor constructs a player character
     * whose name is the 'name' parameter and the
     * player's starting room is the 'start' parameter.
     */
    public Player(String name, int start) {
        this.drawText = new ArrayList<String>();
        this.hp = 100;
        this.stamina = 100;
        this.defense = 0;
        this.height = 0;
        this.location = ZombieworldServer.totalRooms.get(start);
        ZombieworldServer.totalRooms.get(start).players.add(this);
        this.name = name;
        this.inventory = new ArrayList<Item>();
        this.head = null;
        this.chest = null;
        this.legs = null;
        this.hands = null;
        this.shoulders = null;
        this.shoes = null;
        this.arms = null;
        this.back = null;
    }

    /*
     * Player(String name, Room start){...}
     * This constructor constructs a player character
     * whose name is the 'name' parameter and the
     * player's starting room is the 'start' parameter.
     */
    public Player(String name, int hp, int stam, int def, int height, int loc, ArrayList<Item> inv, Item.Armor head, Item.Armor chest, Item.Armor legs, Item.Armor hands, Item.Armor shoulders, Item.Armor shoes, Item.Armor arms, Item.Armor back) {
        this.drawText = new ArrayList<String>();
        this.hp = hp;
        this.stamina = stam;
        this.defense = def;
        this.height = height;
        this.location = ZombieworldServer.totalRooms.get(loc);
        ZombieworldServer.totalRooms.get(loc).players.add(this);
        this.name = name;
        this.inventory = inv;
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.hands = hands;
        this.shoulders = shoulders;
        this.shoes = shoes;
        this.arms = arms;
        this.back = back;
    }

    /*
     * append(String input){...}
     * This method is used as a means of communication
     * between the player and the thread controlling the
     * player.
     */
    public void append(String input) {
        this.drawText.add(input);
    }

    /*
     * append(String input, Color color){...}
     * This method is used as a means of communication
     * between the player and the thread controlling the
     * player.
     */
    public void append(String input, Color color) {
        String temp = input;
        if (color.equals(Color.MAGENTA)) {
            temp = temp + ",Color.MAGENTA";
        } else if (color.equals(Color.BLACK)) {
            temp = temp + ",Color.BLACK";
        } else if (color.equals(Color.CYAN)) {
            temp = temp + ",Color.CYAN";
        } else if (color.equals(Color.RED)) {
            temp = temp + ",Color.RED";
        } else if (color.equals(Color.WHITE)) {
            temp = temp + ",Color.WHITE";
        }
        this.drawText.add(temp);
    }

    public void notify(String input) {
        for (int i = 0; i < this.location.players.size(); i++) {
            if (!this.location.players.get(i).equals(this)) {
                this.location.players.get(i).drawText.add(input);
            }
        }
    }

    /*
     * move(String string){...}
     * This move method interprets the input from
     * the player in the console and passes the
     * input on to move(int direc){...} as an int.
     */
    public void move(String string) {
        if (!combat) {
            String input = string.substring((string.indexOf(" ") + 1), string.length());
            if (input.length() > 4) {
                input = input.substring(0, 4);
            }
            this.move(ZombieworldServer.movable.indexOf(input) % 6);
        } else {
            append("You are in combat! You cannot move at this time.\n");
        }
    }

    /*
     * move(int direc){...}
     * This move method moves the player in the
     * specified direction given by the previous
     * move(String string){...} method.
     */
    public void move(int direc) {
        if (this.stamina <= 5) {
            append("You are too tired to move!\n");
            return;
        }
        this.stamina = this.stamina - 5;
        int go = direc % 7;
        if (go < 3) {
            if (go == -1) {
                append("That is not a legal direction!\n");
            }
            if (go == 0) {
                if (this.location.north != null) {
                    append("Your character moved north.\n");
                    notify(this.name + " moved north.\n");
                    this.location.north.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.north;
                } else {
                    append("Your character cannot move north!\n");
                }
            }
            if (go == 1) {
                if (this.location.east != null) {
                    append("Your character moved east.\n");
                    notify(this.name + " moved east.\n");
                    this.location.east.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.east;
                } else {
                    append("Your character cannot move east!\n");
                }
            }
            if (go == 2) {
                if (this.location.south != null) {
                    append("Your character moved south.\n");
                    notify(this.name + " moved south.\n");
                    this.location.south.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.south;
                } else {
                    append("Your character cannot move south!\n");
                }
            }
        }
        if (go > 2) {
            if (go == 3) {
                if (this.location.west != null) {
                    append("Your character moved west.\n");
                    notify(this.name + " moved west.\n");
                    this.location.west.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.west;
                } else {
                    append("Your character cannot move west!\n");
                }
            }

            if (go == 4) {
                if (this.location.up != null) {
                    append("Your character moved up.\n");
                    notify(this.name + " moved up.\n");
                    this.location.up.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.up;
                    this.height = this.height + 1;
                } else {
                    append("Your character cannot go up!\n");
                }
            }
            if (go == 5) {
                if (this.location.down != null) {
                    append("Your character moved down.\n");
                    notify(this.name + " moved down.\n");
                    this.location.down.players.add(this);
                    this.location.players.remove(this);
                    this.location = this.location.down;
                    this.height = this.height - 1;
                } else {
                    append("Your character cannot go down!\n");
                }
            }
        }
        this.mapOut(this.location);
    }

    /*
     * flee(String string){...}
     * This method takes in a string and hands it to the
     * flee(int direc) method to move the player into a
     * new area whilst in combat.
     */ /*
    public void flee(String string) {
        String input = string.toUpperCase();
        if ((input.contains("FLEE ") && input.length() > 5) || (input.contains("F ") && input.length() > 2)) {
            input = input.substring(input.indexOf(" ") + 1);
            this.flee(ZombieworldServer.movable.indexOf(input));
        } else if (input.equals("FLEE") || input.equals("F")) {
            this.flee((int) Math.round(Math.random() * 6));
        }
    }*/

 /*
     * flee(int direc){...}
     * This method moves the player in the specified
     * direction, however, only usable while in combat
     * and all zombies get a free strike.
     */ /*
    public void flee(int direc) {
        if (!combat) {
            append("You are not in combat.\n");
            return;
        }
        if (combat) {
            if (player1.location.enemies.size() > 0 && player1.location.enemies.get(0).hp > 0) {
                for (int i = 0; i < player1.location.enemies.size(); i++) {
                    this.location.enemies.get(i).strike(this);
                }
            }
            if (this.stamina <= 5) {
                append("You are too tired to move!\n");
                return;
            }
            this.stamina = this.stamina - 5;
            int go = direc % 7;
            if (go < 3) {
                if (go == -1) {
                    append("That is not a legal direction!\n");
                }
                if (go == 0) {
                    if (this.location.north != null) {
                        append("Your character fled north.\n");
                        this.location.north.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.north;
                    } else {
                        append("Your character cannot move north!\n");
                    }
                }
                if (go == 1) {
                    if (this.location.east != null) {
                        append("Your character fled east.\n");
                        this.location.east.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.east;
                    } else {
                        append("Your character cannot move east!\n");
                    }
                }
                if (go == 2) {
                    if (this.location.south != null) {
                        append("Your character fled south.\n");
                        this.location.south.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.south;
                    } else {
                        append("Your character cannot move south!\n");
                    }
                }
            }
            if (go > 2) {
                if (go == 3) {
                    if (this.location.west != null) {
                        append("Your character fled west.\n");
                        this.location.west.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.west;
                    } else {
                        append("Your character cannot move west!\n");
                    }
                }

                if (go == 4) {
                    if (this.location.down != null) {
                        append("Your character fled up.\n");
                        this.location.down.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.down;
                        this.height = this.height + 1;
                    } else {
                        append("Your character cannot go up!\n");
                    }
                }
                if (go == 5) {
                    if (this.location.up != null) {
                        append("Your character fled down.\n");
                        this.location.up.players.add(this);
                        this.location.players.remove(this);
                        this.location = this.location.up;
                        this.height = this.height - 1;
                    } else {
                        append("Your character cannot go down!\n");
                    }
                }
            }
            this.look(6);
        }
        if (this.location.players.size() > 0 && this.location.enemies.size() > 0) {
            append("As you flee into the other room, a zombie in there notices you!\n");
            combat = true;
        } else {
            append("You have successfully fled... for now.\n");
            combat = false;
        }
    }*/

 /*
     * look(){...}
     * The player looks in the current room and
     * text displays the current area and adjacent
     * rooms along with possible exits.
     */
    public void look(String string) {
        String input = string.toUpperCase();
        if (input.contains(" ")) {
            input = input.substring(input.indexOf(" ") + 1);
            if (input.length() > 4) {
                input = input.substring(0, 4);
            }
        }
        if (input.equals("LOOK") || input.equals("L")) {
            this.look(6);
        } else {
            this.look(ZombieworldServer.movable.indexOf(input) % 6);
        }
    }

    /*
     * look(int direc){...}
     * This method looks in the specified direction. If
     * look(String string) was called and not given a
     * valid direction, look(int direc) is called on the
     * current room the player is located.
     */
    public void look(int direc) {
        int go = direc;
        go = go % 7;
        if (go < 3) {
            if (go == -1) {
                append("That is not a valid direction to look in.\n");
            }
            if (go == 0) {
                append("Your character looked north.\n");
                mapOut(this.location.north);
            }
            if (go == 1) {
                append("Your character looked east.\n");
                mapOut(this.location.east);
            }
            if (go == 2) {
                append("Your character looked south.\n");
                mapOut(this.location.south);
            }
        } else {
            if (go == 3) {
                append("Your character looked west.\n");
                mapOut(this.location.west);
            }
            if (go == 4) {
                append("Your character looked up.\n");
                mapOut(this.location.up);
            }
            if (go == 5) {
                append("Your character looked down.\n");
                mapOut(this.location.down);
            }
            if (go == 6) {
                append("You examine your surroundings.\n");
                mapOut(this.location);
            }
        }
    }

    /*
     * mapOut(Room room){...}
     * This method prints out the surrounding area
     * as well as the current room. Only adds [ ]
     * when there exists a room. Also prints out
     * the list of exits.
     */
    public void mapOut(Room room) {
        if (room == null) {
            append("You can't see any more that way.\n");
            return;
        }
        String exits = "You see an exit to the: ";
        append("------------------------\n");
        append(("P: " + (room.players.size() / 10) + (room.players.size() % 10)));
        append("H", Color.BLACK);
        if (room.north != null) {
            if (room.north.north != null) {
                append("[");
                append("X", Color.BLACK);
                append("]");
            } else {
                append("[X]", Color.BLACK);
            }
        } else {
            append("[X]", Color.BLACK);
        }
        append("Z_", Color.BLACK);
        append("I:  " + (room.items.size() / 10) + (room.items.size() % 10) + "\n");
        append("Z: " + (room.enemies.size() / 10) + (room.enemies.size() % 10) + "   ");
        if (room.north != null) {
            exits = exits + "north";
            append("[");
            if (room.north.players.contains(this)) {
                append("X", Color.MAGENTA);
            } else {
                append("X", Color.BLACK);
            }
            append("]");
        } else {
            append("[X]", Color.BLACK);
        }
        append("    R: " + (room.resources / 10) + (room.resources % 10) + "\n");
        append(")", Color.BLACK);
        exits = exits + ", ";
        if (room.west != null) {
            if (room.west.west != null) {
                append("[");
                append("X", Color.BLACK);
                append("]");
            } else {
                append("[X]", Color.BLACK);
            }
            exits = exits + "west";
            append("[");
            if (room.west.players.contains(this)) {
                append("X", Color.MAGENTA);
            } else {
                append("X", Color.BLACK);
            }
            append("]");
        } else {
            append("[X][X]", Color.BLACK);
        }
        exits = exits + ", ";
        append("[");
        if (room.players.contains(this)) {
            append("X", Color.MAGENTA);
        } else {
            append("X", Color.BLACK);
        }
        append("]");
        if (room.east != null) {
            exits = exits + "east";
            append("[");
            if (room.east.players.contains(this)) {
                append("X", Color.MAGENTA);
            } else {
                append("X", Color.BLACK);
            }
            append("]");
            if (room.east.east != null) {
                append("[");
                append("X", Color.BLACK);
                append("]");
            } else {
                append("[X]", Color.BLACK);
            }
        }
        exits = exits + ", ";
        append("\nH:");
        int tempHeight = this.height;
        if (room.down != null && room.down.players.size() > 0) {
            tempHeight = tempHeight + 1;
        } else if (room.up != null && room.up.players.size() > 0) {
            tempHeight = tempHeight - 1;
        }
        if (tempHeight < 0) {
            append("-");
        } else {
            append(" ");
        }
        append("" + (tempHeight / 10) + Math.abs(tempHeight % 10));
        append("))", Color.BLACK);
        if (room.south != null) {
            exits = exits + "south";
            append("[");
            if (room.south.players.contains(this)) {
                append("X", Color.MAGENTA);
            } else {
                append("X", Color.BLACK);
            }
            append("]");
        } else {
            append("[X]", Color.BLACK);
        }
        append("    E: " + (room.exits / 10) + (room.exits % 10) + "\n");
        if (room.south != null) {
            if (room.south.south != null) {
                append("Z))", Color.BLACK);
                append("       [");
                append("X", Color.BLACK);
                append("]");
            } else {
                append("Z))", Color.BLACK);
                append("       [X]", Color.BLACK);
            }
        }
        exits = exits + ". You see a way: ";
        if (room.up != null) {
            exits = exits + "up";
        }
        exits = exits + ", ";
        if (room.down != null) {
            exits = exits + "down";
        }
        exits = exits + ".";
        while (exits.contains(" ,")) {
            int i = exits.indexOf(" ,");
            exits = exits.substring(0, i) + exits.substring(i + 2, exits.length());
        }
        while (exits.contains(", .")) {
            exits = exits.substring(0, exits.indexOf(", .")) + exits.substring(exits.indexOf(", .") + 2);
        }
        if (exits.substring(0, exits.indexOf(".")).length() < 26) {
            exits = exits.substring(26);
        }
        if (!exits.contains("up") && !exits.contains("down")) {
            if (exits.length() >= 26) {
                exits = exits.substring(0, exits.indexOf(".") + 1);
            } else {
                exits = exits.substring(exits.indexOf(".") + 1);
            }
        }
        append("\n------------------------\n");
        if (exits.length() > 0) {
            append(exits + "\n");
        } else {
            append("There is nowhere to go!\n");
        }
        String enemList = "";
        if (room.enemies.size() > 0) {
            enemList = "You see:\n" + room.enemies.size() + " Zombie";
            if (room.enemies.size() > 1) {
                enemList = enemList + "s";
            }
            enemList = enemList + " here.";
        }
        String itemList = "";
        boolean ground = false;
        if (room.items.size() > 0) {
            ground = true;
            if (enemList.equals("")) {
                itemList = "You see:\n";
            }
            ArrayList<String> temp = new ArrayList<String>();
            for (int i = 0; i < this.location.items.size(); i++) {
                temp.add(this.location.items.get(i).name);
            }
            while (temp.size() > 0) {
                int num = 0;
                String check = temp.get(0);
                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).equals(check)) {
                        temp.remove(i);
                        num++;
                        i--;
                    }
                }
                itemList = itemList + (num + "x " + check + "\n");
            }
        }
        String playerList = "";
        if (room.players.size() > 0) {
            for (int i = 0; i < room.players.size(); i++) {
                playerList = playerList + room.players.get(i).name + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            if (room.players.size() > 1) {
                playerList = playerList + " are here.\n";
            } else if (room.players.size() == 1) {
                if (!room.players.contains(this)) {
                    playerList = playerList + " is here.\n";
                } else {
                    playerList = "";
                }
            }
        }
        if (!enemList.equals("")) {
            append(enemList + "\n");
        }
        if (ground) {
            itemList = itemList.substring(0, itemList.length() - 1);
            append(itemList + "\n");
        }
        if (!playerList.equals("")) {
            append(playerList + "\n");
        }
    }

    /*
     * explore(){...}
     * The player explores the current room in an
     * attempt to discover additional areas. Once
     * a room has been explored, it cannot be
     * explored again.
     */
    public void explore() {
        int num = (int) Math.round(Math.random() * 6);
        while (num > 0) {
            int direc = (int) Math.round(Math.random() * 6);
            num--;
        }
    }

    /*
     * stats(){...}
     * This method displays the player's status in
     * terms of health, stamina, and other necessary
     * statistics.
     */
    public void stats() {
        append("------------------------\nName: " + this.name + "    Age: 00\nCondition: ");
        if (this.hp >= 75) {
            append("Healthy");
        } else if (this.hp < 75 && this.hp >= 50) {
            append("Injured");
        } else if (this.hp < 50 && this.hp >= 25) {
            append("Wounded");
        } else if (this.hp < 25 && this.hp >= 10) {
            append("Dying");
        } else if (this.hp < 10) {
            append("In Critical Condition");
        }
        append(" and ");
        if (this.stamina >= 75) {
            append("Rested");
        } else if (this.stamina < 75 && this.stamina >= 50) {
            append("Tired");
        } else if (this.stamina < 50 && this.stamina >= 25) {
            append("Fatigued");
        } else if (this.stamina < 25 && this.stamina >= 5) {
            append("Exhausted");
        } else if (this.stamina < 5) {
            append("Unable to move.");
        }
        append("\n------------------------\n");
    }

    /*
     * cond(){...}
     * Same as stats() except this method only shows
     * the player's health and stamina conditions.
     */
    public void cond() {
        append("You are ");
        if (this.hp >= 75) {
            append("Healthy");
        } else if (this.hp < 75 && this.hp >= 50) {
            append("Injured");
        } else if (this.hp < 50 && this.hp >= 25) {
            append("Wounded");
        } else if (this.hp < 25 && this.hp >= 10) {
            append("Dying");
        } else if (this.hp < 10) {
            append("Critical Condition");
        }
        append(" and ");
        if (this.stamina >= 75) {
            append("Rested");
        } else if (this.stamina < 75 && this.stamina >= 50) {
            append("Tired");
        } else if (this.stamina < 50 && this.stamina >= 25) {
            append("Fatigued");
        } else if (this.stamina < 25 && this.stamina >= 5) {
            append("Exhausted");
        } else if (this.stamina < 5) {
            append("Unable to move");
        }
        append(".\n");
    }

    /*
     * get(String name){...}
     * This method checks whether the item exists at
     * all and whether it's on the floor or on a corpse
     * then sends it to the get(Item item) method.
     */
    public void get(String name) {
        String check = name.toUpperCase();
        if (check.contains("PICK UP")) {
            check = check.substring(check.indexOf(" ") + 1);
        }
        if (check.contains(" ")) {
            check = check.substring(check.indexOf(" ") + 1);
        }
        if (this.inventory.size() < 18) {
            for (int i = 0; i < this.location.items.size(); i++) {
                if (this.location.items.get(i).name.toUpperCase().contains(check)) {
                    this.getI(this.location.items.get(i));
                    return;
                }
            }
        } else {
            append("You can't carry anymore!\n");
            return;
        }
        append("You see no such item.\n");
    }

    /*
     * get(Item item){...}
     * This method picks up the item off of the floor or
     * corpse and adds it to the player's inventory.
     */
    public void getI(Item item) {
        item.location.items.remove(item);
        item.location = null;
        append("You pick up " + item.name + ".\n");
        this.inventory.add(item);
    }

    /*
     * drop(String name){...}
     * This method checks whether the items exists at
     * all in the player's inventory then sends it to the
     * drop(Item item) method.
     */
    public void drop(String name) {
        String check = name.toUpperCase();
        if (check.contains(" ")) {
            check = check.substring(check.indexOf(" ") + 1);
        }
        if (this.inventory.size() > 0) {
            for (int i = 0; i < this.inventory.size(); i++) {
                if (this.inventory.get(i).name.toUpperCase().contains(check)) {
                    this.dropI(this.inventory.get(i));
                    return;
                }
            }
        } else {
            append("You have nothing in your pack.\n");
            return;
        }
        append("You see no such item in your pack.\n");
    }

    /*
     * drop(Item item){...}
     * This method drops the item from your inventory
     * onto the floor.
     */
    public void dropI(Item item) {
        this.inventory.remove(item);
        this.location.items.add(item);
        item.location = this.location;
        append("You drop " + item.name + ".\n");
    }

    /*
     * inventory(){...}
     * This method displays all of the contents of the
     * player's inventory.
     */
    public void inventory() {
        if (this.inventory.size() < 1) {
            append("Your pack contains nothing.\n");
            return;
        }
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < this.inventory.size(); i++) {
            temp.add(this.inventory.get(i).name);
        }
        while (temp.size() > 0) {
            int num = 0;
            String check = temp.get(0);
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).equals(check)) {
                    temp.remove(i);
                    num++;
                    i--;
                }
            }
            append(num + "x " + check + "\n");
        }
    }

    /*
     * equip(String string){...}
     * This method checks whether the player has the
     * specific item asked for and hands it to the
     * equip(Item item) method.
     */
    public void equip(String name) {
        String check = name.toUpperCase();
        if (check.contains(" ")) {
            check = check.substring(check.indexOf(" ") + 1);
        }
        if (this.inventory.size() > 0) {
            for (int i = 0; i < this.inventory.size(); i++) {
                if (this.inventory.get(i).name.toUpperCase().contains(check)) {
                    if (this.inventory.get(i) instanceof Item.Armor) {
                        this.equipI((Item.Armor) this.inventory.get(i));
                    } else {
                        append("You cannot equip that.\n");
                    }
                    return;
                }
            }
        } else {
            append("You have nothing in your pack.\n");
            return;
        }
        append("You see no such item in your pack.\n");
    }

    /*
     * equip(Item.Armor item){...}
     * This method adds the to the player's equipped
     * slot. You cannot wear multiple armor in the
     * same slot, however.
     */
    public void equipI(Item.Armor item) {
        if (item.slot.toUpperCase().equals("HEAD")) {
            if (this.head == null) {
                this.head = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("CHEST")) {
            if (this.chest == null) {
                this.chest = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("LEGS")) {
            if (this.legs == null) {
                this.legs = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("HANDS")) {
            if (this.hands == null) {
                this.hands = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("SHOULDERS")) {
            if (this.shoulders == null) {
                this.shoulders = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("SHOES")) {
            if (this.shoes == null) {
                this.shoes = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("ARMS")) {
            if (this.arms == null) {
                this.arms = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        } else if (item.slot.toUpperCase().equals("BACK")) {
            if (this.back == null) {
                this.back = item;
            } else {
                append("You are already wearing something there.\n");
                return;
            }
        }
        this.defense = this.defense + item.defense;
        this.inventory.remove(item);
        append("You equipped " + item.name + ".\n");
    }

    /*
     * unequip(String name){...}
     * This method checks whether or not the player
     * is wearing said item at all and then hands it
     * to the unequip(Item.Armor item) method if the
     * player is wearing it.
     */
    public void unequip(String name) {
        String check = name.toUpperCase();
        if (check.contains(" ")) {
            check = check.substring(check.indexOf(" ") + 1);
        }
        if (this.head != null && this.head.name.toUpperCase().contains(check)) {
            this.unequipI(this.head);
        } else if (this.chest != null && this.chest.name.toUpperCase().contains(check)) {
            this.unequipI(this.chest);
        } else if (this.legs != null && this.legs.name.toUpperCase().contains(check)) {
            this.unequipI(this.legs);
        } else if (this.hands != null && this.hands.name.toUpperCase().contains(check)) {
            this.unequipI(this.hands);
        } else if (this.shoulders != null && this.shoulders.name.toUpperCase().contains(check)) {
            this.unequipI(this.shoulders);
        } else if (this.shoes != null && this.shoes.name.toUpperCase().contains(check)) {
            this.unequipI(this.shoes);
        } else if (this.arms != null && this.arms.name.toUpperCase().contains(check)) {
            this.unequipI(this.arms);
        } else if (this.back != null && this.back.name.toUpperCase().contains(check)) {
            this.unequipI(this.back);
        } else {
            append("You are not wearing anything like that.\n");
        }
    }

    /*
     * unequip(Item.Armor item){...}
     * This method removes the piece of armor from
     * the player and adds it back to their inv-
     * entory.
     */
    public void unequipI(Item.Armor item) {
        if (item.slot.equals("HEAD")) {
            if (this.head != null) {
                this.head = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("CHEST")) {
            if (this.chest != null) {
                this.chest = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("LEGS")) {
            if (this.legs != null) {
                this.legs = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("HANDS")) {
            if (this.hands != null) {
                this.hands = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("SHOULDERS")) {
            if (this.shoulders != null) {
                this.shoulders = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("SHOES")) {
            if (this.shoes != null) {
                this.shoes = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("ARMS")) {
            if (this.arms != null) {
                this.arms = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        } else if (item.slot.equals("BACK")) {
            if (this.back != null) {
                this.back = null;
            } else {
                append("You are not wearing anything there.\n");
                return;
            }
        }
        this.defense = this.defense - item.defense;
        this.inventory.add(item);
        append("You took off " + item.name + ".\n");
    }

    /*
     * equiment(){...}
     * This method displays what equipment the player
     * is currently wearing.
     */
    public void equipment() {
        append("You are currently wearing:\nHead: ");
        if (this.head != null) {
            append(this.head.name);
        } else {
            append("Nothing.");
        }
        append("\nChest: ");
        if (this.chest != null) {
            append(this.chest.name);
        } else {
            append("Nothing.");
        }
        append("\nLegs: ");
        if (this.legs != null) {
            append(this.legs.name);
        } else {
            append("Nothing.");
        }
        append("\nHands: ");
        if (this.hands != null) {
            append(this.hands.name);
        } else {
            append("Nothing.");
        }
        append("\nShoulders: ");
        if (this.shoulders != null) {
            append(this.shoulders.name);
        } else {
            append("Nothing.");
        }
        append("\nShoes: ");
        if (this.shoes != null) {
            append(this.shoes.name);
        } else {
            append("Nothing.");
        }
        append("\nArms: ");
        if (this.arms != null) {
            append(this.arms.name);
        } else {
            append("Nothing.");
        }
        append("\nBack: ");
        if (this.back != null) {
            append(this.back.name);
        } else {
            append("Nothing.");
        }
        append("\nYour defense is: " + this.defense + "\n");
    }
}
