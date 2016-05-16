package model;

import math.BinaryMatrix;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import controller.GOLController;

public class GOLField{
    private BinaryMatrix state;
    private GOLController ctrl;

    public GOLField(int rows,int columns,GOLController ctrl){
        if(rows <= 0 || columns <= 0){
            throw new IllegalArgumentException("bounds for field must be greater than zero");
        }
        else if(rows < 3 || columns < 3){
            //notify user that this might be a bad idea.
            System.out.println("field size is below recommend size of at least 3x3. while this should work its totally useless and not recommended since the gol algorithm uses tiles of 3x3 for the step computation.\n");
        }
        this.state = new BinaryMatrix(rows,columns);
        this.ctrl = ctrl;
    }

    /** calculates one step of game of life field.
     * Notifys any observers with a index ArrayList  with the changed indexes saved inside the struct-like MatrixIndex class.
     **/
    public ArrayList<FieldChange> step(){
        //create new state matrix and changed list.
        BinaryMatrix next = new BinaryMatrix(this.state.getRows(),this.state.getColumns());
        ArrayList<FieldChange> changed = new ArrayList<FieldChange>();
        //iterate over old state
        for(int i = 0; i< this.state.getRows();++i){
            for(int j = 0; j < this.state.getColumns(); ++j){
                //get field value
                boolean field = this.state.getValue(i,j);
                boolean newField = field;
                //get the sourrinding matrix for each field.
                BinaryMatrix sub = this.state.getSubmatrix(i-1,i+2,j-1,j+2);//first bound is inclusive,second exclusive.
                int alives = amountOfAliveInMatrix(sub);
                if(field) --alives;//substract self.
                if(!field && alives == 3){
                    //newborn cell.
                    newField = true; 
                }
                else if(field){
                    if(alives < 2 || alives > 3){//if not enough or too many alive neighbours -> die.
                        newField = false; 
                    }
                    //otherwise the cell stays alive
                }
                //set in new state
                next.setValue(i,j,newField);
                //if changed setChanged and add to changed list.
                if(newField != field){
                    // System.out.printf("settings value at (row:%d,col:%d) to %s\n",i,j,newField ? "1" : "0");
                    //add to changed list
                    changed.add(new FieldChange(j,i,newField));
                }
                

            }
        }
        //Save Changes
        if(!changed.isEmpty()){
            this.state = next;
        }
        return changed;
    }
    
    public int getRows(){
        return this.state.getRows();
    }
    public int getColumns(){
       return this.state.getColumns(); 
    }
    public boolean getValue(int x, int y){
        return this.state.getValue(y,x);
    }
    public void setValue(FieldChange change){
        this.state.setValue(change.y,change.x,change.value);//Matrix is accessed like row,column which is y,x 
    }

    public void resetState(){
        int row = this.state.getRows();
        int col = this.state.getColumns();

        this.state = new BinaryMatrix(row,col);
    }
    public void printState(){
        System.out.println(this.state); 
    }

    //Stable Matrix Systems
    private static BinaryMatrix stableBlock(){
        return new BinaryMatrix(new boolean[][]{
                                                {true,true},
                                                {true,true}
                                               });    
    }
    private static BinaryMatrix stablePond(){
        return new BinaryMatrix(new boolean[][]{
                                                {false,true,true,false},
                                                {true,false,false,true},
                                                {true,false,false,true},
                                                {false,true,true,false}
                                               });
    }
    private static BinaryMatrix stableLoaf(){
        return new BinaryMatrix(new boolean[][]{
            {false,true,true,false},
            {true,false,false,true},
            {false,true,false,true},
            {false,false,true,false}
        });
    }
    private static BinaryMatrix stableTub(){
        return new BinaryMatrix(new boolean[][]{
            {false,true,false},
            {true,false,true},
            {false,true,false}
        });
    
    }
    public String calculateAmountOfStableLifeforms(){
        return String.format("Block: %d\nTub: %d\nLoaf: %d\nPond: %d\n",
                this.state.amountOfMatchesForSubmatrix(stableBlock()),
                this.state.amountOfMatchesForSubmatrix(stableTub()),
                this.state.amountOfMatchesForSubmatrix(stableLoaf()),
                this.state.amountOfMatchesForSubmatrix(stablePond())
                );
    }

    //Helper Methods.
    private static int amountOfAliveInMatrix(BinaryMatrix mat){
        int amount = 0;
        for(int i = 0; i< mat.getRows(); ++i){
            for(int j = 0; j < mat.getColumns(); ++j){
                if(mat.getValue(i,j)) ++amount;
            }
        }
        return amount;
    }

}
