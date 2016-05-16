package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.geom.Line2D.*;
import javax.swing.JComponent;

public class GridCellComponent extends JComponent{
    private int indexX;
    private int indexY;
    private boolean topAttached,bottomAttached,leftAttached,rightAttached;
    private boolean active;

    public GridCellComponent(MouseListener handler,int indexX,int indexY){//the default inner cell
        super();
        this.addMouseListener(handler);
        this.indexX = indexX;
        this.indexY = indexY;
        //some layouting
        // this.setPreferredSize(new Dimension(5,5));
        // this.setMinimumSize(new Dimension(5,5));
    }
    public GridCellComponent(MouseListener handler,int indexX,int indexY, boolean topAttached,boolean bottomAttached,boolean leftAttached,boolean rightAttached){
        this(handler,indexX,indexY);
        this.topAttached = topAttached;
        this.bottomAttached = bottomAttached;
        this.leftAttached = leftAttached;
        this.rightAttached = rightAttached;
        System.out.printf("cell at (%d,%d) top:%b bot:%b left:%b right:%b\n",indexX,indexY,topAttached,bottomAttached,leftAttached,rightAttached);
        this.setActive(false);
    }
    public Dimension getPreferredSize(){
        return new Dimension(50,50); 
    }
    public void setActive(boolean newActive){
        this.active = newActive;
        this.repaint();;
    }
    public boolean isActive(){
        return this.active; 
    }
    public void toggleActive(){
        this.setActive(!this.active);
    }
    public int getIndexX(){
        return this.indexX; 
    }
    public int getIndexY(){
        return this.indexY;
    }

    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        //get dims
        int width = this.getWidth();
        int height = this.getHeight();
        //Background
        g2.setPaint(this.active ? Color.green : Color.gray);
        g2.fillRect(0,0,width,height);


        //Reset Color
        g2.setPaint(Color.black);
        //Borders
        if(!this.topAttached){
            g2.draw(new Line2D.Float(0,0,width,0));
        }
        if(!this.bottomAttached){
            g2.draw(new Line2D.Float(0,height,width,height));
        }
        if(!this.leftAttached){
            g2.draw(new Line2D.Float(0,0,0,height));
        }
        if(!this.rightAttached){
            g2.draw(new Line2D.Float(width,0,width,height));
        }


    }

}
