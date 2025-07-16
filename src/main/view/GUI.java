package main.view;

import main.model.PointManager;

import java.awt.*;
import javax.swing.*;

/**
 * Représente l'interface graphique de l'application.
 *
 * @author Florian Pépin
 * @version 1.0
 */
public class GUI extends JFrame {

    public GUI(PointManager pointManager) {
        super("Force Direct Graph");
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new ForceDirectGraph(pointManager), BorderLayout.CENTER);
        this.add(new PointInformationView(pointManager), BorderLayout.EAST);
        this.setVisible(true);
    }

    /**
     * Crée une interface graphique general.
     *
     * @param pointManager Le gestionnaire de points à utiliser pour l'interface.
     */
    public static void runGUI(PointManager pointManager) {
        new GUI(pointManager);
    }

}
