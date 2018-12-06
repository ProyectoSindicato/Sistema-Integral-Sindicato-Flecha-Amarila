/*VistaAccidentesController */

package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Accidentes;
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
public class VistaAccidentesController implements Initializable {

    @FXML
    private TextField txtAccident, txtUser, txtDriver, txtPlace;
    @FXML
    private TextArea txtReason, txtDetails, txtObs;
    @FXML
    private Button btnBack, btnAdd, btnModify, btnErase, btnSearch;
    @FXML
    private TableView<Accidentes> tableAccidents;
    @FXML
    private TableColumn<Accidentes, String> colDriver;
    @FXML
    private TableColumn<Accidentes, String> colDate;
    @FXML
    private TableColumn<Accidentes, String> colPlace;
    @FXML
    private TableColumn<Accidentes, String> colDetails;
    @FXML
    private TableColumn<Accidentes, String> colObs;
    @FXML
    private TableColumn<Accidentes, String> colReason;
    @FXML
    private TableColumn<Accidentes, Integer> colID;
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
        if(this.employee.getType() != 4 && this.employee.getType() != 1){
         btnAdd.setDisable(true);
         btnModify.setDisable(true);
         btnErase.setDisable(true);
    }
        txtUser.setText(employee.getIdEmpleado());
    }


    /* Inicialize some constructors.*/
    public VistaAccidentesController() {
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
        String place = txtPlace.getText();
        LocalDate value = date.getValue();
        String reason = txtReason.getText();
        String details = txtDetails.getText();
        String obs = txtObs.getText();

        if ((!(idEmployee.equals(""))) && (!(idDriver.equals(""))) && (!(place.equals("")))
                && (value != null) && (!(reason.equals(" ")))) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar un accidente?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (isIdConductorCorrect()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        Accidentes bean = new Accidentes();
                        bean.setIdEmpleado(idEmployee);
                        bean.setIdConductor(idDriver);
                        bean.setLugar(place);
                        bean.setDetalles(details);
                        bean.setMotivo(reason);
                        bean.setObservaciones(obs);
                        bean.setFecha(String.valueOf(value));

                        if (bean.insertAccident(idEmployee)) {
                            addRefresh();
                            showAlert(AlertType.INFORMATION, "Information Message", " El accidente se ha ingresado correctamente.");
                        } else {
                            showAlert(AlertType.ERROR, "Error Message", " Error al añadir el accidente.");
                        }
                    } catch (Exception ex) {
                        showAlert(AlertType.ERROR, "Error Message", " Error al añadir el accidente.");
                    }
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
        }
    } // add

    @FXML
    public void modifyAccident(ActionEvent e) {
        Accidentes modify = tableAccidents.getSelectionModel().getSelectedItem();
        String id = txtAccident.getText();
        String idEmployee = txtUser.getText();
        String idDriver = txtDriver.getText();
        String place = txtPlace.getText();
        LocalDate value = date.getValue();
        String reason = txtReason.getText();
        String details = txtDetails.getText();
        String obs = txtObs.getText();

        if (modify != null) {
            if ((!(idEmployee.equals(" "))) && (value != null) && (!(id.equals(" "))) && (!(idDriver.equals("")))
                    && (!(place.equals(""))) && (!(reason.equals("")))) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea modificar un accidente?");
                Optional<ButtonType> result = confirm.showAndWait();

                if (isIdConductorCorrect()) {
                    if (result.get() == ButtonType.OK) {
                        try {
                            Accidentes bean = new Accidentes();

                            bean.setId(Integer.parseInt(id));
                            bean.setIdConductor(idDriver);
                            bean.setIdEmpleado(idEmployee);
                            bean.setLugar(place);
                            bean.setDetalles(details);
                            bean.setObservaciones(obs);
                            bean.setFecha(String.valueOf(value));
                            bean.setMotivo(reason);

                            if (bean.modifyAccident(idEmployee)) {
                                addRefresh();
                                showAlert(AlertType.INFORMATION, "Information Message.", "El accidente se ha añadido correctamente.");
                            } else {
                                showAlert(AlertType.ERROR, "Error Message.", "Error al modificar el accidente.");
                            }
                        } catch (Exception ex) {
                            showAlert(AlertType.ERROR, "Error Message.", "Error al modificar el accidente.");
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
        Accidentes delete = tableAccidents.getSelectionModel().getSelectedItem();

        if (delete != null) {
            if ((!(txtAccident.getText().equals("")))) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea eliminar un accidente?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.get() == ButtonType.OK) {
                    int idAccident = Integer.parseInt(txtAccident.getText());
                    Accidentes bean = new Accidentes();
                    bean.setId(idAccident);
                    if (bean.deleteAccident(idAccident)) {
                        deleteRefresh();
                        showAlert(AlertType.INFORMATION, "Information Message", "Se ha eliminado el accidente correctamente.");
                    } else {
                        showAlert(AlertType.ERROR, "Error Message", "Error al eliminar un accidente.");
                    }
                }
            } else {
                showAlert(AlertType.WARNING, "Warning Message", "Favor de seleccionar un accidente.");
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", "Favor de seleccionar un accidente. Checar datos.");
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
        String sql = "SELECT Id, IdConductor, IdEmpleado, Fecha, Lugar, Motivo, Detalles, Observaciones FROM Accidentes";
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
                String dt = String.valueOf(rs.getDate(4));
                String place = String.valueOf(row[4]);
                String reason = String.valueOf(row[5]);
                String details = String.valueOf(row[6]);
                String obs = String.valueOf(row[7]);
                Accidentes a = new Accidentes(Integer.parseInt(id), idDriver, dt, place, reason, details, obs);
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
                String sql = "SELECT Id, IdConductor, IdEmpleado, Fecha, Lugar, Motivo, Detalles, Observaciones FROM Accidentes";
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
                    String dt = String.valueOf(rs.getDate(4));
                    String place = String.valueOf(row[4]);
                    String reason = String.valueOf(row[5]);
                    String details = String.valueOf(row[6]);
                    String obs = String.valueOf(row[7]);
                    Accidentes a = new Accidentes(Integer.parseInt(id), idDriver, dt, place, reason, details, obs);
                    putDataColumns();
                    tableAccidents.getItems().add(a);
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
        colPlace.setCellValueFactory(new PropertyValueFactory<>("Lugar"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("Detalles"));
        colObs.setCellValueFactory(new PropertyValueFactory<>("Observaciones"));
    }
    
    public void showRowsInFields(){
        Accidentes show = tableAccidents.getSelectionModel().getSelectedItem();
        if(show == null){
            clearFields();
        }else{
           
           int idAccident = show.getId();
            String idDriver = show.getIdConductor();
            String dateShow = show.getFecha();
            String place = show.getLugar();
            String reason = show.getMotivo();
            String details = show.getDetalles();
            String obs = show.getObservaciones();
            
            txtAccident.setText(String.valueOf(idAccident));
            txtDriver.setText(idDriver);
            date.setValue(LocalDate.parse(dateShow));
            txtPlace.setText(place);
            txtReason.setText(reason);
            txtDetails.setText(details);
            txtObs.setText(obs);  
           
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
        txtDetails.clear();
        txtDriver.clear();
        txtObs.clear();
        txtReason.clear();
        txtPlace.clear();
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
        Accidentes filter = new Accidentes();
        clearTable();
        updateFillTable(filter.filterAccident(id));
    }
    
    @FXML
    public void handlerFilter(){
        filterActive =! filterActive;
        
        if(filterActive){
            btnAdd.setDisable(true);
            btnErase.setDisable(true);
            btnModify.setDisable(true);
            
            txtDriver.textProperty().addListener(event);
            txtDetails.setDisable(true);
            txtObs.setDisable(true);
            txtReason.setDisable(true);
            txtPlace.setDisable(true);
            date.setDisable(true);
        }else{
            btnAdd.setDisable(false);
            btnErase.setDisable(false);
            btnModify.setDisable(false);
            
            txtDriver.textProperty().removeListener(event);
            txtDetails.setDisable(false);
            txtObs.setDisable(false);
            txtReason.setDisable(false);
            txtPlace.setDisable(false);
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
