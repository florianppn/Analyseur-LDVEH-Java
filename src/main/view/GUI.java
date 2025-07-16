package main.view;

import java.awt.*;
import javax.swing.*;

/**
 * Représente l'interface graphique de l'application.
 *
 * @author Florian Pépin
 * @version 1.0
 */
public class GUI extends JFrame {

    public GUI() {
        super("Force Direct Graph");

        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
    }

}
