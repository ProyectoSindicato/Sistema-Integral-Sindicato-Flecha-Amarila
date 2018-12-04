
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class Roles {
    
    int Id;
    String IdEmpleado;
    String Corrida;
    String nombreEmpleado;

    ConexionAccess conexion;
    
    public Roles(){
        
    }
    
    public Roles(int Id, String IdEmpleado, String Corrida, String nombreEmpleado) {
        this.Id = Id;
        this.IdEmpleado = IdEmpleado;
        this.Corrida = Corrida;
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }

    public String getCorrida() {
        return Corrida;
    }

    public void setCorrida(String Corrida) {
        this.Corrida = Corrida;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }
    
    public boolean agregarRol(String id){
        this.IdEmpleado = id;
        String sql  = "INSERT INTO Roles(Id,IdEmpleado,Corrida) VALUES (?,?,?)";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, Id);
                ps.setString(2, IdEmpleado);
                ps.setString(3,Corrida);
                ps.execute();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al agregar rol en la base de datos." + e.getMessage());
            return false;
        }
        
    }
    
    public boolean modificarRol(int id){
        this.Id = id;
        String sql  = "UPDATE Roles SET IdEmpleado =?, Corrida=? WHERE Id=?";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setString(1, IdEmpleado);
                ps.setString(2,Corrida);
                ps.setInt(3, Id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al modificar rol en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarRol(int id){
        this.Id = id;
        String sql  = "DELETE FROM Roles WHERE Id=?";
        try{
            conexion = new ConexionAccess();
             conexion.conectar();
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, Id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error al eliminar rol en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public ResultSet filtroRoles(String corrida){
     this.Corrida = corrida;
     ResultSet res = null;
     conexion = new ConexionAccess();
     String sql = "SELECT Id, IdEmpleado, Corrida FROM Roles WHERE Corrida=?";
     try{
         conexion.conectar();
         PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
             ps.setString(1, Corrida);
             res = ps.executeQuery();
     }catch(SQLException e){
         System.out.println("Error al cargar la base de datos." + e.getMessage());
     }
     return res;
    }
    
}
