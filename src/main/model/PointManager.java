package main.model;

import java.util.*;

import main.utils.*;

/**
 * Représentation des points du livre LDVEH.
 * Cette classe gère la liste des points, leurs enfants et parents.
 * 
 * @author Florian Pépin
 * @version 2.0
 */
public class PointManager extends AbstractListenableModel {

    private JsonReader livre; // Le livre LDVEH
    private List<Point> points; // Liste des points du livre
    private Map<Integer,Integer> parents; // Map pour stocker le nombre de fois où chaque point est parent
    private Point currentPoint; // Point courant

    public PointManager(String name) {
        super();
        this.livre = new JsonReader(name);;
        this.points = new ArrayList<>();
        this.parents = new HashMap<>();
        this.currentPoint = null;

        this.initPoints();
        this.initChildrenPoints();
        this.initParents();
    }

    public Point getCurrentPoint() {
        return this.currentPoint;
    }

    public Map<Integer,Integer> getParents() {
        return this.parents;
    }

    public int getParent(int id) {
        return this.parents.get(id);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public Point getPoint(int id) {
        for (Point point : this.points) {
            if (point.getID() == id) {
                return point;
            }
        }
        return null;
    }

    public void setCurrentPoint(Point point) {
        this.currentPoint = point;
        this.fireChange();
    }

    /**
     * Initialise la liste des points à partir du livre LDVEH.
     * Chaque point est créé à partir de la section correspondante du livre.
     */
    private void initPoints() {
        for (int i = 1; i <= this.livre.getSectionLength(); i++) {
            this.points.add(new Point(i, this.livre.getSection(i)));
        }
    }

    /**
     * Initialise la map des parents.
     * Pour chaque point, on initialise le nombre de parents à 0.
     * Ensuite, pour chaque point enfant, on incrémente le compteur de parents du point parent.
     */
    private void initParents() {
        for(Point parent : this.points) {
            this.parents.put(parent.getID(), 0);
        }
        for(Point parent : this.points) {
            for(Point child : parent.getChilds()) {
                if(this.parents.containsKey(child.getID())) {
                    this.parents.put(child.getID(), this.parents.get(child.getID()) + 1);
                } else {
                    this.parents.put(child.getID(), 1);
                }
            }
        }
    }

    /**
     * Initialise la liste des enfants pour chaque point.
     * Pour chaque point, on parcourt ses identifiants d'enfants
     * et on ajoute les points correspondants.
     */
    public void initChildrenPoints() {
        for (int i = 0; i < this.points.size(); i++) {
            Point tmp = this.points.get(i);
            for (Integer childID : tmp.getChildsID()) {
                if (childID >= 0 && childID <= this.points.size()) {
                    tmp.addChild(this.points.get(childID-1));
                }
            }
        }
    }

}

