package transformer;

import shapes.GShape;

import java.awt.*;

public class GRotate extends GTransformer {
    private Point localCenter;
    private Point screenCenter;
    private double startAngle;

    public GRotate(GShape shape) {
        super(shape);
    }

//    @Override
//    public void start(int x, int y) {
//        Rectangle bounds = shape.getTransformedBounds();
//
//        this.center = new Point(
//                bounds.x + bounds.width / 2,
//                bounds.y + bounds.height / 2
//        );
//
//        this.startAngle = Math.atan2(y - center.y, x - center.x);
//    }

    @Override
    public void start(int x, int y) {
        Rectangle localBounds = shape.getShape().getBounds();

        this.localCenter = new Point(
                localBounds.x + localBounds.width / 2,
                localBounds.y + localBounds.height / 2
        );

        Point transformedCenter = new Point();
        shape.getAffineTransform().transform(this.localCenter, transformedCenter);
        this.screenCenter = transformedCenter;

        this.startAngle = Math.atan2(y - screenCenter.y, x - screenCenter.x);
    }

//    @Override
//    public void keep(int x, int y) {
//        double currentAngle = Math.atan2(y - center.y, x - center.x);
//        double dAngle = currentAngle - this.startAngle;
//
//        shape.getAffineTransform().rotate(dAngle, center.x, center.y);
//
//        this.startAngle = currentAngle;
//    }

    @Override
    public void keep(int x, int y) {
        double currentAngle = Math.atan2(y - screenCenter.y, x - screenCenter.x);
        double dAngle = currentAngle - this.startAngle;

        shape.getAffineTransform().rotate(dAngle, localCenter.x, localCenter.y);

        shape.getAffineTransform().transform(this.localCenter, this.screenCenter);

        this.startAngle = currentAngle;
    }

    @Override
    public void finish(int x, int y) {
        // 현재는 마무리 작업 없음
    }
}