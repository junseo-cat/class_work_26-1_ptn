package transformer;

import shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class GScale extends GTransformer {
    private Point2D p0;
    private GShape.EAnchor eAnchor;

    public GScale(GShape shape, GShape.EAnchor eAnchor) {
        super(shape);
        this.eAnchor = eAnchor;
    }

    @Override
    public void start(int x, int y) {
        this.p0 = new Point2D.Double(x, y);
    }

    @Override
    public void keep(int x, int y) {
        Point2D pCurrent = new Point2D.Double(x, y);

        Rectangle r = shape.getShape().getBounds();
        double w = r.getWidth();
        double h = r.getHeight();

        double sx = 1.0;
        double sy = 1.0;
        double tx = 0;
        double ty = 0;

        Point2D p0Local = new Point2D.Double();
        Point2D p1Local = new Point2D.Double();

        try {
            shape.getAffineTransform().inverseTransform(this.p0, p0Local);
            shape.getAffineTransform().inverseTransform(pCurrent, p1Local);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        double dx = p1Local.getX() - p0Local.getX();
        double dy = p1Local.getY() - p0Local.getY();

        switch (eAnchor) {
            case eEE:
                sx = (w + dx) / w;
                tx = r.getX();
                ty = r.getY();
                break;

            case eWW:
                sx = (w - dx) / w;
                tx = r.getX() + w;
                ty = r.getY();
                break;

            case eSS:
                sy = (h + dy) / h;
                tx = r.getX();
                ty = r.getY();
                break;

            case eNN:
                sy = (h - dy) / h;
                tx = r.getX();
                ty = r.getY() + h;
                break;

            case eSE:
                sx = (w + dx) / w;
                sy = (h + dy) / h;
                tx = r.getX();
                ty = r.getY();
                break;

            case eSW:
                sx = (w - dx) / w;
                sy = (h + dy) / h;
                tx = r.getX() + w;
                ty = r.getY();
                break;

            case eNE:
                sx = (w + dx) / w;
                sy = (h - dy) / h;
                tx = r.getX();
                ty = r.getY() + h;
                break;

            case eNW:
                sx = (w - dx) / w;
                sy = (h - dy) / h;
                tx = r.getX() + w;
                ty = r.getY() + h;
                break;

            default:
                return;
        }

        if (sx > 0 && sy > 0) {
            AffineTransform affineTransform = shape.getAffineTransform();
            affineTransform.translate(tx, ty);
            affineTransform.scale(sx, sy);
            affineTransform.translate(-tx, -ty);
        }

        this.p0 = pCurrent;
    }

    @Override
    public void finish(int x, int y) {
        // 현재는 마무리 작업 없음
    }
}
