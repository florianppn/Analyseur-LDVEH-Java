package ldveh.stats;

import ldveh.json.JsonReader;
import ldveh.parcours.RandomPath;
import ldveh.point.PointManager;

import javax.swing.*;

/**
 * Représentation des statistiques graphiques du parcours aléatoire.
 * 
 * @author Tom David
 * @version 1.0
 */
public class Demo {

    /**
     * Démarrage des statistiques.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JsonReader jsonReader = new JsonReader("livre");
            PointManager pointManager = new PointManager(jsonReader);
            StatsFrame frame = new StatsFrame(RandomPath.parcoursAleatoire(jsonReader,pointManager,false));

        });

    }

}
