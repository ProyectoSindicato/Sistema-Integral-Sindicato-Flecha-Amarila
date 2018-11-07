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
        registrar = new JButton("Registrar");
        registrar.addActionListener(this);
        add(registrar);
        
        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        add(buscar); 
        
        eliminar = new JButton("Eliminar");
        eliminar.addActionListener(this);
        add(eliminar); 
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
        }else if(e.getSource() == buscar){
            /*
                Frame que contiene la interfaz necesaria para realizar consultas sencillas de tipo "select"
            */
            Buscar buscar = new Buscar ();
            buscar.setLayout(new GridLayout(5,1));
        }else if(e.getSource() == eliminar){
            /*
                Frame que contiene la interfaz necesaria para realizar consultas sencillas de tipo "select"
            */
            Eliminar eliminar = new Eliminar ();
            eliminar.setLayout(new GridLayout(2,1));
        }
    } 
}
