package main.parcours;

import main.combat.Tuple;
import main.hero.Hero;
import main.point.*;

import java.util.List;

/**
 * Représentation du parcours du chemin le plus court vers la mort.
 * 
 * @author Tom David et Florian Pépin
 * @version 1.0
 */
public class SmallPathDeath {

    /**
     * Retourne le chemin le plus court vers la mort.
     * 
     * @param point actuel du LDVEH.
     * @param hero du LDVEH.
     * @param pointManager gestionnaire des points du LDVEH.
     * @param points liste des points.
     * @return une liste contenant les sections du plus court chemin vers la mort.
     */
    public static List<Point> findSmallPathDeath(Point point, Hero hero, PointManager pointManager, List<Point> points) {
        points.add(point);

        if (point.getChildsPoint().isEmpty()) {
            return points;
        }

        List<Tuple> childs = point.nextSectionHero(hero, point);

        for (Tuple child : childs) {
            
            int id = child.getChoice();
            if(point.getChildsPoint() == null && point.getID() != 350 ) {
                break;
            }
            if(id==-1){
                break;
            }

            Point childPoint = pointManager.getPointList().get(id-1);
            Hero childHero = child.getHero();
            List<Point> path = findSmallPathDeath(childPoint, childHero,pointManager, points);
            if (!path.isEmpty()) {
                return points;
            }
        }
        return points;
    }

}
