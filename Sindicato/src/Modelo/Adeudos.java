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
public class Adeudos {

    int Id;
    String IdConductor;
    int Importe;
    String IdEmpleado;

    ConexionAccess conexion;

    public Adeudos() {

    }

    public Adeudos(int Id, String IdConductor, int Importe) {
        this.Id = Id;
        this.IdConductor = IdConductor;
        this.Importe = Importe;
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
    
    public int getImporte() {
        return Importe;
    }

    public void setImporte(int Importe) {
        this.Importe = Importe;
    }

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }

    public boolean insertAdeudo(String id) {
        String sql = "INSERT INTO Adeudos(IdConductor,Importe) VALUES(?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setInt(2, Importe);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error to insert in the database." + e.getMessage());
            return false;
        }
    }

    public boolean modifyAdeudo(String id) {
        this.IdEmpleado = id;
        String sql = "UPDATE Adeudos SET IdConductor = ?, Importe =? WHERE Id=?";
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, IdConductor);
                ps.setInt(2, Importe);
                ps.setInt(3, Id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error modify in the database." + e.getMessage());
            return false;
        }
    }

    public boolean deleteAdeudo(int id) {
        this.Id = id;
        String sql = "DELETE FROM Adeudos WHERE Id=?";
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
            String sql = "SELECT Id, IdConductor, Importe FROM Adeudos" +
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
