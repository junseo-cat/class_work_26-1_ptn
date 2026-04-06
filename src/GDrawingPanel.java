import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GDrawingPanel extends JPanel {

    public GDrawingPanel() {
        this.setBackground(Color.WHITE);

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    private int x0, y0;
    private int x1, y1;

    private void startRectangularShape(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }
    private void finishRectangularShape(int x, int y) {
        this.x1 = x;
        this.y1 = y;

        Graphics2D g2d = (Graphics2D) this.getGraphics();
        g2d.drawRect(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }

    /*
    private  void startDrawing(int x, int y){
        //그림을 그릴 수 있는 종합세트. 도구보고 네모 그려 하면 그림. 마우스 위치만 기억하면 됌. 원점에서는 안그리고 움직이면 그림 지우고 그리고 지우고 그리고 .
        Graphics graphics = this.getGraphics();
    }
    //지우고 그리깅 또는 종이를 두장을 써 하나는 그래픽스를 그려놈. 다른거는 새로 그려. 새로운 종이에 그리는 것을 더블 버퍼링
    private void keepDrawing(int x, int y){

    }
    private void stopDrawing(int x, int y){

    }
    */


    private class MouseHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
//            if (e.getClickCount() == 1){
//                startRectangularShape(e.getX(), e.getY());
//            } else if (e.getClickCount() == 2) {
//                finishRectangularShape(e.getX(), e.getY());
//            }
        }


        @Override
        public void mousePressed(MouseEvent e) {
            startRectangularShape(e.getX(), e.getY());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            finishRectangularShape(e.getX(), e.getY());
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}
