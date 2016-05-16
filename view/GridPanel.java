package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GridPanel extends JPanel{
    private GridCellComponent[][] cells;
	public GridPanel(MouseListener mouseHandler,int amountX,int amountY){
		this.setOpaque(true);
        this.createGrid(mouseHandler,amountX,amountY);
	}	
	public void createGrid(MouseListener handler,int x, int y){
		this.setLayout(new GridLayout(x,y));
        this.cells = new GridCellComponent[x][y];
		for(int i = 0; i < y; ++i){
			for(int j = 0; j < x; ++j){
				GridCellComponent cell = new GridCellComponent(handler,j,i,
                        i == 0,/* top attached */
                        i == (y-1), /* bottom attached */
                        j == 0, /* left attached */
                        j == (x-1) /* right attached */
                        );
                this.cells[j][i] = cell;
				this.add(cell);
			}
		}
	}
    public void updateCell(int indexX,int indexY,boolean value){//must be synchronized to avoid double adding cells.
        GridCellComponent cell = this.cells[indexX][indexY];
        cell.setActive(value);
    } 
}
