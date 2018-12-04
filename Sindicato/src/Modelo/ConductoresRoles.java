
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public ConductoresRoles(){
        
    }
    
    public boolean agregarRolConductor(int id){
        this.IdRol = id;
        String sql  = "INSERT INTO ConductoresRoles(IdRol,IdConductor) VALUES (?,?)";
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
    
    public boolean modificarRolConductor(int id){
        this.IdRol = id;
        String sql  = "UPDATE ConductoresRoles SET IdConductor=? WHERE IdRol=?";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setString(1, IdConductor);
                ps.setInt(2, IdRol);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al modificar rol de conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarRolConductor(int id, String conductor){
        this.IdRol = id;
        this.IdConductor = conductor;
        
        String sql  = "DELETE FROM ConductoresRoles WHERE IdRol=? AND IdConductor =?";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, IdRol);
                ps.setString(2, IdConductor);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al eliminar rol de conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public ResultSet filtroRolesAsignados(String id){
     this.IdConductor = id;
     ResultSet res = null;
     conexion = new ConexionAccess();
     String sql = "SELECT IdRol ,IdConductor FROM ConductoresRoles WHERE IdConductor=?";
     try{
         conexion.conectar();
         PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
         try {
             ps.setString(1, IdConductor);
             res = ps.executeQuery();
         } catch (Exception e) {}
     }catch(SQLException e){
         System.out.println("Error al cargar la base de datos." + e.getMessage());
     }
     return res;
    }
    
    
    
    
}
