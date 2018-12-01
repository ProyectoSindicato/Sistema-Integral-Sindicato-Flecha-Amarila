
package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Domicilio {
    private int id;
    private String estado;
    private String ciudad;
    private String colonia;
    private String calle;
    private int numero;
    private int cp;
    
    ConexionAccess conexion;

    public Domicilio() {
    }

    public Domicilio(int id, String estado, String ciudad, String colonia, String calle, int numero, int cp) {
        this.id = id;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.calle = calle;
        this.numero = numero;
        this.cp = cp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    

    public ConexionAccess getConexion() {
        return conexion;
    }

    public void setConexion(ConexionAccess conexion) {
        this.conexion = conexion;
    }
    
    public boolean insertarDomicilio() {
        String sql = "INSERT INTO Domicilio(Estado,Ciudad,Colonia,Calle,NumeroExt,CodigoPostal)"
                + "VALUES(?,?,?,?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, estado);
                ps.setString(2, ciudad);
                ps.setString(3, colonia);
                ps.setString(4, calle);               
                ps.setInt(5, numero);
                ps.setInt(6, cp);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar domicilio en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarReporte(int id) {
        String sql = "UPDATE Domicilio SET Estado = ?,Ciudad =?, Colonia= ?, Calle=?, NumeroExt=?, CodigoPostal=?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, estado);
                ps.setString(2, ciudad);
                ps.setString(3, colonia);
                ps.setString(4, calle);               
                ps.setInt(5, numero);
                ps.setInt(6, cp);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar domicilio en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarReporte(int id) {
        String sql = "DELETE FROM Domicilio WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                //ps.setInt(1, this.id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar de la base de datos. " + e.getMessage());
            return false;
        }
    }
    
    public ResultSet filtrarDomicilio(String id) {
        ResultSet resultado = null;
        System.out.println("Entró al filtro de reportes.");
        conexion = new ConexionAccess();
        conexion.conectar();
        try { //Este SQL contiene todo lo que necesito para traerme la lista de lo que quiero (Checar en el controller, parte: actualizarBD).
            String sql = "SELECT Id,Estado,Ciudad,Colonia,Calle,NumeroExt,CodigoPostal FROM Domicilio WHERE Id = ?";
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
