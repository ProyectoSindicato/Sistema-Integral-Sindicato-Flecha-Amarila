package controladores;

import ConexionAccess.ConexionAccess;
import Modelo.Reportes;
import java.awt.HeadlessException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.Button;

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
        int confirmar;
        //  String idRep = txtReporte.getText();
        String idUsr = txtUsuario.getText();
        String idCond = txtConductor.getText();
        String lugar = txtLugar.getText();
        LocalDate value = date.getValue();
        String combo = comboBox.getSelectionModel().getSelectedItem();
        String desc = txtDescripcion.getText();

        if ((!(desc.equals(""))) && (combo != null)
                && (!(idCond.equals(""))) && (!(lugar.equals(""))) && (value != null)) {
            confirmar = JOptionPane.showConfirmDialog(null, "¿Desea ingresar un reporte?", "Confirmar agregar reporte.",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirmar == 0) {
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
                        JOptionPane.showMessageDialog(null, "Se ha agregado el reporte.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al agregar un reporte.");
                    }

                } catch (HeadlessException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error al ingresar. Checar datos.");
                    System.out.println("Aquí está el error: " + ex.getLocalizedMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Favor de ingresar todos los datos requeridos.",
                    "Error al agregar datos.", JOptionPane.PLAIN_MESSAGE);
        }

    } //botón.

    // Función de modificar.
    @FXML
    public void modificarReporte(ActionEvent e) {
        int confirmar;
        Reportes modificar = tablaReporte.getSelectionModel().getSelectedItem();
        int id = Integer.parseInt(txtReporte.getText());
        String idUsr = txtUsuario.getText();
        String idConductor = txtConductor.getText();
        String tipo = comboBox.getValue();
        String lugar = txtLugar.getText();
        LocalDate value = date.getValue();
        String desc = txtDescripcion.getText();
        String combo = comboBox.getSelectionModel().getSelectedItem();

        if (modificar != null) {
            if (!(idConductor.equals("")) && !(tipo.equals("")) && !(lugar.equals(""))
                    && (value != null) && !(desc.equals("")) && (combo != null)) {
                confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente quiere modificar el reporte?",
                        "Confirmar modificación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirmar == 0) {
                    try {
                        Reportes m = new Reportes();
                        m.setId(id);
                        m.setIdConductor(idConductor);
                        m.setTipo(tipo);
                        m.setLugar(lugar);
                        m.setFecha(String.valueOf(value));
                        m.setDescripcion(desc);
                        if (m.modificarReporte(idUsr) == true) {
                            insertarYRefrescar();
                            JOptionPane.showMessageDialog(null, "Modificación exitosa.");

                        } else {
                            JOptionPane.showMessageDialog(null, "Error al modificar reporte.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al modificar el reporte. Checar datos.");
                    }
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "No se a seleccionado nada", "Error al modificar.",
                    JOptionPane.PLAIN_MESSAGE);
        }

    }

    // Funcion de eliminar.
    @FXML
    public void eliminarReporte(ActionEvent e) {
        int confirmar;
        Reportes borrar = tablaReporte.getSelectionModel().getSelectedItem();

        if (borrar != null) {
            if (!(txtReporte.getText().equals(""))) {
                confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar el reporte?",
                        "Confirmar eliminación.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirmar == 0) {
                    int idReporte = Integer.parseInt(txtReporte.getText());

                    Reportes eliminar = new Reportes();
                    eliminar.setId(idReporte);

                    if (eliminar.eliminarReporte(idReporte)) {
                        insertarYRefrescar();
                        JOptionPane.showMessageDialog(null, "Reporte eliminado exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar reporte. ");
                    }
                }
            } else if (txtReporte.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No se a seleccionado nada.", "Eliminar.", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (borrar == null) {
            JOptionPane.showMessageDialog(null, "No se a seleccionado nada.", "Eliminar.", JOptionPane.PLAIN_MESSAGE);
        }

    }

    /* 
        Sección 2. Manipulación de tableview y tablecolumn.
     */
    @FXML
    private void tablaReportesAction(MouseEvent e) {
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
            txtLugar.setDisable(true);
            txtDescripcion.setDisable(true);
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
        System.out.println("Entré al filtro de controller.");
    }

    class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }
}
