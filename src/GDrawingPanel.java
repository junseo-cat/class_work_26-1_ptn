
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class GDrawingPanel extends JPanel {
    //private // eDrawingState;

    private enum EDrawingState {
        eIdle,
        eDrawing,
        eMoving,
        eResizeing,
        eShearing
    }
    private EDrawingState eDrawingState = EDrawingState.eIdle;

    // constructors
    public GDrawingPanel() {
        this.setBackground(Color.WHITE);

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    private int x0, y0;
    private int x1, y1;
    private BufferedImage bufferImage;
    private void startRectangularShape(int x, int y) {
        this.x0 = x;
        this.y0 = y;

        if (this.getWidth() <= 0 || this.getHeight() <= 0) {
            return;
        }

        if (this.bufferImage == null
                || this.bufferImage.getWidth() != this.getWidth()
                || this.bufferImage.getHeight() != this.getHeight()) {
            this.bufferImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferGraphics = this.bufferImage.createGraphics();
            bufferGraphics.setColor(this.getBackground());
            bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            bufferGraphics.dispose();
        }

    }
    private void finishRectangularShape(int x, int y) {
        this.x1 = x;
        this.y1 = y;


        Graphics2D bufferGraphics = this.bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawRect(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
        bufferGraphics.dispose();

        Graphics2D panelGraphics = (Graphics2D) this.getGraphics();
        if (panelGraphics != null) {
            panelGraphics.drawImage(this.bufferImage, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    //상태 변수들


    private class MouseHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
//			if(e.getButton() == 1) {
//                if (e.getClickCount() == 1) {
//                    //
//                } else if (e.getClickCount() == 2) {
//                    //
//                }
//            }

//            if (e.getClickCount() == 1) {
//                startRectangularShape(e.getX(), e.getY());
//            } else if (e.getClickCount() == 2) {
//                finishRectangularShape(e.getX(), e.getY());
//            }

        }




        @Override
        public void mousePressed(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                eDrawingState = EDrawingState.eDrawing;
                startRectangularShape(e.getX(), e.getY());
            }

        }
        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState == EDrawingState.eDrawing) {
                //
                finishRectangularShape(e.getX(), e.getY());
            }

        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState == EDrawingState.eDrawing) {
                eDrawingState = EDrawingState.eIdle;
            }

        }
        @Override
        public void mouseMoved(MouseEvent e) {
//            private void mouseButtton1Clocked (HouseEvent e){
//                if (!isDrawing) {
//                    startRectangularShape(e.getX(), e.getY());
//                }
//            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }

    }
}

/*
public class GShape {
     private int x0, y0, x1, y1;

     public GShape (int x0, int y0, int x1, int y1) {
         this.x0 = x0;
         this.y0 = y0;
         this.x1 = x1;
         this.y1 = y1;
     }

     public void draw(Graphics g) {
         g.setColor(Color.BLACK);
         g.drawRect(x0, y0, x1 - x0, y1 - y0);
     }

     public void setLocation0(int x, int y) {
         this.x0 = x;
         this.y0 = y;
     }
     public void setLocation1(int x, int y) {
         this.x1 = x;
         this.y1 = y;
     }
}
*/
