package ServerPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * This is the Server Program for The Dead World!
 */
public class ZombieworldServer {

    static ArrayList<String> movable = new ArrayList<String>();
    static JFrame frame = new JFrame("ZombieWorld");
    static JTextField console = new JTextField();
    static JTextPane tPane = new JTextPane();
    static JScrollPane scroller = new JScrollPane(tPane);
    static JFrame exitWindow = new JFrame("Exit?");
    static Document doc = tPane.getDocument();
    static Font font;
    static Style style;
    static ArrayList<Room> totalRooms = new ArrayList<Room>();
    static ServerSocket server;
    static ArrayList<Player> players = new ArrayList<Player>();

    /*
     * append(String s){...}
     * This method appends the text to the end of the
     * document with the default color of white due
     * to the black background.
     */
    public static void append(String s) {
        style = tPane.addStyle("Styles", null);
        StyleConstants.setForeground(style, Color.WHITE);
        try {
            doc.insertString(doc.getLength(), s, style);
        } catch (BadLocationException ex) {
            System.out.println("BadLocationException ex caught.");
        }
        tPane.setCaretPosition(tPane.getDocument().getLength());
    }

    /*
     * append(String s, Color color){...}
     * This method is the same as append(String s) except
     * that this method also changes the color of the
     * indicated text instead of defaulting to white.
     */
    public static void append(String s, Color color) {
        style = tPane.addStyle("Styles", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), s, style);
        } catch (BadLocationException ex) {
            System.out.println("BadLocationException ex caught.");
        }
        tPane.setCaretPosition(tPane.getDocument().getLength());
    }

    /*
     * worldGen(){...}
     * This method generates all of the rooms within the city
     * by parsing the map.txt file.
     */
    public static void worldGen() {
        //Generate the rooms.
        File file = new File("map.txt");
        try {
            Scanner scan = new Scanner(file);
            String temp = scan.nextLine();
            int i = Integer.parseInt(temp.substring(temp.indexOf("Last Room #:") + 12));
            for (int k = 0; k <= i; k++) {
                totalRooms.add(new Room());
            }
            while (scan.hasNextLine()) {
                Room tempN, tempE, tempS, tempW, tempU, tempD;
                String text = scan.nextLine();
                if (text.contains("Room ") && text.contains(" {")) {
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempN = null;
                    } else {
                        tempN = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempE = null;
                    } else {
                        tempE = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempS = null;
                    } else {
                        tempS = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempW = null;
                    } else {
                        tempW = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempU = null;
                    } else {
                        tempU = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    temp = scan.nextLine();
                    if (temp.equals("None")) {
                        tempD = null;
                    } else {
                        tempD = totalRooms.get(Integer.parseInt(temp.substring(temp.indexOf("Room ") + 5).trim()));
                    }
                    totalRooms.get(Integer.parseInt(text.substring(text.indexOf("Room ") + 5, text.indexOf(" {")).trim())).changes(tempN, tempE, tempS, tempW, tempU, tempD);
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
        }
    }

    /*
     * windowSetup(){...}
     * This is the window setup method. This window
     * is designed for admins to keep track of their
     * players and perform admin commands.
     */
    public static void windowSetup() {
        scroller.setWheelScrollingEnabled(true);
        scroller.add(new JScrollBar());
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Close the server then exit the program.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    server.close();
                    System.exit(1);
                } catch (IOException ex) {
                    System.exit(1);
                }
            }
        });
        frame.setResizable(false);
        frame.setBounds(0, 0, 559, 291);
        frame.add(console, BorderLayout.SOUTH);
        frame.add(scroller, BorderLayout.CENTER);
        frame.setVisible(true);
        tPane.setEditable(false);
        tPane.setBackground(Color.BLACK);
        font = tPane.getFont();
        tPane.setFont(font.deriveFont(Font.BOLD));
        //Background color.
        console.setBackground(Color.BLACK);
        //Text color.
        console.setForeground(Color.WHITE);
        //All legal direction names.
        movable.add("NORTH");
        movable.add("EAST");
        movable.add("SOUTH");
        movable.add("WEST");
        movable.add("UP");
        movable.add("DOWN");
        //
        movable.add("NORT");
        movable.add("EAS");
        movable.add("SOUT");
        movable.add("WES");
        movable.add("");
        movable.add("DOW");
        //
        movable.add("NOR");
        movable.add("EA");
        movable.add("SOU");
        movable.add("WE");
        movable.add("");
        movable.add("DO");
        //
        movable.add("NO");
        movable.add("");
        movable.add("SO");
        movable.add("");
        movable.add("");
        movable.add("");
        //
        movable.add("N");
        movable.add("E");
        movable.add("S");
        movable.add("W");
        movable.add("U");
        movable.add("D");
    }

    /*
     * The server program to run The Dead World. The standard port to listen on
     * is: 9898
     */
    public static void main(String[] args) throws Exception {
        worldGen();
        windowSetup();
        new tickThread().start();
        //Open the server on the specified port.
        try {
            server = new ServerSocket(9898);
        } catch (BindException b) {
            System.exit(1);
        }
        try {
            while (true) {
                //Create a new thread to handle each client.
                try {
                    Socket connection = server.accept();
                    append("Connection accepted at: " + connection.getInetAddress() + "\n");
                    new CustomThread(connection).start();
                } catch (SocketException e) {
                }
            }
        } catch (SocketException e) {
        } catch (IOException ex) {
        }
    }

    /*
     * A custom-made Thread that handles input from clients using The Dead
     * World.
     */
    private static class CustomThread extends Thread {

        private Player player;
        private PrintWriter outgoing;
        BufferedReader incoming;
        private Socket socket;
        private File file = new File("players.txt");
        boolean login = false;
        boolean newPass = false;
        boolean signup = false;
        boolean prompt = false;
        String username = "";

        public CustomThread(Socket socket) {
            this.socket = socket;
        }

        /*
         * find(String name, File file){...}
         * This method will look through the specified file for an
         * the specified input.
         */
        public boolean find(String name, String input, File file) {
            Scanner scan;
            try {
                scan = new Scanner(file);
            } catch (FileNotFoundException e) {
                return false;
            }
            if (input.equals("name")) {
                while (scan.hasNextLine()) {
                    String temp = scan.nextLine();
                    if (temp.contains(name) && temp.contains("{")) {
                        return true;
                    }
                }
            } else if (input.equals("item")) {
                return false;
            }
            return false;
        }

        /*
         * getPass(String username, String, given, File file){...}
         * This method will look through the specified file for the
         * user's password.
         */
        public boolean getPass(String username, String given, File file) {
            Scanner scan;
            try {
                scan = new Scanner(file);
            } catch (FileNotFoundException e) {
                return false;
            }
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (temp.contains(username) && temp.contains("{")) {
                    scan.nextLine();
                    temp = scan.nextLine();
                    String test = "Password: " + given;
                    if (test.hashCode() == temp.hashCode()) {
                        test = "";
                        temp = "";
                        scan.nextLine();
                        scan.close();
                        return true;
                    } else {
                        test = "";
                        temp = "";
                        scan.nextLine();
                        scan.close();
                        return false;
                    }
                }
            }
            return false;
        }

        public Player constructPlayer(String name, File file) {
            Scanner scan;
            int active = -1;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).name.equals(name)) {
                    active = i;
                    break;
                }
            }
            if (active != -1) {
                return players.get(active);
            } else {
                try {
                    scan = new Scanner(file);
                } catch (FileNotFoundException e) {
                    return null;
                }
                while (scan.hasNextLine()) {
                    String temp = scan.nextLine();
                    if (temp.contains(name) && temp.contains("{")) {
                        String tempName = scan.nextLine();
                        scan.nextLine();
                        String tempHp = scan.nextLine();
                        String tempStam = scan.nextLine();
                        String tempDef = scan.nextLine();
                        String tempHeight = scan.nextLine();
                        String tempLoc = scan.nextLine();
                        //Split this string up to get item names.
                        //Then add to the list.
                        String tempInv = scan.nextLine();
                        //Resultant ArrayList goes here.

                        //Make a method that also looks through Items
                        //Until it reaches the armor section and then
                        //creates a that armor based on stats.
                        String tempHead = scan.nextLine();
                        String tempChest = scan.nextLine();
                        String tempLegs = scan.nextLine();
                        String tempHands = scan.nextLine();
                        String tempShoulders = scan.nextLine();
                        String tempShoes = scan.nextLine();
                        String tempArms = scan.nextLine();
                        String Back = scan.nextLine();
                        scan.close();
                        int realHp = Integer.parseInt(tempHp);
                        int realStam = Integer.parseInt(tempStam);
                        int realDef = Integer.parseInt(tempDef);
                        int realHeight = Integer.parseInt(tempHeight);
                        int realLoc = Integer.parseInt(tempLoc);
                        //constuct the Player, add it to the list of active players, and return it for the client.
                        Player newPlayer = new Player(tempName, realHp, realStam, realDef, realHeight, realLoc, new ArrayList<Item>(), null, null, null, null, null, null, null, null);
                        players.add(newPlayer);
                        return newPlayer;
                    }
                }
            }
            return null;
        }

        /*
         * This method saves the player's information to the player's.txt
         * file for later usage.
         */
        public void savePlayer(Player save) {
            if (players.contains(save)) {
                players.remove(save);
            }
            Scanner scan = new Scanner("");
            //Create backup of player file.
            File file = new File("players.txt");
            File newFile = new File("playersBackup.txt");
            try {
                FileWriter writer = new FileWriter(newFile, false);
                scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    writer.write(scan.nextLine() + System.lineSeparator());
                }
                writer.close();
                try {
                    outgoing = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
            } catch (FileNotFoundException e) {
            } catch (IOException ex) {
            }
            try {
                FileWriter writer = new FileWriter(new File("players.txt"), false);
                scan = new Scanner(newFile);
                while (scan.hasNextLine()) {
                    String temp = scan.nextLine();
                    if (temp.contains(username) && temp.contains("{")) {
                        writer.write(username + " {" + System.lineSeparator());
                        writer.write(scan.nextLine() + System.lineSeparator());
                        writer.write(scan.nextLine() + System.lineSeparator());
                        writer.write(save.hp + System.lineSeparator());
                        scan.nextLine();
                        writer.write(save.stamina + System.lineSeparator());
                        scan.nextLine();
                        writer.write(save.defense + System.lineSeparator());
                        scan.nextLine();
                        writer.write(save.height + System.lineSeparator());
                        scan.nextLine();
                        writer.write(totalRooms.indexOf(save.location) + System.lineSeparator());
                        scan.nextLine();
                        //Parse inventory here.
                        writer.write("[]" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                        writer.write("None" + System.lineSeparator());
                        scan.nextLine();
                    } else {
                        writer.write(temp + System.lineSeparator());
                    }
                }
                writer.close();
                try {
                    outgoing = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
            } catch (IOException e) {
            }
            return;
        }

        /*
         * commandInput(String given){...}
         * This is where the commands input by the player
         * are handled. You will need to add more if() checks
         * as you add more commands to the game. This is also
         * where login and signup are handled.
         */
        public void commandInput(String given) {
            //No empty strings.
            if (given.length() == 0) {
                return;
            }
            //Have the user login to their account.
            if (!login && !signup && username.length() == 0) {
                username = given;
                if (find(given, "name", file)) {
                    prompt = true;
                    commandOutput("Log into account " + given + "? [Y/N]\n");
                } else {
                    signup = true;
                    commandOutput("User not found. Would you like to make an account? [Y/N]\n");
                }
                return;
            }
            //Prompt for accepting login attempt
            if (prompt) {
                if (given.toUpperCase().equals("Y")) {
                    newPass = false;
                    commandOutput("Password: ");
                    prompt = false;
                    return;
                } else if (given.toUpperCase().equals("N")) {
                    username = "";
                    commandOutput("Please reenter the account name you wish to log into.\n");
                    prompt = false;
                    return;
                } else {
                    return;
                }
            }
            //Prompt for making a new account;
            if (signup) {
                if (given.toUpperCase().equals("Y")) {
                    newPass = true;
                    commandOutput("Please enter a password for this account:\n");
                    signup = false;
                    return;
                } else if (given.toUpperCase().equals("N")) {
                    username = "";
                    commandOutput("Please reenter the account name you wish to log into.\n");
                    signup = false;
                    return;
                } else {
                    return;
                }
            }
            //Unidentified user with no password.
            if (newPass) {
                //Write the new player's information to the file.
                try {
                    FileWriter writer = new FileWriter("players.txt", true);
                    writer.write(username + " {");
                    writer.write(System.lineSeparator());
                    writer.write(username);
                    writer.write(System.lineSeparator());
                    writer.write("Password: " + given);
                    writer.write(System.lineSeparator());
                    writer.write("100");
                    writer.write(System.lineSeparator());
                    writer.write("100");
                    writer.write(System.lineSeparator());
                    writer.write("0");
                    writer.write(System.lineSeparator());
                    writer.write("0");
                    writer.write(System.lineSeparator());
                    writer.write("0");
                    writer.write(System.lineSeparator());
                    writer.write("[]");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("None");
                    writer.write(System.lineSeparator());
                    writer.write("}");
                    writer.write(System.lineSeparator());
                    writer.close();
                    try {
                        outgoing = new PrintWriter(socket.getOutputStream(), true);
                    } catch (IOException ex) {
                    }
                    commandOutput("Thank you! Please log into your new account by reentering this username!\n");
                    username = "";
                    newPass = false;
                } catch (IOException e) {
                    username = "";
                    newPass = false;
                    commandOutput("Unexpected error occured. Please try again!\n");
                }
                return;
            }
            if (!login && username.length() > 0) {
                if (getPass(username, given, file)) {
                    player = constructPlayer(username, file);
                    login = true;
                    commandOutput("Welcome back " + username + ".\n");
                } else {
                    commandOutput("Incorrect password. Please reenter username.\n");
                    username = "";
                }
                return;
            }
            String input = given.toUpperCase();
            if (input.equals("QUIT") || input.equals("DISCONNECT")) {
                try {
                    //#Save_Player;
                    commandOutput("Thank you and goodbye!\n");
                    savePlayer(player);
                    socket.close();
                } catch (IOException ex) {
                }
            } else if (input.contains("CON") || input.contains("CONDITION") || input.contains("STATUS")) {
                player.cond();
            } else if ((input.contains("MOVE ") && input.length() > 5) || (input.contains("M ") && input.length() > 2) || (movable.contains(input))) {
                player.move(input);
            } else if (input.contains("LOOK") || input.equals("L") || (input.contains("L ") && movable.contains(input.substring(input.indexOf(" ") + 1)))) {
                player.look(input);
            } else if (input.contains("GET") || input.contains("PICK UP")) {
                player.get(input);
            } else if ((input.contains("EQ ") && input.substring(0, input.indexOf(" ")).length() < 4) || (input.contains("EQUIP ") && input.substring(0, input.indexOf(" ")).length() < 7) || (input.contains("WEAR ") && input.substring(0, input.indexOf(" ")).length() < 5)) {
                player.equip(input);
            } else if (input.contains("UNEQ ") || input.contains("UNEQUIP ")) {
                player.unequip(input);
            } else if (input.equals("I") || input.equals("INVENTORY") || input.equals("INV")) {
                player.inventory();
            } else if (input.equals("EQ") || input.equals("EQUIPMENT")) {
                player.equipment();
            } else if (input.contains("DROP ")) {
                player.drop(input);
            } else if (input.equals("STAT") || input.equals("ST") || input.equals("STATS")) {
                player.stats();
            } else if (input.equals("")) {
                player.cond();
            } else if (input.contains("FLEE") || input.contains("F ") || input.equals("F")) {
                //player.flee(input);
            }
            while (player.drawText.size() > 0) {
                commandOutput(player.drawText.get(0));
                player.drawText.remove(0);
            }
        }

        /*
         * commandOutput(){...}
         * This method is used to send text to the client
         * for use in the serverSpeak() command.
         */
        public void commandOutput(String out) {
            outgoing.println(out);
            try {
                outgoing = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
            }
        }

        //Run the thread!
        public void run() {
            try {
                // Input from client. Use incoming.readLine();
                incoming = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                // Output for client. Use outgoing.print("...");
                outgoing = new PrintWriter(socket.getOutputStream(), true);
                incoming.readLine();
                incoming.readLine();
                commandOutput("Please enter your ");
                commandOutput("Username: ,Color.BLUE");
                //While still connected, accept input from client.
                while (true) {
                    //Read lines from here.
                    String input = incoming.readLine();
                    try {
                        input = input.trim();
                    } catch (NullPointerException eeeq) {
                    }
                    //Parse Commands here. Print output using commandOutput();
                    commandInput(input);
                }
            } catch (IOException e) {
            }
        }
    }

    private static class tickThread extends Thread {

        /*
         * resolve(Player player){...}
         * This method attempts to resolve combat one step at a time.
         */
        public static void resolve() {
            for (int i = 0; i < players.size(); i++) {
                //Have players attack.
            }
            for (int j = 0; j < players.size(); j++) {
                for(int k = 0; k < players.get(j).location.enemies.size(); k++) {
                    int random = (int) (Math.random() * players.get(j).location.enemies.size());
                    players.get(j).location.enemies.get(k).strike(players.get(j).location.players.get(random));
                }
            }
        }

        /*
         * This method handles server ticks and 
         */
        public static void tick() {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).location.enemies.size() > 0) {
                    players.get(i).combat = true;
                    resolve();
                } else {
                    players.get(i).combat = false;
                }
            }
        }

        //Run the thread!
        public void run() {
            while (true) {
                tick();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    continue;
                }
            }
        }
    }
}
