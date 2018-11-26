package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class IncapacidadesController implements Initializable {
    private PreparedStatement statement;
    private ResultSet result;
    private Empleado employee;
    private ConexionAccess conexion;
    
    @FXML
    private GridPane fechaInicio,fechaFin;
    // Para obtener fechas se recure el DatePicker
    @FXML 
    private DatePicker date,date2;
    @FXML
    private Button registrar;
    @FXML
    private TextField claveConductorTextField;
    @FXML
    private TextArea motivoTextArea;
    
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
    }
    void datePicker()
      {
           date = new DatePicker();
           fechaInicio.add(date, 0, 0);
           date2 = new DatePicker();
           fechaFin.add(date2, 0, 0);
      }
    @FXML
    private void handleButtonAction(ActionEvent e){
        //if(conexion.conectar()){
            if(e.getSource() == registrar){
                registrar();
            }
        //}
    }
    public void registrar(){
        try {
            // Si el que realiza la insercion es un director de finanzas
            statement = conexion.getConexion().prepareStatement("insert "
                    + "into Incapacidad"
                    + "(IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo)"
                    + " values(?,?,?,?,?)");
            statement.setInt(1,Integer.parseInt(claveConductorTextField.getText()));
            statement.setInt(2,Integer.parseInt(employee.getIdEmpleado()));
            statement.setDate(3, Date.valueOf(date.getValue()));
            statement.setDate(4,Date.valueOf(date2.getValue()));
            statement.setString(5, motivoTextArea.getText());
            //statement.executeQuery();
            if(statement.execute()){
                System.out.println("Done!");
            }else {
                System.out.println("algo salio mal :C");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker();
    }    
    
}
