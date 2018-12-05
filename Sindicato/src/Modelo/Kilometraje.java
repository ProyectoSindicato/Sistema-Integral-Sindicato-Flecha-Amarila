/* Modelo Kilometraje */ 
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class Kilometraje {

    int Id;
    String IdConductor;
    String IdDirector;
    String Fecha;
    String Motivo;    

    ConexionAccess conexion;

    public Kilometraje() {

    }

    public Kilometraje(int Id, String IdConductor, String Fecha, String Motivo) {
        this.Id = Id;
        this.IdConductor = IdConductor;
        this.Fecha = Fecha;
        this.Motivo = Motivo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getIdConductor() {
        return IdConductor;
    }

    public void setIdConductor(String IdConductor) {
        this.IdConductor = IdConductor;
    }

    public String getIdDirector() {
        return IdDirector;
    }

    public void setIdDirector(String IdDirector) {
        this.IdDirector = IdDirector;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
   
    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String Motivo) {
        this.Motivo = Motivo;
    }    

    public boolean insertarKilometraje(String id) {
        this.IdDirector= id;
        String sql = "INSERT INTO AutorizacionKilometraje(IdConductor,IdDirector,Motivo,Fecha) "
                + "VALUES(?,?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setString(2, IdDirector);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Motivo);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error to insert in the database." + e.getMessage());
            return false;
        }
    }

    public boolean modificarKilometraje(String id) {
        this.IdDirector = id;
        String sql = "UPDATE AutorizacionKilometraje SET IdConductor = ?, IdDirector =?, Motivo=?, Fecha=?"
                + "WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setString(2, IdDirector);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Motivo);
                ps.setInt(5, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error modify in the database." + e.getMessage());
            return false;
        }
    }

    public boolean eliminarKilometraje(int id) {
        this.Id = id;
        String sql = "DELETE FROM AutorizacionKilometraje WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Delete error in the database." + e.getMessage());
            return false;
        }
    }

    public ResultSet filtrarKilometraje(String id)
    {
        ResultSet rs = null;
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            String sql = "SELECT Id, IdConductor, IdDirector, Motivo, Fecha FROM AutorizacionKilometraje" +
                    " WHERE IdConductor =?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
        }catch(SQLException e)
        {
            System.out.println("No entró, revisa.");
            e.printStackTrace();
        }
        return rs;
    }
}
