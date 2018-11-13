package Beans;

import ConexionBD.AccessDB;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BeanPersonas {

    private int idPersona;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private int edad;
    private String descripcion;

    AccessDB conexion;

    /* Creamos los constructores. */
    public BeanPersonas(int idPersona, String nombre, String apellidoP, String apellidoM,
            int edad, String descripcion) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.descripcion = descripcion;
    }

    public BeanPersonas(){
    }
    
    /* Creamos todos nuestros setters y getters. */
    public void setID(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getID() {
        return idPersona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getEdad() {
        return edad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /* 
        **********Sección de sentencias**********
        Cada vez que se crea una, debemos hacer la conexion y cerrarla.
        
        Realmente los Bean son para eso, para poder acceder a cada objeto de la clase y 
        reducirnos el trabajo de estar haciendo sentencias más largas.
    */
    
    public boolean insertarPersona() {
        String sql = "INSERT INTO Personas(Nombre, ApellidoP, ApellidoM,Edad, Descripción)"
                + "VALUES(?,?,?,?,?)";
        conexion = new AccessDB();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, apellidoP);
                ps.setString(3, apellidoM);
                ps.setInt(4, edad);
                ps.setString(5, descripcion);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al insertar ---" + ex.getMessage());
            return false;
        }
    }
    
    public boolean actualizarPersona(int idPersona){
        this.idPersona = idPersona;
        String sql = "UPDATE Personas SET Nombre = ?, ApellidoP = ?, ApellidoM = ?,"+
                        "Edad = ?, Descripción = ? WHERE Id = ?";
        conexion = new AccessDB();
        conexion.conectar();
        try{
             try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, apellidoP);
                ps.setString(3, apellidoM);
                ps.setInt(4, edad);
                ps.setString(5, descripcion);
                ps.setInt(6,idPersona);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        }catch(SQLException ex){
            System.out.println("Error al modificar ---" + ex.getMessage());
            return false;
        }
    }
    
    public boolean eliminarPersona(int id) {
        this.idPersona = id;
        String sql= "DELETE FROM Personas WHERE Id =?";
         conexion = new AccessDB();
         conexion.conectar();
        try{
           
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1,id);
                ps.executeUpdate();
                ps.close();
            }
            return true;
        }catch(SQLException ex){
            System.out.println("Error al eliminar---" + ex.getMessage());
            return false;
        }
    }
    
}
