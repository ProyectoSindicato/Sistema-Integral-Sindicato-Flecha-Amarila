package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Mantenimiento;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

public class VistaMantenimientoController implements Initializable{
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private boolean search = false;
    private Mantenimiento mantenimientos;
    
    @FXML 
    private DatePicker date;
    @FXML
    private GridPane fechaDate;
    @FXML
    private Button agregar,modificar,eliminar,buscar, btnBack;
    @FXML
    private TextField claveAutobusTextField;
    @FXML
    private TextArea motivoTextArea;
    @FXML
    private TableView <Mantenimiento> mantenimientoTabla;
    @FXML
    private TableColumn <Mantenimiento, String> claveMantenimiento;
    @FXML
    private TableColumn <Mantenimiento, String> claveAutobus;
    @FXML
    private TableColumn <Mantenimiento, String> claveConductor;
    @FXML
    private TableColumn <Mantenimiento, String> nombreConductor;
    @FXML
    private TableColumn <Mantenimiento, String> fecha;
    @FXML
    private TableColumn <Mantenimiento, String> motivo;
    @FXML
    private TableColumn <Mantenimiento, String> idEmpleado;
    @FXML
    private TableColumn <Mantenimiento, String> nombreJefe;
    @FXML
    private final ChangeListener<String> searchListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               mantenimientoTabla.getItems().clear();
               fillTable(claveAutobusTextField.getText());
            }
    };
    
    public void datePicker(){
           date = new DatePicker();
           fechaDate.add(date, 0, 0);
    }
    public void setParameters(Empleado employee,ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
        if(this.employee.getType() != 5 && this.employee.getType() != 1){
            agregar.setDisable(true);
            eliminar.setDisable(true);
            modificar.setDisable(true);
        }
    }
    public void fillTable(String idAutobus){
        try {
            if(!search){
                statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdAutobus,IdDirector,Fecha,Motivo "
                    + "From Mantenimiento");
            }else{
               statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdAutobus,IdDirector,Fecha,Motivo "
                    + "From Mantenimiento where IdAutobus=?");
               try{
               statement.setInt(1, Integer.parseInt(idAutobus));
               }catch(Exception e){}
            }
            result = statement.executeQuery();
            while(result.next()){
                mantenimientos = new Mantenimiento();
                mantenimientos.setClaveMantenimiento(result.getString(1));
                mantenimientos.setClaveAutobus(result.getString(2));
                mantenimientos.setClaveConductor(getIdConductor(result.getString(2)));
                mantenimientos.setNombreConductor(employeeName(mantenimientos.getClaveConductor()));
                System.out.println(employeeName(mantenimientos.getClaveConductor()));
                mantenimientos.setFecha(result.getDate(4).toString());
                mantenimientos.setMotivo(result.getString(5));
                mantenimientos.setIdEmpleado(result.getString(3));
                mantenimientos.setNombreJefe(employeeName(result.getString(3)));
                putData();
                mantenimientoTabla.getItems().add(mantenimientos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void putData(){
        claveMantenimiento.setCellValueFactory(new PropertyValueFactory<>("ClaveMantenimiento"));
        claveAutobus.setCellValueFactory(new PropertyValueFactory<>("claveAutobus"));
        claveConductor.setCellValueFactory(new PropertyValueFactory<>("ClaveConductor"));
        nombreConductor.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
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
    public String getIdConductor(String idAutobus){
        String driverId = "";
        LocalDate mostRecent = null;
        try {
            statement = conexion.getConexion().prepareStatement("Select  IdConductor,FechaAsignacion"
                    + " From ConductorAutobus where IdAutobus=?");
            statement.setString(1, idAutobus);
            resultEmpleado = statement.executeQuery();
            while(resultEmpleado.next()){
                System.out.println("asdawd");
                if(mostRecent != null){
                    System.out.println("jjeje");
                    if(mostRecent.isBefore(resultEmpleado.getDate(2).toLocalDate())){
                        mostRecent = resultEmpleado.getDate(2).toLocalDate();
                        driverId = resultEmpleado.getString(1);
                        System.out.println("aaaa ver"+driverId);
                    }
                }else{
                    mostRecent = resultEmpleado.getDate(2).toLocalDate();
                    driverId = resultEmpleado.getString(1);
                    System.out.println("chale "+driverId);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return driverId;
    }
    public boolean isIdAutobusCorrect(){
        try {
            statement = conexion.getConexion().prepareStatement("Select Id "
                    + "From Autobus "
                    + "where Id=?");
            try{
                statement.setString(1,claveAutobusTextField.getText());
                result = statement.executeQuery();
                if(result.next()){
                 return true;   
                }else{
                    showAlert(Alert.AlertType.ERROR,"Error","Clave de autobus incorrecta favor de verificar");
                    return false;
                }
            }catch(Exception e){
                showAlert(Alert.AlertType.ERROR,"Error","Clave de autobus incorrecta favor de verificar");
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
        mantenimientos = mantenimientoTabla.getSelectionModel().getSelectedItem();
        if(mantenimientos != null){
            claveAutobusTextField.setText(mantenimientos.getClaveAutobus());
            date.setValue(LocalDate.parse(mantenimientos.getFecha()));
            motivoTextArea.setText(mantenimientos.getMotivo());
        }
    }
    public boolean whatFieldIsEmpty(){
        LocalDate dateValue = date.getValue();
        if(claveAutobusTextField.getText().equals("")){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor ingresa la clave del autobus.");
            return false;
        }else if(dateValue == null){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor selecciona una fecha.");
            return false;
        }else if(motivoTextArea.getText().equals("")){
            showAlert(Alert.AlertType.WARNING,"¡Ups!","Por favor especifica el motivo.");
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
        claveAutobusTextField.clear();
        claveAutobusTextField.setPromptText("Ingresa clave");
        date.setValue(null);
        motivoTextArea.clear();
    }
    public void registrar(){
        try {
            if(whatFieldIsEmpty()){
                if(isIdAutobusCorrect()){
                    statement = conexion.getConexion().prepareStatement("insert "
                    + "into Mantenimiento"
                    + "(IdAutobus,IdDirector,Fecha,Motivo)"
                    + " values(?,?,?,?)");
                    statement.setString(1,claveAutobusTextField.getText());
                    statement.setString(2,employee.getIdEmpleado());
                    statement.setDate(3, Date.valueOf(date.getValue()));
                    statement.setString(4, motivoTextArea.getText());
                    if(statement.executeUpdate() != 0){
                        mantenimientoTabla.getItems().clear();
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
        if(mantenimientos != null){
                try {
                    if(whatFieldIsEmpty()){
                        if(showAlertConfirmation("¿Esta seguro que desea actualizar el registro"
                        + "\ncon numero de clave \""+mantenimientos.getClaveMantenimiento()
                        +"\"?"
                        ).get()
                        == ButtonType.OK){
                            if(isIdAutobusCorrect()){
                                statement = conexion.getConexion().prepareStatement("update "
                                + "Mantenimiento set"
                                + " IdAutobus=?,Fecha=?,Motivo=?"
                                + "where Id=?");
                                statement.setString(1,claveAutobusTextField.getText());
                                statement.setDate(2, Date.valueOf(date.getValue().toString()));
                                statement.setString(3,motivoTextArea.getText());
                                statement.setInt(4, Integer.parseInt(mantenimientos.getClaveMantenimiento()));
                                if(statement.executeUpdate() != 0){
                                    showAlert(Alert.AlertType.INFORMATION,"","Registro actualizado.");
                                    mantenimientoTabla.getItems().clear();
                                    if(!search){
                                        fillTable("");
                                    }else{
                                       fillTable(claveAutobusTextField.getText());
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
        if(mantenimientos != null){
            try {
                if(showAlertConfirmation("¿Esta seguro que desea eliminar el registro"
                + "\ncon numero de clave \""+mantenimientos.getClaveMantenimiento()
                +"\"?"
                ).get()
                == ButtonType.OK){
                    statement = conexion.getConexion().prepareStatement("Delete "
                    + "From Mantenimiento where Id=?");
                    statement.setInt(1, Integer.parseInt(mantenimientos.getClaveMantenimiento()));
                    if(statement.executeUpdate() != 0){
                        showAlert(Alert.AlertType.INFORMATION,"","Registro eliminado.");
                        mantenimientoTabla.getItems().clear();
                        if(!search){
                            fillTable("");
                        }else{
                           fillTable(claveAutobusTextField.getText());
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
            if(this.employee.getType() == 5 || this.employee.getType()==1){agregar.setDisable(true);}
            claveAutobusTextField.textProperty().addListener(searchListener);
        }else{
            search = false;
            clearFields();
            if(this.employee.getType() == 5 || this.employee.getType()==1){agregar.setDisable(false);}
            mantenimientoTabla.getItems().clear();
            fillTable("");
            claveAutobusTextField.textProperty().removeListener(searchListener);
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
    
    @FXML
    public void backDesk(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/Vista/FXMLDocument.fxml"));
                        loader.load();
                        FXMLDocumentController document = loader.getController();
                        document.setParameters(employee,conexion);
                        Parent p = loader.getRoot();
                        Scene scene = new Scene(p);
                        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                        s.setScene(scene);
                        s.setMaximized(true);
                        s.setResizable(true);
                        s.show();
    }
    
    
}
