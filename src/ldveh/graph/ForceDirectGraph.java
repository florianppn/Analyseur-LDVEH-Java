package ldveh.graph;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import ldveh.json.JsonReader;
import ldveh.point.Point;
import ldveh.point.PointManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseAdapter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Représentation d'un graph utilsant un algorithme de force.
 * 
 * @author Tom David
 * @version 1.0
 */
public class ForceDirectGraph extends JFrame {

    /**
     * Contient une instance properties.
     */
    private Properties properties;
    /**
     * Contient une instance nxGraph.
     */
    private mxGraph graph;
    /**
     * Contient une instance JPanel.
     */
    private JPanel textPanel;
    /**
     * Contient une instance JTextArea.
     */
    private JTextArea textArea;
    /**
     * Contient une instance PointManager.
     */
    private PointManager pointManager;
    /**
     * ???
     */
    private int startPointX;
    /**
     * ???
     */
    private int startPointY;
    /**
     * ???
     */
    private int expansionX;
    /**
     * ???
     */
    private int expansionY;
    /**
     * ???
     */
    private Map<Integer,Integer> VertexSortList = new HashMap<>() ;
    /**
     * Contient une instance mxGraphComponent.
     */
    private mxGraphComponent graphComponent;

    /**
     * Constructeur par défaut.
     * 
     * @param pointManager du graphique.
     */
    public ForceDirectGraph(PointManager pointManager) {

        this.properties = new Properties();

        try {
            properties.load(ForceDirectGraph.class.getResourceAsStream("config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.startPointX = Integer.parseInt(properties.getProperty("StartGraphX"));
        this.startPointY = Integer.parseInt(properties.getProperty("StartGraphY"));
        this.expansionX = Integer.parseInt(properties.getProperty("ExpendedGraphX"));
        this.expansionY = Integer.parseInt(properties.getProperty("ExpendedGraphY"));


        this.pointManager = pointManager;
        setTitle(properties.getProperty("TitleName"));
        setSize(Integer.parseInt(properties.getProperty("WindowWidth")), Integer.parseInt(properties.getProperty("WindowHeight")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Créer le graphique et l'ajouter au JPanel
        this.graph = new mxGraph();

        graph.getModel().beginUpdate();
        createTotalGraph();
        graph.getModel().endUpdate();
        this.graphComponent = new mxGraphComponent(graph);
        add(graphComponent, BorderLayout.CENTER);

        // Créer le JPanel pour afficher le texte
        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        add(textPanel, BorderLayout.EAST);

        // Créer le JTextArea pour afficher le texte
        textArea = new JTextArea(10,20);
        textArea.setBackground(Color.decode("#ececec"));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        addListener();

    }

    /**
     * Ajouter les écouteurs pour le zoom sur le graphique.
     */
    public void addListener() {
        graphComponent.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    graphComponent.zoomIn();
                } else if (e.getWheelRotation() > 0) {
                    graphComponent.zoomOut();
                }
            }
        });

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && graphComponent.getGraph().getModel().isVertex(cell)) {

                    String cellId = graphComponent.getGraph().convertValueToString(cell);

                    String text = pointManager.getPoint(Integer.parseInt(cellId)).getText();
                    text = text.replace("\\n", " ");
                    text = text.replace("\"", " ");
                    text = text.replaceAll("http.*?\\[Illustration\\]", "");
                    String child = String.valueOf(pointManager.getPoint(Integer.parseInt(cellId)).getChildsID().size());
                    String parent = String.valueOf(pointManager.countParent(Integer.parseInt(cellId)));
                    String size = String.valueOf(graph.getCellGeometry(cell).getHeight());

                    text = "[Id] : "+cellId+"     "+"[Size] : "+size+"\n"+
                            "[Child] : "+child+"     [Parent] : "+parent+"\n==========[Text]==========\n"
                            +text;


                    textArea.setText(text);

                }

            }
        });

    }

    /**
     * Créer le graph total.
     * 1. Créer les noeuds.
     * 2. Créer les arêtes.
     * 3. Appliquer les règles d'interaction.
     * 4. Appliquer les règles de force.
     * 5. Appliquer les règles de déplacement.
     * 6. Appliquer les règles de duplication.
     * 7. Appliquer les règles de suppression.
     * 8. Appliquer les règles de couleur.
     */
    public void createTotalGraph() {
        Random random = new Random();
        graph.getModel().beginUpdate();
        try {
            for (Point parent : pointManager.getPointList()) {
                double x = random.nextInt(expansionX) + startPointX;
                double y = random.nextInt(expansionY) + startPointY;
                int countParent = pointManager.countParent(parent.getID());
                float widthNode = countParent * 10 + 10;

                Object parentVertex = graph.insertVertex(graph.getDefaultParent(), String.valueOf(parent.getID()), parent.getID(), x, y, widthNode, widthNode, "shape=ellipse");
                VertexSortList.put(parent.getID(), countParent);

                graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, getColorParent(countParent), new Object[] { parentVertex });
                //opcaity 50%
                graph.setCellStyles(mxConstants.STYLE_OPACITY, properties.getProperty("NodeOpacity"), new Object[] { parentVertex });
                graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "none", new Object[] { parentVertex });
            }
            VertexSortList = sortByValue(VertexSortList,false);

            createEdges();
            stackPoint(Integer.parseInt(properties.getProperty("ReapeatDisplacement")));
            duplicateAndRemoveVertices();
            interactRules();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Dupliquer et supprimer les noeuds pour les afficher au premier plan (pas utile si l'opacity est en dessous de 50%).
     */
    public void duplicateAndRemoveVertices() {
        // Affiche les noeud du plus petit au plus grand
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        Object[] clonedVertices = graph.cloneCells(vertices);

        graph.getModel().beginUpdate();
        for (Object vertex : clonedVertices) {
            graph.addCell(vertex);
        }
        graph.getModel().remove(vertices);
        graph.getModel().endUpdate();
    }

    /**
     * Appliquer la force de Coulomb entre deux noeuds.
     * 
     * @param node1
     * @param node2
     */
    public void forceExpulsionPoint(Object node1, Object node2) {
        mxGeometry geometry1 = graph.getCellGeometry(node1);
        mxGeometry geometry2 = graph.getCellGeometry(node2);

        double dx = geometry2.getX() - geometry1.getX();
        double dy = geometry2.getY() - geometry1.getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        if (currentDistance < 0.001) {
            geometry2.setX(geometry2.getX() + 0.1);
            geometry2.setY(geometry2.getY() + 0.1);
        } else {
            double k = Double.parseDouble(properties.getProperty("CoulombForce"));
            double forceMagnitude = k / (currentDistance * currentDistance);

            double forceX = forceMagnitude * (dx / currentDistance);
            double forceY = forceMagnitude * (dy / currentDistance);

            geometry2.setX(geometry2.getX() + forceX);
            geometry2.setY(geometry2.getY() + forceY);
        }

        graph.getModel().setGeometry(node2, geometry2);
    }

    /**
     * Appliquer la force de Hooke entre deux noeuds.
     * 
     * @param node1
     * @param node2
     */
    public void forceAttractionPoint(Object node1, Object node2) {
        double dx = graph.getCellGeometry(node1).getX() - graph.getCellGeometry(node2).getX();
        double dy = graph.getCellGeometry(node1).getY() - graph.getCellGeometry(node2).getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        double k = Double.parseDouble(properties.getProperty("HookForce"));
        double force = (-k * currentDistance);

        double forceX =  force * (dx / currentDistance) ;
        double forceY =  force * (dy / currentDistance) ;


        mxGeometry geometry = graph.getCellGeometry(node2);
        geometry.setX(geometry.getX() - forceX);
        geometry.setY(geometry.getY() - forceY);
        graph.getModel().setGeometry(node2, geometry);
    }

    /**
     * Appliquer la force de répulsion pour tout les noeuds dans un rayon donné.
     * 
     * @param targetNode
     */
    public void forceExpulsionWithinRadius(Object targetNode) {
        // Parcourir tous les noeuds
        for (Object node : graph.getChildVertices(graph.getDefaultParent())) {
            if (node != targetNode) {
                mxGeometry source = graph.getCellGeometry(targetNode);
                mxGeometry target = graph.getCellGeometry(node);

                double dx = target.getX() - source.getX();
                double dy = target.getY() - source.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                double nodehitbox = source.getWidth() * 2;
                if (distance < nodehitbox) {
                    // Calculer un vecteur de déplacement basé sur la direction de l'éloignement
                    double displacementX = dx * nodehitbox / distance;
                    double displacementY = dy * nodehitbox / distance;

                    mxGeometry newGeometry = new mxGeometry(target.getX() + displacementX, target.getY() + displacementY, target.getWidth(), target.getHeight());
                    graph.getModel().setGeometry(node, newGeometry);
                }
            }
        }
    }

    /**
     * Appliquer toute les forces sur tout es noeuds dans un ordre donné.
     * 
     * @param repeat
     */
    public void stackPoint(Integer repeat) {
        graph.getModel().beginUpdate();
        try {
            for(int i = 0;i<repeat;i++){
                for (Map.Entry<Integer, Integer> entry : VertexSortList.entrySet()) {
                    Object sourceVertex = getVertex(entry.getKey());

                    for(Object vertexParent : getVerticesParent(getVertex(entry.getKey()))){
                        forceAttractionPoint(sourceVertex, vertexParent);
                    }

                }

                for (Map.Entry<Integer, Integer> entry : VertexSortList.entrySet()) {
                    Object sourceVertex = getVertex(entry.getKey());
                    forceExpulsionWithinRadius(sourceVertex);

                }

            }
        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Créer les arêtes entre les noeuds.
     */
    public void createEdges() {
        graph.getModel().beginUpdate();
        try {
            Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, properties.getProperty("EdgeColor"));
            edgeStyle.put(mxConstants.STYLE_OPACITY, properties.getProperty("EdgeOpacity"));

            for (Point parent : pointManager.getPointList()) {
                Object parentVertex = getVertex(parent.getID());
                for (Point child : parent.getChildsPoint()) {
                    Object childVertex = getVertex(child.getID());
                    graph.insertEdge(graph.getDefaultParent(), null, "", parentVertex, childVertex);

                }

            }
        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Récupérer un noeud par son identifiant.
     * 
     * @param point
     * @return un noeud.
     */
    private Object getVertex(Integer point) {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            Integer vertexId = (Integer) graph.getModel().getValue(vertex);
            if (vertexId.equals(point)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Trier les noeuds par leur nombre de parents.
     * 
     * @param map ???
     * @param asc ???
     * @return ???
     */
    public static Map<Integer, Integer> sortByValue(Map<Integer, Integer> map,boolean asc) {
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());

        if(asc)
            Collections.sort(list, (val1, val2) -> val1.getValue().compareTo(val2.getValue()));
        else
            Collections.sort(list, (val1, val2) -> val2.getValue().compareTo(val1.getValue()));


        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());

        }

        return sortedMap;
    }

    /**
     * Récupérer les noeuds parents d'un noeud donné.
     * 
     * @param vertex ???
     * @return ??
     */
    public List<Object> getVerticesParent(Object vertex) {
        List<Object> verticesPointingToTarget = new ArrayList<>();
        Object[] edges = graph.getChildEdges(graph.getDefaultParent());

        for (Object edge : edges) {
            Object destinationVertex = graph.getModel().getTerminal(edge, false);
            if (destinationVertex.equals(vertex)) {
                Object sourceVertex = graph.getModel().getTerminal(edge, true);
                verticesPointingToTarget.add(sourceVertex);
            }
        }

        return verticesPointingToTarget;
    }

    /**
     * Appliquer les règles d'interaction sur les points du graph.
     */
    private void interactRules() {
        graph.setCellsEditable(Boolean.parseBoolean(properties.getProperty("CellsEditable")));
        graph.setCellsMovable(Boolean.parseBoolean(properties.getProperty("CellsMovable")));
        graph.setCellsResizable(Boolean.parseBoolean(properties.getProperty("CellsResizable")));
        graph.setCellsSelectable(Boolean.parseBoolean(properties.getProperty("CellsSelectable")));
        graph.setCellsDisconnectable(Boolean.parseBoolean(properties.getProperty("CellsDisconnectable")));
        graph.setCellsLocked(Boolean.parseBoolean(properties.getProperty("CellsLocked")));
    }

    /**
     * Récupérer la couleur d'un noeud en fonction de son nombre de parents.
     * 
     * @param count
     * @return ???
     */
    private String getColorParent(int count) {
        int red = (int) (255 * count / 10);
        int green = (int) (255 * (10 - count) / 10);
        String color = String.format("#%02X%02X%02X", red, green, 0);

        return color;
    }



}
