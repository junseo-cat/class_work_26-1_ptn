import javax.swing.*;

public class GToolBar extends JToolBar {
    private  JRadioButton rectangleButton;
    private JRadioButton ovalButton;
    private ButtonGroup buttonGroup;

    public GToolBar() {
        ButtonGroup group = new ButtonGroup();
        this.rectangleButton = new JRadioButton("Rectangle");
        this.add(rectangleButton);
        group.add(rectangleButton);

        this.ovalButton = new JRadioButton("oval");
        this.add(ovalButton);
        group.add(ovalButton);
    }
    //test
}

