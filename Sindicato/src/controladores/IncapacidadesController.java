package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Incapacidades;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class IncapacidadesController implements Initializable {
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private Incapacidades incapacidades;
    @FXML
    private GridPane fechaInicioDate,fechaFinDate;
    // Para obtener fechas se recure el DatePicker
    @FXML 
    private DatePicker date,date2;
    @FXML
    private Button registrar;
    @FXML
    private TextField claveConductorTextField;
    @FXML
    private TextArea motivoTextArea;
    @FXML
    private TableView<Incapacidades> incapacidadesTabla;
    @FXML
    private TableColumn <Incapacidades, String> claveConductor;
    @FXML
    private TableColumn <Incapacidades, String> nombreConductor;
    @FXML
    private TableColumn <Incapacidades, String> fechaInicio;
    @FXML
    private TableColumn <Incapacidades, String> fechaFin;
    @FXML
    private TableColumn <Incapacidades, String> motivo;
    @FXML
    private TableColumn <Incapacidades, String> nombreJefe;
    @FXML
    private TableColumn <Incapacidades, String> idEmpleado;;
    
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
    }
    void datePicker()
      {
           date = new DatePicker();
           fechaInicioDate.add(date, 0, 0);
           date2 = new DatePicker();
           fechaFinDate.add(date2, 0, 0);
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
            if(isIdConductorCorrect()){
                // Si el que realiza la insercion es un director de finanzas
                statement = conexion.getConexion().prepareStatement("insert "
                        + "into Incapacidades"
                        + "(IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo)"
                        + " values(?,?,?,?,?)");
                statement.setString(1,claveConductorTextField.getText());
                statement.setString(2,employee.getIdEmpleado());
                statement.setDate(3, Date.valueOf(date.getValue()));
                statement.setDate(4,Date.valueOf(date2.getValue()));
                statement.setString(5, motivoTextArea.getText());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Aqui trono"+ex.getMessage());
        }
    }
    public boolean isIdConductorCorrect(){
        try {
            statement = conexion.getConexion().prepareStatement("Select Id "
                    + "From Conductor "
                    + "where Id=?");
            statement.setString(1,claveConductorTextField.getText());
            result = statement.executeQuery();
            if(result.next()){
                System.out.println("simon");
             return true;   
            }else{
                alert.setTitle("Error");
                alert.setHeaderText("Clave de conductor no encontrada.");
                alert.setContentText("Por favor verifica la clave del conductor, debe "
                        + "contener unicamente numeros. Espacios, letras"
                        + " y caracteres especiales no son aceptados");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public void fillTable(){
        try {
            statement = conexion.getConexion().prepareStatement("Select "
                    + "IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo "
                    + "From Incapacidades");
            result = statement.executeQuery();
            while(result.next()){
                incapacidades.setClaveConductor(result.getString(1));
                incapacidades.setNombreConductor(employeeName(result.getString(1)));
                incapacidades.setFechaInicio(result.getDate(3).toString());
                incapacidades.setFechaFin(result.getDate(4).toString());
                incapacidades.setMotivo(result.getString(5));
                incapacidades.setIdEmpleado(employee.getIdEmpleado());
                incapacidades.setNombreJefe(employeeName(employee.getIdEmpleado()));
                putData();
                incapacidadesTabla.getItems().add(incapacidades);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String employeeName(String idConductor){
        String driverName = "";
        try {
            statement = conexion.getConexion().prepareStatement("Select "
                    + "Nombres,ApellidoPaterno,ApellidoMaterno "
                    + "From Empleado where id=?");
            statement.setString(1, idConductor);
            resultEmpleado = statement.executeQuery();
            if(resultEmpleado.next()){
                driverName = resultEmpleado.getString(1)+" "+resultEmpleado.getString(2)
                        +" "+resultEmpleado.getString(3);
                return driverName;
            }
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(IncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    public void putData(){
        claveConductor.setCellValueFactory(new PropertyValueFactory<>("ClaveConductor"));
        nombreConductor.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
        fechaInicio.setCellValueFactory(new PropertyValueFactory<>("FechaInicio"));
        fechaFin.setCellValueFactory(new PropertyValueFactory<>("FechaFin"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
        nombreJefe.setCellValueFactory(new PropertyValueFactory<>("NombreJefe"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker();
        alert = new Alert(AlertType.ERROR);
        incapacidades= new Incapacidades();
    }    
    
}
