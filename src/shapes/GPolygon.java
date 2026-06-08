package shapes;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class GPolygon extends GShape {
    private Vector<Point2D> points;
    private Point2D movingPoint;
    private boolean closed;

    public GPolygon() {
        this.points = new Vector<Point2D>();
        this.movingPoint = null;
        this.closed = false;
        this.shape = new Path2D.Double();
    }

    @Override
    public void setLocation0(int x, int y) {
        this.points.clear();
        this.points.add(new Point2D.Double(x, y));
        this.movingPoint = new Point2D.Double(x, y);
        this.closed = false;
        this.rebuildPath();
    }
    @Override
    public void setLocation1(int x, int y) {
        if (this.closed) {
            return;
        }

        if (this.points.isEmpty()) {
            return;
        }

        this.movingPoint = new Point2D.Double(x, y);
        this.rebuildPath();
    }

    @Override
    public void addPoint(int x, int y) {
        if (this.closed) {
            return;
        }

        this.points.add(new Point2D.Double(x, y));
        this.movingPoint = new Point2D.Double(x, y);
        this.rebuildPath();
    }

    @Override
    public boolean isNearFirstPoint(int x, int y) {
        if (this.points.size() < 3) {
            return false;
        }

        Point2D firstPoint = this.points.get(0);

        double dx = x - firstPoint.getX();
        double dy = y - firstPoint.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= 10;
    }

    @Override
    public void closeShape() {
        if (this.points.size() < 3) {
            return;
        }

        this.closed = true;
        this.movingPoint = null;
        this.rebuildPath();
    }

    private void rebuildPath() {
        Path2D path = new Path2D.Double();

        if (this.points.isEmpty()) {
            this.shape = path;
            return;
        }

        Point2D firstPoint = this.points.get(0);
        path.moveTo(firstPoint.getX(), firstPoint.getY());

        for (int i = 1; i < this.points.size(); i++) {
            Point2D point = this.points.get(i);
            path.lineTo(point.getX(), point.getY());
        }

        if (this.closed) {
            path.closePath();
        } else if (this.movingPoint != null) {
            path.lineTo(this.movingPoint.getX(), this.movingPoint.getY());
        }

        this.shape = path;
    }

    @Override
    public GShape clone() {
        GPolygon cloned = (GPolygon) super.clone();

        cloned.points = new Vector<Point2D>();
        for (Point2D point : this.points) {
            cloned.points.add((Point2D) point.clone());
        }

        if (this.movingPoint != null) {
            cloned.movingPoint = (Point2D) this.movingPoint.clone();
        } else {
            cloned.movingPoint = null;
        }

        cloned.closed = this.closed;
        cloned.rebuildPath();

        return cloned;
    }

}
