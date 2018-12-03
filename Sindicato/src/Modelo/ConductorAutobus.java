package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class ConductorAutobus {
    int IdAutobus;
    String IdConductor;
    String fechaAsignacion;
    String NombreConductor;

    public String getNombreConductor() {
        return NombreConductor;
    }

    public void setNombreConductor(String NombreConductor) {
        this.NombreConductor = NombreConductor;
    }
    ConexionAccess conexion;
    
    public ConductorAutobus(){
        
    }

    public ConductorAutobus(int IdAutobus, String IdConductor, String NombreConductor,String fechaAsignacion) {
        this.IdAutobus = IdAutobus;
        this.IdConductor = IdConductor;
        this.NombreConductor = NombreConductor;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdAutobus() {
        return IdAutobus;
    }

    public void setIdAutobus(int IdAutobus) {
        this.IdAutobus = IdAutobus;
    }

    public String getIdConductor() {
        return IdConductor;
    }

    public void setIdConductor(String IdConductor) {
        this.IdConductor = IdConductor;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    
    public boolean agregarAsignacion(){
        String sql = "INSERT INTO ConductorAutobus(IdAutobus, IdConductor, FechaAsignacion) VALUES (?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, IdAutobus);
                ps.setString(2, IdConductor);
                ps.setDate(3, Date.valueOf(fechaAsignacion));
                ps.execute();
                ps.close();
            }
            return true;
        }catch(SQLException e){
            System.out.println("Error al agregar asignación a un conductor." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarAsignacion(){
        String sql = "UPDATE ConductorAutobus SET IdConductor= ?, FechaAsignacion=? WHERE IdAutobus=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setString(1, IdConductor);
                ps.setDate(2, Date.valueOf(fechaAsignacion));
                ps.setInt(3, IdAutobus);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar asignación." + e.getMessage());
            return false;
        }
        
    }
    
    public boolean eliminarAsignacion(){
        String sql = "DELETE FROM ConductorAutobus WHERE IdAutobus=? AND IdConductor=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, IdAutobus);
                ps.setString(2,IdConductor);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        }catch(SQLException ex){
            return false;
        }
    }
    
}
