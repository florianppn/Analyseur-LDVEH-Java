package main.parcours;

import main.combat.Tuple;
import main.hero.Hero;
import main.point.*;

import java.util.*;

/**
 * La classe ParcoursCourtChemin est responsable de la recherche du chemin le plus court
 */
public class SmallPath {
    
    private List<Point> points;


    public SmallPath() {
        this.points = new ArrayList<>();
    }



    /**
     * Trouver le chemin le plus court.
     * @param points
     * @return une liste de points.
     */
    public static List<Point> findPath(List<Point> points) {
        Point start = points.get(0);
        Point end = points.get(points.size() - 1);

        Queue<List<Point>> queue = new LinkedList<>();
        Map<Integer, Integer> distance = new HashMap<>();

        List<Point> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);
        distance.put(start.getID(), 0);

        while (!queue.isEmpty()) {
            List<Point> currentPath = queue.poll();
            Point currentPoint = currentPath.get(currentPath.size() - 1);

            if (currentPoint == end) {
                return currentPath;
            }

            for (Point child : currentPoint.getChildsPoint()) {
                int newDistance = distance.get(currentPoint.getID()) + 1;
                if (!distance.containsKey(child.getID()) || newDistance < distance.get(child.getID())) {
                    List<Point> newPath = new ArrayList<>(currentPath);
                    newPath.add(child);
                    queue.add(newPath);
                    distance.put(child.getID(), newDistance);
                }
            }
        }

        return new ArrayList<>();
    }


    /**
     * Trouver le chemin le plus court pour un hero.
     * @param points
     * @param hero
     * @return une liste de points.
     */
    public static List<Point> findPathHero(List<Point> points, Hero hero) {
        Point start = points.get(0);
        Point end = points.get(points.size() - 1);

        Queue<List<Tuple>> queue = new LinkedList<>();
        //[ID, distance]
        Map<Integer, Integer> distance = new HashMap<>();

        List<Tuple> initialPath = new ArrayList<>();
        initialPath.add(new Tuple(start, hero));
        queue.add(initialPath);
        distance.put(start.getID(), 0);

        while (!queue.isEmpty()) {
            List<Tuple> currentPath = queue.poll();
            Point currentPoint = currentPath.get((currentPath.size() - 1)).getPoint();

            if (currentPoint == end) {
                return convertTupleToPoints(currentPath);
            }

            List<Tuple> childs = currentPoint.nextSectionHero(hero, currentPoint);
            for (Tuple child : childs) {
                if(child.getChoice()!=-1){
                    child.setPoint(points.get(child.getChoice()-1));
                }



            }

            for (Tuple child : childs) {
                int newDistance = distance.get(currentPoint.getID()) + 1;
                if (child.getChoice()!=-1) {
                    if (!distance.containsKey(child.getPoint().getID()) || newDistance < distance.get(child.getPoint().getID())) {
                        List<Tuple> newPath = new ArrayList<>(currentPath);
                        newPath.add(child);
                        queue.add(newPath);
                        distance.put(child.getPoint().getID(), newDistance);
                    }
                }

            }
        }
        System.out.println("No path found");

        return new ArrayList<>();
    }

    /**
     * Convertit une liste de tuples en une liste de points.
     * @param tuples
     * @return une liste de points.
     */
    public static List<Point> convertTupleToPoints(List<Tuple> tuples) {
        List<Point> points = new ArrayList<>();
        for (Tuple tuple : tuples) {
            points.add(tuple.getPoint());
        }
        return points;
    }



    /**
     * Regarde si le chemin est bien possible a faire en regardant si chaque point a pour enfant le point suivant.
     * @param points
     */
    public static boolean checkList(List<Point> points){
        for(int i = 0; i < points.size()-1; i++){
            if(!points.get(i).getChildsPoint().contains(points.get(i+1))){
                return false;
            }
        }
        return true;
    }

}