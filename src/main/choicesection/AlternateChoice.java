package main.choicesection;

/**
 * Representation d'un choix alternatif dans un LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class AlternateChoice {

    /**
     * Contient le texte du choix alternatif.
     */
    protected String text;
    /**
     * Contient le numero de section du choix alternatif.
     */
    protected Integer section;

    /**
     * Constructeur par défaut.
     * 
     * @param text du choix.
     * @param section du choix.
     */
    public AlternateChoice(String text, Integer section) {

        this.text = text;
        this.section = section;

    }

    /**
     * Renvoie le texte du choix.
     * 
     * @return le texte du choix.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Renvoie la section du choix.
     * 
     * @return la section du choix.
     */
    public Integer getSection() {
        return this.section;
    }

    /**
     * Renvoie une chaine de caractere avec les informations du choix alternatif.
     * 
     * @return une chaine de caractere contenant les infos de l'instance AlternateChoice.
     */
    @Override
    public String toString() {
        return "• Texte : " + this.text + "\n• Section : " + this.section + "\n";
    }

}

