package bdn;
/*  The java.net package contains the basics needed for network operations. */
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.util.*;

public class MultipleSocketServer implements Runnable {

    private Socket connection;
    private String TimeStamp;
    private int ID;

    public static void main(String[] args) {
        int port = 19999;
        int count = 0;
        try {
            ServerSocket socket1 = new ServerSocket(port);
            System.out.println("MultipleSocketServer Initialized");
            while (true) {
                Socket connection = socket1.accept();
                Runnable runnable = new MultipleSocketServer(connection, ++count);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        } catch (Exception e) {
        }
    }

    MultipleSocketServer(Socket s, int i) {
        this.connection = s;
        this.ID = i;
    }

    public void run() {
        try {
            BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(is);
            int character;
            StringBuffer process = new StringBuffer();
            while ((character = isr.read()) != 13) {
                process.append((char) character);
            }
            System.out.println(process);
            TimeStamp = new java.util.Date().toString();
            String returnCode = "Your text was: " + process + (char) 13;
            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
            osw.write(returnCode);
            osw.flush();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }
}