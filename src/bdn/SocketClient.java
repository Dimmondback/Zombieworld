package bdn;
/*  The java.net package contains the basics needed for network operations. */

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;

/**
 * The SocketClient class is a simple example of a TCP/IP Socket Client.
 *
 */
public class SocketClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 19999;
        StringBuffer instr = new StringBuffer();
        System.out.println("SocketClient initialized");
        try {
            InetAddress address = InetAddress.getByName(host);
            Socket connection = new Socket(address, port);
            BufferedOutputStream bos = new BufferedOutputStream(connection.
                    getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
            Scanner in = new Scanner(System.in);
            String process = "Text: " + in.nextLine() + (char) 13;
            osw.write(process);
            osw.flush();
            BufferedInputStream bis = new BufferedInputStream(connection.
                    getInputStream());
            InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
            int c;
            while ((c = isr.read()) != 13) {
                instr.append((char) c);
            }
            connection.close();
            System.out.println(instr);
        } catch (IOException f) {
            System.out.println("IOException: " + f);
        } catch (Exception g) {
            System.out.println("Exception: " + g);
        }
    }
}