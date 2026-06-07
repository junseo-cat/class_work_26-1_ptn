package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.InvocationTargetException;

public abstract class GShape implements Cloneable{

    public enum EAnchor {
        eNW,
        eNN,
        eNE,
        eEE,
        eSE,
        eSS,
        eSW,
        eWW,
        eRotate,

        eMove, //도형 자체
    }
    private boolean isSelected;

    protected Shape shape;
    private  Anchors anchors;


    public GShape() {
        this.isSelected = false;
        this.anchors = new Anchors();
    }

    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            cloned.shape = (Shape) (((RectangularShape) this.shape).clone());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    //getter setter
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            this.anchors.setPosition(this.shape.getBounds());
        }
    }

    public EAnchor onShape(int x, int y) {
        EAnchor eAnchor = null;
        if (this.isSelected) {
            eAnchor = this.anchors.onShape(x, y);
        }
        if (eAnchor == null) {
            if (this.shape.contains(x, y)) {
                eAnchor = EAnchor.eMove;
            }
        }
        return eAnchor;
    }

    public void draw(Graphics2D g) {
        if (this.isSelected) {
            this.anchors.draw(g);
        }
        g.draw(shape);
    }

    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void translate(int dx, int dy) {}


    private static class Anchors {
        public  int w = 15;
        public  int h = 15;

        private final Ellipse2D[] anchors;

        public Anchors() {
            anchors = new Ellipse2D[EAnchor.values().length-1];
            for (int i = 0; i < anchors.length; i++) {
                this.anchors[i] = new Ellipse2D.Float();
            }
        }

        public EAnchor onShape(int x, int y) {
            for (int i = 0; i < anchors.length; i++) {
                if (this.anchors[i].contains(x, y)) {
                    return EAnchor.values()[i];
                }
            }
            return null;
        }

        public void setPosition(Rectangle br) {
            int brw = br.width;
            int brh = br.height;
            int brx = br.x - this.w/2;
            int bry = br.y - this.h/2;
            this.anchors[EAnchor.eNW.ordinal()].setFrame(brx, bry, w, h);
            this.anchors[EAnchor.eNN.ordinal()].setFrame(brx + brw/2, bry, w, h);
            this.anchors[EAnchor.eNE.ordinal()].setFrame(brx +brw, bry, w, h);
            this.anchors[EAnchor.eEE.ordinal()].setFrame(brx +brw, bry + brh/2, w, h);
            this.anchors[EAnchor.eSE.ordinal()].setFrame(brx + brw, bry + brh, w, h);
            this.anchors[EAnchor.eSS.ordinal()].setFrame(brx + brw/2, bry + brh, w, h);
            this.anchors[EAnchor.eSW.ordinal()].setFrame(brx, bry + brh, w, h);
            this.anchors[EAnchor.eWW.ordinal()].setFrame(brx, bry + brh/2, w, h);
            this.anchors[EAnchor.eRotate.ordinal()].setFrame(brx + brw/2, bry - h*3, w, h);
        }

        public void draw (Graphics2D g) {
            for (int i = 0; i < anchors.length; i++) {
                g.draw(anchors[i]);
            }
        }
    }
}
