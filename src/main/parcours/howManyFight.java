package main.parcours;

import main.point.Point;
import main.point.PointManager;

/**
 * Représente le parcours du nombre de combats dans le LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class howManyFight {

    /**
     * Renvoie le nombre de combats dans le LDVEH.
     * 
     * @param pointManager gère les points du LDVEH.
     * @return le nombre de combats dans le LDVEH.
     */
    public static int findHowManyFight(PointManager pointManager) {
        int cnt = 0;
        for(Point point : pointManager.getPointList()) {
            if(point.getCombatNode() != null) {
                cnt += 1;
            }
        }
        return cnt;

    }



}