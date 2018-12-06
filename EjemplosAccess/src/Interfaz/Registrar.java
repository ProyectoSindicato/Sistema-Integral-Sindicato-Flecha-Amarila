package Interfaz;

import ejemplosaccess.ConexionBaseDatos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Registrar extends JFrame implements ActionListener{
    private  JButton guardar;
    private ConexionBaseDatos conexion;
    private File file = null;
    
    public Registrar (){
        this.setSize(500,300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        conexion = new ConexionBaseDatos();
        conexion.conectar();
        this.addWindowListener(new EventosVentana(conexion));

        guardar = new JButton("Registrar");
        guardar.addActionListener(this);
        JPanel guardarPanel = new JPanel();
        guardarPanel.add(guardar);
        add(guardarPanel);
    }

    public void registrar(){
        FileInputStream fis;
        File foto;
        ResultSet resultSelect;
        PreparedStatement pstm2;
        String pathImages;
        int conImagen = 0;
        int sinImagen = 0;
        try {
            PreparedStatement pstm =  conexion.getConexion().prepareStatement(
                    "select * from Empleado where CorreoElectronico!=''"
            );
            resultSelect = pstm.executeQuery();
            while(resultSelect.next()){
                System.out.println(resultSelect.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Registrar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {                                  
        /*
         Al salir se finaliza la conexion con base de datos
        */
        conexion.close();
        System.out.println("Conexion finalizada");
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getSource() == guardar){
           registrar();
           /*JFileChooser chooser = new JFileChooser();
            int respuesta = chooser.showOpenDialog(null);
            String archivo="";
            if(respuesta == JFileChooser.APPROVE_OPTION){
                file = chooser.getSelectedFile();
                System.out.println(file);
            }*/
       }
    }
}
