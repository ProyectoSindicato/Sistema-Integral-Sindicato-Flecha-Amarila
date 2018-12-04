package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class Autobuses {
    
    int Id;
    String Marca;
    String Modelo;

    ConexionAccess conexion;
    
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    public Autobuses(int Id, String Marca, String Modelo) {
        this.Id = Id;
        this.Marca = Marca;
        this.Modelo = Modelo;
    }
    
    public Autobuses(){
    }
    
    public boolean agregarAutobus(int id){
        this.Id = id;
        conexion = new ConexionAccess();
       conexion.conectar();

        String sql = "INSERT INTO Autobus(Id,Marca,Modelo) VALUES (?,?,?)";
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
                ps.setInt(1, id);
                ps.setString(2, Marca);
                ps.setString(3, Modelo);
                ps.execute();
                ps.close();
            }
            return true;
        }catch(SQLException ex)
        {  
            System.out.println("Error al ingresar un autobús. " + ex.getMessage());
            return false;
        }
    }
    
    public boolean modificarAutobus(int id){
        this.Id = id;
        String sql = "UPDATE Autobus SET Marca =?, Modelo =? WHERE Id =?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
            ps.setInt(1,id);
            ps.setString(2,Marca);
            ps.setString(3,Modelo);
            ps.setInt(4, id);
            ps.executeUpdate();
            ps.close();
            }
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar autobús en la base de datos." + e.getMessage());
            return false;
        }
        
    }
    
    public boolean eliminarAutobus(int id){
        this.Id = id;
        String sql = "DELETE FROM Autobus WHERE Id =?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
            ps.close();
            }
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar autobús en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public ResultSet filtroBus(int id){
        this.Id = id;
        ResultSet res = null;
        conexion = new ConexionAccess();
        try{
            conexion.conectar();
            String sql = "SELECT Id, Marca, Modelo FROM Autobus WHERE Id=?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            try{
                ps.setInt(1, Id);
                res = ps.executeQuery();
            }catch(Exception ex){}
        }catch(SQLException e){
            System.out.println("Error. No hace la conexión a la base de datos." + e.getMessage());
        }
        return res;
    }
    
    
}
