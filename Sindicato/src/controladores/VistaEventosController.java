package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Eventos;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class VistaEventosController implements Initializable {
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private Eventos eventos;
    private boolean search = false;
    
    @FXML
    private GridPane fechaInicioDate,fechaFinDate;
    // Para obtener fechas se recure el DatePicker
    @FXML 
    private DatePicker date,date2;
    @FXML
    private Button agregar,modificar,eliminar,buscar;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TableView<Eventos> eventosTabla;
    @FXML
    private TableColumn <Eventos, String> claveEvento;
    @FXML
    private TableColumn <Eventos, String> nombreJefe;
    @FXML
    private TableColumn <Eventos, String> idEmpleado;;
    @FXML
    private TableColumn <Eventos, String> fechaInicio;
    @FXML
    private TableColumn <Eventos, String> fechaFin;
    @FXML
    private TableColumn <Eventos, String> nombreEvento;
    
    
    @FXML
    private ChangeListener<String> searchListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               eventosTabla.getItems().clear();
               fillTable(nombreTextField.getText());
            }
        };
    
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
        if(this.employee.getType() != 3){
            agregar.setDisable(true);
            eliminar.setDisable(true);
            modificar.setDisable(true);
        }
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
        if(e.getSource() == agregar){
            registrar();
        }else if(e.getSource() == modificar){
            actualizar();
        }else if(e.getSource() == eliminar){
            eliminar();
        }
    }
    @FXML
    private void tablaReportesAction(MouseEvent e) {
        setDataOnFields();
    }
    public void registrar(){
        LocalDate dateValue = date.getValue();
        LocalDate dateValue2 = date2.getValue();
        try {
            if(!nombreTextField.getText().equals("")
                    && dateValue !=null
                    && dateValue2 != null){
                if(date.getValue().isBefore(date2.getValue())){
                    statement = conexion.getConexion().prepareStatement("insert "
                    + "into Eventos"
                    + "(IdDirector,FechaInicio,FechaFin,Nombre)"
                    + " values(?,?,?,?)");
                    statement.setString(1,employee.getIdEmpleado());
                    statement.setDate(2, Date.valueOf(date.getValue()));
                    statement.setDate(3,Date.valueOf(date2.getValue()));
                    statement.setString(4, nombreTextField.getText());
                    if(statement.executeUpdate() != 0){
                        eventosTabla.getItems().clear();
                        fillTable("");
                    }else{
                        showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                    }
                }else{
                    showAlert(AlertType.WARNING,"¡Ups!","La fecha de inicio "
                            + "debe ser menor que la fecha en la que"
                            + "termina el evento");
                }
            }else{
                showAlert(AlertType.ERROR,"Error","Por favor llena todos los campos.");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void actualizar(){
        if(eventos != null){
                LocalDate dateValue = date.getValue();
                LocalDate dateValue2 = date2.getValue();
                try {
                    if(!nombreTextField.getText().equals("")
                            && dateValue !=null
                            && dateValue2 != null){
                        if(showAlertConfirmation("¿Esta seguro que desea actualizar el registro"
                        + "\ncon numero de clave \""+eventos.getClaveEvento()
                        +"\"?"
                        ).get()
                        == ButtonType.OK){
                            if(date.getValue().isBefore(date2.getValue())){
                                statement = conexion.getConexion().prepareStatement("update "
                                + "Eventos set"
                                + " IdDirector=?,FechaInicio=?,FechaFin=?,Nombre=? "
                                + "where Id=?");
                                statement.setString(1,employee.getIdEmpleado());
                                statement.setDate(2, Date.valueOf(date.getValue().toString()));
                                statement.setDate(3, Date.valueOf(date2.getValue().toString()));
                                statement.setString(4, nombreTextField.getText());
                                statement.setInt(5, Integer.parseInt(eventos.getClaveEvento()));
                                if(statement.executeUpdate() != 0){
                                    showAlert(AlertType.INFORMATION,"","Registro actualizado.");
                                    eventosTabla.getItems().clear();
                                    if(!search){
                                        fillTable("");
                                    }else{
                                        fillTable(nombreTextField.getText());
                                    }
                                }else{
                                    showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                                }
                            }else{
                                showAlert(AlertType.WARNING,"¡Ups!","La fecha de inicio "
                                        + "debe ser menor que la fecha en la que"
                                        + "termina el evento");
                            }
                        }
                    }else{
                        showAlert(AlertType.ERROR,"Error","Por favor llena todos los campos.");
                    }
                } catch (SQLException ex) {
                    System.out.println("Aqui trono"+ex);
                }
        }else{
            showAlert(AlertType.WARNING,"¡Ups!","Por favor selecciona algun registro que desees modificar");
        }
    }
    public void eliminar(){
        if(eventos != null){
            try {
                if(showAlertConfirmation("¿Esta seguro que desea eliminar el registro"
                + "\ncon numero de clave \""+eventos.getClaveEvento()
                +"\"?"
                ).get()
                == ButtonType.OK){
                    statement = conexion.getConexion().prepareStatement("Delete "
                    + "From Eventos where Id=?");
                    statement.setInt(1, Integer.parseInt(eventos.getClaveEvento()));
                    if(statement.executeUpdate() != 0){
                        showAlert(AlertType.INFORMATION,"","Registro eliminado.");
                        eventosTabla.getItems().clear();
                        if(!search){
                            fillTable("");
                        }else{
                            fillTable(nombreTextField.getText());
                        }
                    }else{
                        showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                    }

                }

            } catch (SQLException ex) {
                System.out.println("Aqui trono"+ex);
            }
        }else{
            showAlert(AlertType.WARNING,"¡Ups!","Por favor selecciona algun registro que desees modificar");
        }
    }
    public void buscar(){
        if(!search){
            search = true;
            clearFields();
            if(this.employee.getType() == 3){
                agregar.setDisable(true);
                modificar.setDisable(true);
            }
            nombreTextField.textProperty().addListener(searchListener);
        }else{
            search = false;
            clearFields();
            if(this.employee.getType() == 3){
                agregar.setDisable(false);
                modificar.setDisable(false);
            }
            eventosTabla.getItems().clear();
            fillTable("");
            nombreTextField.textProperty().removeListener(searchListener);
        }
    }
    public void fillTable(String nombreEvento){
        try {
            if(!search){
                statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdDirector,FechaInicio,FechaFin,Nombre "
                    + "From Eventos");
            }else{
               statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdDirector,FechaInicio,FechaFin,Nombre "
                    + "From Eventos where Nombre=?");
               statement.setString(1, nombreEvento);
            }
            result = statement.executeQuery();
            while(result.next()){
                eventos= new Eventos();
                eventos.setClaveEvento(result.getString(1));
                eventos.setIdEmpleado(result.getString(2));
                eventos.setNombreJefe(employeeName(result.getString(2)));
                eventos.setFechaInicio(result.getDate(3).toString());
                eventos.setFechaFin(result.getDate(4).toString());
                eventos.setNombreEvento(result.getString(5));
                putData();
                eventosTabla.getItems().add(eventos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaEventosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String employeeName(String idEmpleado){
        String driverName = "";
        try {
            statement = conexion.getConexion().prepareStatement("Select "
                    + "Nombres,ApellidoPaterno,ApellidoMaterno "
                    + "From Empleado where id=?");
            statement.setString(1, idEmpleado);
            resultEmpleado = statement.executeQuery();
            if(resultEmpleado.next()){
                driverName = resultEmpleado.getString(1)+" "+resultEmpleado.getString(2)
                        +" "+resultEmpleado.getString(3);
                return driverName;
            }
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(VistaEventosController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    public void putData(){
        claveEvento.setCellValueFactory(new PropertyValueFactory<>("ClaveEvento"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
        nombreJefe.setCellValueFactory(new PropertyValueFactory<>("NombreJefe"));
        fechaInicio.setCellValueFactory(new PropertyValueFactory<>("FechaInicio"));
        fechaFin.setCellValueFactory(new PropertyValueFactory<>("FechaFin"));
        nombreEvento.setCellValueFactory(new PropertyValueFactory<>("NombreEvento"));
    }
    public void clearFields(){
        nombreTextField.clear();
        nombreTextField.setPromptText("Ingresa el nombre del evento");
        date.setValue(null);
        date2.setValue(null);
    }
    public void setDataOnFields(){
        eventos = eventosTabla.getSelectionModel().getSelectedItem();
        if(eventos != null){
            nombreTextField.setText(eventos.getNombreEvento());
            date.setValue(LocalDate.parse(eventos.getFechaInicio()));
            date2.setValue(LocalDate.parse(eventos.getFechaFin()));
        }else{
            clearFields();
        }
    }
    public void showAlert(AlertType error,String header, String body){
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }
    public Optional<ButtonType> showAlertConfirmation(String content){
       alert = new Alert(AlertType.CONFIRMATION);
       alert.setContentText(content);
       return alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker();
        date.setValue(LocalDate.now());
    }
}
