package transformer;

import shapes.GShape;

import java.awt.*;

public class GDrawer extends GTransformer {
    public GDrawer(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {

    }

    @Override
    public void keep(int x, int y) {

    }

    @Override
    public void finish(int x, int y) {

    }

    @Override
    public void cont(int x, int y) {
        super.cont(x, y);
    }
}
