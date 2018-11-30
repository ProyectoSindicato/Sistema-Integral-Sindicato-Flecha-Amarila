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
public class Descuentos {

    int Id;
    int IdAdeudo;
    String IdEmpleado;
    String Fecha;
    String Concepto;
    int Abono;

    ConexionAccess conexion;

    public Descuentos() {

    }

    public Descuentos(int Id, int IdAdeudo, String IdEmpleado, String Fecha, String Concepto, int Abono) {
        this.Id = Id;
        this.IdAdeudo = IdAdeudo;
        this.IdEmpleado = IdEmpleado;
        this.Fecha = Fecha;
        this.Concepto = Concepto;
        this.Abono = Abono;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdAdeudo() {
        return IdAdeudo;
    }

    public void setIdAdeudo(int IdAdeudo) {
        this.IdAdeudo = IdAdeudo;
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

    public String getConcepto() {
        return Concepto;
    }

    public void setConcepto(String Concepto) {
        this.Concepto = Concepto;
    }

    public int getAbono() {
        return Abono;
    }

    public void setAbono(int Abono) {
        this.Abono= Abono;
    }


    public boolean insertAccident(String id) {
        String sql = "INSERT INTO Descuentos(IdAdeudo,IdEmpleado,Fecha,Concepto,Abono) VALUES(?,?,?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, IdAdeudo);
                ps.setString(2, IdEmpleado);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Concepto);
                ps.setInt(5, Abono);
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
        String sql = "UPDATE Descuentos SET IdAdeudo = ?, IdEmpleado =?, Fecha=?, Concepto=?, Abono=? WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, IdAdeudo);
                ps.setString(2, IdEmpleado);
                ps.setDate(3, Date.valueOf(Fecha));
                ps.setString(4, Concepto);
                ps.setInt(5, Abono);
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
        String sql = "DELETE FROM Descuentos WHERE Id=?";
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
        System.out.println("Entré al resultset.");
        ResultSet rs = null;
        conexion = new ConexionAccess();
        conexion.conectar();
        try{
            String sql = "SELECT Id, IdAdeudo, IdEmpleado, Fecha, Concepto, Abono FROM Descuentos" +
                    " WHERE IdAdeudo =?";
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
