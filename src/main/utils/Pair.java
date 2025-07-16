package main.utils;

/**
 * Représentation d'un tuple contenant un choix et un hero.
 * Normalement, il n'est pas possible de modifier un tuple une fois créé,
 * mais pour des raisons de facilité d'utilisation,
 * nous avons décidé de le faire mutable.
 * 
 * @author Florian Pépin
 * @version 2.0
 */
public class Pair<A, B> {

    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }

    public void setFirst(A a) {
        this.a = a;
    }

    public void setSecond(B b) {
        this.b = b;
    }

    /**
     * Renvoie une chaine de caractère contenant les informations du tuple.
     * 
     * @return une chaine de caractère contenant les informations du tuple.
     */
    @Override
    public String toString() {
        return "(" + this.a + ", " + this.b+")";
    }

}