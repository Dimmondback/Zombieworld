package ServerPackage;

    /*
 * Item {...} class
 * There needs to be items in the game to use.
 */

public class Item {

    String name;
    Room location;

    public Item() {
        this.name = "";
        this.location = null;
    }

    public Item(String newName) {
        this.name = newName;
        this.location = null;
    }

    public Item(String newName, Room putHere) {
        this.name = newName;
        this.location = putHere;
    }

    public Item(String newName, Player person) {
        this.name = newName;
        this.location = null;
        person.inventory.add(this);
    }

    class Armor extends Item {

        String slot;
        int defense;

        public Armor() {
            this.slot = "ANY";
            this.name = "";
            this.location = null;
            this.defense = 1;
        }

        public Armor(String newName) {
            this.slot = "ANY";
            this.name = newName;
            this.location = null;
            this.defense = 1;
        }

        public Armor(String newName, Room putHere) {
            this.slot = "ANY";
            this.name = newName;
            this.location = putHere;
            this.defense = 1;
        }

        public Armor(String newName, String equipHere) {
            this.name = newName;
            this.slot = equipHere;
            this.defense = 1;
        }

        public Armor(String newName, String equipHere, Room putHere) {
            this.name = newName;
            this.slot = equipHere;
            this.location = putHere;
            this.defense = 1;
        }

        public Armor(String newName, String equipHere, Room putHere, int newDef) {
            this.name = newName;
            this.slot = equipHere;
            this.location = putHere;
            this.defense = newDef;
        }
    }
}