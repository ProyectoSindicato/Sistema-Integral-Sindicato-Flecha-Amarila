package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Aideé Alvarez
 */
public class Reportes {

    private int Id;
    private String IdConductor;
    private String IdEmpleado;
    private String Tipo;
    private String Lugar;
    private String Fecha;
    private String Descripcion;

    ConexionAccess conexion;

    public Reportes() {

    }

    public Reportes(int Id, String IdConductor, String Tipo, String Lugar, String Fecha, String Descripcion) {
        this.Id = Id;
        this.IdConductor = IdConductor;
        this.Tipo = Tipo;
        this.Lugar = Lugar;
        this.Fecha = Fecha;
        this.Descripcion = Descripcion;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String Lugar) {
        this.Lugar = Lugar;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public ConexionAccess getConexion() {
        return conexion;
    }

    public void setConexion(ConexionAccess conexion) {
        this.conexion = conexion;
    }

    public boolean insertarReporte(String id) {
        this.IdEmpleado = id;
        String sql = "INSERT INTO Reportes(IdConductor,IdEmpleado,Tipo,Lugar,Fecha,Descripcion)"
                + "VALUES(?,?,?,?,?,?)";

        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setString(2, IdEmpleado);
                ps.setString(3, Tipo);
                ps.setString(4, Lugar);
                ps.setDate(5, Date.valueOf(Fecha)); //Checar si esa wea funciona
                ps.setString(6, Descripcion);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar en la base de datos." + e.getMessage());
            return false;
        }
    }

    public boolean modificarReporte(String id) { //Ya está.
        this.IdEmpleado = id;
        String sql = "UPDATE Reportes SET IdConductor = ?,IdEmpleado =?, Tipo= ?, Lugar=?, Fecha=?, Descripcion=?"
                + "WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {

                ps.setString(1, IdConductor);
                ps.setString(2, IdEmpleado);
                ps.setString(3, Tipo);
                ps.setString(4, Lugar);
                ps.setDate(5, Date.valueOf(Fecha));
                ps.setString(6, Descripcion);
                ps.setInt(7, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar reporte en la base de datos." + e.getMessage());
            return false;
        }
    }

    public boolean eliminarReporte(int id) {
        String sql = "DELETE FROM Reportes WHERE Id=?";
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

    /* 
        En si, el filtrado nos regresa lo que queremos ver a partir del id que estés buscando.
        Al ser reportes, solamente buscamos todo lo que queremos ver a partir del idConductor.
     */

    public ResultSet filtrarReporte(String id) {
        ResultSet resultado = null;
        System.out.println("Entró al filtro de reportes.");
        conexion = new ConexionAccess();
        conexion.conectar();
        try { //Este SQL contiene todo lo que necesito para traerme la lista de lo que quiero (Checar en el controller, parte: actualizarBD).
            String sql = "SELECT Id, IdConductor, Tipo, Fecha, Descripcion, Lugar FROM Reportes WHERE idConductor = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, id);
            resultado = ps.executeQuery();
        } catch (SQLException ex) {
            System.out.println("No entró. Revísalo.");
            ex.printStackTrace();
        }
        return resultado;
    }

}