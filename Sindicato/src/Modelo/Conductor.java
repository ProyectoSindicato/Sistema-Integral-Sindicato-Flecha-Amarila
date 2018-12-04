package Modelo;
    
import ConexionAccess.ConexionAccess;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conductor {
    private String idConductor;    
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;    
    private String fechaNacimiento;
    private String lugarNacimiento;
    private int    IdTelefono;
    private String telefono;  
    private String estadoCivil;      
    private String fechaIngreso;
    private String fechaSindicato;
    private String estudios;
    private String noIMSS;
    private String afore;
    private String curp;
    private String rfc;
    private String claveElector;
    private String foto;
    
    private int    idDomicilio;  
    private String estado;
    private String ciudad;
    private String colonia;
    private String calle;
    private String numExt;
    private String cp;
      
    private String base;
    private String servicio;
    private String noCuenta;
    
    private String idLicencia;
    private String fechaExpiracion;
    private String fechaExpedicion;
    
    ConexionAccess conexion;

    public Conductor() {
    }

    public Conductor(String idConductor, String nombres, String apellidoPaterno, String apellidoMaterno, String fechaNacimiento, String lugarNacimiento, String telefono, String estadoCivil, String fechaIngreso, String fechaSindicato, String estudios, String noIMSS, String afore, String curp, String rfc, String claveElector, int idDomicilio, String estado, String ciudad, String colonia, String calle, String numExt, String cp) {
        this.idConductor = idConductor;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.telefono = telefono;
        this.estadoCivil = estadoCivil;
        this.fechaIngreso = fechaIngreso;
        this.fechaSindicato = fechaSindicato;
        this.estudios = estudios;
        this.noIMSS = noIMSS;
        this.afore = afore;
        this.curp = curp;
        this.rfc = rfc;
        this.claveElector = claveElector;
        this.idDomicilio = idDomicilio;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.calle = calle;
        this.numExt = numExt;
        this.cp = cp;
    }

    public Conductor(int id, String tipoId) {
        if(tipoId.equals("Domicilio")) this.idDomicilio = id;
        if(tipoId.equals("Telefono")) this.IdTelefono = id;
        
    }
 
    public Conductor(String idConductor, String nombres, String apellidoPaterno, 
            String apellidoMaterno, String fechaNacimiento, String lugarNacimiento, 
            int IdTelefono, String telefono, String estadoCivil, String fechaIngreso, String fechaSindicato, 
            String estudios, String noIMSS, String afore, String curp, String rfc, 
            String claveElector, /*String foto,*/ int idDomicilio, String estado, String ciudad, 
            String colonia, String calle, String numExt, String cp, String base, 
            String servicio, String noCuenta, String idLicencia, String fechaExpiracion, String fechaExpedicion) {
        this.idConductor = idConductor;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.IdTelefono = IdTelefono;
        this.telefono = telefono;
        this.estadoCivil = estadoCivil;
        this.fechaIngreso = fechaIngreso;
        this.fechaSindicato = fechaSindicato;
        this.estudios = estudios;
        this.noIMSS = noIMSS;
        this.afore = afore;
        this.curp = curp;
        this.rfc = rfc;
        this.claveElector = claveElector;
        //this.foto = foto;
        this.idDomicilio = idDomicilio;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.calle = calle;
        this.numExt = numExt;
        this.cp = cp;        
        this.base = base;
        this.servicio = servicio;
        this.noCuenta = noCuenta;
        this.idLicencia = idLicencia;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }
   
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public int getIdTelefono() {
        return IdTelefono;
    }

    public void setIdTelefono(int IdTelefono) {
        this.IdTelefono = IdTelefono;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSindicato() {
        return fechaSindicato;
    }

    public void setFechaSindicato(String fechaSindicato) {
        this.fechaSindicato = fechaSindicato;
    }

    public String getEstudios() {
        return estudios;
    }

    public void setEstudios(String estudios) {
        this.estudios = estudios;
    }

    public String getNoIMSS() {
        return noIMSS;
    }

    public void setNoIMSS(String noIMSS) {
        this.noIMSS = noIMSS;
    }

    public String getAfore() {
        return afore;
    }

    public void setAfore(String afore) {
        this.afore = afore;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getClaveElector() {
        return claveElector;
    }

    public void setClaveElector(String claveElector) {
        this.claveElector = claveElector;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(int idDomicilio) {
        this.idDomicilio = idDomicilio;
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

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }    

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public String getIdLicencia() {
        return idLicencia;
    }

    public void setIdLicencia(String idLicencia) {
        this.idLicencia = idLicencia;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }
    
    public ConexionAccess getConexion() {
        return conexion;
    }

    public void setConexion(ConexionAccess conexion) {
        this.conexion = conexion;
    }
    
    public boolean insertarDomicilio() {
        //idDomicilio = IDDom;
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
                ps.setString(5, numExt);
                ps.setString(6, cp);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar domicilio en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean insertarEmpleado() {
        //idDomicilio = IDDom;
        //this.idDomicilio = idDomicilioForanea;
        //String sqlIdDomicilio = "SELECT TOP 1 Id from Domicilio ORDER BY Id DESC;";
        //idDomicilio = IDDomicilio;
        
        String sql = "INSERT INTO Empleado(Id,Nombres,ApellidoPaterno,ApellidoMaterno,FechaNacimiento,LugarNacimiento,"
                + "EstadoCivil,IdDomicilio,FechaIngreso,FechaSindicato,Estudios,NumeroIMSS,Afore,Curp,RFC,ClaveElector,Foto)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();      
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idConductor);
                ps.setString(2, nombres);
                ps.setString(3, apellidoPaterno);
                ps.setString(4, apellidoMaterno);
                ps.setDate  (5, Date.valueOf(fechaNacimiento));
                ps.setString(6, lugarNacimiento);
                ps.setString(7, estadoCivil);
                ps.setInt   (8, idDomicilio);
                ps.setDate  (9, Date.valueOf(fechaIngreso));
                ps.setDate  (10, Date.valueOf(fechaSindicato));
                ps.setString(11, estudios);
                ps.setString(12, noIMSS);
                ps.setString(13, afore);
                ps.setString(14, curp);
                ps.setString(15, rfc);
                ps.setString(16, claveElector);
                ps.setString(17, foto);
                
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar empleado en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean insertarLicencia() {
        String sql = "INSERT INTO Licencia(Id,FechaExpiracion,FechaExpedicion)"
                + "VALUES(?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idLicencia);
                ps.setDate  (2, Date.valueOf(fechaExpiracion));
                ps.setDate  (3, Date.valueOf(fechaExpedicion));
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar licencia en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean insertarConductor(String idEmpleadoForanea, String idLicenciaForanea) {
        this.idConductor = idEmpleadoForanea;
        this.idLicencia = idLicenciaForanea;
        String sql = "INSERT INTO Conductor(Id,IdLicencia,Base,Servicio,NoCuenta)"
                + "VALUES(?,?,?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idConductor);
                ps.setString(2, idLicencia);
                ps.setString(3, base);
                ps.setString(4, servicio);
                ps.setInt(5, Integer.parseInt(noCuenta));
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean insertarTelefono() {
        //this.idConductor = idEmpleadoForanea;
        String sql = "INSERT INTO Telefono(Id,IdEmpleado, NumeroTelefono)"
                + "VALUES(?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, IdTelefono);
                ps.setString(2, idConductor);
                ps.setString(3, telefono);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarDomicilio(int id) {
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
                ps.setString(5, numExt);
                ps.setString(6, cp);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Domicilio en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarEmpleado(String id) {
        String sql = "UPDATE Empleado SET Id = ?,Nombres =?, ApellidoPaterno= ?, ApellidoMaterno=?, FechaNacimiento=?, "
                + "LugarNacimiento=?, EstadoCivil=?, FechaIngreso=?, FechaSindicato=?, Estudios=?, NumeroIMSS=?, "
                + "Afore=?, Curp=?, RFC=?, ClaveElector=?, Foto = ?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idConductor);
                ps.setString(2, nombres);
                ps.setString(3, apellidoPaterno);
                ps.setString(4, apellidoMaterno);               
                ps.setDate  (5, Date.valueOf(fechaNacimiento));
                ps.setString(6, lugarNacimiento);
                ps.setString(7, estadoCivil);
                ps.setDate  (8, Date.valueOf(fechaIngreso));
                ps.setDate  (9, Date.valueOf(fechaSindicato));
                ps.setString(10, estudios);
                ps.setString(11, noIMSS);
                ps.setString(12, afore);
                ps.setString(13, curp);
                ps.setString(14, rfc);
                ps.setString(15, claveElector);
                ps.setString(16, foto);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Empleado en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarLicencia(String id) {
        String sql = "UPDATE Licencia SET Id = ?,FechaExpiracion = ?,FechaExpedicion =?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idLicencia);
                ps.setDate  (2, Date.valueOf(fechaExpiracion));
                ps.setDate  (3, Date.valueOf(fechaExpedicion));
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Licencia en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarTelefono(int id) {
        String sql = "UPDATE Telefono SET NumeroTelefono = ?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, telefono);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Telefono en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean modificarConductor(String id) {
        String sql = "UPDATE Conductor SET IdLicencia = ?,Base = ?,Servicio =?,NoCuenta=?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, idLicencia);
                ps.setString(2, base);
                ps.setString(3, servicio);
                ps.setInt   (4, Integer.parseInt(noCuenta));
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarDomicilio(int id) {
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
            System.out.println("Error al eliminar de la base de datos Domicilio. " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarEmpleado(String id) {
        String sql = "DELETE FROM Empleado WHERE Id="+id;
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
            System.out.println("Error al eliminar de la base de datos Empleado. " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarTelefono(int id) {
        String sql = "DELETE FROM Telefono WHERE Id="+id;
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
            System.out.println("Error al eliminar de la base de datos Telefono. " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarLicencia(String id) {
        String sql = "DELETE FROM Licencia WHERE Id="+id;
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
            System.out.println("Error al eliminar de la base de datos Licencia. " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarConductor(String id) {
        String sql = "DELETE FROM Conductor WHERE Id="+id;
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
            System.out.println("Error al eliminar de la base de datos Conductor. " + e.getMessage());
            return false;
        }
    }
    
    public ResultSet filtrarConductor(String id) {
        ResultSet resultado = null;
        System.out.println("Entró al filtro de reportes.");
        conexion = new ConexionAccess();
        conexion.conectar();
        try { //Este SQL contiene todo lo que necesito para traerme la lista de lo que quiero (Checar en el controller, parte: actualizarBD).
            String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono "
                + "FROM ((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.Id = Conductor.IdLicencia) "
                + "ON Empleado.id  = Conductor.id) INNER JOIN Telefono ON Empleado.id = Telefono.idEmpleado WHERE Empleado.Id LIKE \""+id+"*\" ";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            //ps.setString(1, id);
            resultado = ps.executeQuery();
        } catch (SQLException ex) {
            System.out.println("No entró. Revísalo.");
        }
        return resultado;
    }
    
}
