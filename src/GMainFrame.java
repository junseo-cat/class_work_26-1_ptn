import javax.swing.*;
import java.awt.*;

public class GMainFrame extends JFrame {
    //attribute , 속성입니다~
    private int size;

    // 부품입니다~
    private GMenuBar menuBar;
    private GToolBar toolBar;
    private GDrawingPanel drawingPanel;

    //친구요? association
    //private GDirectory directory;

    //생성자
    public GMainFrame() {
        //set attributes
        //title
        super("GMainFrame");
        //size
        this.setSize(800, 600);
        //close function
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        // create aggregation
        this.menuBar = new GMenuBar();
        this.setJMenuBar(menuBar);

        this.toolBar = new GToolBar();
        this.add(toolBar);
        this.add(this.toolBar, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel);
        this.add(this.drawingPanel, BorderLayout.CENTER);
    }
    //member function
}
