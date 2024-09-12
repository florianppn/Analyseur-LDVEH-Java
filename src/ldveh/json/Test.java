package ldveh.json;

/**
 * Représentation des tests du package ldveh.json;
 * 
 * @author Tom David
 * @version 1.0
 */
public class Test {

    /**
     * Contient une instance jsonReader.
     */
    private JsonReader jsonReader;

    /**
     * Constructeur par défaut.
     */
    public Test() {

        this.jsonReader = new JsonReader("livre");

    }

    /**
     * Tests du package ldveh.json.
     */
    @org.junit.Test
    public void assertJson() {

        assert jsonReader != null;
        assert jsonReader.getRootNode() != null;
        assert jsonReader.getSection(1) != null;
        assert jsonReader.getSetup() != null;
        assert jsonReader.getSectionLength() > 0;
        assert jsonReader.getRootNode().getClass().getName().equals("com.fasterxml.jackson.databind.node.ObjectNode");
        assert jsonReader.getSection(1).getClass().getName().equals("com.fasterxml.jackson.databind.node.ObjectNode");
        assert jsonReader.getSetup().getClass().getName().equals("com.fasterxml.jackson.databind.node.ObjectNode");

        System.out.println("assertJson passed");

    }

}
