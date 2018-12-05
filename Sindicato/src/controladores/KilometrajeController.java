/*VistaKilometrajeController */

package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Kilometraje;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aideé Alvarez
 */
public class KilometrajeController implements Initializable {

    @FXML
    private TextField txtAccident, txtUser, txtDriver;
    @FXML
    private TextArea txtReason;
    @FXML
    private Button btnBack, btnAdd, btnModify, btnErase, btnSearch;
    @FXML
    private TableView<Kilometraje> tableAccidents;
    @FXML
    private TableColumn<Kilometraje, String> colDriver;
    @FXML
    private TableColumn<Kilometraje, String> colDate;
    @FXML
    private TableColumn<Kilometraje, String> colReason;
    @FXML
    private TableColumn<Kilometraje, Integer> colID;
    @FXML
    private GridPane datePickerPanel;

    /* Extra section*/
    private Alert alert;
    @FXML
    private final DatePicker date;
    boolean filterActive;
    EventManager event;
    /* Database section and connection with Employee model.*/
    ResultSetMetaData metadata = null;
    ConexionAccess conexionBD;

    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /* Dir. Seguridad y Prevención social - 4.*/
        if(this.employee.getType() != 4){
         btnAdd.setDisable(true);
         btnModify.setDisable(true);
         btnErase.setDisable(true);
    }
        txtUser.setText(employee.getIdEmpleado());
    }


    /* Inicialize some constructors.*/
    public KilometrajeController() {
        date = new DatePicker();
        conexionBD = new ConexionAccess();
    }

    public void loadElements() {
        datePickerPanel.add(date, 0, 0);
        date.setValue(LocalDate.now());
        txtUser.setDisable(true);
        txtAccident.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadElements();
        fillTableBD();
        filterActive = false;
        event = new EventManager();
    }

    /* SECTION BUTTONS */
    public void addAccident(ActionEvent e) {
        String idEmployee = txtUser.getText();
        String idDriver = txtDriver.getText();
        LocalDate value = date.getValue();
        String reason = txtReason.getText();

        if ((!(idEmployee.equals(""))) && (!(idDriver.equals("")))
                && (value != null) && (!(reason.equals(" ")))) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar una autorización de kilometraje?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (isIdConductorCorrect()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        Kilometraje bean = new Kilometraje();
                        bean.setIdDirector(idEmployee);
                        bean.setIdConductor(idDriver);
                        bean.setMotivo(reason);
                        bean.setFecha(String.valueOf(value));

                        if (bean.insertarKilometraje(idEmployee)) {
                            addRefresh();
                            showAlert(AlertType.INFORMATION, "Information Message", " La autorización de kilometraje se ha ingresado correctamente.");
                        } else {
                            showAlert(AlertType.ERROR, "Error Message", " Error al añadir la autorización de kilometraje.");
                        }
                    } catch (Exception ex) {
                        showAlert(AlertType.ERROR, "Error Message", " Error al añadir la autorización del kilometraje.");
                    }
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
        }
    } // add

    @FXML
    public void modifyAccident(ActionEvent e) {
        Kilometraje modify = tableAccidents.getSelectionModel().getSelectedItem();
        String id = txtAccident.getText();
        String idEmployee = txtUser.getText();
        String idDriver = txtDriver.getText();
        LocalDate value = date.getValue();
        String reason = txtReason.getText();


        if (modify != null) {
            if ((!(idEmployee.equals(" "))) && (value != null) && (!(id.equals(" "))) && (!(idDriver.equals("")))
                     && (!(reason.equals("")))) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea modificar un accidente?");
                Optional<ButtonType> result = confirm.showAndWait();

                if (isIdConductorCorrect()) {
                    if (result.get() == ButtonType.OK) {
                        try {
                            Kilometraje bean = new Kilometraje();

                            bean.setId(Integer.parseInt(id));
                            bean.setIdConductor(idDriver);
                            bean.setIdDirector(idEmployee);
                            bean.setFecha(String.valueOf(value));
                            bean.setMotivo(reason);

                            if (bean.modificarKilometraje(idEmployee)) {
                                addRefresh();
                                showAlert(AlertType.INFORMATION, "Information Message.", "La autorización de kilomtraje se ha añadido correctamente.");
                            } else {
                                showAlert(AlertType.ERROR, "Error Message.", "Error al modificar la autorización de kilometraje.");
                            }
                        } catch (Exception ex) {
                            showAlert(AlertType.ERROR, "Error Message.", "Error al modificar la autorización de kilometraje.");
                        }
                    }
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", "El accidente no se ha seleccionado. Favor de checar datos.");
        }
    }

    @FXML
    public void deleteAccident(ActionEvent e) {
        Kilometraje delete = tableAccidents.getSelectionModel().getSelectedItem();

        if (delete != null) {
            if ((!(txtAccident.getText().equals("")))) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea eliminar una autorización de kilometraje ?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.get() == ButtonType.OK) {
                    int idAccident = Integer.parseInt(txtAccident.getText());
                    Kilometraje bean = new Kilometraje();
                    bean.setId(idAccident);
                    if (bean.eliminarKilometraje(idAccident)) {
                        deleteRefresh();
                        showAlert(AlertType.INFORMATION, "Information Message", "Se ha eliminado la autorización de kilometraje correctamente.");
                    } else {
                        showAlert(AlertType.ERROR, "Error Message", "Error al eliminar una autorización .");
                    }
                }
            } else {
                showAlert(AlertType.WARNING, "Warning Message", "Favor de seleccionar una autorización de kilometraje de la tabla.");
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", "Favor de seleccionar una autorización de kilometraje de la tabla. Checar datos.");
        }

    }

    @FXML
    public void backDesk(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/Vista/FXMLDocument.fxml"));
                        loader.load();
                        FXMLDocumentController document = loader.getController();
                        document.setParameters(employee,conexionBD);
                        Parent p = loader.getRoot();
                        Scene scene = new Scene(p);
                        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                        s.setScene(scene);
                        s.setMaximized(true);
                        s.setResizable(true);
                        s.show();
    }
    /* TABLE SECTION. */
    @FXML
    private void tableFillAction(MouseEvent e) {
        showRowsInFields();
    }

    public void fillTableBD() {
        ResultSet rs;
        String sql = "SELECT Id, IdConductor, IdDirector, Motivo, Fecha FROM AutorizacionKilometraje";
        try {
            conexionBD.conectar();
            rs = conexionBD.ejecutarSQLSelect(sql);
            metadata = rs.getMetaData();
            int cols = metadata.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    if (rs.getObject(i + 1) == null) {
                        row[i] = "";

                    } else {
                        row[i] = rs.getObject(i + 1);
                    }
                }
                String id = String.valueOf(row[0]);
                String idDriver = String.valueOf(row[1]);
                String dt = String.valueOf(rs.getDate(2));                
                String reason = String.valueOf(row[3]);
                Kilometraje a = new Kilometraje(Integer.parseInt(id), idDriver, dt, reason);
                putDataColumns();
                tableAccidents.getItems().add(a);
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
            ex.printStackTrace();
        }
    }

    public void updateFillTable(ResultSet result) {
        ResultSet rs;
        try {
            if (filterActive) {
                rs = result;
            } else {
                String sql = "SELECT Id, IdConductor, IdDirector, Motivo, Fecha FROM AutorizacionKilometraje";
                rs = conexionBD.ejecutarSQLSelect(sql);
            }
            try {
                conexionBD.conectar();
                metadata = rs.getMetaData();
                int cols = metadata.getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        if (rs.getObject(i + 1) == null) {
                            row[i] = "";

                        } else {
                            row[i] = rs.getObject(i + 1);
                        }
                    }
                    String id = String.valueOf(row[0]);
                    String idDriver = String.valueOf(row[1]);
                    String dt = String.valueOf(rs.getDate(2));                
                    String reason = String.valueOf(row[3]);
                    Kilometraje k = new Kilometraje(Integer.parseInt(id), idDriver, dt, reason);
                    putDataColumns();
                    tableAccidents.getItems().add(k);
                }
            } catch (SQLException e) {
                System.out.println("Database error. Cannot fill the table." + e.getMessage());
            }

        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
        }

    }

    void putDataColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colDriver.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
    }
    
    public void showRowsInFields(){
        Kilometraje show = tableAccidents.getSelectionModel().getSelectedItem();
        if(show == null){
            clearFields();
        }else{
           
           int idKilometraje = show.getId();
            String idDriver = show.getIdConductor();
            String dateShow = show.getFecha();
            String reason = show.getMotivo();

            txtAccident.setText(String.valueOf(idKilometraje));
            txtDriver.setText(idDriver);
            date.setValue(LocalDate.parse(dateShow));
            txtReason.setText(reason);
        }
    }

    public void addRefresh() {
        ResultSet r = null;
        clearTable();
        updateFillTable(r);
        clearFields();
    }

    public void deleteRefresh() {
        try {
            clearFields();
            clearTable();
            ResultSet r = null;
            updateFillTable(r);
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error Message", "No se ha seleccionado nada.");
        }
    }
   
    void clearFields() {
        txtAccident.clear();
        txtDriver.clear();
        txtReason.clear();
        date.setValue(LocalDate.now());
    }

    void clearTable() {
        for (int i = 0; i < tableAccidents.getItems().size(); i++) {
            tableAccidents.getItems().clear();
        }
    }

    /* This method is used to find the conductor's id. If fails, then you can not modify or add an accident. */
    public boolean isIdConductorCorrect() {
        try {
            PreparedStatement statement = conexionBD.getConexion().prepareStatement("Select Id "
                    + "From Conductor "
                    + "where Id=?");
            statement.setString(1, txtDriver.getText());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Clave de conductor incorrecta favor de verificar");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    /* SEARCH SECTION*/
    void filterHandler(){
        if(filterActive){
            String idDriver;
            idDriver = txtDriver.getText();
            readFilter(idDriver);
        }
    }
    
    void readFilter(String id){
        Kilometraje filter = new Kilometraje();
        clearTable();
        updateFillTable(filter.filtrarKilometraje(id));
    }
    
    @FXML
    public void handlerFilter(){
        filterActive =! filterActive;
        
        if(filterActive){
            btnAdd.setDisable(true);
            btnErase.setDisable(true);
            btnModify.setDisable(true);
            
            txtDriver.textProperty().addListener(event);            
            txtReason.setDisable(true);
            date.setDisable(true);
        }else{
            btnAdd.setDisable(false);
            btnErase.setDisable(false);
            btnModify.setDisable(false);
            
            txtDriver.textProperty().removeListener(event);
            txtReason.setDisable(false);
            date.setDisable(false);
            ResultSet r = null;
            clearTable();
            
            updateFillTable(r);
        }
    }
    
    class EventManager implements ChangeListener{

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue){
            filterHandler();
        }
        
    }

    /* ALERT SECTION */
    public void showAlert(Alert.AlertType error, String header, String body) {
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }

}
