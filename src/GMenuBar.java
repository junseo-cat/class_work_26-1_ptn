import javax.swing.*;

public class GMenuBar extends JMenuBar {
    private GFileMenu fileMenu;

    public GMenuBar() {
        this.fileMenu = new GFileMenu();
        this.add(fileMenu);
    }
}
