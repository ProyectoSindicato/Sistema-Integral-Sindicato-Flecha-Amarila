package Modelo;

public class Demandas {
    private String claveConductor;
    private String nombreConductor;
    private String fecha;
    private String motivo;
    private String idEmpleado;
    private String claveDemanda;
    private String nombreJefe;
    private String observaciones;

    public String getClaveConductor() {
        return claveConductor;
    }

    public void setClaveConductor(String claveConductor) {
        this.claveConductor = claveConductor;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getClaveDemanda() {
        return claveDemanda;
    }

    public void setClaveDemanda(String claveAsesoria) {
        this.claveDemanda = claveAsesoria;
    }

    public String getNombreJefe() {
        return nombreJefe;
    }

    public void setNombreJefe(String nombreJefe) {
        this.nombreJefe = nombreJefe;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}
