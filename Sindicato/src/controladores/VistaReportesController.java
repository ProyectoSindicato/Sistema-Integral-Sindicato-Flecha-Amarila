package controladores;

import ConexionAccess.ConexionAccess;
import Modelo.Reportes;
import java.awt.HeadlessException;
import java.net.URL;
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
import javax.swing.JOptionPane;
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
    private final ConexionAccess conexionBD;
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
    private String idUsuario; // Usuario que insertará o modificará.
    private boolean filtroActivo; //Filtro del changeListener.

    ObservableList<String> boxOpciones
            = FXCollections.observableArrayList("General", "Inspección", "Tacografía",
                    "Mantenimiento", "Call Center", "Divisiones", "Boletos",
                    "Servicio", "Otro"
            );

    public VistaReportesController() {
        conexionBD = new ConexionAccess();
        date = new DatePicker();

    }

    void cargarElementos() {
        comboBox.getItems().addAll(boxOpciones);
        DatePickerPanel.add(date, 0, 0);

    }

    /* SECCION FXMLLoader */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idUsuario = ""; //prueba.
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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("¿Desea ingresar un reporte?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
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
                        Alert alertconf = new Alert(AlertType.INFORMATION);
                        alertconf.setTitle("Confirmación");
                        alertconf.setContentText("El reporte se ha añadido satisfactoriamente.");
                        alertconf.showAndWait();
                    } else {
                        Alert alerterr = new Alert(AlertType.ERROR);
                        alerterr.setTitle("Error message");
                        alerterr.setContentText("Error al añadir el reporte en la base de datos.");
                        alerterr.showAndWait();
                    }

                } catch (HeadlessException | NumberFormatException ex) {
                    Alert alerterr2 = new Alert(AlertType.ERROR);
                    alerterr2.setTitle("Error message - catch.");
                    alerterr2.setContentText("Error al añadir el reporte.");
                    System.out.println("Aquí está el error: " + ex.getLocalizedMessage());
                    alerterr2.showAndWait();
                }
            }

        } else { //Else de validación de campos vacíos.
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning message");
            alert.setContentText("Los campos están incompletos. Checar datos.");
            alert.showAndWait();
        }

    } //botón.

    // Función de modificar.
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

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("¿Desea modificar un reporte?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
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
                            Alert alertconf = new Alert(AlertType.INFORMATION);
                            alertconf.setTitle("Confirmación");
                            alertconf.setContentText("El reporte se ha modificado satisfactoriamente.");
                            alertconf.showAndWait();
                        } else {
                            Alert alerterr = new Alert(AlertType.ERROR);
                            alerterr.setTitle("Error message");
                            alerterr.setContentText("Error al modificar el reporte en la base de datos.");
                            alerterr.showAndWait();
                        }
                    } catch (Exception ex) {
                        Alert alerterr2 = new Alert(AlertType.ERROR);
                        alerterr2.setTitle("Error message");
                        alerterr2.setContentText("Error al modificar el reporte.");
                        alerterr2.showAndWait();
                    }
                }

            }
        } else {
            Alert alerterr = new Alert(AlertType.ERROR);
            alerterr.setTitle("Warning message");
            alerterr.setContentText("Favor de seleccionar un dato. Checar datos.");
            alerterr.showAndWait();

        }
    }

    // Funcion de eliminar.
    @FXML
    public void eliminarReporte(ActionEvent e) {
        Reportes borrar = tablaReporte.getSelectionModel().getSelectedItem();

        if (borrar != null) {
            if (!(txtReporte.getText().equals(""))) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("¿Desea eliminar un reporte?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    int idReporte = Integer.parseInt(txtReporte.getText());

                    Reportes eliminar = new Reportes();
                    eliminar.setId(idReporte);

                    if (eliminar.eliminarReporte(idReporte)) {
                        insertarYRefrescar();
                        Alert alertconf = new Alert(AlertType.INFORMATION);
                        alertconf.setTitle("Confirmación");
                        alertconf.setContentText("El reporte se ha eliminado satisfactoriamente.");
                        alertconf.showAndWait();
                    } else {
                        Alert alerterr = new Alert(AlertType.ERROR);
                        alerterr.setTitle("Error message");
                        alerterr.setContentText("Error al eliminar el reporte en la base de datos.");
                        alerterr.showAndWait();
                    }
                }
            } else if (txtReporte.getText().equals("")) {
                Alert alerterr = new Alert(AlertType.ERROR);
                alerterr.setTitle("Warning message");
                alerterr.setContentText("Favor de seleccionar un dato. Checar datos.");
                alerterr.showAndWait();
            }
        } else if (borrar == null) {
            Alert alerterr = new Alert(AlertType.ERROR);
            alerterr.setTitle("Warning message");
            alerterr.setContentText("Favor de seleccionar un dato. Checar datos.");
            alerterr.showAndWait();
        }

    }

    /* 
        Sección 2. Manipulación de tableview y tablecolumn.
     */
    @FXML
    private void tablaReportesAction(MouseEvent e
    ) {
        mostrarFilaEnCampos();
    }

    public void llenarTablaBD() //Llena la tabla principal al momento en que se construye el fxml.
    {
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

    /* ¿Qué sucede aquí? Bueno, hay dos opciones:
        1.- Buscas por la acción del botón.
        2.- Todo normal como si leyeras la base de datos.
     */
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

            try { //Todo esto es lo mismo a como si llenaras la tabla normal. Lo único que cambia es lo de arriba.
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

    public void insertarYRefrescar() { //Este es importante. Si utilizas el otro, no va a funcionar la acción del botón.
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
            JOptionPane.showMessageDialog(null, "Se elimino el  con exito", "Eliminar", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "No se a seleccionado ningun Profesor", "Eliminar", JOptionPane.PLAIN_MESSAGE);
        }
    }

    void LimpiarCampos() {
        txtReporte.clear();
        txtConductor.clear();
        txtUsuario.clear();
        txtLugar.clear();
        txtDescripcion.clear();
        comboBox.setValue(" ");
        date.setValue(LocalDate.now());
    }

    void LimpiarTabla() {
        for (int i = 0; i < tablaReporte.getItems().size(); i++) {
            tablaReporte.getItems().clear();
        }
    }

    public void mostrarFilaEnCampos() { //Aquí se llama otra vez el bean para obtener los datos seleccionados de la tabla.
        Reportes reportes = tablaReporte.getSelectionModel().getSelectedItem();
        if (reportes == null) //Si no hay nada, no muestra nada.
        {
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
        if (filtroActivo) { //Si es un true, obtenemos el idConductor para que haga la búsqueda.
            String idConductor;
            idConductor = txtConductor.getText();
            leerFiltro(idConductor); //Este filtro es el que está más abajo.
        }
    }

    @FXML
    private void handleFiltro() { //Acción del botón.
        filtroActivo = !filtroActivo; //Si el filtro es true (osea, se le dio el clic).

        if (filtroActivo) { //Deshabilitamos todo y solamente dejamos al txtfield que queremos que nos escuche.
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);

            txtConductor.textProperty().addListener(eventoFiltro);
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
            txtDescripcion.setDisable(false);
            txtLugar.setDisable(false);
            txtDescripcion.setDisable(false);
            comboBox.setDisable(false);
            date.setDisable(false);
        }
    }

    // Este filtro limpia toda la tabla y busca por medio del resultset toda la información que yo quiero ver.
    private void leerFiltro(String idConductor) {
        Reportes filtro = new Reportes();
        LimpiarTabla();
        actualizarTablaBD(filtro.filtrarReporte(idConductor)); //Checa en Modelo -> Reportes.

    }

    class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }
}
