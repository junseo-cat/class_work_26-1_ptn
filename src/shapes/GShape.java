package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.InvocationTargetException;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import java.awt.geom.Path2D;

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

    protected AffineTransform affineTransform;

    protected Shape shape;
    private  Anchors anchors;

    private Color fillColor;
    private Color lineColor;
    private float strokeWidth;
    private boolean filled;

    public GShape() {
        this.isSelected = false;
        this.anchors = new Anchors();

        this.fillColor = Color.WHITE;
        this.lineColor = Color.BLACK;
        this.strokeWidth = 1.0f;
        this.filled = false;

        this.affineTransform = new AffineTransform();

    }

    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();

            if (this.shape instanceof RectangularShape) {
                cloned.shape = (Shape) (((RectangularShape) this.shape).clone());
            } else if (this.shape instanceof Path2D) {
                cloned.shape = (Shape) ((Path2D) this.shape).clone();
            } else {
                cloned.shape = this.shape;
            }

            cloned.affineTransform = (AffineTransform) this.affineTransform.clone();
            cloned.anchors = new Anchors();

            cloned.fillColor = this.fillColor;
            cloned.lineColor = this.lineColor;
            cloned.strokeWidth = this.strokeWidth;
            cloned.filled = this.filled;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    //getter setter
    public AffineTransform getAffineTransform() {
        return this.affineTransform;
    }
    public Shape getShape() {
        return this.shape;
    }
    public Shape getTransformedShape() {
        return this.affineTransform.createTransformedShape(this.shape);
    }
    public Rectangle getTransformedBounds() {
        return this.getTransformedShape().getBounds();
    }

    //(컬러)속성 게터세터
    public Color getFillColor() {
        return this.fillColor;
    }
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        this.filled = true;
    }
    public Color getLineColor() {
        return this.lineColor;
    }
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    public float getStrokeWidth() {
        return this.strokeWidth;
    }
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
    public boolean isFilled() {
        return this.filled;
    }
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            this.anchors.setPosition(this.shape, this.affineTransform);
        }
    }

    public EAnchor onShape(int x, int y) {
        EAnchor eAnchor = null;
        if (this.isSelected) {
            eAnchor = this.anchors.onShape(x, y);
        }
        if (eAnchor == null) {
            try {
                AffineTransform inverseTransform = this.affineTransform.createInverse();

                Point2D mousePoint = new Point2D.Double(x, y);
                Point2D transformedMousePoint = inverseTransform.transform(mousePoint, null);

                if (this.shape.contains(transformedMousePoint)) {
                    eAnchor = EAnchor.eMove;
                }
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
        }
        return eAnchor;
    }

    public void draw(Graphics2D g) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        if (this.filled) {
            g.setColor(this.fillColor);
            g.fill(transformedShape);
        }

        g.setColor(this.lineColor);
        g.setStroke(new BasicStroke(this.strokeWidth));
        g.draw(transformedShape);

        g.setColor(oldColor);
        g.setStroke(oldStroke);

        if (this.isSelected) {
            this.anchors.setPosition(this.shape, this.affineTransform);
            this.anchors.draw(g);
        }
    }

    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void translate(int dx, int dy) {}

    public void addPoint(int x, int y) {}
    public boolean isNearFirstPoint(int x, int y) {
        return false;
    }
    public void closeShape() {}

    public int onVertex(int x, int y) {
        return -1;
    }
    public void moveVertex(int index, int x, int y) {
    }
    public void drawVertexAnchors(Graphics2D g) {
    }


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

//        public void setPosition(Rectangle br) {
//            int brw = br.width;
//            int brh = br.height;
//            int brx = br.x - this.w/2;
//            int bry = br.y - this.h/2;
//            this.anchors[EAnchor.eNW.ordinal()].setFrame(brx, bry, w, h);
//            this.anchors[EAnchor.eNN.ordinal()].setFrame(brx + brw/2, bry, w, h);
//            this.anchors[EAnchor.eNE.ordinal()].setFrame(brx +brw, bry, w, h);
//            this.anchors[EAnchor.eEE.ordinal()].setFrame(brx +brw, bry + brh/2, w, h);
//            this.anchors[EAnchor.eSE.ordinal()].setFrame(brx + brw, bry + brh, w, h);
//            this.anchors[EAnchor.eSS.ordinal()].setFrame(brx + brw/2, bry + brh, w, h);
//            this.anchors[EAnchor.eSW.ordinal()].setFrame(brx, bry + brh, w, h);
//            this.anchors[EAnchor.eWW.ordinal()].setFrame(brx, bry + brh/2, w, h);
//            this.anchors[EAnchor.eRotate.ordinal()].setFrame(brx + brw/2, bry - h*3, w, h);
//        }
        public void setPosition(Shape shape, AffineTransform affineTransform) {
            Rectangle br = shape.getBounds();

            double x = br.getX();
            double y = br.getY();
            double brw = br.getWidth();
            double brh = br.getHeight();

            setAnchor(EAnchor.eNW, x, y, affineTransform);
            setAnchor(EAnchor.eNN, x + brw / 2, y, affineTransform);
            setAnchor(EAnchor.eNE, x + brw, y, affineTransform);
            setAnchor(EAnchor.eEE, x + brw, y + brh / 2, affineTransform);
            setAnchor(EAnchor.eSE, x + brw, y + brh, affineTransform);
            setAnchor(EAnchor.eSS, x + brw / 2, y + brh, affineTransform);
            setAnchor(EAnchor.eSW, x, y + brh, affineTransform);
            setAnchor(EAnchor.eWW, x, y + brh / 2, affineTransform);

            setAnchor(EAnchor.eRotate, x + brw / 2, y - this.h * 3, affineTransform);
        }
        private void setAnchor(EAnchor eAnchor, double x, double y, AffineTransform affineTransform) {
            Point2D src = new Point2D.Double(x, y);
            Point2D dst = affineTransform.transform(src, null);

            this.anchors[eAnchor.ordinal()].setFrame(
                    dst.getX() - this.w / 2.0,
                    dst.getY() - this.h / 2.0,
                    this.w,
                    this.h
            );
        }

        public void draw (Graphics2D g) {
            for (int i = 0; i < anchors.length; i++) {
                g.draw(anchors[i]);
            }
        }
    }
}
