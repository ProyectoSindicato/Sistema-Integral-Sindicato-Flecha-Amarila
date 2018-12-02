/* Modelo Accidentes */ 
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class Accidentes {

    int Id;
    String IdConductor;
    String IdEmpleado;
    String Fecha;
    String Lugar;
    String Motivo;
    String Detalles;
    String Observaciones;

    ConexionAccess conexion;

    public Accidentes() {

    }

    public Accidentes(int Id, String IdConductor, String Fecha, String Lugar, String Motivo, String Detalles, String Observaciones) {
        this.Id = Id;
        this.IdConductor = IdConductor;
        this.Fecha = Fecha;
        this.Lugar = Lugar;
        this.Motivo = Motivo;
        this.Detalles = Detalles;
        this.Observaciones = Observaciones;
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

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String Lugar) {
        this.Lugar = Lugar;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String Motivo) {
        this.Motivo = Motivo;
    }

    public String getDetalles() {
        return Detalles;
    }

    public void setDetalles(String Detalles) {
        this.Detalles = Detalles;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }

    public boolean insertAccident(String id) {
        this.IdEmpleado= id;
        String sql = "INSERT INTO Accidentes(IdConductor,IdDirector,Fecha,Lugar,Motivo, "
                + "Detalles, Observaciones) VALUES(?,?,?,?,?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setString(2, IdEmpleado);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Lugar);
                ps.setString(5, Motivo);
                ps.setString(6, Detalles);
                ps.setString(7, Observaciones);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error to insert in the database." + e.getMessage());
            return false;
        }
    }

    public boolean modifyAccident(String id) {
        this.IdEmpleado = id;
        String sql = "UPDATE Accidentes SET IdConductor = ?, IdDirector =?, Fecha=?, Lugar=?, Motivo=?, Detalles=?, Observaciones=?"
                + "WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setString(2, IdEmpleado);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Lugar);
                ps.setString(5, Motivo);
                ps.setString(6, Detalles);
                ps.setString(7, Observaciones);
                ps.setInt(8, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error modify in the database." + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccident(int id) {
        this.Id = id;
        String sql = "DELETE FROM Accidentes WHERE Id=?";
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

    public ResultSet filterAccident(String id)
    {
        ResultSet rs = null;
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            String sql = "SELECT Id, IdConductor, IdDirector, Fecha, Lugar, Motivo, Detalles, Observaciones FROM Accidentes" +
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
