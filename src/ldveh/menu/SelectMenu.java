package ldveh.menu;

import ldveh.graph.ForceDirectGraph;
import ldveh.json.JsonReader;
import ldveh.parcours.RandomPath;
import ldveh.point.PointManager;
import ldveh.stats.StatsFrame;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Représentation du menu graphique des statistiques du LDVEH.
 * Elle permet de choisir les différents statistiques.
 * 
 * @author Tom David
 * @version 1.0
 */
public class SelectMenu extends JFrame {

    /**
     * Constructeur par défaut.
     * 
     * @param jsonReader lit le LDVEH au format json.
     * @param pointManager gère les points d'un LDVEH.
     */
    public SelectMenu(JsonReader jsonReader, PointManager pointManager) {
        setTitle("Menu de Sélection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        // Création des boutons
        JButton boutonGraph = new JButton("Fenêtre Graphique");
        JButton boutonStats = new JButton("Fenêtre de Statistiques");
        JButton boutonQuitter = new JButton("Quitter");

        // Ajout des actions aux boutons pour ouvrir les fenêtres correspondantes
        boutonGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ForceDirectGraph graph = new ForceDirectGraph(pointManager);
                graph.setVisible(true);

            }
        });

        boutonStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StatsFrame frame = new StatsFrame(RandomPath.parcoursAleatoire(jsonReader, pointManager, false));
            }
        });

        boutonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(boutonGraph);
        panel.add(boutonStats);
        panel.add(boutonQuitter);
        add(panel);
        setVisible(true);
    }

}
