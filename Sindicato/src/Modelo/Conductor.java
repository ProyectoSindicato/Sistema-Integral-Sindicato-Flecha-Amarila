package Modelo;
    
import ConexionAccess.ConexionAccess;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Blob foto;
    //private BinaryStream foto;
    
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
    
    private int    idBeneficiarios;
    private String beneficiarios;
    private String parentesco;
    private String porcentaje;
    
    private File file;
    private FileInputStream img;
    
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
        if(tipoId.equals("Beneficiarios")) this.idBeneficiarios = id;
        
    }
 
    public Conductor(String idConductor, String nombres, String apellidoPaterno, 
            String apellidoMaterno, String fechaNacimiento, String lugarNacimiento, 
            int IdTelefono, String telefono, String estadoCivil, String fechaIngreso, String fechaSindicato, 
            String estudios, String noIMSS, String afore, String curp, String rfc, 
            String claveElector, Blob foto, int idDomicilio, String estado, String ciudad, 
            String colonia, String calle, String numExt, String cp, String base, 
            String servicio, String noCuenta, String idLicencia, String fechaExpiracion, String fechaExpedicion, 
            int idBeneficiarios, String beneficiarios, String parentesco,String porcentaje) {
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
        this.idBeneficiarios = idBeneficiarios;
        this.beneficiarios = beneficiarios;
        this.parentesco = parentesco;
        this.porcentaje = porcentaje;
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

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
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

    public int getIdBeneficiarios() {
        return idBeneficiarios;
    }

    public void setIdBeneficiarios(int idBeneficiarios) {
        this.idBeneficiarios = idBeneficiarios;
    }

    public String getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(String beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }    

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileInputStream getImg() {
        return img;
    }

    public void setImg(FileInputStream img) {
        this.img = img;
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
    
    public boolean insertarEmpleado(File imageFile, FileInputStream image) {
        //idDomicilio = IDDom;
        //this.idDomicilio = idDomicilioForanea;
        //String sqlIdDomicilio = "SELECT TOP 1 Id from Domicilio ORDER BY Id DESC;";
        //idDomicilio = IDDomicilio;
        file = imageFile;
        img = image;
        
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
                ps.setBinaryStream(17, image, (int) imageFile.length());
                //ps.setBinaryStream(17, image, (int) imageFile.lenght());
                
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
    
    public boolean insertarBeneficiarios() {
        //this.idConductor = idEmpleadoForanea;
        String sql = "INSERT INTO Beneficiarios(Id, IdConductor, Beneficiarios, Parentesco, Porcentaje)"
                + "VALUES(?,?,?,?,?)";
        
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setInt(1, idBeneficiarios);
                ps.setString(2, idConductor);
                ps.setString(3, beneficiarios);
                ps.setString(4, parentesco);
                ps.setString(5, porcentaje);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar beneficiarios en la base de datos." + e.getMessage());
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
    
    public boolean modificarEmpleado(String id, int idDomi)  {
        try {
            Database db = DatabaseBuilder.open(new File(conexion.getLocation()));
            
            Table t = db.getTable("Empleado");
            
            Row r = CursorBuilder.findRowByPrimaryKey(t, id);
            
            if(r!=null) {
                r.put("Id", id);
                t.updateRow(r);
                r.put("Nombres", nombres);
                t.updateRow(r);
                r.put("ApellidoPaterno", apellidoPaterno);
                t.updateRow(r);
                r.put("ApellidoMaterno", apellidoMaterno);
                t.updateRow(r);
                r.put("FechaNacimiento", Date.valueOf(fechaNacimiento));
                t.updateRow(r);
                r.put("LugarNacimiento", lugarNacimiento);
                t.updateRow(r);
                r.put("EstadoCivil", estadoCivil);
                t.updateRow(r);
                r.put("IdDomicilio", idDomi);
                t.updateRow(r);
                r.put("FechaIngreso",  Date.valueOf(fechaIngreso));
                t.updateRow(r);
                r.put("FechaSindicato",  Date.valueOf(fechaSindicato));
                t.updateRow(r);
                r.put("Estudios", estudios);
                t.updateRow(r);
                r.put("NumeroIMSS", noIMSS);
                t.updateRow(r);
                r.put("Afore", afore);
                t.updateRow(r);
                r.put("Curp", curp);
                t.updateRow(r);
                r.put("RFC", rfc);
                t.updateRow(r);
                r.put("ClaveElector", claveElector);
                t.updateRow(r);
                //r.put("Foto", foto);
                //t.updateRow(r);
                db.close();
                
            }
                        
            return true;
        } catch (IOException ex) {   
            Logger.getLogger(Conductor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    public boolean modificarLicencia(String id) {
        String sql = "UPDATE Licencia SET Id = ?,FechaExpiracion = ?,FechaExpedicion =?"
                + "WHERE Id=\""+id+"\"";
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
    
    public boolean modificarBeneficiarios(int id) {
        String sql = "UPDATE Beneficiarios SET Beneficiarios = ?, Parentesco = ?, Porcentaje = ?"
                + "WHERE Id="+id;
        conexion = new ConexionAccess();
        conexion.conectar();
        try {
            try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
                ps.setString(1, beneficiarios);
                ps.setString(2, parentesco);
                ps.setString(3, porcentaje);
                ps.execute();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar Beneficiarios en la base de datos." + e.getMessage());
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
    
    public boolean eliminarEmpleado(String id)  {
        try {
            Database db = DatabaseBuilder.open(new File(conexion.getLocation()));
            
            Table t = db.getTable("Empleado");
            
            Row r = CursorBuilder.findRowByPrimaryKey(t, id);
            
            if(r!=null) {
                t.deleteRow(r);
                //r.put("Foto", foto);
                //t.updateRow(r);
                db.close();
                
            }
                        
            return true;
        } catch (IOException ex) {   
            Logger.getLogger(Conductor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    /*public boolean eliminarEmpleado(String id) {
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
    }*/
    
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
    
    public boolean eliminarBeneficiarios(int id) {
        String sql = "DELETE FROM Beneficiarios WHERE Id="+id;
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
            System.out.println("Error al eliminar de la base de datos Beneficiarios. " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarLicencia(String id) {
        String sql = "DELETE FROM Licencia WHERE Id=\""+id+"\"";
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
            /*String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono "
                + "FROM ((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.Id = Conductor.IdLicencia) "
                + "ON Empleado.id  = Conductor.id) INNER JOIN Telefono ON Empleado.id = Telefono.idEmpleado WHERE Empleado.Id LIKE \""+id+"*\" ";*/
            String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector, Empleado.foto, "
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono, Beneficiarios.Id AS Beneficiarios_id, Beneficiarios.Beneficiarios, Beneficiarios.Parentesco, Beneficiarios.Porcentaje "
                + "FROM (((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN Telefono ON Empleado.[Id] = Telefono.[IdEmpleado]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.[Id] = Conductor.[IdLicencia]) ON Empleado.[Id] = Conductor.[Id]) "
                + "INNER JOIN Beneficiarios ON Conductor.[Id] = Beneficiarios.[IdConductor] WHERE Empleado.Id LIKE \""+id+"*\" ";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            //ps.setString(1, id);
            resultado = ps.executeQuery();
        } catch (SQLException ex) {
            System.out.println("No entró. Revísalo.");
        }
        return resultado;
    }
    
}
