package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aideé Alvarez
 */
public class Infracciones {

    int Id;
    int IdAutobus;
    String IdConductor;
    String IdDirector;
    String Fecha;
    String Motivo;
    String nombreEmpleado;
    ConexionAccess conexion;

    public Infracciones() {

    }

    public Infracciones(int Id, int IdAutobus, String IdConductor, String Fecha, String Motivo, String nombreEmpleado) {
        this.Id = Id;
        this.IdAutobus = IdAutobus;
        this.IdConductor = IdConductor;
        this.Fecha = Fecha;
        this.Motivo = Motivo;
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public ConexionAccess getConexion() {
        return conexion;
    }

    public void setConexion(ConexionAccess conexion) {
        this.conexion = conexion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
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

    public boolean insertarInfraccion(String id) {
        this.IdDirector = id;
        String sql = "INSERT INTO Infracciones(IdAutobus,IdConductor,IdDirector,Fecha,Motivo)"
                + "VALUES(?,?,?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, IdAutobus);
                ps.setString(2, IdConductor);
                ps.setString(3, IdDirector);
                ps.setDate(4, Date.valueOf(Fecha));
                ps.setString(5, Motivo);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al ingresar una infracción en la base de datos." + e.getMessage());
            return false;
        }
    }

    public boolean modificarInfraccion(String id) {
        this.IdDirector = id;
        String sql = "UPDATE Infracciones SET IdAutobus = ?,IdConductor =?,IdDirector=?,  Fecha=?, Motivo=?"
                + " WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, IdAutobus);
                ps.setString(2, IdConductor);
                ps.setString(3, IdDirector);
                ps.setDate(4, Date.valueOf(Fecha));
                ps.setString(5, Motivo);
                ps.setInt(6, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar infracción en la base de datos. " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarInfraccion(int id) {
        this.Id = id;
        String sql = "DELETE FROM Infracciones WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, Id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar de la base de datos. " + e.getMessage());
            return false;
        }
    }

    public ResultSet filtrarInfraccion(String idConductor) {
        this.IdConductor = idConductor;
        ResultSet res = null;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            String sql = "SELECT Id, IdAutobus, IdConductor, Fecha, Motivo FROM Infracciones WHERE IdConductor= ? ";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, IdConductor);
            res = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
