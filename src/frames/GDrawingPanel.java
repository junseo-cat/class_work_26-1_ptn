package frames;

import global.GConstants;
import shapes.GRectangle;
import shapes.GShape;
import shapes.GOval;
import shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {

    private GShapeToolBar toolBar;
    public void associateWith(GShapeToolBar toolBar) {
        this.toolBar = toolBar;
    }

    private enum EDrawingState {
        eIdle,
        eDrawing,
        eMoving,
        eResizing,
        eRotating,
        eShearing
    }
    private EDrawingState eDrawingState;

    private BufferedImage bufferImage;
    private Vector<GShape> shapes;
    private GShape currentShape;

    // constructors
    public GDrawingPanel() {
        // attributes
        this.setBackground(Color.WHITE);
        this.eDrawingState = EDrawingState.eIdle;
        // components list
        this.shapes = new Vector<GShape>();

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        if (g != null) {
            g.drawImage(this.bufferImage, 0, 0, null);
        }
    }
    //RectangularShape
    private void startDrawing(int x, int y) {
        if (getWidth() <= 0 || getHeight() <= 0 || eDrawingState == EDrawingState.eIdle) {
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
    //transform
    private void startTransform(int x, int y) {
        for (GShape shape : shapes) {
            GShape.EAnchor eAnchor = shape.onShape(x, y);
            if (eAnchor != null) {
                if (eAnchor == GShape.EAnchor.eRotate) {
                    eDrawingState = EDrawingState.eRotating;
                } else if (eAnchor == GShape.EAnchor.eMove) {
                    eDrawingState = EDrawingState.eMoving;
                } else { // resize
                    eDrawingState = EDrawingState.eResizing;
                }
                currentShape = shape;
                break;
            }
        }
    }
    // new shape
    private void startNewShape(int x, int y) {
        if (toolBar.getShapeType() == GConstants.EShapeType.eOval) {
            currentShape = new GOval(x, y, x, y);
        } else if (toolBar.getShapeType() == GConstants.EShapeType.eRectangle) {
            currentShape = new GRectangle(x, y, x, y);
        }
    }



    /// 사각형 쉐입에서 트랜스폼으로 이름바뀜
    private void keepTransform(int x, int y) {
        if (this.eDrawingState != EDrawingState.eIdle) {
            Graphics2D bufferGraphics = this.bufferImage.createGraphics();
            bufferGraphics.setColor(this.getBackground());
            bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            bufferGraphics.setColor(this.getForeground());

            if (this.eDrawingState == EDrawingState.eDrawing) {
                this.currentShape.setLocation1(x, y);
                this.currentShape.draw(bufferGraphics);
            } else if (this.eDrawingState == EDrawingState.eMoving) {
                this.currentShape.move(x, y);
            } else if (this.eDrawingState == EDrawingState.eResizing) {
                this.currentShape.resize(x, y);
            } else if (this.eDrawingState == EDrawingState.eRotating) {
                this.currentShape.rotate(x, y);
            }
            for (GShape shape : this.shapes) {
                shape.draw(bufferGraphics);
            }
            bufferGraphics.dispose();
            repaint();
        }
    }



    private void finishRectangularShape(int x, int y) {
        if (this.eDrawingState != EDrawingState.eIdle) {
            if (this.eDrawingState == EDrawingState.eDrawing) {
                this.shapes.add(this.currentShape);
            }
            this.eDrawingState = EDrawingState.eIdle;
            this.currentShape = null;
        }
    }


    //핸들러



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
        }
        private void mouseLButton1Clocked(MouseEvent e) {
        }
        private void mouseLButton2Clocked(MouseEvent e) {

        }
        @Override
        public void mousePressed(MouseEvent e) {
           int x = e.getX();
           int y = e.getY();
            if (eDrawingState == EDrawingState.eIdle) {
                //간소화됨
                if (toolBar.getShapeType() == GConstants.EShapeType.eSelect) {
                    startTransform(x,y);
                } else {
                    startNewShape(x,y);
                    eDrawingState = EDrawingState.eDrawing;
                }
                startDrawing(e.getX(), e.getY());
            }
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState != EDrawingState.eIdle) {
                keepTransform(e.getX(), e.getY());
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState != EDrawingState.eIdle) {
                finishRectangularShape(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
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

