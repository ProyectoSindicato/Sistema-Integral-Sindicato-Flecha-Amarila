
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class ConductoresRoles {
    int IdRol;
    String IdConductor;
    String nombreConductor;

    ConexionAccess conexion;
    
    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }
    
    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public String getIdConductor() {
        return IdConductor;
    }

    public void setIdConductor(String IdConductor) {
        this.IdConductor = IdConductor;
    }

    public ConductoresRoles(int IdRol, String IdConductor, String nombreConductor) {
        this.IdRol = IdRol;
        this.IdConductor = IdConductor;
        this.nombreConductor = nombreConductor;
    }
    
    public boolean agregarRolConductor(int id){
        this.IdRol = id;
        String sql  = "INSERT INTO ConductoresRoles(Id,IdConductor) VALUES (?,?)";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, IdRol);
                ps.setString(2, IdConductor);
                ps.execute();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al agregar rol de conductor en la base de datos." + e.getMessage());
            return false;
        }
        
    }
    
    
}
