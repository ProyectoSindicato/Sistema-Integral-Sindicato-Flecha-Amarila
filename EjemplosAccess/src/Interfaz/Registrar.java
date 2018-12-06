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
            PreparedStatement pstm =  conexion.getConexion().prepareStatement("Select Id from Conductor");
            resultSelect = pstm.executeQuery();
            
            while(resultSelect.next()){
                pathImages = "F:\\Fotos\\";
                pathImages = pathImages + resultSelect.getString(1)+".JPG";
                foto = new File(pathImages);
                
                try{
                    if(foto.exists()){
                        pstm2 =  conexion.getConexion().prepareStatement("Update Empleado"
                                + " set Foto=?"
                                + " where Id=?");
                        fis = new FileInputStream(foto);
                        pstm2.setBinaryStream(1, fis,(int) foto.length());
                        pstm2.setString(2, resultSelect.getString(1));
                        pstm2.executeUpdate();
                        System.out.println("Meti: "+resultSelect.getString(1));
                        fis.close();
                        foto.delete();
                        conImagen = conImagen +1;
                    }else{
                        System.out.println("No pude meter: "+resultSelect.getString(1));
                      pathImages = "F:\\Fotos\\10.JPG";
                      foto = new File(pathImages);
                      pstm2 =  conexion.getConexion().prepareStatement("Update Empleado"
                                + " set Foto=?"
                                + " where Id=?");
                        fis = new FileInputStream(foto);
                        pstm2.setBinaryStream(1, fis,(int) foto.length());
                        pstm2.setString(2, resultSelect.getString(1));
                        pstm2.executeUpdate();
                        sinImagen = sinImagen +1 ;
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }            
            System.out.println("Registro completo, con imagen: "+conImagen+"  sin imagen :" +sinImagen);
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
