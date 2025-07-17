package main;

import main.view.*;
import main.model.*;

/**
 * Représente le point d'entrée de l'application.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Main {
    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        PointManager pointManager = new PointManager("livre");
        GUI.runGUI(pointManager);
    }
}
