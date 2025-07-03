package main.parcours;

import main.point.Point;
import main.point.PointManager;

/**
 * Représente les parcours du nombre de choix de section dans le LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class howManyChoiceSection {

    /**
     * Renvoie le nombre de choix par exigence dans le LDVEH.
     * 
     * @param pointManager gère les points du LDVEH.
     * @return le nombre de combats dans le LDVEH.
     */
    public static int findHowManyTrimChoice(PointManager pointManager) {

        int cnt = 0;

        for(Point point : pointManager.getPointList()) {

            if(point.getPointNode().get("trim_choices") != null) {
                cnt += 1;
            }

        }

        return cnt;

    }

    /**
     * Renvoie le nombre de choix alternatifs dans le LDVEH.
     * 
     * @param pointManager gère les points du LDVEH.
     * @return le nombre de combats dans le LDVEH.
     */
    public static int findHowManyAlternateChoice(PointManager pointManager) {

        int cnt = 0;

        for(Point point : pointManager.getPointList()) {

            if(point.getPointNode().get("alternate_choices") != null) {
                cnt += 1;
            }

        }

        return cnt;

    }

    /**
     * Renvoie le nombre de choix aléatoires dans le LDVEH.
     * 
     * @param pointManager gère les points du LDVEH.
     * @return le nombre de combats dans le LDVEH.
     */
    public static int findHowManyRandomPick(PointManager pointManager) {

        int cnt = 0;

        for(Point point : pointManager.getPointList()) {

            if(point.getPointNode().get("is_random_pick") != null) {
                cnt += 1;
            }

        }

        return cnt;

    }

    /**
     * Renvoie le nombre de choix dans le LDVEH.
     * 
     * @param pointManager gère les points du LDVEH.
     * @return le nombre de combats dans le LDVEH.
     */
    public static int findHowManyChoiceSection(PointManager pointManager) {

        int cnt = 0;

        for(Point point : pointManager.getPointList()) {

            if(point.getPointNode().get("is_random_pick") != null) {
                cnt += 1;
            }
            if(point.getPointNode().get("trim_choices") != null) {
                cnt += 1;
            }
            if(point.getPointNode().get("alternate_choices") != null) {
                cnt += 1;
            }

        }

        return cnt;

    }

}
