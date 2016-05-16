package controller;
import model.*;
import view.*;
import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GOLController implements MouseListener,ActionListener,Runnable{
	private GOLView view;
	private GOLField field;
	private AutoRunner runner;

	private boolean mouseDown;

	public GOLController(int fieldSizeX,int fieldSizeY){
		this.view = new GOLView(this,this,fieldSizeX,fieldSizeY);
		this.field = new GOLField(fieldSizeX,fieldSizeY,this);
	}
    public void run(){
        SwingUtilities.invokeLater(this.view);   
    }

	//MouseListener
	//
	/** Called when the mouse has been clicked on a component. */
	public void mouseClicked(MouseEvent e) {
	}

	/** Called when the mouse enters a component. */
	public void mouseEntered(MouseEvent e) {
		GridCellComponent cell = (GridCellComponent)e.getComponent();
		if(this.mouseDown){
			//simple toggle
			toggleCell(cell);
		}
	}

	/** Called when the mouse exits a component. */
	public void mouseExited(MouseEvent e) {
	}

	/** Called when the mouse has been pressed. */
	public void mousePressed(MouseEvent e) {
		this.mouseDown = true;
		GridCellComponent cell = (GridCellComponent)e.getComponent();
		toggleCell(cell);
	}
	/** Called when the mouse has been released. */
	public void mouseReleased(MouseEvent e) {
		this.mouseDown = false;
		GridCellComponent cell = (GridCellComponent)e.getComponent();
	}
	private void toggleCell(GridCellComponent cell){
		int x = cell.getIndexX();
		int y = cell.getIndexY();
		boolean newVal = !cell.isActive();
		this.view.updateGrid(x,y,newVal);
        this.field.setValue(new FieldChange(x,y,newVal));
	}

	//Action Listener Stuff
	public void actionPerformed(ActionEvent e){
		System.out.printf("actionPerformed on button: %s\n",e.getActionCommand());
		String cmd = e.getActionCommand();
		AbstractButton aBtn = (AbstractButton)e.getSource();
		if(cmd.equals("Step")){
            ArrayList<FieldChange> changes = this.field.step();
            for(FieldChange change: changes){
                this.view.updateGrid(change.x,change.y,change.value);
            }
		}
		else if(cmd.equals("Auto-Step")){
			JToggleButton btn = (JToggleButton)aBtn;
			if(btn.isSelected()){
				System.out.println("auto step selected");
				this.view.disableAllButtonsBeside(btn);
                //Simple Threading.
                this.runner = new AutoRunner(this.view,this.field);
                (new Thread(this.runner)).start();
			}
			else{
				System.out.println("auto step deselected");
				this.view.enableAllButtons();
				//stop the thread
				this.runner.stopRunning();
			}
		}
	}
}
