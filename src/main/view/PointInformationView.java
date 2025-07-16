package main.view;

import main.model.PointManager;
import main.utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue d'information des points dans l'interface graphique.
 * Affiche les informations du point courant.
 *
 * @author Florian Pépin
 * @version 2.0
 */
public class PointInformationView extends JPanel implements ModelListener {

    private PointManager pointManager;
    private JTextArea textArea;

    public PointInformationView(PointManager pointManager) {
        this.pointManager = pointManager;
        this.pointManager.addModelListener(this);
        this.textArea = new JTextArea(34,20);
        this.textArea.setBackground(Color.decode("#ececec"));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane, BorderLayout.CENTER);
        this.textArea.setText("");
    }

    /**
     * Met à jour le texte affiché dans la zone de texte
     */
    public void updateText() {
        String textPoint = this.pointManager.getCurrentPoint().getText();
        textPoint = textPoint.replace("\\n", " ");
        textPoint = textPoint.replace("\"", " ");
        textPoint = textPoint.replaceAll("http.*?\\[Illustration\\]", "");
        String child = String.valueOf(this.pointManager.getCurrentPoint().getChildsID().size());
        String parent = String.valueOf(pointManager.getParent(this.pointManager.getCurrentPoint().getID()));
        int cellId = this.pointManager.getCurrentPoint().getID();
        String res = "[ID] : "+cellId+"     "+"[Enfants] : "+child+"     [Parents] : "+ parent+"\n==========[Text]==========\n"+textPoint;
        this.textArea.setText(res);
    }

    @Override
    public void updatedModel(Object source) {
        this.updateText();
    }

}
