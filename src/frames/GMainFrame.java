package frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GMainFrame extends JFrame {
    // components
    private GMenuBar menuBar;
    private GShapeToolBar toolBar;
    private GDrawingPanel drawingPanel;
    private GAttributePanel attributePanel;
    // associations
    // ...

    public GMainFrame() {
        // attributes
        this.setLocation(200, 200);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // components
        this.menuBar = new GMenuBar();
        this.setJMenuBar(menuBar);

        this.setLayout(new BorderLayout());

        this.toolBar = new GShapeToolBar();
        this.add(toolBar, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        this.attributePanel = new GAttributePanel();
        this.add(attributePanel, BorderLayout.EAST);

        this.drawingPanel.associateWith(this.toolBar);
        this.attributePanel.associateWith(this.drawingPanel);
    }

    private class TooButtonActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}
