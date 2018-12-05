
package ejemplosaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBaseDatos {
    /*
        Para realizar la conexion a base de datos utilizamos el driver "UCanAccess"
        se tiene que importar todos los archivos jar
    */ 
    private final String locacion = "F:\\Sindicato V2.accdb";
    private Connection conn = null;
    
    public void conectar (){
        
        try{
         //utilizamos el driver para Access
         Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
         //obtenemos la conexion
         conn = DriverManager.getConnection("jdbc:ucanaccess://"+locacion);
         //si la conexion tuvo exito
         if (conn!=null){
            System.out.println("Conexion lograda");
         }
          }catch(ClassNotFoundException e){
             System.out.println(e);
          } catch (SQLException ex) {
            Logger.getLogger(ConexionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void close(){
        /*
            Cada vez que la conexion es innecesario se tiene que cerrar, este metodo lo realiza
        */
        try {
            conn.close();
            System.out.println("Conexion finalizada");
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection getConexion(){
        return conn;
    }
}
