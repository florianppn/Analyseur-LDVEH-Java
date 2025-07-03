package main.point;

import main.json.JsonReader;

import java.util.*;

/**
 * Représentation des points d'un livre LDVEH.
 * Contient la liste des points et de leurs enfants.
 * 
 * @author Tom David
 * @version 1.0
 */
public class PointManager{

    public JsonReader livre;
    private List<Point> pointList = new ArrayList<>();
    private Map<Integer,Integer> parentList = new HashMap<>();
    public int nbPoint;

    public PointManager(JsonReader livre){
        this.livre = livre;
        setPointToList();
        setChildPointList();
        setParentMap();
    }

    /**
     * Retourne un Map contenant le nombre de fois où chaque point est parent.
     * 
     * @return un Map contenant le nombre de fois où chaque point est parent.
     */
    public Map<Integer,Integer> getParentList(){
        return parentList;
    }

    /**
     * Initialise la carte des parents pour chaque point dans la liste des points.
     * La carte des parents stocke le nombre de fois où chaque point apparait comme parent.
     */
    private void setParentMap() {
        for(Point parent : pointList) {
            parentList.put(parent.getID(), 0);
        }
        for(Point parent : pointList) {
            for(Point child : parent.getChildsPoint()) {
                if(parentList.containsKey(child.getID())) {
                    parentList.put(child.getID(), parentList.get(child.getID()) + 1);
                } else {
                    parentList.put(child.getID(), 1);
                }
            }
        }
    }

    /**
     * Retourne le nombre de fois où le point apparaît comme parent.
     * 
     * @param id L'identifiant du point pour lequel on souhaite compter les parents.
     * @return Le nombre de fois où le point apparaît comme parent.
     */
    public int countParent(int id){
        return this.parentList.get(id);
    }

    /**
     * Construit la liste des points à partir des sections du livre.
     * Chaque section du livre est transformée en un objet Point et ajoutée à la liste.
     */
    public void setPointToList(){
        for (int i = 1; i <= livre.getSectionLength(); i++) {
            Point test = new Point(i,livre.getSection(i));
            pointList.add(test);
            nbPoint++;
        }

    }

    /**
     * Construit les liens entre les points enfants et leurs parents dans la liste des points.
     * Pour chaque point on parcourt les identifiants de ses enfants et ajoute les points correspondants à sa liste d'enfants.
     */
    public void setChildPointList() {
        for (int i = 0; i < nbPoint; i++) {
            Point tmp = pointList.get(i);
            for (Integer childID : tmp.getChildsID()) {
                if (childID >= 0 && childID <= pointList.size()) {
                    tmp.addChildList(pointList.get(childID-1));
                }
            }
        }
    }

    /**
     * Retourne une liste de points représentant l'ensemble des points du livre.
     * 
     * @return Une liste de points représentant l'ensemble des points du livre.
     */
    public List<Point> getPointList() {
        return pointList;
    }

    /**
     * Retourne le point associé à l'identifiant spécifié, ou null si le point n'est pas trouvé.
     * 
     * @param id L'identifiant du point recherché.
     * @return Le point associé à l'identifiant spécifié, ou null si le point n'est pas trouvé.
     */
    public Point getPoint(int id) {
        for (int i = 0; i < nbPoint; i++) {
            if (pointList.get(i).getID() == id) {
                return pointList.get(i);
            }
        }
        return null;
    }

    /**
     * Affiche proprement les ID d'un chemin.
     * 
     * @param pointList
     */
    public static void displayPointList(List<Point> pointList){
        for (Point point : pointList) {
            System.out.print(point.getID() + " ");
        }
    }

    /**
     * Transorme une liste de points en une liste d'entiers représentant les identifiants des points.
     * 
     * @param pointList Une liste de points
     * @return Une liste d'entiers représentant les identifiants des points de la liste
     */
    public static List<Integer> ListPointToID(List<Point> pointList){
        List<Integer> idList = new ArrayList<>();
        for (Point point : pointList) {
            idList.add(point.getID());
        }
        return idList;
    }

    /**
     * Affiche les 10 premiers et les 10 derniers éléments d'une liste d'entiers sous forme de chaîne de caractères.
     * 
     * @param pointList
     * @return
     */
    public static String displayFirstAndLast(List<Integer> pointList){
        String result = "[";
        if (pointList.size() < 20) {
            for (int i = 0; i < pointList.size(); i++) {
                result += pointList.get(i) + " ";
            }
            result += "]";
            return result;
        }
        for (int i = 0; i < 10; i++) {
            result += pointList.get(i) + " ";
        }
        result += "... ";
        for (int i = pointList.size()-10; i < pointList.size(); i++) {
            result += pointList.get(i) + " ";
        }
        result += "]";
        return result;
    }

}

