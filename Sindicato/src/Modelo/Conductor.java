package Modelo;
    
import ConexionAccess.ConexionAccess;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conductor {
    private String idConductor;    
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;    
    private String fechaNacimiento;
    private String lugarNacimiento;
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

    public Conductor(String idConductor, String nombres, String apellidoPaterno, 
            String apellidoMaterno, String fechaNacimiento, String lugarNacimiento, 
            String telefono, String estadoCivil, String fechaIngreso, String fechaSindicato, 
            String estudios, String noIMSS, String afore, String curp, String rfc, 
            String claveElector, String foto, int idDomicilio, String estado, String ciudad, 
            String colonia, String calle, String numExt, String cp, String base, 
            String servicio, String noCuenta, String idLicencia, String fechaExpiracion, String fechaExpedicion) {
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
        this.foto = foto;
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
    
    public boolean insertarEmpleado(int idDomicilioForanea) {
        this.idDomicilio = idDomicilioForanea;
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
                ps.setString(5, fechaNacimiento);
                ps.setString(6, lugarNacimiento);
                ps.setString(7, estadoCivil);
                ps.setInt   (8, idDomicilio);
                ps.setString(9, fechaIngreso);
                ps.setString(10, fechaSindicato);
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
        String sql = "INSERT INTO Licencia(FechaExpiracion,FechaExpedicion)"
                + "VALUES(?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, fechaExpiracion);
                ps.setString(2, fechaExpedicion);
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
                ps.setString(5, noCuenta);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar conductor en la base de datos." + e.getMessage());
            return false;
        }
    }
    
}
