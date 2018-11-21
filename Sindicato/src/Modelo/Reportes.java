package Modelo;
/**
 *
 * @author Aideé Alvarez
 */
public class Reportes {

    private int Id;
    private int IdConductor;
    private int IdDirector;
    private int IdDelegado;
    private String tipo;
    private String lugar;
    private String fecha;
    private String descripcion;

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setIdConductor(int IdConductor) {
        this.IdConductor = IdConductor;
    }

    public void setIdDirector(int IdDirector) {
        this.IdDirector = IdDirector;
    }

    public void setIdDelegado(int IdDelegado) {
        this.IdDelegado = IdDelegado;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return Id;
    }

    public int getIdConductor() {
        return IdConductor;
    }

    public int getIdDirector() {
        return IdDirector;
    }

    public int getIdDelegado() {
        return IdDelegado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
    
    
    
}
