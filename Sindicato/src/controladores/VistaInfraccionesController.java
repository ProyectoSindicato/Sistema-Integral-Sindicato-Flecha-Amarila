package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Infracciones;
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

/**
 * FXML Controller class
 * @author Aideé Alvarez
 */
public class VistaInfraccionesController implements Initializable {

   @FXML private TextField txtInfraccion, txtAutobus, txtDirector, txtConductor;
   @FXML private TextArea txtMotivo;
   @FXML private GridPane datePickerPanel;
   @FXML private DatePicker date;
   @FXML private Button btnAgregar, btnModificar, btnEliminar, btnBuscar, btnBack;
   @FXML private TableView<Infracciones> tablaInfraccion;
   @FXML private TableColumn<Infracciones,String> colID, colBus, colConductor, colFecha,colMotivo;
    
   /* Sección adicional.*/
   private boolean filtroActivo;
   private ConexionAccess conexionBD;
   ResultSetMetaData metadata = null;
   private Alert alert;
   ManejadorEventos eventoFiltro;
   /* Saber qué empleado es el que entra. */
   private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /*Modificar esto. */
        if(this.employee.getType() != 6){
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
        }
        txtDirector.setText(employee.getIdEmpleado());
    }
   
   public VistaInfraccionesController(){
       date = new DatePicker();
       conexionBD = new ConexionAccess();
   }
   
   public void cargarElementos(){
       datePickerPanel.add(date, 1, 4);
       txtInfraccion.setDisable(true);
       date.setValue(LocalDate.now());
   }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarElementos();
        llenarTablaBD();
        filtroActivo = false;
        eventoFiltro = new ManejadorEventos();
    }    
    
    /*Sección 2. Definiendo botones. */
    @FXML 
    public void agregarInfraccion(ActionEvent e){
        String autobus = txtAutobus.getText();
        String conductor = txtConductor.getText();
        LocalDate value = date.getValue();
        String motivo = txtMotivo.getText();
        String director = txtDirector.getText();
        System.out.println(autobus + conductor + value + motivo + director);
        if( (!(conductor.equals(""))) && (value!=null) && (!(motivo.equals("")))
                && (!autobus.equals(" ")) ){
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar una infracción?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
                if(isIdConductorCorrect()){
                    try{
                     Infracciones bean = new Infracciones();
                        bean.setIdAutobus(Integer.parseInt(autobus));
                        bean.setIdConductor(conductor);
                        bean.setMotivo(motivo);
                        bean.setFecha(String.valueOf(value));
                        bean.setIdDirector(director);
                        
                        if(bean.insertarInfraccion(director)){
                            insertarYRefrescar();
                             showAlert(AlertType.INFORMATION, "Information Message", " La infracción se ha añadido correctamente.");
                        }else{
                             showAlert(AlertType.ERROR, "Error Message", " Error al añadir infracción.");
                        }
                        
                        
                    } catch (NumberFormatException ex){
                        showAlert(AlertType.ERROR, "Error Message", " Error al añadir infracción.");
                    }
                }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
        }
    }
    
    @FXML
    public void modificarInfraccion(ActionEvent e){
        Infracciones inf = tablaInfraccion.getSelectionModel().getSelectedItem();
        String id = txtInfraccion.getText();
        String autobus = txtAutobus.getText();
        String conductor = txtConductor.getText();
        LocalDate value = date.getValue();
        String motivo = txtMotivo.getText();
        String director = txtDirector.getText();
        
        if(inf != null){
            if((!(autobus.equals(""))) && (!(conductor.equals(""))) && (!(director.equals("")))
                && (value!=null) && (!(motivo.equals(""))) && (!(id.equals("")))){
             
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea ingresar una infracción?");
                Optional<ButtonType> result = confirm.showAndWait();
            
                if(result.get() == ButtonType.OK){
                    if(isIdConductorCorrect()){
                  try{
                         Infracciones bean = new Infracciones();
                        bean.setIdAutobus(Integer.parseInt(autobus));
                        bean.setIdConductor(conductor);
                        bean.setMotivo(motivo);
                        bean.setFecha(String.valueOf(value));
                        bean.setIdDirector(director);
                        bean.setId(Integer.parseInt(id));
                       if(bean.modificarInfraccion(director) == true ){
                           insertarYRefrescar();
                           showAlert(AlertType.INFORMATION, "Information Message", "La infracción se ha modificado correctamente.");
                       }else{
                           showAlert(AlertType.ERROR, "Error Message", " Error al modificar infracción.");
                       }
                  }catch(Exception ex){
                      showAlert(AlertType.ERROR, "Error Message", " Error al modificar infracción.");
                         }
                     }
                  } 
             }else{
                showAlert(AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
            }
        } else{
                showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar una infracción.");
        }
    }
    
    @FXML 
    public void eliminarInfraccion(ActionEvent e){
        Infracciones inf = tablaInfraccion.getSelectionModel().getSelectedItem();
                
        if(inf != null){
            if(!(txtInfraccion.getText().equals(""))){
                 Alert confirm = new Alert(AlertType.CONFIRMATION);
                 confirm.setContentText("¿Desea eliminar un reporte?");

                 Optional<ButtonType> result = confirm.showAndWait();
                 
                 if(result.get() == ButtonType.OK){
                     int id = Integer.parseInt(txtInfraccion.getText());
                     
                     Infracciones bean = new Infracciones();
                     bean.setId(id);
                     if(bean.eliminarInfraccion(id)){
                         eliminarYActualizar();
                         showAlert(AlertType.INFORMATION, "Information Message.","Se ha eliminado la infracción correctamente.");
                     }else{
                           showAlert(AlertType.ERROR, "Error Message", " Error al eliminar la infracción.");
                     }
                 }
            }else{
                showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar una infracción.");
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.", "Favor de seleccionar una infracción.");
        }
    }
    
    /* Sección 3. Manipulación de tableview y tablecolumn.*/
    
     @FXML
    private void tablaInfraccionAction(MouseEvent e) {
        mostrarFilaEnCampos();
    }
        
     public void llenarTablaBD() {
        ResultSet rs;
        String sql = "SELECT Id, IdAutobus, IdConductor, Fecha, Motivo FROM Infracciones";

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
                int id = rs.getInt(1);
                int idAutobus = rs.getInt(2);
                String idConductor = String.valueOf(fila[2]);
                String fecha = String.valueOf(rs.getDate(4));
                String motivo = String.valueOf(fila[4]);
                
                Infracciones inf = new Infracciones(id, idAutobus, idConductor, fecha, motivo);
                colocarDatosColumna();
                tablaInfraccion.getItems().add(inf);
            }
        } catch (SQLException e) {
            System.out.println("Error al rellenar la tabla." + e.getMessage());
        }
    }
    
     public void actualizarTablaBD(ResultSet result) {
        ResultSet rs;
        String sql = "SELECT Id, IdAutobus, IdConductor, Fecha, Motivo FROM Infracciones";
        try{
            if(filtroActivo){
                rs = result;
            }else{
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
                int id = rs.getInt(1);
                int idAutobus = rs.getInt(2);
                String idConductor = String.valueOf(fila[2]);
                String fecha = String.valueOf(rs.getDate(4));
                String motivo = String.valueOf(fila[4]);
                
                Infracciones inf = new Infracciones(id, idAutobus, idConductor, fecha, motivo);
                colocarDatosColumna();
                tablaInfraccion.getItems().add(inf);
            }
        } catch (SQLException e) {
            System.out.println("Error al rellenar la tabla." + e.getMessage());
        }
            
        }catch (Exception e){
            
        }
    }
     
    void colocarDatosColumna(){
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colBus.setCellValueFactory(new PropertyValueFactory<>("IdAutobus"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
    }
    
    void limpiarCampos(){
        txtConductor.clear();
        txtMotivo.clear();
        txtInfraccion.clear();
        txtAutobus.clear();
        date.setValue(LocalDate.now());
    }
    
    
    public void insertarYRefrescar() {
        limpiarTabla();
        ResultSet r = null; //Se regresa un nulo porque no lo estamos usando al momento de insertar o modificar.
        actualizarTablaBD(r);
        limpiarCampos();

    }
    public void eliminarYActualizar() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            limpiarCampos();
            limpiarTabla();
            ResultSet r = null;
            actualizarTablaBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ningún reporte. Favor de seleccionar.");
        }
    }
    
    void limpiarTabla(){
        for (int i = 0; i < tablaInfraccion.getItems().size(); i++) {
            tablaInfraccion.getItems().clear();
        }
    }
    
    public void mostrarFilaEnCampos(){
        Infracciones inf = tablaInfraccion.getSelectionModel().getSelectedItem();
        
        if(inf == null){
            limpiarCampos();
        }else{
            int id = inf.getId();
            int idAutobus = inf.getIdAutobus();
            String idConductor = inf.getIdConductor();
            String fecha = inf.getFecha();
            String motivo = inf.getMotivo();
            
            txtInfraccion.setText(String.valueOf(id));
            txtConductor.setText(idConductor);
            txtMotivo.setText(motivo);
            txtAutobus.setText(String.valueOf(idAutobus));
            date.setValue(LocalDate.parse(fecha));
        }
    }
    
    /*Sección 4. Búsqueda de conductor y showAlerts. */
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
                showAlert(Alert.AlertType.ERROR,"Error","Clave de conductor incorrecta favor de verificar");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public void showAlert(Alert.AlertType error, String header, String body) {
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }
    
    /* Sección 5. Filtro de búsqueda.*/
    void manejadorFiltro() {
        if (filtroActivo) {
            String idConductor = txtConductor.getText();
            leerFiltro(idConductor);
        }
    }
    
    @FXML
    private void handleFiltro() { 
        filtroActivo = !filtroActivo;

        if (filtroActivo) {
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
            txtConductor.textProperty().addListener(eventoFiltro);
            txtConductor.setPromptText("Buscar por conductor...");
            txtAutobus.setDisable(true);
            txtMotivo.setDisable(true);
            date.setDisable(true);

        } else { // Sino, se vuelve un false y activamos todo a como estaba antes.
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            btnModificar.setDisable(false);
            txtMotivo.setDisable(false);
            txtAutobus.setDisable(false);
            txtConductor.textProperty().removeListener(eventoFiltro);
            date.setDisable(false);
            ResultSet r =null;
            actualizarTablaBD(r);
        }
    }
    
    private void leerFiltro(String idConductor) {
        Infracciones filtro = new Infracciones();
        limpiarTabla();
        actualizarTablaBD(filtro.filtrarInfraccion(idConductor));
    }
    
    class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); 
        }
    }
}
