package Interfaz;

import ejemplosaccess.ConexionBaseDatos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Buscar extends JFrame implements ActionListener{
    
    private JLabel jLid,jLfoto, jLnombre, jLapellidoP, jLapellidoM;
    private  JButton buscar;
    private JTextField jTid,jTfoto, jTnombre, jTapellidoP, jTapellidoM;
    private JPanel jPid,jPnombre,jPapellidoP,jPapellidoM,jPFoto;
    private ConexionBaseDatos conexion;
    private JScrollPane scrollPane;
    
    public Buscar (){
        setSize(500,300);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Busqueda");
        
        conexion = new ConexionBaseDatos();
        conexion.conectar();
        
        /* Evento que nos permite cerrar la conexion al cerrar la ventana*/
        this.addWindowListener(new EventosVentana(conexion));
        
        jLid= new JLabel ("ID:");
        jLnombre= new JLabel ("Nombre:");
        jLapellidoP= new JLabel ("Apellido Paterno"); 
        jLapellidoM= new JLabel ("Apellido Materno");
        jLfoto = new JLabel ("Foto:");
        
        jTid = new JTextField(5);

        jTnombre= new JTextField (20); 
        jTnombre.setEditable(false);
        jTapellidoP= new JTextField (20);
        jTapellidoP.setEditable(false);
        jTapellidoM= new JTextField (20);
        jTapellidoM.setEditable(false);
        
        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        
        jPid = new JPanel();
        jPnombre = new JPanel();
        jPapellidoP = new JPanel();
        jPapellidoM = new JPanel();
        jPFoto = new JPanel();
        
        jPid.add(jLid);
        jPid.add(jTid);
        jPid.add(buscar);
        add(jPid);
        
        jPnombre.add(jLnombre);
        jPnombre.add(jTnombre);
        add(jPnombre);
        
        jPapellidoP.add(jLapellidoP);
        jPapellidoP.add(jTapellidoP);
        add(jPapellidoP);
        
        jPapellidoM.add(jLapellidoM);
        jPapellidoM.add(jTapellidoM);
        add(jPapellidoM);
        
        scrollPane = new JScrollPane(jLfoto,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setSize(500, 500);
        add(scrollPane);
    }
   
    /*
        Aqui se muestra un query sencillo sobre un select, en este caso ralizamos una busqueda por "id"
    */
    public void querySelect(){
        ImageIcon icono=null;
        try {
            String selectQuery = "select Nombre,ApellidoPaterno," +
                    "ApellidoMaterno,Foto from Alumno where id=?";
            PreparedStatement pstm =  conexion.getConexion().prepareStatement(selectQuery);
            // Obtengo el "id" que deseo buscar
            int indice = Integer.parseInt(jTid.getText());
            // y se lo proporciono al statement, el numero 1 hace referencia al primer "?" dentro de la consulta
            pstm.setInt(1, indice);
            // Y se ejecuta el query
            ResultSet res = pstm.executeQuery();       
            int valida = 0;
            while(res.next()){
                valida = valida + 1;
                jTnombre.setText(res.getNString(1));
                jTapellidoP.setText(res.getNString(2));
                jTapellidoM.setText(res.getNString(3));
             /* 
                Extraemos el resultado, el resultado se extrae de forma 
                hexadecimal por lo que hay que convertir a bytes.
                Capturamos la imagen en una variable tipo blob que permite 
                extraer los bytes a utilizar
                */
                Blob blob = res.getBlob(4);
                // Se establece que se tomara un rango de bytes es decir desde el primero hasta el ultimo
                byte[] data = blob.getBytes(1, (int)blob.length());
                BufferedImage img = null;
                // Se hace la lectura de los bytes y se almacena en el BufferedImage posibilitando realizar la conversion a imagen
                img = ImageIO.read(new ByteArrayInputStream(data));
                // Por ultimo se guarda en una variable Icon listo para ser utilizada la imagen
                icono = new ImageIcon(img);
                // Publico la imagen en un JLabel
                jLfoto.setIcon(icono);
            }
            if(valida == 0){
                System.out.println("Registro no encontrado");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == buscar){
          querySelect();
      }
    }
}
