package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Autobuses;
import Modelo.ConductorAutobus;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 * @author Aideé Alvarez
 */
public class VistaAutobusesController implements Initializable {

    /* Primera tabla */
    @FXML private Button btnAgregar, btnModificar, btnEliminar, btnBuscar,  btnAgregar2, btnModificar2, btnEliminar2, btnBuscar2;
    @FXML private TableView<Autobuses> tablaAutobuses;
    @FXML private TableColumn<Autobuses, Integer> colID;
    @FXML private TableColumn<Autobuses, String> colMarca, colModelo;
    @FXML private TextField txtAutobus, txtMarca, txtModelo;
    
    @FXML private GridPane datePickerPanel;
    /* Segunda tabla..*/
    @FXML private TableView<ConductorAutobus> tablaAsignacion;
    @FXML private TableColumn<ConductorAutobus, Integer> colAutobus;
    @FXML private TableColumn<ConductorAutobus, String> colConductor, colFecha, colNombre;
    @FXML private TextField txtAutobus2, txtConductor;
    /* Sección de extras.*/
    ConexionAccess conexionBD;
    private boolean filtroActivo;
    private DatePicker date;
    private Alert alert;
    private ResultSetMetaData metadata;
    private ResultSetMetaData metadata2;
    /* Sección Empleado */
    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /*¿Quiénes hacen esto? */
        if( this.employee.getType() != 1){
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
            btnAgregar2.setDisable(true);
            btnEliminar2.setDisable(true);
            btnModificar2.setDisable(true);
        }
    }
    
    public VistaAutobusesController(){
        conexionBD = new ConexionAccess();
        date = new DatePicker();
    }
    
    public void cargarElementos(){
        datePickerPanel.add(date, 0, 0);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarElementos();
        filtroActivo = false;
        llenarAutobusesBD();
        llenarAsignacionBD();
    }    
    
    /* PARTE 1. Tabla Autobuses. */
     // Sección 1. Funcionamiento de botones.
    @FXML
    public void agregarAuto(ActionEvent e){
        String id = txtAutobus.getText();
        String modelo = txtModelo.getText();
        String marca = txtMarca.getText();
        
        if((!(id.equals(""))) && (!(modelo.equals(""))) && (!marca.equals(""))){
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar un autobús?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
             try{
                 Autobuses bean = new Autobuses();
                 bean.setId(Integer.parseInt(id));
                 bean.setModelo(modelo);
                 bean.setMarca(marca);
                 
                 if(bean.agregarAutobus(Integer.parseInt(id)) == true){
                     insertarRefrescarAuto();
                     showAlert(AlertType.INFORMATION, "Information Message.","Se ha añadido un autobús.");
                 }else{
                     showAlert(AlertType.ERROR, "Error Message.","Error al añadir un autobús.");
                 }
             }catch(NumberFormatException ex){
                 showAlert(AlertType.ERROR, "Error Message", " Error al añadir autobús.");
             }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message", " Favor de ingresar todos los datos.");
        }
    }
    
    @FXML 
    public void modificarAuto(ActionEvent e){
        String id = txtAutobus.getText();
        String modelo = txtModelo.getText();
        String marca = txtMarca.getText();
        
        if((!(id.equals(""))) && (!(modelo.equals(""))) && (!marca.equals(""))){
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea modificar un autobús?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
             try{
                 Autobuses bean = new Autobuses();
                 bean.setId(Integer.parseInt(id));
                 bean.setModelo(modelo);
                 bean.setMarca(marca);
                 
                 if(bean.modificarAutobus(Integer.parseInt(id)) == true){
                     insertarRefrescarAuto();
                     showAlert(AlertType.INFORMATION, "Information Message.","Se ha modificado un autobús.");
                 }else{
                     showAlert(AlertType.ERROR, "Error Message.","Error al modificar un autobús.");
                 }
             }catch(NumberFormatException ex){
                 showAlert(AlertType.ERROR, "Error Message", " Error al modificar autobús.");
             }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message", " Favor de ingresar todos los datos.");
        }
    }
    
    @FXML
    public void eliminarAuto(ActionEvent e){
        Autobuses borrar = tablaAutobuses.getSelectionModel().getSelectedItem();
        String id = txtAutobus.getText();
        if(borrar != null)
        {
            if((!(id.equals("")))){
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea eliminar un autobús?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
             try{
                 Autobuses bean = new Autobuses();
                 bean.setId(Integer.parseInt(id));
                 
                 if(bean.eliminarAutobus(Integer.parseInt(id)) == true){
                     insertarRefrescarAuto();
                     showAlert(AlertType.INFORMATION, "Information Message.","Se ha eliminado un autobús.");
                 }else{
                     showAlert(AlertType.ERROR, "Error Message.","Error al eliminar un autobús.");
                 }
             }catch(NumberFormatException ex){
                 showAlert(AlertType.ERROR, "Error Message", " Error al eliminar autobús.");
             }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar un autobús.");
        }
        }else{
             showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar un autobús.");
        }
    } 
    
    // Seccion 2. Funcionamiento tabla.
    @FXML
    private void tablaAutoAction(MouseEvent e){
        mostrarAutobusesCampos();
    }
    
    public void llenarAutobusesBD(){
        ResultSet rs;
        String sql = "SELECT Id, Marca, Modelo FROM Autobus";
        try
        {
         conexionBD.conectar();
         rs = conexionBD.ejecutarSQLSelect(sql);
         metadata = rs.getMetaData();
         int cols = metadata.getColumnCount();
         
         while(rs.next())
         {
             Object[] fila = new Object[cols];
             for (int i = 0; i < cols; i++) {
                 if(rs.getObject(i+1) == null){
                     fila[i] = "";
                 }else{
                     fila[i] = rs.getObject(i+1);
                 }
             }
             
             String id = String.valueOf(fila[0]);
             String marca = String.valueOf(fila[1]);
             String modelo = String.valueOf(fila[2]);
             
             Autobuses auto = new Autobuses(Integer.parseInt(id), marca, modelo);
             colocarDatosColumna();
             tablaAutobuses.getItems().add(auto);
         }
        }catch(SQLException e){
        }
    }
    
    public void actualizarAutobusBD(ResultSet result){
        ResultSet rs;
        try{
            if(filtroActivo){
                rs = result;
            }else{
                String sql = "SELECT Id, Marca, Modelo FROM Autobus";
                rs = conexionBD.ejecutarSQLSelect(sql);
            }
            
            try{
                 conexionBD.conectar();
                 metadata = rs.getMetaData();
                 int cols = metadata.getColumnCount();
         
                while(rs.next())
                 {
                     Object[] fila = new Object[cols];
             for (int i = 0; i < cols; i++) {
                 if(rs.getObject(i+1) == null){
                     fila[i] = "";
                 }else{
                     fila[i] = rs.getObject(i+1);
                 }
             }
             
                     String id = String.valueOf(fila[0]);
                     String marca = String.valueOf(fila[1]);
                     String modelo = String.valueOf(fila[2]);
             
                     Autobuses auto = new Autobuses(Integer.parseInt(id), marca, modelo);
                     colocarDatosColumna();
                     tablaAutobuses.getItems().add(auto);
                 }
                
            }catch(SQLException ex){
                  System.out.println("Error al filtrar la tabla." + ex.getMessage());
            }
        }catch(Exception e){
             showAlert(AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
        }
    }
    
    public void insertarRefrescarAuto(){
        limpiarTablaAutobus();
        ResultSet r = null;
        actualizarAutobusBD(r);
        limpiarCampos();
    }
    public void eliminarYActualizar() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            limpiarCampos();
            limpiarTablaAutobus();
            ResultSet r = null;
            actualizarAutobusBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ningún autobús. Favor de seleccionar.");
        }
    }
    
    
    void colocarDatosColumna(){
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("Marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
    }
    
    void limpiarCampos(){
        txtAutobus.clear();
        txtMarca.clear();
        txtModelo.clear();
    }
    
    void limpiarTablaAutobus(){
        for (int i = 0; i < tablaAutobuses.getItems().size(); i++) {
            tablaAutobuses.getItems().clear();
        }
    }
    
    void mostrarAutobusesCampos(){
        Autobuses auto = tablaAutobuses.getSelectionModel().getSelectedItem();
        if(auto == null){
            limpiarCampos();
        }else{
            int id = auto.getId();
            String modelo = auto.getModelo();
            String marca = auto.getMarca();
            
            txtAutobus.setText(String.valueOf(id));
            txtMarca.setText(marca);
            txtModelo.setText(modelo);
        }
    }
    
    /* ***********************Parte 2. Segunda Tabla.****************************** */
    //Sección botones.
    @FXML 
    private void agregarAsignacion(ActionEvent e){
     String autobus = txtAutobus2.getText();
     String conductor = txtConductor.getText();
     LocalDate value = date.getValue();
        
     if((!(autobus.equals(""))) && (!(conductor.equals(""))) && value!= null){
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea asignar un autobús?");
            Optional<ButtonType> result = confirm.showAndWait();
         
            if(result.get() == ButtonType.OK){
                if(isIdBusCorrect()){
                    if(isIdConductorCorrect()){
                        try{
                            ConductorAutobus bean = new ConductorAutobus();
                            
                            bean.setIdAutobus(Integer.parseInt(autobus));
                            bean.setIdConductor(conductor);
                            bean.setFechaAsignacion(String.valueOf(value));
                            
                            if(bean.agregarAsignacion() == true){
                                insertarRefrescarAsign();
                                showAlert(AlertType.INFORMATION, "Information Message", "Se ha añadido una asignación de autobús.");
                            }else{
                                showAlert(AlertType.ERROR, "Error Message", " Error al añadir asignación.");
                            }
                        }catch(NumberFormatException ex){
                            showAlert(AlertType.ERROR, "Error Message", " Error al añadir asignación.");
                        }
                    }
                }
            } 
     }else{
         showAlert(AlertType.WARNING, "Warning Message", "Favor de llenar todos los campos.");
     }
    }
    
    @FXML 
    public void modificarAsignacion(ActionEvent e){
        ConductorAutobus modificar = tablaAsignacion.getSelectionModel().getSelectedItem();
        String autobus = txtAutobus2.getText();
        String conductor = txtConductor.getText();
        LocalDate value = date.getValue();
        
        if(modificar != null){
            if((!(autobus.equals(""))) && (!(conductor.equals(""))) && value!=null){
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea modificar un reporte?");

                Optional<ButtonType> result = confirm.showAndWait();
                
                if(result.get() == ButtonType.OK){
                   if(isIdBusCorrect()){
                       System.out.println("Es correcta.");
                        if(isIdConductorCorrect()){
                            System.out.println("ID Conductor correcta");
                            System.out.println("Fecha correcta:" + value);
                      try{
                          ConductorAutobus bean = new ConductorAutobus();
                          bean.setIdAutobus(Integer.parseInt(autobus));
                          bean.setIdConductor(conductor);
                          bean.setFechaAsignacion(String.valueOf(value));
                          
                          if(bean.modificarAsignacion() == true){
                              insertarRefrescarAsign();
                              showAlert(AlertType.INFORMATION, "Information Message.", "La asignación ha sido modificada.");
                          }else{
                              System.out.println("Entré aquí.");
                              showAlert(AlertType.ERROR, "Error Message.", "No se pudo modificar la asignación.");
                          }
                          
                         }catch(NumberFormatException ex){
                             System.out.println("Entré acá." + ex.getMessage());
                             showAlert(AlertType.ERROR, "Error Message.", "No se pudo modificar la asignación.");
                        }
                    }
                   }
                }
            } else{
                showAlert(AlertType.WARNING, "Warning Message.", "Favor de llenar los campos.");
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.", "No se ha seleccionado nada.");
        }
    }
    
    @FXML
    public void eliminarAsignacion(ActionEvent e){
        ConductorAutobus borrar = tablaAsignacion.getSelectionModel().getSelectedItem();
        String autobus = txtAutobus2.getText();
        String conductor = txtConductor.getText();
        if(borrar != null)
        {
            if((!(autobus.equals(""))) && (!(conductor.equals("")))){
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea eliminar una asignación?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
             try{
                 ConductorAutobus bean = new ConductorAutobus();
                 bean.setIdAutobus(Integer.parseInt(autobus));
                 bean.setIdConductor(conductor);
                 if(bean.eliminarAsignacion() == true){
                     eliminarRefrescarAsign();
                     showAlert(AlertType.INFORMATION, "Information Message.","Se ha eliminado la asignación.");
                 }else{
                     showAlert(AlertType.ERROR, "Error Message.","Error al eliminar la asignación.");
                 }
             }catch(NumberFormatException ex){
                 showAlert(AlertType.ERROR, "Error Message", " Error al eliminar la asignación.");
             }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message", " Favor de seleccionar una asignación.");
        }
        }else{
             showAlert(AlertType.WARNING, "Warning Message", "  Favor de seleccionar una asignación.");
        }
    } 
    
    
    // Sección tabla.
    @FXML 
    private void tablaAsignacionAction(MouseEvent e){
        mostrarAsignacionCampos();
    }
    
    public void llenarAsignacionBD(){
        ResultSet rs;
        String sql = "SELECT IdAutobus,IdConductor,FechaAsignacion FROM ConductorAutobus";
        try
        {
         conexionBD.conectar();
         rs = conexionBD.ejecutarSQLSelect(sql);
         metadata2 = rs.getMetaData();
         int cols = metadata2.getColumnCount();
         
         while(rs.next())
         {
             Object[] fila = new Object[cols];
             for (int i = 0; i < cols; i++) {
                 if(rs.getObject(i+1) == null){
                     fila[i] = "";
                 }else{
                     fila[i] = rs.getObject(i+1);
                 }
                 
             }
             
             String autobus = String.valueOf(fila[0]);
             String conductor = String.valueOf(fila[1]);
             String fecha = String.valueOf(rs.getDate(3));
             String nombre = nombreConductor(conductor);
            ConductorAutobus asign = new ConductorAutobus(Integer.parseInt(autobus), conductor,nombre, fecha);
            colocarDatosColumnaAsignacion();
            tablaAsignacion.getItems().add(asign);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", " Error al llenar tabla de asignación autobús.");
        }
    }
    
    public void actualizarAsignacionBD(ResultSet result){
        ResultSet rs;
        try{
            if(filtroActivo){
                rs = result;
            }else{
                String sql = "SELECT IdAutobus,IdConductor,FechaAsignacion FROM ConductorAutobus";
                rs = conexionBD.ejecutarSQLSelect(sql);
            }
            try
                 { 
                      
                       conexionBD.conectar();
                       metadata2 = rs.getMetaData();
                       int cols = metadata2.getColumnCount();
         
                   while(rs.next())
                   {
                        Object[] fila = new Object[cols];
                       for (int i = 0; i < cols; i++) {
                              if(rs.getObject(i+1) == null){
                             fila[i] = "";
                              }else{
                                fila[i] = rs.getObject(i+1);
                              }
                       
                    }
             String autobus = String.valueOf(fila[0]);
             String conductor = String.valueOf(fila[1]);
             String fecha = String.valueOf(rs.getDate(3));
             String nombre = nombreConductor(conductor);
            ConductorAutobus asign = new ConductorAutobus(Integer.parseInt(autobus), conductor, nombre, fecha);
            colocarDatosColumnaAsignacion();
            tablaAsignacion.getItems().add(asign);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", " Error al llenar tabla de asignación autobús.");
        }
        }catch(Exception e){
             showAlert(AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
        }
    }
    
    
    void colocarDatosColumnaAsignacion(){
        colAutobus.setCellValueFactory(new PropertyValueFactory<>("IdAutobus"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("FechaAsignacion"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("NombreConductor"));
    }
    
    void limpiarCamposAsignacion(){
        txtAutobus2.clear();
        txtConductor.clear();
        date.setValue(LocalDate.now());
    }
    
    void limpiarTablaAsignacion(){
        for (int i = 0; i < tablaAsignacion.getItems().size(); i++) {
            tablaAsignacion.getItems().clear();
        }
    }
    
    public void insertarRefrescarAsign(){
        limpiarTablaAsignacion();
        ResultSet r = null;
        actualizarAsignacionBD(r);
        limpiarCamposAsignacion();
    }
    
    public void eliminarRefrescarAsign() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            limpiarCamposAsignacion();
            limpiarTablaAsignacion();
            ResultSet r = null;
            actualizarAsignacionBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ninguna asignación. Favor de seleccionar.");
        }
    }
    
    
    void mostrarAsignacionCampos(){
        ConductorAutobus asignar = tablaAsignacion.getSelectionModel().getSelectedItem();
        if(asignar == null){
            limpiarCampos();
        }else{
            int id = asignar.getIdAutobus();
            String conductor = asignar.getIdConductor();
            String fecha = asignar.getFechaAsignacion();
            
            txtAutobus2.setText(String.valueOf(id));
            txtConductor.setText(conductor);
            date.setValue(LocalDate.parse(fecha));
        }
    }
    
    /*SECCION SHOW ALERTS */
    
    public void showAlert(Alert.AlertType error, String header, String body) {
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
             statement.setString(1, txtConductor.getText()); 
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
    
    public boolean isIdBusCorrect(){
        try{
            PreparedStatement st = conexionBD.getConexion().prepareStatement("SELECT Id FROM Autobus WHERE Id= ?");
            st.setInt(1, Integer.parseInt(txtAutobus2.getText()));
            ResultSet result = st.executeQuery();
            if(result.next()) {
                return true;
            }else{
                showAlert(AlertType.ERROR, "Error Message.", "El ID del autobús es incorrecta. Intente de nuevo.");
                return false;
            }
        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public String nombreConductor(String conductor){ //debe ser el id del conductor.
        String nombre = "";
        try{
            PreparedStatement ps = conexionBD.getConexion().prepareStatement("SELECT Nombres, ApellidoPaterno FROM Empleado WHERE Id=? ");
            ps.setString(1, conductor);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nombre = rs.getString(1) + " " + rs.getString(2);
                return nombre;
            }
            return "";
        }catch(SQLException ex){
            ex.printStackTrace();
            return "";
        } 
    }
    
}
