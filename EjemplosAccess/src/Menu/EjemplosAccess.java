
package Menu;

import Interfaz.Interfaz;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class EjemplosAccess {

    public static void main(String[] args) {
        Interfaz interfaz = new Interfaz();
        interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaz.setLayout(new GridLayout(3,1));
        interfaz.setSize(500,300);
        interfaz.setVisible(true);
        interfaz.setTitle("Ejemplo");
        interfaz.setLocationRelativeTo(null);
    }
    
}
