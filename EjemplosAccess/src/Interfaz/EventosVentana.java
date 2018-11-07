package Interfaz;

import ejemplosaccess.ConexionBaseDatos;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EventosVentana implements WindowListener{
    private ConexionBaseDatos conexion;
    
    public EventosVentana(ConexionBaseDatos conexion){
        this.conexion = conexion;
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        conexion.close();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
      
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
       
    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
    
}
