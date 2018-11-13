package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccessDB {

    /* ****NO SE LES OLVIDE CAMBIAR LA LOCALIZACION DE LA BASE DE DATOS ;)  */
    
    private final String location = "C:\\Users\\Aideé Alvarez\\Documents\\ITLEON\\7mo. Semestre\\Gestión de Proyectos de Software\\Sistema-Integral-Sindicato-Flecha-Amarila\\JavaFX\\pruebafx1.accdb";
    private Connection conn = null;

        /*  
            A comparación de la conexión que hizo Isaac, es mejor obtener booleanos para saber si hicimos
            una buena conexión. Así como está, también sirve para cualquier otro tipo de base de datos, que por cierto,
            lo adapté a como está en mysql.
        */
    
    public boolean conectar() {
        try {
            //utilizamos el driver para Access
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            //obtenemos la conexion
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + location);
          
            if (conn != null) {
                System.out.println("Conexion lograda");
                return true;
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(AccessDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    /*  Quiero creer que esta sentencia es la única que vamos a utilizar. Hay más pero
        no creo que sean necesarios(por ahora). 
    */
    
    public ResultSet ejecutarSQLSelect(String sql) {
        try{
            Statement sentencia = conn.createStatement();
            return sentencia.executeQuery(sql);
        }catch(SQLException ex){
            System.out.println("Error al ejecutar instrucción" + ex);
            return null;
        }
    }
    public void close() {

        try {
            conn.close();
            System.out.println("Conexion finalizada");
        } catch (SQLException ex) {
            Logger.getLogger(AccessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConexion() {
        return conn;
    }
}
