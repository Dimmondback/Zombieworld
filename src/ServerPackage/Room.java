package ServerPackage;

import java.util.ArrayList;

/*
 * Room {...} class
 * You need to be able to have rooms/areas to walk
 * around in! 6 directions to move in: The four
 * cardinal directions and up and down.
 */
public class Room {

    Room north, east, south, west, up, down;
    int resources, exits;
    ArrayList<Item> items;
    ArrayList<Player> players;
    ArrayList<Enemy> enemies;
    //ArrayList<Npc> npcs;

    /*
     * Room(){...}
     * This constructor constructs a room with no
     * attached rooms and has not been explored. The
     * room also spawns with an available resource.
     */
    public Room() {
        north = null;
        east = null;
        south = null;
        west = null;
        up = null;
        down = null;
        resources = 0;
        exits = 0;
        items = new ArrayList<Item>();
        players = new ArrayList<Player>();
        enemies = new ArrayList<Enemy>();
        //npcs = new ArrayList<Npc>();
    }

    public Room changes(Room newNorth, Room newEast, Room newSouth, Room newWest, Room newUp, Room newDown) {
        north = newNorth;
        east = newEast;
        south = newSouth;
        west = newWest;
        up = newUp;
        down = newDown;
        if (newNorth != null) {
            exits = exits + 1;
        }
        if (newEast != null) {
            exits = exits + 1;
        }
        if (newSouth != null) {
            exits = exits + 1;
        }
        if (newWest != null) {
            exits = exits + 1;
        }
        if (newUp != null) {
            exits = exits + 1;
        }
        if(newDown != null) {
            exits = exits + 1;
        }
        return this;
    }
}