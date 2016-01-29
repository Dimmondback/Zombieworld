package ServerPackage;

import static ServerPackage.Zombieworld.append;
import java.util.ArrayList;

/*
 * Enemy {...} class
 * You need to have enemies to fight in the game.
 */
public class Enemy {

    int hp;
    int dmgMax;
    int dmgMin;
    Room location;
    String name;
    ArrayList<Item> loot;

    /*
     * Enemy(){...}
     * This constructor constructs an enemy in a
     * random room (cannot spawn in a safehouse).
     */
    public Enemy() {
        int rand = (int) (Math.random() * ZombieworldServer.totalRooms.size() + 1);
        this.name = "Zombie";
        this.hp = (int) Math.round(Math.random() * 9 + 1);
        int num = ZombieworldServer.totalRooms.get(rand).enemies.size();
        if (num > 0) {
            this.name = this.name + "(" + num + ")";
        }
        this.dmgMin = (int) Math.round(Math.random() * 5);
        this.dmgMax = (int) Math.round(Math.random() * 5 + this.dmgMin);
        this.location = ZombieworldServer.totalRooms.get(rand);
        this.loot = new ArrayList<Item>();
    }

    /*
     * Enemy(Room start){...}
     * This constructor constructs an enemy in a
     * room 'start' (cannot be a safehouse).
     */
    public Enemy(Room start) {
        this.name = "Zombie";
        this.hp = (int) Math.round(Math.random() * 9 + 1);
        int num = start.enemies.size();
        if (num > 0) {
            this.name = this.name + "(" + num + ")";
        }
        this.dmgMin = (int) Math.round(Math.random() * 5);
        this.dmgMax = (int) Math.round(Math.random() * 5 + this.dmgMin);
        this.location = start;
        this.loot = new ArrayList<Item>();
    }

    /*
     * Enemy(Item item){...}
     * This constructor constructs an enemy in a
     * random room (cannot spawn in a safehouse)
     * with the specified item in its loot-table.
     */
    public Enemy(Item item) {
        int rand = (int) (Math.random() * ZombieworldServer.totalRooms.size() + 1);
        this.name = "Zombie";
        this.hp = (int) Math.round(Math.random() * 9 + 1);
        int num = ZombieworldServer.totalRooms.get(rand).enemies.size();
        if (num > 0) {
            this.name = this.name + "(" + num + ")";
        }
        this.dmgMin = (int) Math.round(Math.random() * 5);
        this.dmgMax = (int) Math.round(Math.random() * 5 + this.dmgMin);
        this.location = ZombieworldServer.totalRooms.get(rand);
        this.loot = new ArrayList<Item>();
        this.loot.add(item);
    }

    /*
     * Enemy(Room start, Item item){...}
     * This constructor constructs an enemy in the
     * specified room (cannot be a safehouse) with
     * the specified item in its loot-table.
     */
    public Enemy(Room start, Item item) {
        this.name = "Zombie";
        this.hp = (int) Math.round(Math.random() * 9 + 1);
        int num = start.enemies.size();
        if (num > 0) {
            this.name = this.name + "(" + num + ")";
        }
        this.dmgMin = (int) Math.round(Math.random() * 5);
        this.dmgMax = (int) Math.round(Math.random() * 5 + this.dmgMin);
        this.location = start;
        this.loot = new ArrayList<Item>();
        this.loot.add(item);
    }

    /*
     * strike(){...}
     * This method allows the zombie to strike at
     * the player doing variable damage between a
     * minimum and maximum value.
     */
    public void strike(Player target) {
        double missChance = Math.random() * 100;
        if (missChance > 20) {
            int damage = (int) Math.round(Math.random() * this.dmgMax + this.dmgMin) - target.defense;
            if (damage > 0) {
                append(this.name + " strikes you for " + damage + " damage.\n");
                target.hp = target.hp - damage;
            } else {
                append(this.name + " struck you but did no damage.\n");
            }
        } else {
            append(this.name + " missed you.\n");
        }
    }
}
