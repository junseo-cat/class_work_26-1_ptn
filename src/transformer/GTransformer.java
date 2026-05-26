package transformer;

import shapes.GShape;

import java.awt.*;

public abstract class GTransformer {
    protected GShape shape;
    public GTransformer(GShape shape) {
        this.shape = shape;
    }

    abstract public void start(int x, int y);
    public abstract void keep(int x, int y);
    public abstract void finish(int x, int y);
    public void cont(int x, int y) {

    }
}
