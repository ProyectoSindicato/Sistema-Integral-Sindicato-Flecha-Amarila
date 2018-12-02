package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Demandas;
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

public class VistaDemandasController implements Initializable{
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private boolean search = false;
    private Demandas demandas;
    
    @FXML 
    private DatePicker date;
    @FXML
    private GridPane fechaDate;
    @FXML
    private Button agregar,modificar,eliminar,buscar;
    @FXML
    private TextField claveConductorTextField;
    @FXML
    private TextArea observacionesTextArea,motivoTextArea;
    @FXML
    private TableView <Demandas> demandaTabla;
    @FXML
    private TableColumn <Demandas, String> claveDemanda;
    @FXML
    private TableColumn <Demandas, String> claveConductor;
    @FXML
    private TableColumn <Demandas, String> nombreConductor;
    @FXML
    private TableColumn <Demandas, String> fecha;
    @FXML
    private TableColumn <Demandas, String> motivo;
    @FXML
    private TableColumn <Demandas, String> observaciones;
    @FXML
    private TableColumn <Demandas, String> idEmpleado;
    @FXML
    private TableColumn <Demandas, String> nombreJefe;
    @FXML
    private final ChangeListener<String> searchListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               demandaTabla.getItems().clear();
               fillTable(claveConductorTextField.getText());
            }
    };
    
    public void datePicker(){
           date = new DatePicker();
           fechaDate.add(date, 0, 0);
    }
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
        if(this.employee.getType() != 4){
            agregar.setDisable(true);
            eliminar.setDisable(true);
            modificar.setDisable(true);
        }
    }
    public void fillTable(String idConductor){
        try {
            if(!search){
                statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdConductor,IdDirector,Fecha,Motivo,Observaciones "
                    + "From Demandas");
            }else{
               statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdConductor,IdDirector,Fecha,Motivo,Observaciones "
                    + "From Demandas where IdConductor=?");
               statement.setString(1, idConductor);
            }
            result = statement.executeQuery();
            while(result.next()){
                demandas = new Demandas();
                demandas.setClaveDemanda(result.getString(1));
                demandas.setClaveConductor(result.getString(2));
                demandas.setNombreConductor(employeeName(result.getString(2)));
                demandas.setFecha(result.getDate(4).toString());
                demandas.setMotivo(result.getString(5));
                demandas.setIdEmpleado(employee.getIdEmpleado());
                demandas.setNombreJefe(employeeName(employee.getIdEmpleado()));
                demandas.setObservaciones(result.getString(6));
                putData();
                demandaTabla.getItems().add(demandas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void putData(){
        claveDemanda.setCellValueFactory(new PropertyValueFactory<>("ClaveDemanda"));
        claveConductor.setCellValueFactory(new PropertyValueFactory<>("ClaveConductor"));
        nombreConductor.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
        observaciones.setCellValueFactory(new PropertyValueFactory<>("Observaciones"));
        nombreJefe.setCellValueFactory(new PropertyValueFactory<>("NombreJefe"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
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
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
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
                showAlert(Alert.AlertType.ERROR,"Error","Clave de conductor incorrecta favor de verificar");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public void showAlert(Alert.AlertType error,String header, String body){
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }
    public void setDataOnFields(){
        demandas = demandaTabla.getSelectionModel().getSelectedItem();
        if(demandas != null){
            claveConductorTextField.setText(demandas.getClaveConductor());
            date.setValue(LocalDate.parse(demandas.getFecha()));
            motivoTextArea.setText(demandas.getMotivo());
            observacionesTextArea.setText(demandas.getObservaciones());
        }
    }
    public boolean whatFieldIsEmpty(){
        LocalDate dateValue = date.getValue();
        if(claveConductorTextField.getText().equals("")){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor ingresa una clave de conductor.");
            return false;
        }else if(dateValue == null){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor selecciona una fecha.");
            return false;
        }else if(motivoTextArea.getText().equals("")){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor especifica el motivo de la demanda.");
            return false;
        }
        return true;
    }
    public Optional<ButtonType> showAlertConfirmation(String content){
       alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setContentText(content);
       return alert.showAndWait();
    }
    public void clearFields(){
        claveConductorTextField.clear();
        claveConductorTextField.setPromptText("Ingresa clave");
        date.setValue(null);
        motivoTextArea.clear();
        observacionesTextArea.clear();
    }
    public void registrar(){
        try {
            if(whatFieldIsEmpty()){
                if(isIdConductorCorrect()){
                    statement = conexion.getConexion().prepareStatement("insert "
                    + "into Demandas"
                    + "(IdConductor,IdDirector,Fecha,Motivo,Observaciones)"
                    + " values(?,?,?,?,?)");
                    statement.setString(1,claveConductorTextField.getText());
                    statement.setString(2,employee.getIdEmpleado());
                    statement.setDate(3, Date.valueOf(date.getValue()));
                    statement.setString(4, motivoTextArea.getText());
                    statement.setString(5, observacionesTextArea.getText());
                    if(statement.executeUpdate() != 0){
                        demandaTabla.getItems().clear();
                        fillTable("");
                    }else{
                        showAlert(Alert.AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void actualizar(){
        if(demandas != null){
                try {
                    if(whatFieldIsEmpty()){
                        if(showAlertConfirmation("¿Esta seguro que desea actualizar el registro"
                        + "\ncon numero de clave \""+demandas.getClaveDemanda()
                        +"\"?"
                        ).get()
                        == ButtonType.OK){
                            if(isIdConductorCorrect()){
                                statement = conexion.getConexion().prepareStatement("update "
                                + "Demandas set"
                                + " IdConductor=?,Fecha=?,Motivo=?,Observaciones=? "
                                + "where Id=?");
                                statement.setString(1,claveConductorTextField.getText());
                                statement.setDate(2, Date.valueOf(date.getValue().toString()));
                                statement.setString(3,motivoTextArea.getText());
                                statement.setString(4,observacionesTextArea.getText());
                                statement.setInt(5, Integer.parseInt(demandas.getClaveDemanda()));
                                if(statement.executeUpdate() != 0){
                                    showAlert(Alert.AlertType.INFORMATION,"","Registro actualizado.");
                                    demandaTabla.getItems().clear();
                                    if(!search){
                                        fillTable("");
                                    }else{
                                        fillTable(claveConductorTextField.getText());
                                    }
                                }else{
                                    showAlert(Alert.AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                                }

                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Aqui trono"+ex);
                }
        }else{
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor selecciona algun registro que desees modificar");
        }
    }
    public void eliminar(){
        if(demandas != null){
            try {
                if(showAlertConfirmation("¿Esta seguro que desea eliminar el registro"
                + "\ncon numero de clave \""+demandas.getClaveDemanda()
                +"\"?"
                ).get()
                == ButtonType.OK){
                    statement = conexion.getConexion().prepareStatement("Delete "
                    + "From Demandas where Id=?");
                    statement.setInt(1, Integer.parseInt(demandas.getClaveDemanda()));
                    if(statement.executeUpdate() != 0){
                        showAlert(Alert.AlertType.INFORMATION,"","Registro eliminado.");
                        demandaTabla.getItems().clear();
                        if(!search){
                            fillTable("");
                        }else{
                            fillTable(claveConductorTextField.getText());
                        }
                    }else{
                        showAlert(Alert.AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                    }

                }

            } catch (SQLException ex) {
                System.out.println("Aqui trono"+ex);
            }
        }else{
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor selecciona algun registro que desees modificar");
        }
    }
    public void buscar(){
        if(!search){
            search = true;
            clearFields();
            if(this.employee.getType() == 4){agregar.setDisable(true);}
            claveConductorTextField.textProperty().addListener(searchListener);
        }else{
            search = false;
            clearFields();
            if(this.employee.getType() == 4){agregar.setDisable(false);}
            demandaTabla.getItems().clear();
            fillTable("");
            claveConductorTextField.textProperty().removeListener(searchListener);
        }
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker();
        date.setValue(LocalDate.now());
    }
}
