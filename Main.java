import controller.GOLController;

public class Main{
    public static void main(String[] args){
        GOLController ctrl = new GOLController(30,30);
        javax.swing.SwingUtilities.invokeLater(ctrl);
    }
}
