package Modelo;
    
public class Conductor {
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String fechaNacimiento;
    private String lugarNacimiento;
    private String telefono;
    private String calle;
    private String colonia;
    private String cp;
    private String ciudad;
    private String estado;
    private String fechaIngreso;
    private String fechaSindicato;
    private String estudios;
    private String noIMSS;
    private String afore;
    private String curp;
    private String rfc;
    private String claveElector;
    private String noTarjeta;
    private String base;
    private String servicio;

    public Conductor(String apellidoPaterno, String apellidoMaterno, String nombres, String fechaNacimiento, String lugarNacimiento, String telefono, String calle, String colonia, String cp, String ciudad, String estado, String fechaIngreso, String fechaSindicato, String estudios, String noIMSS, String afore, String curp, String rfc, String claveElector, String noTarjeta, String base, String servicio) {
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.telefono = telefono;
        this.calle = calle;
        this.colonia = colonia;
        this.cp = cp;
        this.ciudad = ciudad;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.fechaSindicato = fechaSindicato;
        this.estudios = estudios;
        this.noIMSS = noIMSS;
        this.afore = afore;
        this.curp = curp;
        this.rfc = rfc;
        this.claveElector = claveElector;
        this.noTarjeta = noTarjeta;
        this.base = base;
        this.servicio = servicio;
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getNoTarjeta() {
        return noTarjeta;
    }

    public void setNoTarjeta(String noTarjeta) {
        this.noTarjeta = noTarjeta;
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
    
    
    
}
