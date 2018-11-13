package Interfaz;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import ejemplosaccess.ConexionBaseDatos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Eliminar extends JFrame implements ActionListener{
    
    private JLabel jLid, mensaje;
    private  JButton eliminar;
    private JTextField jTid;
    private JPanel jPid;
    private ConexionBaseDatos conexion;
    private JScrollPane scrollPane;
    
    public Eliminar (){
        setSize(500,300);
        setVisible(true);
        setLocationRelativeTo(null);
        
        conexion = new ConexionBaseDatos();
        conexion.conectar();
        
        /* Evento que nos permite cerrar la conexion al cerrar la ventana*/
        this.addWindowListener(new EventosVentana(conexion));
        
        jLid= new JLabel ("ID:");
        mensaje= new JLabel ("");
                
        jTid = new JTextField(5);

        eliminar = new JButton("Eliminar");
        eliminar.addActionListener(this);
        
        jPid = new JPanel();
        
        jPid.add(jLid);
        jPid.add(jTid);
        jPid.add(eliminar);
        add(jPid);

        add(mensaje);
    }
   
    /*
        Para realizar sentencias "delete" se obtienen dos casos, debido a que es necesario
        almacenar imagenes en el proyecto las sentencias "delete" y "update" se ven
        afectadas a la hora de utilizar datos tipo "OLE" (tipo necesario para imagenes)
        si nosotros tratamos de eliminar un registro mediante la instruccion 
        "delete from ____ where ____" no lograremos eliminar nada.
        Para poder lograrlo lo realizamos con la siguiente forma (solo cuando una tabla contiene
        datos tipos OLE de otro modo la sentencia puede ser igual que siempre).
    
    */
    
    // Sentencia delete para cuando una tabla hace uso de datos tipos OLE
    public void queryDelete(){
        
        // Para realizar las importaciones necesarias se utiliza todo lo referente a "Jackess"
        Database db;
        try {
            /*
                Se construye un objeto "database" para hacer referencia a nuestra base de datos
                en su constructor se ingresa el directorio donde esta alojada.
            */
            db = DatabaseBuilder.open(new File("C:\\Users\\Isaac_000\\Documents"
                    + "\\NetBeansProjects\\EjemplosAccess\\ejemplo.accdb"));
            // Se construye un objeto "Table" para apuntar a la tabla que deseamos manipular.
            Table t = db.getTable("Alumno");
            
            /*
                Se construye un objeto "Row" para realizar la busqueda de los datos que nos interesan
                en este ejemplo indico que tomare de la tabla "t" los datos cuando su clave
                primaria corresponda al id que incertamos con ayuda del "CursorBuilder" (utilizado para apuntar
                a datos).
            */
            Row r = CursorBuilder.findRowByPrimaryKey(t,Integer.parseInt(jTid.getText()));
            // Comprobamos si logro encontrar el registro
            if(r!=null){
                /*
                    Ahora decidimos que hacer con el, se puede borrar o actualizar
                    con "deleteRow()" o "updateRow()" respectivamente
                */
               t.deleteRow(r);
                // Al terminar cerramos el uso de la base de datos
                db.close();
                mensaje.setText("Registro eliminado");
            }else{
               mensaje.setText("Mensaje no encontrado");
            }      
        } catch (IOException ex) {
            Logger.getLogger(Eliminar.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    /* 
    Por otro lado si la tabla NO contiene datos tipos OLE no habra ningun problema 
    si utilizamos una sentencia comun.
    Nota: es normal obtener un error si intentamos borrar un dato que esta almacenado
    dentro de una tabla que de ella dependen otras, por ejemplo la tabla "Materia" depende
    de "Alumno" ya que esta contiene de manera foranea la clave de alumno por lo tanto
    no podremos borrar registros de "Alumno" ya que de ella depende "Materia".
    */
    public void delete2(){
        try {
            String deleteQuery = "DELETE From Materia where Id=?";
            PreparedStatement st = conexion.getConexion().prepareStatement(deleteQuery);
            st.setInt(1, Integer.parseInt(jTid.getText()));
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Eliminar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == eliminar){
          // Ejecuta el metodo que desees
          //queryDelete();
         //delete2();
      }
    }
}
