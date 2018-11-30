package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Incapacidades;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class IncapacidadesController implements Initializable {
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private Incapacidades incapacidades;
    private boolean search = false;
    
    @FXML
    private GridPane fechaInicioDate,fechaFinDate;
    // Para obtener fechas se recure el DatePicker
    @FXML 
    private DatePicker date,date2;
    @FXML
    private Button agregar,modificar,eliminar,buscar;
    @FXML
    private TextField claveConductorTextField;
    @FXML
    private TextArea motivoTextArea;
    @FXML
    private TableView<Incapacidades> incapacidadesTabla;
    @FXML
    private TableColumn <Incapacidades, String> claveIncapacidad;
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
    
    @FXML
    private ChangeListener<String> searchListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               incapacidadesTabla.getItems().clear();
               fillTable(claveConductorTextField.getText());
            }
        };
    
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
        if(this.employee.getType() != 0 && this.employee.getType() != 4){
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
            if(!claveConductorTextField.getText().equals("")
                    && !employee.getIdEmpleado().equals("")
                    && dateValue !=null
                    && dateValue2 != null
                    && !motivoTextArea.getText().equals("")){
                if(isIdConductorCorrect()){
                    if(isConductorAlready()){
                        if(date.getValue().isBefore(date2.getValue())){
                            statement = conexion.getConexion().prepareStatement("insert "
                            + "into Incapacidades"
                            + "(IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo)"
                            + " values(?,?,?,?,?)");
                            statement.setString(1,claveConductorTextField.getText());
                            statement.setString(2,employee.getIdEmpleado());
                            statement.setDate(3, Date.valueOf(date.getValue()));
                            statement.setDate(4,Date.valueOf(date2.getValue()));
                            statement.setString(5, motivoTextArea.getText());
                            if(statement.executeUpdate() != 0){
                                incapacidadesTabla.getItems().clear();
                                fillTable("");
                            }else{
                                showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                            }
                        }else{
                            showAlert(AlertType.WARNING,"¡Ups!","La fecha de inicio "
                                    + "debe ser menor que la fecha en la que"
                                    + "termina la incapacidad");
                        }
                    }else{
                        showAlert(AlertType.WARNING,"¡Usuario ya cuenta con incapacida!"
                                ,"Actualmente el conductor \""+employeeName(claveConductorTextField.getText())
                        + "\" cuenta con una incapacitacion \nvigente, si deseas extender o reducir su periodo"+
                                "de incapacidad\npor favor actualiza su ultimo registro vigente.");
                    }
                }
            }else{
                showAlert(AlertType.ERROR,"Error","Por favor llena todos los campos.");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void actualizar(){
        if(incapacidades != null){
                LocalDate dateValue = date.getValue();
                LocalDate dateValue2 = date2.getValue();
                try {
                    if(!claveConductorTextField.getText().equals("")
                            && !employee.getIdEmpleado().equals("")
                            && dateValue !=null
                            && dateValue2 != null
                            && !motivoTextArea.getText().equals("")){
                        if(showAlertConfirmation("¿Esta seguro que desea actualizar el registro"
                        + "\ncon numero de clave \""+incapacidades.getClaveIncapacidad()
                        +"\"?"
                        ).get()
                        == ButtonType.OK){
                            if(isIdConductorCorrect()){
                                if(date.getValue().isBefore(date2.getValue())){
                                    statement = conexion.getConexion().prepareStatement("update "
                                    + "Incapacidades set"
                                    + " IdConductor=?,FechaInicio=?,FechaFin=?,Motivo=? "
                                    + "where Id=?");
                                    statement.setString(1,claveConductorTextField.getText());
                                    statement.setDate(2, Date.valueOf(date.getValue().toString()));
                                    statement.setDate(3, Date.valueOf(date2.getValue().toString()));
                                    statement.setString(4,motivoTextArea.getText());
                                    statement.setInt(5, Integer.parseInt(incapacidades.getClaveIncapacidad()));
                                    if(statement.executeUpdate() != 0){
                                        showAlert(AlertType.INFORMATION,"","Registro actualizado.");
                                        incapacidadesTabla.getItems().clear();
                                        if(!search){
                                            fillTable("");
                                        }else{
                                            fillTable(claveConductorTextField.getText());
                                        }
                                    }else{
                                        showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                                    }
                                }else{
                                    showAlert(AlertType.WARNING,"¡Ups!","La fecha de inicio "
                                            + "debe ser menor que la fecha en la que"
                                            + "termina la incapacidad");
                                }
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
        if(incapacidades != null){
            try {
                if(showAlertConfirmation("¿Esta seguro que desea eliminar el registro"
                + "\ncon numero de clave \""+incapacidades.getClaveIncapacidad()
                +"\"?"
                ).get()
                == ButtonType.OK){
                    statement = conexion.getConexion().prepareStatement("Delete "
                    + "From Incapacidades where Id=?");
                    statement.setInt(1, Integer.parseInt(incapacidades.getClaveIncapacidad()));
                    if(statement.executeUpdate() != 0){
                        showAlert(AlertType.INFORMATION,"","Registro eliminado.");
                        incapacidadesTabla.getItems().clear();
                        if(!search){
                            fillTable("");
                        }else{
                            fillTable(claveConductorTextField.getText());
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
            agregar.setDisable(true);
            claveConductorTextField.textProperty().addListener(searchListener);
        }else{
            search = false;
            clearFields();
            agregar.setDisable(false);
            incapacidadesTabla.getItems().clear();
            fillTable("");
            claveConductorTextField.textProperty().removeListener(searchListener);
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
             return true;   
            }else{
                showAlert(AlertType.ERROR,"Error","Clave de conductor incorrecta favor de verificar");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /*
    Verificamos si al conductor que se intenta registrar
    no se encuentra en un periodo vigente.
    */
    public boolean isConductorAlready(){
        LocalDate dateValue = date.getValue();
        boolean maxDate = false;
        try {
            statement = conexion.getConexion().prepareStatement("Select FechaFin "
                    + "From Incapacidades "
                    + "where IdConductor=?");
            statement.setString(1,claveConductorTextField.getText());
            result = statement.executeQuery();
            while(result.next()){
                if(dateValue.isAfter(result.getDate(1).toLocalDate())){
                    maxDate = true;
                }else{
                    maxDate = false;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return maxDate;
    }
    public void fillTable(String idConductor){
        try {
            if(!search){
                statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo "
                    + "From Incapacidades");
            }else{
               statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdConductor,IdEmpleado,FechaInicio,FechaFin,Motivo "
                    + "From Incapacidades where IdConductor=?");
               statement.setString(1, idConductor);
            }
            result = statement.executeQuery();
            while(result.next()){
                incapacidades= new Incapacidades();
                incapacidades.setClaveIncapacidad(result.getString(1));
                incapacidades.setClaveConductor(result.getString(2));
                incapacidades.setNombreConductor(employeeName(result.getString(2)));
                incapacidades.setFechaInicio(result.getDate(4).toString());
                incapacidades.setFechaFin(result.getDate(5).toString());
                incapacidades.setMotivo(result.getString(6));
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
        claveIncapacidad.setCellValueFactory(new PropertyValueFactory<>("ClaveIncapacidad"));
        claveConductor.setCellValueFactory(new PropertyValueFactory<>("ClaveConductor"));
        nombreConductor.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
        fechaInicio.setCellValueFactory(new PropertyValueFactory<>("FechaInicio"));
        fechaFin.setCellValueFactory(new PropertyValueFactory<>("FechaFin"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
        nombreJefe.setCellValueFactory(new PropertyValueFactory<>("NombreJefe"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
    }
    public void clearFields(){
        claveConductorTextField.clear();
        claveConductorTextField.setPromptText("Ingresa clave");
        date.setValue(null);
        date2.setValue(null);
        motivoTextArea.clear();
    }
    public void disableFields(){
        date.setDisable(true);
        date2.setDisable(true);
        motivoTextArea.setDisable(true);
        agregar.setDisable(true);
    }
    public void enableFields(){
        date.setDisable(false);
        date2.setDisable(false);
        motivoTextArea.setDisable(false);
        agregar.setDisable(false);
        modificar.setDisable(false);
    }
    public void setDataOnFields(){
        incapacidades = incapacidadesTabla.getSelectionModel().getSelectedItem();
        if(incapacidades != null){
            claveConductorTextField.setText(incapacidades.getClaveConductor());
            date.setValue(LocalDate.parse(incapacidades.getFechaInicio()));
            date2.setValue(LocalDate.parse(incapacidades.getFechaFin()));
            motivoTextArea.setText(incapacidades.getMotivo());
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
