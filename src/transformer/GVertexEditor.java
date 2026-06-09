package transformer;

import shapes.GShape;

public class GVertexEditor extends GTransformer {
    private int vertexIndex;

    public GVertexEditor(GShape shape, int vertexIndex) {
        super(shape);
        this.vertexIndex = vertexIndex;
    }

    @Override
    public void start(int x, int y) {
        //
    }

    @Override
    public void keep(int x, int y) {
        this.shape.moveVertex(this.vertexIndex, x, y);
    }

    @Override
    public void finish(int x, int y) {
        //
    }
}