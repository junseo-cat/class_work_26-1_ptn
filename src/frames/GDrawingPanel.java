package frames;

import global.GConstants;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GTransformer;
import transformer.GTranslator;
import transformer.GScale;
import transformer.GRotate;
import transformer.GVertexEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {
    // declaration
    private enum EDrawingState {
        eIdle,
        eTransforming
    }
    //associations
    private GShapeToolBar toolBar;
    //attributes
    private EDrawingState eDrawingState;
    private Color bgColor;

    //components
    private BufferedImage bufferImage;
    private final Vector<GShape> shapes;
    private GTransformer transformer;

    // constructors
    public GDrawingPanel() {
        // attributes
        setBackground(bgColor = Color.WHITE);
        eDrawingState = EDrawingState.eIdle;
        // components list
        shapes = new Vector<GShape>();
        bufferImage = null;
        transformer = null;

        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    // setters and getters
    public void associateWith(GShapeToolBar toolBar) {
        this.toolBar = toolBar;
    }

    // methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bufferImage != null) {
            g.drawImage(bufferImage, 0, 0, null);
        }
    }

    private void prepareDrawing() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        if (bufferImage == null
                || bufferImage.getWidth() != getWidth()
                || bufferImage.getHeight() != getHeight()) {
            bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferGraphics = bufferImage.createGraphics();
            bufferGraphics.setColor(getBackground());
            bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
            bufferGraphics.dispose();
        }
    }

    public void redraw() {
        if (this.bufferImage == null
                || this.bufferImage.getWidth() != getWidth()
                || this.bufferImage.getHeight() != getHeight()) {
            this.prepareDrawing();
        }

        if (this.bufferImage == null) {
            return;
        }

        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(getForeground());

        for (GShape shape : shapes) {
            if (shape != null) {
                shape.draw(bufferGraphics);

                if (toolBar != null
                        && toolBar.getShapeType() == GConstants.EShapeType.eVertexSelect) {
                    shape.drawVertexAnchors(bufferGraphics);
                }
            }
        }

        bufferGraphics.dispose();
        repaint();
    }

    private void clearSelection() {
        for (GShape s : shapes) {
            s.setSelected(false);
        }
    }

    public GShape getSelectedShape() {
        for (GShape shape : shapes) {
            if (shape.isSelected()) {
                return shape;
            }
        }
        return null;
    }

    //-------------Transform

    private void startTransform(int x, int y) {
        if (toolBar.getShapeType() == GConstants.EShapeType.eVertexSelect) {
            clearSelection();
            redraw();
            startVertexTransform(x, y);
        } else if (toolBar.getShapeType() == GConstants.EShapeType.eSelect) { // context
            //startSelectedTransform
            for (GShape shape : shapes) {
                GShape.EAnchor eAnchor = shape.onShape(x, y);

                if (eAnchor != null) {
                    clearSelection();
                    shape.setSelected(true);

                    this.redraw();

                    if (eAnchor == GShape.EAnchor.eRotate) {
                        this.transformer = new GRotate(shape);
                    } else if (eAnchor == GShape.EAnchor.eMove) {
                        this.transformer = new GTranslator(shape);
                    } else { // resize
                        this.transformer = new GScale(shape, eAnchor);
                    }
                    this.transformer.start(x,y);
                    break;
                }
            }
        }else if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
            //start2PointDrawing
            GShape currentShape = toolBar.getShapeType().getShape();

            clearSelection();

            shapes.add(currentShape);

            transformer = new GDrawer(currentShape);
            transformer.start(x, y);

            //currentShape.setSelected(true);
            redraw();
        } else if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) {
            // startNPointDrawing: 첫 점만 담당
            GShape currentShape = toolBar.getShapeType().getShape();

            clearSelection();
            shapes.add(currentShape);

            transformer = new GDrawer(currentShape);
            transformer.start(x, y);

            currentShape.setSelected(false);

            redraw();
        }
        this.prepareDrawing();
    }
    private void startVertexTransform(int x, int y) {
        boolean found = false;

        for (int i = shapes.size() - 1; i >= 0; i--) {
            GShape shape = shapes.get(i);
            int vertexIndex = shape.onVertex(x, y);

            if (vertexIndex != -1) {
                found = true;

                clearSelection();
                shape.setSelected(false);

                this.transformer = new GVertexEditor(shape, vertexIndex);
                this.transformer.start(x, y);

                redraw();
                break;
            }
        }

        if (!found) {
            this.transformer = null;
            redraw();
        }
    }

    //keepT
    private void keepTransform(int x, int y) {
        if (this.transformer == null) {
            return;
        }

        this.transformer.keep(x, y);
        this.redraw();
    }

    private void continueDrawing(int x, int y) {
        if (this.transformer == null) {
            return;
        }

        GShape currentShape = this.transformer.getShape();

        if (currentShape.isNearFirstPoint(x, y)) {
            currentShape.closeShape();
            //currentShape.setSelected(true);

            this.finishTransform(x, y);
            this.eDrawingState = EDrawingState.eIdle;

            this.redraw();
        } else {
            this.transformer.cont(x, y);
            this.redraw();
        }
    }

    private void finishTransform(int x, int y) {
        if (this.transformer != null) {
            this.transformer.finish(x, y);
            this.transformer = null;
        }
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 1) { // left button
                if (e.getClickCount() == 1) { // single click
                    mouseLButton1Clocked(e);
                } else if (e.getClickCount() == 2) { // double click
                    mouseLButton2Clocked(e);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
                if (eDrawingState == EDrawingState.eTransforming) { // target state
                    keepTransform(e.getX(), e.getY());
                }
            }
        }

        private void mouseLButton1Clocked(MouseEvent e) {
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
                if (eDrawingState == EDrawingState.eIdle) { // target state
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eTransforming;
                } else {
                    continueDrawing(e.getX(), e.getY());
                }
            }
        }

        private void mouseLButton2Clocked(MouseEvent e) {
            if (eDrawingState == EDrawingState.eTransforming) {
                if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
                    finishTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eIdle;
                } else if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) {
                    if (transformer != null) {
                        GShape currentShape = transformer.getShape();
                        currentShape.closeShape();
                        //currentShape.setSelected(true);
                        finishTransform(e.getX(), e.getY());
                        eDrawingState = EDrawingState.eIdle;
                        redraw();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
                if (eDrawingState == EDrawingState.eIdle) { // target state
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eTransforming;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
                if (eDrawingState == EDrawingState.eTransforming) {
                    keepTransform(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
                if (eDrawingState == EDrawingState.eTransforming) {
                    finishTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eIdle;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}