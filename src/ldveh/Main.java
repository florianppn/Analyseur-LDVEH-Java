package ldveh;

import ldveh.menu.SelectMenu;
import ldveh.json.JsonReader;
import ldveh.point.PointManager;

import java.io.*;
import java.util.Properties;

/**
 * Représente le démarrage de la partie graphique du projet LDVEH.
 * 
 * @author Tom David
 * @version 1.0
 */
public class Main {

    /**
     * Démarrage du projet LDVEH.
     */
    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("config.properties"));

        JsonReader jsonReader = new JsonReader(properties.getProperty("livre"));
        PointManager pointManager = new PointManager(jsonReader);

        new SelectMenu(jsonReader, pointManager);

    }
}
