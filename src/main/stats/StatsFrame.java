package main.stats;

import main.hero.*;
import main.json.JsonReader;
import main.parcours.*;
import main.point.Point;
import main.point.PointManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Représentation de la fenêtre de statistiques.
 * 
 * @author Tom David
 * @version 1.0
 */
public class StatsFrame {

    /**
     * Contient une instance JFrame.
     */
    private JFrame frame;
    /**
     * Contient la hauteur de la fenêtre.
     */
    private int height = 800;
    /**
     * Contient la largeur de la fenêtre.
     */
    private int width = 1200;

    /**
     * Constructeur par défaut.
     * 
     * @param parcours parcours aléatoire.
     */
    public StatsFrame(RandomPath parcours) {

        frame = new JFrame("StatsFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        // Utilisation de GridBagLayout pour une organisation plus flexible
        frame.setLayout(new GridBagLayout());

        // Création du graphique et ajout à la première ligne
        System.out.println("Génération des chemins aléatoires... (cela peut prendre un certain temps)");
        List<List<Point>> allpath = parcours.listRandomPathFound(200);

        System.out.println(allpath.size() + " chemins générés");
        GraphChemin graphWithInput = new GraphChemin(allpath,width,parcours);

        JScrollPane scrollPane = new JScrollPane(graphWithInput);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 500));

        // Ajout du graphique à la fenêtre
        GridBagConstraints gbcGraph = new GridBagConstraints();
        gbcGraph.gridx = 0;
        gbcGraph.gridy = 0;
        gbcGraph.weightx = 1.0;
        gbcGraph.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbcGraph);

        JsonReader jsonReader = new JsonReader("livre");
        Setup setup = new Setup(jsonReader);
        PointManager pointManager = new PointManager(jsonReader);
        Hero randomHero = new RandomHero(setup).generateRandomHero();

        // Création des statistiques
        List<Point> points = parcours.getPoints();
        List<Point> best = SmallPath.findPath(points);
        List<Point> bestAbsolu = SmallPath.findPathHero(points,randomHero);
        List<Point> bestDeath = SmallPathDeath.findSmallPathDeath(points.get(0),randomHero,pointManager,new ArrayList<>());

        // Création des statistiques
        List<Integer> bestID = PointManager.ListPointToID(best);
        List<Integer> bestIDAbso = PointManager.ListPointToID(bestAbsolu);
        List<Integer> bestDeathID = PointManager.ListPointToID(bestDeath);





        JLabel label3 = new JLabel("Chemin le plus rapide : "+PointManager.displayFirstAndLast(bestIDAbso)+"    Size = "+bestAbsolu.size());
        GridBagConstraints gbcLabel3 = new GridBagConstraints();
        gbcLabel3.gridx = 0;
        gbcLabel3.gridy = 2;
        frame.add(label3, gbcLabel3);

        JLabel label5 = new JLabel("Chemin le plus rapide absolut (sans hero,combat) : "+PointManager.displayFirstAndLast(bestID)+"    Size = "+best.size());
        GridBagConstraints gbcLabel5 = new GridBagConstraints();
        gbcLabel5.gridx = 0;
        gbcLabel5.gridy = 3;
        frame.add(label5, gbcLabel5);

        JLabel label4 = new JLabel("Mort la plus rapide : "+PointManager.displayFirstAndLast(bestDeathID)+"    Size = "+bestDeath.size());
        GridBagConstraints gbcLabel4 = new GridBagConstraints();
        gbcLabel4.gridx = 0;
        gbcLabel4.gridy = 4;
        frame.add(label4, gbcLabel4);

        JLabel label6 = new JLabel("    Nombre TrimChoice : "+howManyChoiceSection.findHowManyTrimChoice(pointManager)+ "     Nombre AlternateChoice : "+howManyChoiceSection.findHowManyAlternateChoice(pointManager)+ "    Nombre RandomPick : "+howManyChoiceSection.findHowManyRandomPick(pointManager)+"    Nombre de choice : "+howManyChoiceSection.findHowManyChoiceSection(pointManager));
        GridBagConstraints gbcLabel6 = new GridBagConstraints();
        gbcLabel6.gridx = 0;
        gbcLabel6.gridy = 5;
        frame.add(label6, gbcLabel6);

        JLabel label7 = new JLabel(" Nombre de points : "+pointManager.getPointList().size()+"    Nombre de combats : "+howManyFight.findHowManyFight(pointManager));
        GridBagConstraints gbcLabel7 = new GridBagConstraints();
        gbcLabel7.gridx = 0;
        gbcLabel7.gridy = 6;
        frame.add(label7, gbcLabel7);



        frame.setVisible(true);
    }

}
