package shapes;

import java.awt.*;

public class GRectangle extends GShape{

    public GRectangle() {
        this.shape = new Rectangle();
    }


    public void setLocation0(int x, int y) {
        Rectangle r = (Rectangle) shape;
        r.setFrame(x, y, 0,0);
    }
    public void setLocation1(int x, int y) {
        Rectangle r = (Rectangle) shape;
        int w = x - r.x;
        int h = y - r.y;
        r.setFrame(r.x, r.y, 0,0);
    }
    public void translate(int dx, int dy) {
        Rectangle r = (Rectangle) shape;
        r.setFrame(r.x + dx, r.y + dy, r.width, r.height);
    }
}
