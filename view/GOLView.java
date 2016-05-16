package view;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class GOLView implements Runnable{
    private GridPanel panel;
    private int dimX;
    private int dimY;
    private MouseListener mouseHandler;
    private ActionListener buttonHandler;
    private AbstractButton[] controlButtons;
    private JFrame frame;


    public GOLView(MouseListener mouseHandler,ActionListener buttonHandler,int dimX,int dimY){
        this.dimX = dimX;
        this.dimY = dimY;
        this.mouseHandler = mouseHandler;
        this.buttonHandler = buttonHandler;
    }
    public void run(){
        final JFrame rootFrame = new JFrame();
        this.frame = rootFrame;
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = rootFrame.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        this.panel = new GridPanel(this.mouseHandler,this.dimX,this.dimY);

        //Layouting
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.9;

        contentPane.add(this.panel,c);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        //Buttons
        this.controlButtons = new AbstractButton[2];
        this.controlButtons[0] = new JButton("Step");
        this.controlButtons[1] = new JToggleButton("Auto-Step");
        for(AbstractButton btn : this.controlButtons){
            btn.addActionListener(this.buttonHandler);
            buttons.add(btn);
        }
        
        c.weighty = 0.1;
        c.gridy = 1;

        contentPane.add(buttons,c);
        
        rootFrame.pack();
        rootFrame.setVisible(true);

    }
    public void disableAllButtonsBeside(AbstractButton button){
        for(AbstractButton btn: this.controlButtons){
            if(btn != button){
                btn.setEnabled(false); 
            }
        }
    }
    public void enableAllButtons(){
        for(AbstractButton btn: this.controlButtons){
            btn.setEnabled(true);
        } 
    }
    public void updateGrid(int x,int y,boolean value){
        //passthrough
        this.panel.updateCell(x,y,value);
    }
}
