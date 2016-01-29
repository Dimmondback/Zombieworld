package ServerPackage;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;

/*
 * This is the "The Dead World" game!
 */
public class Zombieworld extends KeyAdapter {

    static Zombieworld game;
    static boolean online = false;
    static JFrame frame = new JFrame("ZombieWorld");
    static JTextField console = new JTextField();
    static JTextPane tPane = new JTextPane();
    static JScrollPane scroller = new JScrollPane(tPane);
    static JFrame exitWindow = new JFrame("Exit?");
    static Document doc = tPane.getDocument();
    static Font font;
    static Style style;
    static BufferedReader incoming;
    static PrintWriter outgoing;
    static Socket socket;
    static ContinueThread gameThread;

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
     * login(){...}
     * This method asks the player to login to the server.
     * If they have never played before they will be asked
     * to make an account in order to play.
     */
    public static void login() {
    }

    /*
     * windowSetup(){...}
     * This is the window setup method. Unless you
     * want to change the size of the window or
     * other such options, do not touch this.
     */
    public static void windowSetup() {
        scroller.setWheelScrollingEnabled(true);
        scroller.add(new JScrollBar());
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitWindow();
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
    }

    /*
     * exitWindow(){...}
     * This is the exit window that pops up when
     * the ESCAPE button is pressed. You can
     * change this how you like to suit your game.
     */
    public static void exitWindow() {
        JButton yes = new JButton("Exit");
        JButton no = new JButton("Cancel");
        yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    socket.close();
                    exitWindow.dispose();
                    frame.dispose();
                    System.exit(0);
                } catch (IOException ex) {
                    exitWindow.dispose();
                    frame.dispose();
                    System.exit(0);
                } catch (NullPointerException en) {
                    exitWindow.dispose();
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
        no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frame.setFocusableWindowState(true);
                exitWindow.dispose();
            }
        });
        yes.setSize(100, 40);
        no.setSize(100, 40);
        exitWindow.setSize(265, 85);
        exitWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        exitWindow.setUndecorated(true);
        exitWindow.getRootPane().setBorder(new LineBorder(Color.getHSBColor((float) .6, (float) .1, (float) .9), 3));
        exitWindow.setLocation(frame.getX() + 145, frame.getY() + 95);
        frame.setFocusableWindowState(false);
        exitWindow.setLayout(null);
        exitWindow.setResizable(false);
        exitWindow.add(yes);
        exitWindow.add(no);
        frame.setFocusable(false);
        yes.setLocation(20, 20);
        no.setLocation(140, 20);
        exitWindow.setVisible(true);
    }

    /*
     * welcomeMessage(){...}
     * This is the welcoming message to your game.
     */
    public static void welcomeMessage() {
        append("Hello and welcome to The Dead World! Currently this game is in preAlpha state but will be updated every so often!\n");
        append("Please enter the IP address of the server you wish to connect to!\n");
    }

    /*
     * keyPressed(KeyEvent evt){...}
     * This is where the game recognizes key presses.
     * Only edit this if you want additional keys to be
     * recognized by the program.
     */
    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!online) {
                startup();
            }
            String send = console.getText();
            console.setText("");
            tPane.setCaretPosition(tPane.getDocument().getLength());
            append(">" + send.toUpperCase() + "\n");
            try {
                outgoing.println(send);
            } catch (NullPointerException npe) {
                serverSpeak("Your game could not establish connection to the server using that IP. Please try again!\n");
            }
            if (send.toUpperCase().equals("QUIT") || send.toUpperCase().equals("DISCONNECT")) {
                serverSpeak("Disconnecting from server.\n");
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ie) {
                }
                try {
                    socket.close();
                } catch (IOException ex) {
                } catch (NullPointerException npex) {
                }
                welcomeMessage();
                incoming = null;
                outgoing = null;
                socket = null;
                gameThread.stop();
                gameThread = null;
                online = false;
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exitWindow();
        }
    }

    /*
     * serverSpeak(String input){...}
     * This method handles what to draw for the user. This
     * is determined by the input coming from the server.
     */
    public static void serverSpeak(String input) {
        if (input == null) {
            return;
        }
        if (input.length() == 0) {
            append("\n");
            return;
        }
        if (input.contains("Color.")) {
            String[] temp = input.split(",");
            if (input.contains("Color.MAGENTA")) {
                append(temp[0], Color.MAGENTA);
            } else if (input.contains("Color.BLACK")) {
                append(temp[0], Color.BLACK);
            } else if (input.contains("Color.CYAN")) {
                append(temp[0], Color.CYAN);
            } else if (input.contains("Color.RED")) {
                append(temp[0], Color.RED);
            } else if (input.contains("Color.WHITE")) {
                append(temp[0], Color.WHITE);
            } else if (input.contains("Color.BLUE")) {
                append(temp[0], Color.BLUE);
            } else {
                append(temp[0]);
            }
        } else {
            append(input);
        }
    }

    /*
     * startup(){...}
     * This method attempts to connect the client to the
     * server.
     */
    public static void startup() {
        try {
            String IP = console.getText();
            socket = new Socket(IP, 9898);
            gameThread = new ContinueThread(socket);
            gameThread.start();
            Thread.sleep(1000);
            if (gameThread.isOnline) {
                online = true;
            }
        } catch (SocketException ee) {
            append("\nWARNING! ", Color.RED);
            append("Game has lost connection to the server. Please reenter the IP to connect.\n");
            online = false;
            console.setText("");
            tPane.setCaretPosition(tPane.getDocument().getLength());
        } catch (IOException ex) {
            online = false;
        } catch (InterruptedException ex) {
            append("Game could not connect to the server. Please press reenter the IP to connect.");
        }
    }

    /*
     * main(String[] args) throws IOException {...}
     * The main method begins the game. You will not
     * need to change this unless you're changing the
     * server address and/or port number.
     */
    public static void main(String[] args) throws IOException {
        game = new Zombieworld();
        console.addKeyListener(game);
        tPane.addKeyListener(game);
        windowSetup();
        welcomeMessage();
    }

    /*
     * A custom-made Thread that handles input from The Dead World server.
     */
    private static class ContinueThread extends Thread {

        private Socket socket;
        private boolean isOnline = false;

        public ContinueThread(Socket socket) {
            this.socket = socket;
        }

        //Run the thread!
        public void run() {
            try {
                // Input from client. Use incoming.readLine();
                incoming = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                // Output for client. Use outgoing.print("...");
                outgoing = new PrintWriter(socket.getOutputStream(), true);
                //Check if there is a response from the server.
                String temp = "";
                try {
                    outgoing.println("Connection Established.");
                    isOnline = true;
                } catch (NullPointerException exee) {
                    this.stop();
                }
                while (true) {
                    serverSpeak(temp);
                    temp = incoming.readLine();
                }
            } catch (IOException e) {
            } catch (NullPointerException n) {
                serverSpeak("WARNING! Server failure! Attempting restart.\n");
                online = false;
                startup();
                Thread.currentThread().start();
            }
        }
    }
}
