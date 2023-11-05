package org.example;

import org.example.gui.ClientWindow;
import org.example.gui.ServerWindow;
import org.example.server.Server;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientWindow(serverWindow);
        new ClientWindow(serverWindow);
    }
}