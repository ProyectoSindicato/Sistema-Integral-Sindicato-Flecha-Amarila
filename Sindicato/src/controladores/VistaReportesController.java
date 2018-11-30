package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Reportes;
import java.awt.HeadlessException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author Aideé Alvarez.
 */
public class VistaReportesController implements Initializable {

    ResultSetMetaData metadata = null;
    private ConexionAccess conexionBD;
    @FXML
    private TextField txtReporte;
    @FXML
    private TextField txtUsuario, txtConductor;
    @FXML
    private TextField txtLugar;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TableView<Reportes> tablaReporte;
    @FXML
    private TableColumn<Reportes, String> colTipo;
    @FXML
    private TableColumn<Reportes, String> colDescripcion;
    @FXML
    private TableColumn<Reportes, String> colDate;
    @FXML
    private TableColumn<Reportes, String> colLugar;
    @FXML
    private TableColumn<Reportes, Integer> colID;
    @FXML
    private TableColumn<Reportes, String> colConductor;
    @FXML
    ComboBox<String> comboBox = new ComboBox<String>();
    @FXML
    private Button btnAgregar, btnModificar, btnEliminar;

    @FXML
    private GridPane DatePickerPanel;
    private final DatePicker date;
    ManejadorEventos eventoFiltro;
    private boolean filtroActivo;
    private Alert alert;
    ObservableList<String> boxOpciones
            = FXCollections.observableArrayList("General", "Inspección", "Tacografía",
                    "Mantenimiento", "Call Center", "Divisiones", "Boletos",
                    "Servicio", "Otro"
            );

    /* Sección Empleado */
    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /*Actas y acuerdos 6, Trabajo 5 */
        if(this.employee.getType() != 6 && this.employee.getType() != 5){
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
        }
    }

    void asignarID() {
        txtUsuario.setText(employee.getIdEmpleado());
    }

    public VistaReportesController() {
        conexionBD = new ConexionAccess();
        date = new DatePicker();
    }

    void cargarElementos() {
        comboBox.getItems().addAll(boxOpciones);
        DatePickerPanel.add(date, 0, 0);
        //  txtUsuario.setDisable(true);
    }

    /* SECCION FXMLLoader */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroActivo = false; //Nuestro filtro comienza en false.
        cargarElementos();
        llenarTablaBD();
        eventoFiltro = new ManejadorEventos(); //Activamos el evento.           
    }

    /* 
            Sección 1. Funcionamiento de botones.
     */
    // Función de agregar.
    @FXML
    public void agregarReporte(ActionEvent e) {

        String idUsr = txtUsuario.getText();
        String idCond = txtConductor.getText();
        String lugar = txtLugar.getText();
        LocalDate value = date.getValue();
        String combo = comboBox.getSelectionModel().getSelectedItem();
        String desc = txtDescripcion.getText();

        if ((!(desc.equals(""))) && (combo != null)
                && (!(idCond.equals(""))) && (!(lugar.equals(""))) && (value != null)) {

            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar un reporte?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
               if(isIdConductorCorrect()){
                     try {
                    Reportes bean = new Reportes();
                    bean.setIdConductor(idCond);
                    bean.setIdEmpleado(idUsr);
                    bean.setTipo(combo);
                    bean.setLugar(lugar);
                    bean.setFecha(String.valueOf(value));
                    bean.setDescripcion(desc);
                    if (bean.insertarReporte(idUsr) == true) {
                        insertarYRefrescar(); //Aquí se cambia por el insertar(que contiene el rs).
                        showAlert(AlertType.INFORMATION, "Information Message", " El reporte se ha ingresado correctamente.");
                    } else {
                        showAlert(AlertType.ERROR, "Error Message", " Error al añadir el reporte.");
                    }

                } catch (HeadlessException | NumberFormatException ex) {
                    showAlert(AlertType.ERROR, "Error Message", " Error al añadir reporte.");
                }
               }
            }
        } else { //Else de validación de campos vacíos.
            showAlert(AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
        }
    } //botón.

    @FXML
    public void modificarReporte(ActionEvent e) {
        Reportes modificar = tablaReporte.getSelectionModel().getSelectedItem();
        String id = txtReporte.getText();
        String idUsr = txtUsuario.getText();
        String idConductor = txtConductor.getText();
        String tipo = comboBox.getValue();
        String lugar = txtLugar.getText();
        LocalDate value = date.getValue();
        String desc = txtDescripcion.getText();
        String combo = comboBox.getSelectionModel().getSelectedItem();

        if (modificar != null) {
            if ((!(idConductor.equals(""))) && (!(tipo.equals(""))) && (!(lugar.equals("")))
                    && (value != null) && (!(desc.equals(""))) && (combo != null) && (!(id.equals(""))) && (!(idUsr.equals("")))) {

                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea modificar un reporte?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.get() == ButtonType.OK) {
                    if(isIdConductorCorrect()){
                        try {
                        Reportes m = new Reportes();
                        m.setId(Integer.parseInt(id));
                        m.setIdConductor(idConductor);
                        m.setTipo(tipo);
                        m.setLugar(lugar);
                        m.setFecha(String.valueOf(value));
                        m.setDescripcion(desc);
                        if (m.modificarReporte(idUsr) == true) {
                            insertarYRefrescar();
                            showAlert(AlertType.INFORMATION, "Information Message", " El reporte se ha modificado correctamente.");

                        } else {
                            showAlert(AlertType.ERROR, "Error Message", " Error al modificar reporte.");
                        }
                    } catch (Exception ex) {
                        showAlert(AlertType.ERROR, "Error Message", " Error al modificar reporte.");
                    }
                    }
                }
            }
        } else {
            showAlert(AlertType.WARNING, "Warning Message", " El reporte se ha ingresado correctamente.");
        }
    }

    @FXML
    public void eliminarReporte(ActionEvent e) {
        Reportes borrar = tablaReporte.getSelectionModel().getSelectedItem();

        if (borrar != null) {
            if (!(txtReporte.getText().equals(""))) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea eliminar un reporte?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.get() == ButtonType.OK) {
                    int idReporte = Integer.parseInt(txtReporte.getText());

                    Reportes eliminar = new Reportes();
                    eliminar.setId(idReporte);
                    if (eliminar.eliminarReporte(idReporte)) {
                        eliminarYActualizar();
                        showAlert(AlertType.INFORMATION, "Information Message", " El reporte ha sido eliminado correctamente.");
                    } else {
                        showAlert(AlertType.ERROR, "Error Message", " Error al modificar reporte.");
                    }
                }
            } else if (txtReporte.getText().equals("")) {
                showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar un reporte.");
            }
        } else if (borrar == null) {
            showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar un reporte.");
        }
    }

    /* 
        Sección 2. Manipulación de tableview y tablecolumn.
     */
    @FXML
    private void tablaReportesAction(MouseEvent e) {
        mostrarFilaEnCampos();
    }

    public void llenarTablaBD() {
        ResultSet rs;
        String sql = "SELECT Id, IdConductor, Tipo, Fecha, Descripcion, Lugar FROM Reportes";

        try {
            conexionBD.conectar();
            rs = conexionBD.ejecutarSQLSelect(sql);
            metadata = rs.getMetaData();
            int cols = metadata.getColumnCount();
            while (rs.next()) {
                Object[] fila = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    if (rs.getObject(i + 1) == null) {
                        fila[i] = "";

                    } else {
                        fila[i] = rs.getObject(i + 1);
                    }
                }
                String id = String.valueOf(fila[0]);
                String idCond = String.valueOf(fila[1]);
                String tipo = (String) fila[2];
                String fecha = String.valueOf(rs.getDate(4));
                String desc = String.valueOf(fila[4]);
                String lugar = String.valueOf(fila[5]);
                Reportes rep = new Reportes(Integer.parseInt(id), idCond, tipo, lugar, fecha, desc);
                colocarDatosColumna();

                tablaReporte.getItems().add(rep);
            }
        } catch (SQLException e) {
            System.out.println("Error al rellenar la tabla." + e.getMessage());
        }
    }

    public void actualizarTablaBD(ResultSet result) //Llenar la tabla con un result set.
    {
        ResultSet rs;
        try {
            if (filtroActivo) { //Si el filtro está activado, quiere decir que el txtField está en escucha y se sustituye el result set por ese.
                rs = result;
            } else { //En caso contrario, solamente volvemos a ejecutar la sentencia normal de traer todo a la tabla.
                String sql = "SELECT Id, IdConductor, Tipo, Fecha, Descripcion, Lugar FROM Reportes";
                rs = conexionBD.ejecutarSQLSelect(sql);
            }
            try {
                conexionBD.conectar();
                metadata = rs.getMetaData();
                int cols = metadata.getColumnCount();
                while (rs.next()) {
                    Object[] fila = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        if (rs.getObject(i + 1) == null) {
                            fila[i] = "";

                        } else {
                            fila[i] = rs.getObject(i + 1);
                        }
                    }
                    String id = String.valueOf(fila[0]);
                    String idCond = String.valueOf(fila[1]);
                    String tipo = (String) fila[2];
                    String fecha = String.valueOf(rs.getDate(4));
                    String desc = String.valueOf(fila[4]);
                    String lugar = String.valueOf(fila[5]);
                    Reportes rep = new Reportes(Integer.parseInt(id), idCond, tipo, lugar, fecha, desc);
                    colocarDatosColumna();
                    tablaReporte.getItems().add(rep);
                }
            } catch (SQLException e) {
                System.out.println("Error al filtrar la tabla." + e.getMessage());
            }

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
        }
    }

    void colocarDatosColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        colLugar.setCellValueFactory(new PropertyValueFactory<>("Lugar"));
    }

    void ActualizaRefresca() {
        ResultSet r = null;
        LimpiarTabla();
        actualizarTablaBD(r);
        LimpiarCampos();
    }

    public void insertarYRefrescar() {
        LimpiarTabla();
        ResultSet r = null; //Se regresa un nulo porque no lo estamos usando al momento de insertar o modificar.
        actualizarTablaBD(r);
        LimpiarCampos();

    }

    public void eliminarYActualizar() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            LimpiarCampos();
            LimpiarTabla();
            ResultSet r = null;
            actualizarTablaBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ningún reporte. Favor de seleccionar.");
        }
    }

    void LimpiarCampos() {
        txtReporte.clear();
        txtConductor.clear();
        txtUsuario.clear();
        txtLugar.clear();
        txtDescripcion.clear();
        comboBox.setValue("");
        date.setValue(LocalDate.now());
    }

    void LimpiarTabla() {
        for (int i = 0; i < tablaReporte.getItems().size(); i++) {
            tablaReporte.getItems().clear();
        }
    }

    public void mostrarFilaEnCampos() { //Aquí se llama otra vez el bean para obtener los datos seleccionados de la tabla.
        Reportes reportes = tablaReporte.getSelectionModel().getSelectedItem();
        if (reportes == null) {
            LimpiarCampos();
        } else {
            int IdReporte = reportes.getId();
            String IdConductor = reportes.getIdConductor();
            String lugar = reportes.getLugar();
            String fecha = reportes.getFecha();
            String descripcion = reportes.getDescripcion();
            String valorComboBox = reportes.getTipo();
            txtReporte.setText(String.valueOf(IdReporte));
            txtConductor.setText(String.valueOf(IdConductor));
            comboBox.setValue(valorComboBox);
            date.setValue(LocalDate.parse(fecha));
            txtLugar.setText(lugar);
            txtDescripcion.setText(descripcion);
        }
    }

    void manejadorFiltro() {
        if (filtroActivo) {
            String idConductor;
            idConductor = txtConductor.getText();
            leerFiltro(idConductor);
        }
    }

    /* Sección de búsqueda. */
    @FXML
    private void handleFiltro() { //Acción del botón.
        filtroActivo = !filtroActivo; //Si el filtro es true (osea, se le dio el clic).

        if (filtroActivo) { //Deshabilitamos todo y solamente dejamos al txtfield que queremos que nos escuche.
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);

            txtConductor.textProperty().addListener(eventoFiltro);
            txtConductor.setPromptText("Buscar conductor...");
            txtDescripcion.setDisable(true);
            txtReporte.setDisable(true);
            txtLugar.setDisable(true);
            comboBox.setDisable(true);
            date.setDisable(true);

        } else { // Sino, se vuelve un false y activamos todo a como estaba antes.
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            btnModificar.setDisable(false);
            txtConductor.textProperty().removeListener(eventoFiltro);
            txtConductor.setPromptText("XXXXXXX");
            txtDescripcion.setDisable(false);
            txtLugar.setDisable(false);
            txtDescripcion.setDisable(false);
            comboBox.setDisable(false);
            date.setDisable(false);
            ResultSet r = null;
            actualizarTablaBD(r);
        }
    }

    // Este filtro limpia toda la tabla y busca por medio del resultset toda la información que yo quiero ver.
    private void leerFiltro(String idConductor) {
        Reportes filtro = new Reportes();
        LimpiarTabla();
            actualizarTablaBD(filtro.filtrarReporte(idConductor));
    }

    class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }

    public void showAlert(AlertType error, String header, String body) {
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }

      public boolean isIdConductorCorrect(){
        try {
            PreparedStatement statement = conexionBD.getConexion().prepareStatement("Select Id "
                    + "From Conductor "
                    + "where Id=?");
            statement.setString(1,txtConductor.getText());
            ResultSet result = statement.executeQuery();
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
//    
}
