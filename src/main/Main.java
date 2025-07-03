package main;

import main.menu.SelectMenu;
import main.json.JsonReader;
import main.point.PointManager;

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
        JsonReader jsonReader = new JsonReader("livre");
        PointManager pointManager = new PointManager(jsonReader);
        new SelectMenu(jsonReader, pointManager);
    }
}
