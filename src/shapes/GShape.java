package shapes;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public abstract class GShape implements Cloneable{

    public enum EAnchor {
        eRotate, eMove, eResize
    }

    protected Shape shape;
    protected int x0, y0, x1, y1;

    public GShape() {}

    public GShape clone() {
        try{
            return (GShape) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    public EAnchor onShape(int x, int y) {
        return EAnchor.eMove;
    }


    public void move(int x, int y) {
        int width = this.x1 - this.x0;
        int height = this.y1 - this.y0;
        this.x0 = x;
        this.y0 = y;
        this.x1 = this.x0 + width;
        this.y1 = this.y0 + height;
    }

    public void resize(int x, int y) {}
    public void rotate(int x, int y) {}

    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }
    public void setLocation1(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void setSize(int width, int height) {
        this.x1 = this.x0 + width;
        this.y1 = this.y0 + height;
    }

//	protected Rectangle getBounds() {
//		int x = Math.min(this.x0, this.x1);
//		int y = Math.min(this.y0, this.y1);
//		int width = Math.abs(this.x1 - this.x0);
//		int height = Math.abs(this.y1 - this.y0);
//		return new Rectangle(x, y, width, height);
//	}

    public abstract void draw(Graphics2D g);

}
