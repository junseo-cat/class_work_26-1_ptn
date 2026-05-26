package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{

    public GOval() {
        this.shape = new Ellipse2D.Double();
    }

    public void draw(Graphics2D g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setColor(Color.BLACK);
        g2D.drawOval(x0, y0, x1 - x0, y1 - y0);
    }

}
