package main.view;

import main.model.Point;
import main.model.PointManager;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;

/**
 * Représentation d'un graphe utilisant un algorithme de force.
 * C'est une vue contrôleur qui gère l'affichage et l'interaction avec les points du graphe.
 *
 * @author Tom David
 * @version 2.0
 */
public class ForceDirectGraph extends JPanel {

    private mxGraph graph;
    private PointManager pointManager;
    private int startPointX;
    private int startPointY;
    private int expansionX;
    private int expansionY;
    private double coulombForce;
    private double hookForce;
    private Map<Integer,Integer> VertexSortList = new HashMap<>() ;
    private mxGraphComponent graphComponent;

    public ForceDirectGraph(PointManager pointManager) {
        this.pointManager = pointManager;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("./resources/config.properties"));
            this.startPointX = Integer.parseInt(prop.getProperty("StartGraphX"));
            this.startPointY = Integer.parseInt(prop.getProperty("StartGraphY"));
            this.expansionX = Integer.parseInt(prop.getProperty("ExpendedGraphX"));
            this.expansionY = Integer.parseInt(prop.getProperty("ExpendedGraphY"));
            this.coulombForce = Double.parseDouble(prop.getProperty("CoulombForce"));
            this.hookForce = Double.parseDouble(prop.getProperty("HookForce"));
        } catch (IOException ex) {
            this.startPointX = 2000;
            this.startPointY = 2000;
            this.expansionX = 200;
            this.expansionY = 200;
            this.coulombForce = 70;
            this.hookForce = 0.9;
        }

        setLayout(new BorderLayout());
        this.graph = new mxGraph();
        this.graph.getModel().beginUpdate();
        this.createTotalGraph();
        this.graph.getModel().endUpdate();
        this.graphComponent = new mxGraphComponent(this.graph);
        this.graphComponent.setConnectable(false);
        this.add(this.graphComponent, BorderLayout.CENTER);
        this.graphComponent.zoomAndCenter();
        this.addListener();

    }

    /**
     * Ajouter les écouteurs pour gérer les interactions de la souris.
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
                    Integer vertexId = (Integer) graph.getModel().getValue(cell);
                    ForceDirectGraph.this.pointManager.setCurrentPoint(ForceDirectGraph.this.pointManager.getPoint(vertexId));
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
            for (Point parent : pointManager.getPoints()) {
                double x = random.nextInt(expansionX) + startPointX;
                double y = random.nextInt(expansionY) + startPointY;
                int countParent = pointManager.getParent(parent.getID());
                float widthNode = countParent * 10 + 10;

                Object parentVertex = graph.insertVertex(graph.getDefaultParent(), String.valueOf(parent.getID()), parent.getID(), x, y, widthNode, widthNode, "shape=ellipse");
                VertexSortList.put(parent.getID(), countParent);

                graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, getColorParent(countParent), new Object[] { parentVertex });
                graph.setCellStyles(mxConstants.STYLE_OPACITY, "50", new Object[] { parentVertex });
                graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "none", new Object[] { parentVertex });
            }
            VertexSortList = sortByValue(VertexSortList,false);
            createEdges();
            stackPoint(10);
            duplicateAndRemoveVertices();
            interactRules();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Dupliquer et supprimer les nœuds pour les afficher au premier plan.
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
     * Appliquer la force de Coulomb entre deux nœuds.
     *
     * @param node1 le premier nœud.
     * @param node2 le deuxième nœud.
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
            double k = this.coulombForce;
            double forceMagnitude = k / (currentDistance * currentDistance);

            double forceX = forceMagnitude * (dx / currentDistance);
            double forceY = forceMagnitude * (dy / currentDistance);

            geometry2.setX(geometry2.getX() + forceX);
            geometry2.setY(geometry2.getY() + forceY);
        }

        graph.getModel().setGeometry(node2, geometry2);
    }

    /**
     * Appliquer la force de Hooke entre deux nœuds.
     *
     * @param node1 le premier nœud.
     * @param node2 le deuxième nœud.
     */
    public void forceAttractionPoint(Object node1, Object node2) {
        double dx = graph.getCellGeometry(node1).getX() - graph.getCellGeometry(node2).getX();
        double dy = graph.getCellGeometry(node1).getY() - graph.getCellGeometry(node2).getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        double k = this.hookForce;
        double force = (-k * currentDistance);

        double forceX =  force * (dx / currentDistance) ;
        double forceY =  force * (dy / currentDistance) ;


        mxGeometry geometry = graph.getCellGeometry(node2);
        geometry.setX(geometry.getX() - forceX);
        geometry.setY(geometry.getY() - forceY);
        graph.getModel().setGeometry(node2, geometry);
    }

    /**
     * Appliquer la force de répulsion pour tous les nœuds dans un rayon donné.
     *
     * @param targetNode le nœud cible à expulser.
     */
    public void forceExpulsionWithinRadius(Object targetNode) {
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
     * Appliquer toutes les forces sur tous les nœuds dans un ordre donné.
     *
     * @param repeat le nombre de fois que les forces seront appliquées.
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
     * Créer les arêtes entre les nœuds.
     */
    public void createEdges() {
        graph.getModel().beginUpdate();
        try {
            Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#34495E");
            edgeStyle.put(mxConstants.STYLE_OPACITY, 30);

            for (Point parent : pointManager.getPoints()) {
                Object parentVertex = getVertex(parent.getID());
                for (Point child : parent.getChilds()) {
                    Object childVertex = getVertex(child.getID());
                    graph.insertEdge(graph.getDefaultParent(), null, "", parentVertex, childVertex);

                }

            }
        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Récupérer un nœud par son identifiant.
     *
     * @param point l'identifiant du point.
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
     * Trier les nœuds par leur nombre de parents.
     *
     * @param map la map à trier.
     * @param asc si true, trie par ordre croissant, sinon par ordre décroissant.
     * @return une map triée par valeur.
     */
    public static Map<Integer, Integer> sortByValue(Map<Integer, Integer> map, boolean asc) {
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
     * Récupérer les nœuds parents d'un nœud donné.
     *
     * @param vertex le nœud dont on veut les parents.
     * @return une liste de nœuds parents.
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
        graph.setCellsEditable(false);
        graph.setCellsMovable(false);
        graph.setCellsResizable(false);
        graph.setCellsSelectable(false);
        graph.setCellsDisconnectable(false);
        graph.setCellsLocked(true);
    }

    /**
     * Récupérer la couleur d'un noeud en fonction de son nombre de parents.
     *
     * @param count le nombre de parents du noeud.
     * @return la couleur au format hexadécimal.
     */
    private String getColorParent(int count) {
        int red = (int) (255 * count / 10);
        int green = (int) (255 * (10 - count) / 10);
        String color = String.format("#%02X%02X%02X", red, green, 0);
        return color;
    }

}