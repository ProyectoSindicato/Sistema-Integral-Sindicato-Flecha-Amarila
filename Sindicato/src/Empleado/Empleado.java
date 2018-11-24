package Empleado;

public class Empleado {
    private int type;
    private String idEmpleado;
    
    public Empleado(int type, String idEmpleado){
        this.type = type;
        this.idEmpleado = idEmpleado;
    }

    public int getType() {
        return type;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }
}
