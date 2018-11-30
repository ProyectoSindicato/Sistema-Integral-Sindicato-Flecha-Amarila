package ConexionAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aideé Alvarez
 */
public class ConexionAccess {
    
    /* ****NO SE LES OLVIDE CAMBIAR LA LOCALIZACION DE LA BASE DE DATOS ;)  */
    
   // private final String location = "F:\\Documentos\\Sistema-Integral-Sindicato-Flecha-Amarila\\Base de datos\\copia-de-seguridad\\Sindicato.accdb";
    //private final String location = "C:\\Users\\Personal\\Documents\\ITL\\7to semestre\\Gestion de proyectos de SW\\Proyecto Final\\Sistema-Integral-Sindicato-Flecha-Amarila\\Base de datos\\Sindicato.accdb";
   // private final String location = "F:\\Documentos\\Sistema-Integral-Sindicato-Flecha-Amarila\\Base de datos\\copia-de-seguridad\\Sindicato.accdb";
    private final String location = "C:\\Users\\Aideé Alvarez\\Documents\\Sindicato.accdb";
    
    private Connection conn = null;

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
            System.out.println(ex.getMessage());
            return false;
        }
        return false;
    }
 
    public ResultSet ejecutarSQLSelect(String sql) {
        try{
            Statement sentencia = conn.createStatement();
            return sentencia.executeQuery(sql);
        }catch(SQLException ex){
            System.out.println("Error al ejecutar instrucción, " + ex);
            return null;
        }
    }
    public void close() {

        try {
            conn.close();
            System.out.println("Conexion finalizada.");
        } catch (SQLException ex) {
            Logger.getLogger(ConexionAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConexion() {
        return conn;
    }
}
