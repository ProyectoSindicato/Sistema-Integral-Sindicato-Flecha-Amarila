package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Permisos;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class VistaPermisosController implements Initializable {
    private PreparedStatement statement,statementEmpleado;
    private ResultSet result,resultEmpleado;
    private Empleado employee;
    private ConexionAccess conexion;
    private Alert alert;
    private Permisos permisos;
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
    private ComboBox <String> tipoComboBox;
    @FXML
    private TableView<Permisos> incapacidadesTabla;
    @FXML
    private TableColumn <Permisos, String> claveIncapacidad;
    @FXML
    private TableColumn <Permisos, String> claveConductor;
    @FXML
    private TableColumn <Permisos, String> nombreConductor;
    @FXML
    private TableColumn <Permisos, String> fechaInicio;
    @FXML
    private TableColumn <Permisos, String> fechaFin;
    @FXML
    private TableColumn <Permisos, String> tipo;
    @FXML
    private TableColumn <Permisos, String> motivo;
    @FXML
    private TableColumn <Permisos, String> nombreJefe;
    @FXML
    private TableColumn <Permisos, String> idEmpleado;;
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
        if(this.employee.getType() != 0 && this.employee.getType() != 5){
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
                    && dateValue !=null
                    && dateValue2 != null
                    && tipoComboBox.getValue()!= null
                    && !motivoTextArea.getText().equals("")){
                if(isIdConductorCorrect()){
                    if(isConductorAlready()){
                        if(date.getValue().isBefore(date2.getValue())){
                            statement = conexion.getConexion().prepareStatement("insert "
                            + "into Permisos"
                            + "(IdEmpleado,IdConductor,Tipo,FechaInicio,FechaFin,Motivo)"
                            + " values(?,?,?,?,?,?)");
                            statement.setString(1,employee.getIdEmpleado());
                            statement.setString(2,claveConductorTextField.getText());
                            statement.setString(3,tipoComboBox.getSelectionModel().getSelectedItem());
                            statement.setDate(4, Date.valueOf(date.getValue()));
                            statement.setDate(5,Date.valueOf(date2.getValue()));
                            statement.setString(6, motivoTextArea.getText());
                            if(statement.executeUpdate() != 0){
                                incapacidadesTabla.getItems().clear();
                                fillTable("");
                            }else{
                                showAlert(AlertType.WARNING,"¡Ups!","Parece que algo salio mal, vuelve a intentarlo.");
                            }
                        }else{
                            showAlert(AlertType.WARNING,"¡Ups!","La fecha de inicio "
                                    + "debe ser menor que la fecha en la que"
                                    + "termina el permiso");
                        }
                    }else{
                        showAlert(AlertType.WARNING,"¡Usuario ya cuenta con un permiso!"
                                ,"Actualmente el conductor \""+employeeName(claveConductorTextField.getText())
                        + "\" cuenta con un permiso \nvigente, si deseas extender o reducir su periodo"+
                                "\npor favor actualiza su la información.");
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
        if(permisos != null){
                LocalDate dateValue = date.getValue();
                LocalDate dateValue2 = date2.getValue();
                try {
                    if(!claveConductorTextField.getText().equals("")
                            && dateValue !=null
                            && dateValue2 != null
                            && !tipoComboBox.getSelectionModel().getSelectedItem().equals("")
                            && !motivoTextArea.getText().equals("")){
                        if(showAlertConfirmation("¿Esta seguro que desea actualizar el registro"
                        + "\ncon numero de clave \""+permisos.getClavePermiso()
                        +"\"?"
                        ).get()
                        == ButtonType.OK){
                            if(isIdConductorCorrect()){
                                if(date.getValue().isBefore(date2.getValue())){
                                    statement = conexion.getConexion().prepareStatement("update "
                                    + "Permisos set"
                                    + " IdConductor=?,Tipo=?,FechaInicio=?,FechaFin=?,Motivo=? "
                                    + "where Id=?");
                                    statement.setString(1,claveConductorTextField.getText());
                                    statement.setString(2,tipoComboBox.getSelectionModel().getSelectedItem());
                                    statement.setDate(3, Date.valueOf(date.getValue().toString()));
                                    statement.setDate(4, Date.valueOf(date2.getValue().toString()));
                                    statement.setString(5,motivoTextArea.getText());
                                    statement.setInt(6, Integer.parseInt(permisos.getClavePermiso()));
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
                                            + "termina el permiso");
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
        if(permisos != null){
            try {
                if(showAlertConfirmation("¿Esta seguro que desea eliminar el registro"
                + "\ncon numero de clave \""+permisos.getClavePermiso()
                +"\"?"
                ).get()
                == ButtonType.OK){
                    statement = conexion.getConexion().prepareStatement("Delete "
                    + "From Permisos where Id=?");
                    statement.setInt(1, Integer.parseInt(permisos.getClavePermiso()));
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
            if(this.employee.getType() == 0 || this.employee.getType() == 5){
                agregar.setDisable(true);
            }
            claveConductorTextField.textProperty().addListener(searchListener);
        }else{
            search = false;
            clearFields();
            if(this.employee.getType() == 0 || this.employee.getType() == 5){
                agregar.setDisable(false);
            }
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
        boolean maxDate = true;
        try {
            statement = conexion.getConexion().prepareStatement("Select FechaFin "
                    + "From Permisos "
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
                    + "Id,IdEmpleado,IdConductor,Tipo,FechaInicio,FechaFin,Motivo "
                    + "From Permisos");
            }else{
               statement = conexion.getConexion().prepareStatement("Select "
                    + "Id,IdEmpleado,IdConductor,Tipo,FechaInicio,FechaFin,Motivo "
                    + "From Permisos where IdConductor=?");
               statement.setString(1, idConductor);
            }
            result = statement.executeQuery();
            while(result.next()){
                permisos= new Permisos();
                permisos.setClavePermiso(result.getString(1));
                permisos.setClaveConductor(result.getString(3));
                permisos.setNombreConductor(employeeName(result.getString(3)));
                permisos.setTipo(result.getString(4));
                permisos.setFechaInicio(result.getDate(5).toString());
                permisos.setFechaFin(result.getDate(6).toString());
                permisos.setMotivo(result.getString(7));
                permisos.setIdEmpleado(result.getString(2));
                permisos.setNombreJefe(employeeName(result.getString(2)));
                putData();
                incapacidadesTabla.getItems().add(permisos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(VistaIncapacidadesController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    public void putData(){
        claveIncapacidad.setCellValueFactory(new PropertyValueFactory<>("ClavePermiso"));
        claveConductor.setCellValueFactory(new PropertyValueFactory<>("ClaveConductor"));
        nombreConductor.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
        fechaInicio.setCellValueFactory(new PropertyValueFactory<>("FechaInicio"));
        fechaFin.setCellValueFactory(new PropertyValueFactory<>("FechaFin"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
        nombreJefe.setCellValueFactory(new PropertyValueFactory<>("NombreJefe"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
    }
    public void clearFields(){
        claveConductorTextField.clear();
        claveConductorTextField.setPromptText("Ingresa clave");
        tipoComboBox.setValue(null);
        date.setValue(null);
        date2.setValue(null);
        motivoTextArea.clear();
    }
    public void setDataOnFields(){
        permisos = incapacidadesTabla.getSelectionModel().getSelectedItem();
        if(permisos != null){
            claveConductorTextField.setText(permisos.getClaveConductor());
            date.setValue(LocalDate.parse(permisos.getFechaInicio()));
            date2.setValue(LocalDate.parse(permisos.getFechaFin()));
            for (int i = 0; i < tipoComboBox.getItems().size(); i++) {
                if(tipoComboBox.getItems().get(i).equals(permisos.getTipo())){
                    tipoComboBox.getSelectionModel().select(i);
                }
            }
            motivoTextArea.setText(permisos.getMotivo());
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
        ObservableList<String> boxOpciones
            = FXCollections.observableArrayList("Por horas", "Especial");
        tipoComboBox.setItems(boxOpciones);
    }
}
