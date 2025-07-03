package main.stats;

import main.parcours.RandomPath;
import main.point.Point;
import main.point.PointManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Représentation d'un graphique représentant les chemins aléatoires.
 * 
 * @author Tom David
 * @version 1.0
 */
public class GraphChemin extends JPanel {

    /**
     * Contient une grille de points.
     */
    private List<List<Point>> data;

    /**
     * Constructeur par défaut.
     *
     * @param data     liste de chemins.
     * @param width    largeur du graphique.
     * @param parcours
     */
    public GraphChemin(List<List<Point>> data, int width, RandomPath parcours) {

        this.data = data;
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Graphique des chemins aléatoires");
        add(label, BorderLayout.NORTH);
        JPanel chartPanel = createChartPanel();
        ajustSize(data, width - 35);
        add(chartPanel, BorderLayout.CENTER);
        setBackground(Color.lightGray);
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setBackground(Color.lightGray);
        JLabel label1 = new JLabel("Nombre de chemins : " + data.size() + "  |" + "   Nombre de fin :" + nbEnd() + "  |" + "   Chemin le + long : " + mapMax(data) + "  | " + "  Taille chemin vert : " + SizeFirstEnd());

        JLabel label2 = new JLabel("Graphique de liste de chemin aléatoire, Vert = Chemin gagnant , Gris = Hero mort.");
        JLabel label4 = new JLabel("Liste du chemin vert : "+ PointManager.displayFirstAndLast(ListFirstEnd()));
        labelsPanel.add(label1);
        labelsPanel.add(label2);
        labelsPanel.add(label4);

        double luck = (double) parcours.winRate(data) /data.size()*100;
        JLabel label3 = new JLabel("Chance de finir le livre en jouant de manière aléatoire : "+luck+"%");

        labelsPanel.add(label3);
        add(labelsPanel, BorderLayout.SOUTH);
    }


    /**
     * Ajuste la taille de la liste de chemins en fonction de la taille de la fenetre, pour ne pas dépasser la largeur du graphique.
     * 
     * @param data Liste de chemins
     * @param width Largeur du graphique
     */
    private void ajustSize(List<List<Point>> data,int width) {

            while (data.size() > width) {
                data.remove(0);
            }

    }

    /**
     * Recherche la taille du chemin le plus long.
     * 
     * @param list Liste de chemins
     * @return Taille du chemin le plus long
     */
    public int mapMax(List<List<Point>> list) {
        int maxValue = Integer.MIN_VALUE;
        for (List<Point> entry : list) {
            maxValue = Math.max(maxValue, entry.size());
        }
        return maxValue;
    }

    /**
     * Recherche le nombre de chemins qui arrivent à la fin.
     * 
     * @return Nombre de chemins qui arrivent à la fin.
     */
    public int nbEnd() {
        int nbEnd = 0;
        for (List<Point> entry : data) {
            if (entry.get(entry.size() - 1).getID() == 350){
                nbEnd++;
            }
        }
        return nbEnd;
    }

    /**
     * Recherche la taille du premier chemin qui arrive à la fin.
     * 
     * @return Taille du premier chemin qui arrive à la fin.
     */
    public int SizeFirstEnd() {
        ArrayList<Integer> sizeEnd = new ArrayList<>();
        for (List<Point> entry : data) {
            if (entry.get(entry.size() - 1).getID() == 350){
                sizeEnd.add(entry.size());
            }
        }
        if (sizeEnd.isEmpty())
            return 0;
        return Collections.min(sizeEnd);

    }

    /**
     * Recherche la liste des identifiants du premier chemin qui arrive à la fin.
     *
     * @return Liste des identifiants du premier chemin qui arrive à la fin.
     */
    public List<Integer> ListFirstEnd() {
        List<Integer> sizeEnd = new ArrayList<>();
        List<Point> tmp = new ArrayList<>();
        for (List<Point> entry : data) {
            if (entry.get(entry.size() - 1).getID() == 350){
                tmp = entry;
            }
        }
        for (Point point : tmp) {
            sizeEnd.add(point.getID());
        }
        return sizeEnd;

    }



    /**
     * Crée un JPanel représentant le graphique.
     * 
     * @return JPanel représentant le graphique.
     */
    private JPanel createChartPanel() {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);

                int width = getWidth();
                int height = getHeight();
                int barWidth = width / data.size();

                double maxData = mapMax(data);
                double maxSize = (double) height / maxData;



                int i = 0;
                for (List<Point> entry : data) {
                    i++;
                    g.setColor(Color.GRAY);
                    if (entry.get(entry.size() - 1).getID() == 350){
                        g.setColor(Color.GREEN);
                    }

                    int x = i * barWidth;
                    int barHeight = (int) (entry.size() * maxSize);
                    int y = height - barHeight;
                    g.fillRect(x, y, barWidth, barHeight);
                }

                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.PLAIN, 17));
                String text = "PATHSIZE  ↕";
                for (int j = 0; j < text.length(); j++) {
                    g.drawString(String.valueOf(text.charAt(j)), width - 20, height / 2+j*15);
                }
                g.drawString("PATH ↔  ", width/2, height-10);


                g.setColor(Color.GRAY);
            }
        };

        return chartPanel;
    }


}
