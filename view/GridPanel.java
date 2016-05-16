package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GridPanel extends JPanel{
    private GridCellComponent[][] cells;
    private ArrayList<GridCellComponent> activeCells;

	public GridPanel(MouseListener mouseHandler,int amountX,int amountY){
		this.setOpaque(true);
        this.createGrid(mouseHandler,amountX,amountY);
	}	
	public void createGrid(MouseListener handler,int x, int y){
		this.setLayout(new GridLayout(x,y));
        this.cells = new GridCellComponent[x][y];
        this.activeCells = new ArrayList<GridCellComponent>(x*y);//we have a maximum of n*m active cells;

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
        if(value){
            activeCells.add(cell);
        }
        else{
            activeCells.remove(cell);
        }
    } 
    public synchronized void resetGrid(){
        for(GridCellComponent cell: this.activeCells){
            cell.setActive(false);
        } 
        this.activeCells.clear();//reset.
    }
}
