package main.choicesection;

/**
 * Representation d'un choix aleatoire dans un LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class RandomPick extends AlternateChoice {

    /**
     * Contient le numero de debut de l'intervalle.
     */
    private Integer startInterval;
    /**
     * Contient le numero de fin de l'intervalle.
     */
    private Integer endInterval;

    /**
     * Constructeur par défaut.
     * 
     * @param text du choix.
     * @param section du choix.
     * @param startInterval debut de l'intervalle aleatoire.
     * @param endInterval fin de l'intervalle aleatoire.
     */
    public RandomPick(String text, Integer section, Integer startInterval, Integer endInterval) {

        super(text, section);
        this.startInterval = startInterval;
        this.endInterval = endInterval;

    }

    /**
     * Renvoie le nombre de départ de l'intervalle du choix aleatoire.
     * 
     * @return le depart de l'intervalle.
     */
    public Integer getStartInterval() {
        return this.startInterval;
    }

    /**
     * Renvoie le nombre final de l'intervalle.
     * 
     * @return la fin de l'intervalle.
     */
    public Integer getEndInterval() {
        return this.endInterval;
    }

    /**
     * Détermine si la condition est satisfaite.
     * 
     * @param n est un nombre quelconque.
     * @return true si n est dans l'intervalle sinon false.
     */
    public boolean isSatisfied(Integer n) {
        if(this.startInterval <= n && n <= this.endInterval){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Renvoie une chaine de caractere contenant les informations du choix aléatoire.
     * 
     * @return une chaine de caractere contenant les infos de l'instance RandomPick.
     */
    @Override
    public String toString() {
        return super.toString() + "• Intervalle : [" + this.startInterval + ", " + this.endInterval + "]\n";
    }

}