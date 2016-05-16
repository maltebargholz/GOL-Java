package math;


public class BinaryMatrix{

    private boolean matrix[][];
    private int column;
    private int row;

    /**
     * @param rows    Number of Matrix' rows
     * @param columns Number of Matrix' columns
     */
    public BinaryMatrix(int rows, int columns) {
        this.row = rows;
        this.column = columns;
        this.matrix = new boolean[rows][columns];
    }

    public BinaryMatrix(boolean[][] matrixRes) {
        this.row = matrixRes.length;
        this.column = matrixRes[0].length;
        this.matrix = deepCopy(matrixRes);
    }

    /** Returns a referenced submatrix in the given range.
     * Indexes which lay outside of the original matrix a given the value {@code false}.
     * @param startRow The starting row of the submatrix. inclusive. may be smaller than zero.
     * @param endRow The end row of the submatrix. exclusive. may be greater than amount of rows.
     * @param startCol The starting column of the submatrix. inclusive. may be smaller than zero.
     * @param endCol The end column of the submatrix. exclusive. may be greater than the amount columns.
     * @throws IllegalArgumentException If {@code startRow > endRow}.
    **/
    public BinaryMatrix getSubmatrix(int startRow,int endRow,int startCol,int endCol) throws IllegalArgumentException,ArrayIndexOutOfBoundsException{
        if(startRow > endRow){
            throw new IllegalArgumentException("Invalid Submatrix Range"); 
        }
        int subRows = endRow-startRow;
        int subCols = endCol-startCol;
        BinaryMatrix sub = new BinaryMatrix(subRows,subCols);
       // System.out.printf("init sub matrix with dim (%d,%d) rowRange (%d -> %d) colRange (%d -> %d)\n",subRows,subCols,startRow,endRow,startCol,endCol);        

        for(int i = 0; (i+startRow) < endRow; ++i){
            final int index = i+startRow;
            boolean[] rowToInsert = new boolean[subCols]; 
            if(index >= 0 && index < this.matrix.length){
                // index is within bounds
                // -> insert correct column elements
                for(int j = 0; (j+startCol) < endCol; ++j){
                    final int colIndex = j+startCol;
                    if(colIndex >= 0 && colIndex < this.matrix[0].length){
                        //if column index is within bounds insert corresponding element
                        //System.out.printf("indexes are within bounds inserting %b at (%d,%d)\n",this.matrix[index][colIndex],i,j);
                        rowToInsert[j] = this.matrix[index][colIndex];
                    }
                    else{
                        //System.out.printf("index (%d,%d) is oob in cols\n",index,colIndex);
                        //otherwise do nothing. since for out-of-bounds elements we insert 0 aka false elements. 
                    }
                }
            }
            else{
                //System.out.printf("index (%d,*) is oob in rows\n",index);
                //do nothing simply insert a empty row. 
            }
            sub.setRow(i,rowToInsert);
            // sub.setRow(i,java.util.Arrays.copyOfRange(this.gmatrix[i+startRow],startCol,endCol));
        }
        //System.out.printf("finished sub:\n%s\n",sub);
        return sub;
    }

    public boolean[] getRow(int i) {
        return this.matrix[i];
    }
    public boolean[][] getMatrix() {
        return matrix;
    }

    public void setRow(int i, boolean[] input) {
        this.matrix[i] = input;
    }

    public void setValue(int m, int n, boolean v) {
        this.matrix[m][n] = v;
    }

    public int getColumns() {
        return column;
    }

    public int getRows() {
        return row;
    }
    public String getDimensions(){
        return String.format("%dx%d",this.row,this.column); 
    }

    public boolean getValue(int i, int j) {
        return this.matrix[i][j];
    }
    //Equality Methods
    @Override public boolean equals(Object x){
        if(x == null || getClass() != x.getClass()){ 
            System.out.println("some error in equals");
            return false;
        }
        //Otherwise safely cast to Matrix
        BinaryMatrix other = (BinaryMatrix)x;
        if(other.row == this.row && other.column == this.column){
            //check deep equality
            return java.util.Arrays.deepEquals(this.matrix,other.getMatrix());
        }
        else{
            return false; 
        }
    
    }
    @Override public int hashCode(){
        return java.util.Arrays.deepHashCode(this.matrix); 
    }
    //Submatrix Equality
    public int amountOfMatchesForSubmatrix(BinaryMatrix other){
        if(other == null || other.row > this.row || other.column > this.column){
            return 0;
        }
        //Check for complete Equality
        if(this.row == other.row && this.column == other.column){
            System.out.printf("Checking for complete equality\n");
            return this.equals(other) ? 1 : 0;
        }
        //Otherwise iterate over each fitting row and column
        final int possibleRows = this.row - other.row;
        final int possibleColumns = this.column - other.column;

        int counter = 0;
        System.out.printf("Got %d possible rows and %d possible columns\n",possibleRows,possibleColumns);

        for(int i = 0; i <= possibleRows; ++i){
             for(int j = 0; j <= possibleColumns; ++j){
                BinaryMatrix sub = this.getSubmatrix(i,i+other.row,j,j+other.column);
                System.out.printf("got sub: %s at index: (%d,%d)\n",sub,i,j);
                if(sub.equals(other)){
                    counter++;             
                }
             }
        }
        return counter;
    }
    //Helper Methods

    /** deep copys a binary array
     **/
    private static boolean[][] deepCopy(boolean[][] orig) {
        //make sure we dont have a null ref.
        if(orig == null){
            return null;
        }

        boolean[][] result = new boolean[orig.length][];
        for (int i = 0; i < orig.length; ++i) {
            result[i] = java.util.Arrays.copyOf(orig[i], orig[i].length);
        }
        return result;
    }    
   public String toString(){
        String desc = new String();
        //print column identifier
        //print edge delimiter
        desc += String.format("%-5s","*");
        //print each column id
        for (int columnIndex = 0; columnIndex < this.column; columnIndex++){
            desc += String.format("%-10d",columnIndex);
        }
        //add newline
        desc += "\n";

        //loop over the matrix and print out each line
        for (int rowIndex = 0; rowIndex < this.row; rowIndex++){
            boolean[] row = this.matrix[rowIndex];
            //add the row id
            desc += String.format("%-5d",rowIndex);
            //add all column entries in this row
            for(int columnIndex = 0; columnIndex < row.length;columnIndex++){
                desc += String.format("%-10s",row[columnIndex] ? "1" : "0");
            }
            //add newline
            desc += "\n";
            
        }
       return desc;
    }

}
