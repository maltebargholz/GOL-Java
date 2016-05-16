package controller;
import model.*;
import view.*;
import java.util.*;

public class AutoRunner implements Runnable{

    public volatile boolean running;
    private GOLView view;
    private GOLField field;
    
    public AutoRunner(GOLView view,GOLField field){
        this.view = view;
        this.field = field;
    }
    public void run(){
        this.running = true;
        while(this.running){
            System.out.println("Runnable EVT: " + javax.swing.SwingUtilities.isEventDispatchThread());                         
            sleep();

            long startTime = System.nanoTime();
            ArrayList<FieldChange> changed = field.step();
            long endTime = System.nanoTime();

            long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
            System.out.println("stepping took " + duration +" ms.");

            javax.swing.SwingUtilities.invokeLater( new Runnable(){
                public void run(){
                    long startTime = System.nanoTime();

                    for(FieldChange change: changed){
                        view.updateGrid(change.x,change.y,change.value);
                    }
                    long endTime = System.nanoTime();

                    long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
                    System.out.println("updating took " + duration +" ms.");
                } 
            });
        }

    }  
    private void sleep(){
        try{
            Thread.sleep(250);
        }
        catch(Exception e){

        }
    }
    public synchronized void stopRunning(){
        this.running = false;
    }
}
