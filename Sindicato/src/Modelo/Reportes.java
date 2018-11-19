package Modelo;

import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Aideé Alvarez
 */
public class Reportes {

    private int Id;
    private int IdConductor;
    private int IdDirector;
    private int IdDelegado;
    private String Tipo;
    private String Lugar;
    private String Fecha;
    private String Descripcion;

    ConexionAccess conexion;
    
    public Reportes()
    {
        
    }

    public Reportes(int Id, int IdConductor, int IdDirector, int IdDelegado, String Tipo, String Lugar, String Fecha, String Descripcion, ConexionAccess conexion) {
        this.Id = Id;
        this.IdConductor = IdConductor;
        this.IdDirector = IdDirector;
        this.IdDelegado = IdDelegado;
        this.Tipo = Tipo;
        this.Lugar = Lugar;
        this.Fecha = Fecha;
        this.Descripcion = Descripcion;
        this.conexion = conexion;
    }
    
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdConductor() {
        return IdConductor;
    }

    public void setIdConductor(int IdConductor) {
        this.IdConductor = IdConductor;
    }

    public int getIdDirector() {
        return IdDirector;
    }

    public void setIdDirector(int IdDirector) {
        this.IdDirector = IdDirector;
    }

    public int getIdDelegado() {
        return IdDelegado;
    }

    public void setIdDelegado(int IdDelegado) {
        this.IdDelegado = IdDelegado;
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

    public boolean insertarReporte()
    {
        String sql = "INSERT INTO Reportes(Id,IdConductor,IdDirector,IdDelegado,Tipo,Lugar,Fecha,Descripcion)"+
                     "VALUES(?,?,?,?,?,?,?)";
        conexion = new ConexionAccess();
        conexion.conectar();
        try
        {
           try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql))
           {
               ps.setInt(1, IdConductor);
               ps.setInt(2, IdDirector);
               ps.setInt(3, IdDelegado);
               ps.setString(4, Tipo);
               ps.setString(5, Lugar);
               ps.setString(6, Fecha);
               ps.setString(7, Descripcion);
               ps.execute();
               ps.close();
           }
           return true; 
        }catch(SQLException e)
        {
            System.out.println("Error al insertar en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarReporte()
    {
      String sql = "UPDATE Reportes SET Tipo= ?, Lugar=?, Fecha=?, Descripcion=?"+
                   "WHERE Id=?";  
      conexion = new ConexionAccess();
      conexion.conectar();
      
      try{
          try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql))
          {
              ps.setString(1, Tipo);
              ps.setString(2, Lugar);
              ps.setString(3, Fecha);
              ps.setString(4, Descripcion);
              ps.setInt(5, Id);
              ps.execute();
              ps.close();
          }
         return true;
      }catch(SQLException e)
      {
          System.out.println("Error al modificar reporte en la base de datos." + e.getMessage()); 
           return false;
     }
    }
    
    public boolean eliminarReporte()
    {
       String sql= "DELETE FROM Reportes WHERE Id=?";
       conexion = new ConexionAccess();
       conexion.conectar();
       try
       {
           try(PreparedStatement ps = conexion.getConexion().prepareStatement(sql))
               
           {
               ps.setInt(1, Id);
               ps.executeUpdate();
               ps.close();
             return true;
           }
       }catch(SQLException e)
       {
           System.out.println("Error al eliminar de la base de datos. " + e.getMessage());
           return false;
       }
    } 
} //class