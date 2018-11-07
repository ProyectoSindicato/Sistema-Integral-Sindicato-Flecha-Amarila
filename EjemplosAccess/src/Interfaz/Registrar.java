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
    private JLabel jLfoto, jLnombre, jLapellidoP, jLapellidoM;
    private  JButton abrir, guardar;
    private JTextField jTfoto, jTnombre, jTapellidoP, jTapellidoM;
    private JPanel jPnombre,jPapellidoP,jPapellidoM,jPFoto;
    private ConexionBaseDatos conexion;
    private File file = null;
    
    public Registrar (){
        this.setSize(500,300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        conexion = new ConexionBaseDatos();
        conexion.conectar();
        this.addWindowListener(new EventosVentana(conexion));
        
        jLnombre= new JLabel ("Nombre:");
        jLapellidoP= new JLabel ("Apellido Paterno"); 
        jLapellidoM= new JLabel ("Apellido Materno");
        jLfoto = new JLabel ("Foto:");
        
        jTfoto = new JTextField (20);
        jTfoto.setEditable(false);
        jTnombre= new JTextField (20); 
        jTapellidoP= new JTextField (20); 
        jTapellidoM= new JTextField (20);
        
        abrir = new JButton("Abrir");
        abrir.addActionListener(this);
        
        guardar = new JButton("Registrar");
        guardar.addActionListener(this);
        
        jPnombre = new JPanel();
        jPapellidoP = new JPanel();
        jPapellidoM = new JPanel();
        jPFoto = new JPanel();
        
        jPnombre.add(jLnombre);
        jPnombre.add(jTnombre);
        add(jPnombre);
        
        jPapellidoP.add(jLapellidoP);
        jPapellidoP.add(jTapellidoP);
        add(jPapellidoP);
        
        jPapellidoM.add(jLapellidoM);
        jPapellidoM.add(jTapellidoM);
        add(jPapellidoM);
        
        jPFoto.add(jLfoto);
        jPFoto.add(jTfoto);
        jPFoto.add(abrir);
        add(jPFoto);
        
        JPanel guardarPanel = new JPanel();
        guardarPanel.add(guardar);
        add(guardarPanel);
    }
    
    public void registrar(){
        FileInputStream fis;
        try {
            PreparedStatement pstm =  conexion.getConexion().prepareStatement("Insert into Alumno"
                    + "(Nombre,ApellidoPaterno,ApellidoMaterno,Foto) values(?,?,?,?)");
            pstm.setString(1, jTnombre.getText());
            pstm.setString(2, jTapellidoP.getText());
            pstm.setString(3, jTapellidoM.getText());

            fis = new FileInputStream(file);
            pstm.setBinaryStream(4, fis,(int) file.length());
            pstm.execute();
            pstm.close();
            System.out.println("Registro completo");
        } catch (SQLException ex) {
            Logger.getLogger(Registrar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
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
       if(e.getSource() == abrir){
           JFileChooser chooser = new JFileChooser();
            int respuesta = chooser.showOpenDialog(null);
            String archivo="";
            if(respuesta == JFileChooser.APPROVE_OPTION){
                file = chooser.getSelectedFile();
                jTfoto.setText(chooser.getCurrentDirectory().getPath());
            }
       }else if(e.getSource() == guardar){
           registrar();
       }
    }
}
