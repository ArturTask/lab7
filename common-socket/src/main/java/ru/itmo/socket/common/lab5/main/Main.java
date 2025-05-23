package ru.itmo.socket.common.lab5.main;

import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.lab5.manager.XmlCollectionLoader;

public class
Main {
    public static void main(String[] args) {
        LabWorkTreeSetManager manager = LabWorkTreeSetManager.getInstance();
        new XmlCollectionLoader(manager, "collection.txt").load();
        MainMenu mainMenu = new MainMenu();
        mainMenu.run();
    }
}
