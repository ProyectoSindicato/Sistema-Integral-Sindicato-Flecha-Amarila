package Interfaz;

import ejemplosaccess.ConexionBaseDatos;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Interfaz extends JFrame implements ActionListener{
    private JButton registrar, buscar, eliminar;
    private ConexionBaseDatos conexion;
    
    public Interfaz (){  
        registrar = new JButton("Almacenar fotos");
        registrar.addActionListener(this);
        add(registrar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == registrar){
            /*
                Frame que contiene un formulario pequeño para demostrar la manera de ingresar datos
                a una tabla
            */
            Registrar registrar = new Registrar();
            registrar.setLayout(new GridLayout(5,1));
        }
    } 
}
