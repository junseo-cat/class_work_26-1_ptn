package frames;

import global.GConstants;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GTransformer;

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


    //components
    private BufferedImage bufferImage;
    private final Vector<GShape> shapes;
    private GTransformer transformer;

    // constructors
    public GDrawingPanel() {
        // attributes
        setBackground(Color.WHITE);
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

    private void startTransform(int x, int y) {
        if (toolBar.getShapeType() == GConstants.EShapeType.eSelect) { // context
            for (GShape shape : shapes) {
                GShape.EAnchor eAnchor = shape.onShape(x, y);
                if (eAnchor != null) {
                    if (eAnchor == GShape.EAnchor.eRotate) {
                        this.transformer = new GDrawer(shape);
                    } else if (eAnchor == GShape.EAnchor.eMove) {
                        this.transformer = new GDrawer(shape);
                    } else if (eAnchor == GShape.EAnchor.eResize) {
                        this.transformer = new GDrawer(shape);
                    }
                    this.transformer.start(x,y);
                    break;
                }
            }
        }else {
            GShape currentShape = toolBar.getShapeType().getShape();
            shapes.add(currentShape);

            this.transformer = new GDrawer(currentShape);
            this.transformer.start(x,y);

        }
        this.prepareDrawing();
    }

    private void keepTransform(int x, int y) {
        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(getForeground());

        this.transformer.keep(x,y);

//        if (eDrawingState == EDrawingState.eDrawing) {
//            currentShape.setLocation1(x, y);
//            currentShape.draw(bufferGraphics);
//        } else if (eDrawingState == EDrawingState.eMoving) {
//            currentShape.move(x, y);
//        } else if (eDrawingState == EDrawingState.eResizing) {
//            currentShape.resize(x, y);
//        } else if (eDrawingState == EDrawingState.eRotating) {
//            currentShape.rotate(x, y);
//        }

        for (GShape shape : shapes) {
            if (shape != null) {
                shape.draw(bufferGraphics);
            }
        }
        bufferGraphics.dispose();
        repaint();
    }

    private void continueDrawing(int x, int y) {

    }

    private void finishTransform(int x, int y) {
        this.transformer.finish(x,y);
        this.transformer = null;
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
            if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
                if (eDrawingState == EDrawingState.eTransforming) {
                    finishTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eIdle;
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