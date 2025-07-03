package main.parcours;

import main.hero.*;
import main.json.JsonReader;
import main.point.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Une class representant un parcours aleatoire.
 */
public class RandomPath {

    private ArrayList<Point> AllPath = new ArrayList<>();

    private List<Point> points;
    private Point startNode;
    private Point endNode;

    public Hero hero;

    /**
     * Construction d'une nouvelle instance RandomPath.
     * @param points est une liste de points.
     * @param hero est le hero.
     * @param setup est le setup du livre.
     */
    public RandomPath(List<Point> points, Hero hero, Setup setup) {
        this.points = points;

        if (points == null || points.isEmpty()) {
            return ;
        }

        this.startNode = points.get(0);
        this.endNode = points.get(points.size() - 1);

        this.hero = hero;

    }

    /**
     * Trouver un chemin aleatoire.
     * @return une liste de points.
     */
    public List<Point> findRandomPath() {

        if (this.points == null || this.points.isEmpty()) {
            return new ArrayList<>();
        }
        Point currentNode = startNode;
        List<Point> visitedNodes = new ArrayList<>();
        Hero herotmp = new Hero(hero);

        while (!currentNode.getChildsID().isEmpty()) {

            visitedNodes.add(currentNode);
            List<Point> childs = currentNode.getChildsPoint();

            int nextSection = currentNode.nextSection(herotmp);

            if (nextSection==0 ) {
                visitedNodes.add(endNode);
                return visitedNodes;
            } else if (nextSection==-1) {
                return visitedNodes;
            }

            for (Point child : childs) {
                if (child.getID() == nextSection) {
                    currentNode = child;
                    break;
                }
            }
        }

        visitedNodes.add(currentNode);
        return visitedNodes;
    }


    /**
     * Trouver x nombre de chemin aleatoire.
     * @return une liste de liste de points.
     */
    public List<List<Point>> listFindRandomPath(Integer occurence) {
        List<List<Point>> allpath = new ArrayList<>();
        for(int j = 0; j<occurence; j++) {
            List<Point> tmp = findRandomPath();
            if (tmp.get(tmp.size()-1).getID() == endNode.getID()) {
                System.out.println("Taille du chemin winner : " + tmp.size());
                //PointManager.displayPointList(tmp);
            }
            allpath.add(tmp);
        }
        return allpath;
    }

    /**
     * Parcourss aleatoire jusqu'a trouver le chemin gagnant et continue si le nombre eest inferieur a occurence.
     * @return une liste de liste de points.
     */
    public List<List<Point>> listRandomPathFound(Integer occurence) {
        List<List<Point>> allpath = new ArrayList<>();
        boolean found = false;
        while (!found || allpath.size() < occurence){
            List<Point> tmp = findRandomPath();

                allpath.add(tmp);
                if (tmp.get(tmp.size()-1).getID() == endNode.getID()) {
                    System.out.println("Taille du chemin winner : " + tmp.size());
                    found = true;
                }
        }
        return allpath;

    }

    /**
     * Calculer le pourcentage de chance de reussir.
     * @return le pourcentage de chance de reussir.
     */
    public int winRate(List<List<Point>> list){
        int nbWin = 0;
        for (List<Point> entry : list) {
            if (entry.get(entry.size() - 1).getID() == endNode.getID()){
                nbWin++;
            }
        }
        return nbWin;
    }

    /**
     * @return la liste de points.
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Initialise un parcours.
     */
    public static RandomPath parcoursAleatoire(JsonReader jsonReader, PointManager pointManager, boolean heroCheat) {
        java.util.List<Point> points = pointManager.getPointList();
        Setup setup = new Setup(jsonReader);
        if (heroCheat) {
            Hero randomHero = new RandomHero(setup).generateRandomHero();
            RandomPath parcours = new RandomPath(points,randomHero,setup);
            return parcours;
        }
        List<Equipment> equipmentList = setup.getEquipmentList(0,2);
        Hero hero = new Hero(equipmentList, setup.updateDisciplineTree(0,2,1,4,7), setup.updateWeaponsTree(equipmentList), 10000, 10000, 10000);
        RandomPath parcours = new RandomPath(points,hero,setup);
        return parcours;
    }
}
