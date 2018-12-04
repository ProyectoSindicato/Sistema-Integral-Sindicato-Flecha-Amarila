package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.ConductoresRoles;
import Modelo.Roles;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * @author Aideé Alvarez
 */
public class VistaRolesController implements Initializable {

    @FXML private Button btnAgregar, btnModificar, btnEliminar, btnBuscar, btnAgregar2, btnModificar2, btnEliminar2, btnBuscar2, btnBack;
    @FXML private TextField txtEmpleado, txtRol, txtCorrida, txtRolAsignado, txtConductor;
    @FXML private TableView<Roles> tablaRoles;
    @FXML private TableColumn<Roles, Integer> colRol;
    @FXML private TableColumn<Roles, String> colEmpleado, colNombre, colCorrida;
    @FXML private TableView<ConductoresRoles> tablaConRol;
    @FXML private TableColumn<ConductoresRoles, Integer> colRolAsignacion;
    @FXML private TableColumn<ConductoresRoles, String> colConductor, colNombreConductor;
    private boolean filtroTablaRol;
    private boolean filtroTablaAsignar;
    Alert alert;
    ConexionAccess conexionBD;
    ResultSetMetaData metadata;
    
    ManejadorEventos evento;
    ManejadorEventosAsignacion eventoAsign;
    
    public VistaRolesController(){
        conexionBD = new ConexionAccess();
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroTablaAsignar = false;
        filtroTablaRol     = false;
        llenarRolesBD();
        llenarRolesAsignadosBD();
         txtRol.setDisable(true);
         evento =new ManejadorEventos();
         eventoAsign = new ManejadorEventosAsignacion();
         txtEmpleado.setDisable(true);
    }    
    
    /* Empleados.*/
    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /*¿Quiénes hacen esto? Delegados, Actas y Acuerdos.*/
        if( this.employee.getType() != 0 && this.employee.getType() != 6){
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
            btnAgregar2.setDisable(true);
            btnEliminar2.setDisable(true);
            btnModificar2.setDisable(true);
        }
        txtEmpleado.setText(employee.getIdEmpleado());
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
    /* ************************TABLA ROLES ********************************/
    @FXML
    public void agregarRol(ActionEvent e){
        String empleado = txtEmpleado.getText();
        String corrida = txtCorrida.getText();
        
        if((!(empleado.equals(""))) && (!(corrida.equals("")))){
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea añadir un rol?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
                if(isIdEmployeeCorrect()){
                    try{
                    Roles bean = new Roles();
                    bean.setIdEmpleado(empleado);
                    bean.setCorrida(corrida);
                    
                    if(bean.agregarRol(empleado) == true){
                        insertarRefrescar();
                        showAlert(AlertType.INFORMATION, "Information Message.","Se añadió el rol correctamente.");
                    }else{
                        showAlert(AlertType.ERROR, "Error Message.","Error al añadir el rol.");
                    }
                      }catch(NumberFormatException ex){
                     showAlert(AlertType.ERROR, "Error Message.","Error al añadir el rol.");
                   
              }
                }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.","Favor de rellenar los campos.");
        }
    }
    
    @FXML
    public void modificarRol(ActionEvent e){
        Roles rol = tablaRoles.getSelectionModel().getSelectedItem();
        String empleado = txtEmpleado.getText();
        String roltxt = txtRol.getText();
        String corrida = txtCorrida.getText();
        
        if(rol != null){
            if((!(empleado.equals(""))) && (!(roltxt.equals(""))) && (!(corrida.equals("")))){
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea modificar un rol?");
            Optional<ButtonType> result = confirm.showAndWait();
                    if(result.get() == ButtonType.OK){
                          try{
                              Roles bean = new Roles();
                               bean.setId(Integer.parseInt(roltxt));
                               bean.setIdEmpleado(empleado);
                               bean.setCorrida(corrida);
                    
                              if(bean.modificarRol(Integer.parseInt(roltxt)) == true){
                                    insertarRefrescar();
                                   showAlert(AlertType.INFORMATION, "Information Message.","El rol se ha modificado correctamente.");
                              }else{
                                   showAlert(AlertType.ERROR, "Error Message.","Error al modificar el rol.");
                              }
                              
                          }catch(Exception ex){
                               showAlert(AlertType.ERROR, "Error Message.","Error al modificar el rol.");
                          }
                    }
            }else{
                 showAlert(AlertType.WARNING, "Warning Message.","Favor de rellenar los campos.");
            }
        }else{
             showAlert(AlertType.WARNING, "Warning Message.","Favor de seleccionar un rol.");
        }
    }
    
    @FXML
    public void eliminarRol(ActionEvent e){
         Roles rol = tablaRoles.getSelectionModel().getSelectedItem();
         String roltxt = txtRol.getText();
         if(rol != null){
            if( (!(roltxt.equals("")))){
                   Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                  confirm.setContentText("¿Desea eliminar un rol?");
                  Optional<ButtonType> result = confirm.showAndWait();
                    if(result.get() == ButtonType.OK){
                          try{
                              Roles bean = new Roles();
                               bean.setId(Integer.parseInt(roltxt));
                    
                              if(bean.eliminarRol(Integer.parseInt(roltxt)) == true){
                                  eliminarYActualizar();
                                   showAlert(AlertType.INFORMATION, "Information Message.","El rol se ha eliminado correctamente.");
                              }else{
                                   showAlert(AlertType.ERROR, "Error Message.","Error al eliminar el rol.");
                              }
                              
                          }catch(Exception ex){
                               showAlert(AlertType.ERROR, "Error Message.","Error al eliminar el rol.");
                          }
                    }
            }else{
                 showAlert(AlertType.WARNING, "Warning Message.","Seleccione un rol.");
            }
        }else{
             showAlert(AlertType.WARNING, "Warning Message.","Favor de seleccionar un rol.");
        }   
    }
    
    /* TABLA DE ROL*/
     @FXML 
     private void tablaRolAction(MouseEvent e){
         mostrarRolesCampos();
     }
    
     public void llenarRolesBD(){
         ResultSet rs;
        String sql = "SELECT Id,IdEmpleado, Corrida FROM Roles";
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
             
             String rol = String.valueOf(fila[0]);
             String empleado = String.valueOf(fila[1]);
             String corrida = String.valueOf(fila[2]);
             String nombre = nombreEmpleado(empleado);
             Roles r = new Roles(Integer.parseInt(rol),empleado,corrida,nombre);
             colocarDatosRoles();
             tablaRoles.getItems().add(r);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", "Error al llenar la tabla.");
            e.getMessage();
        }
     }
     
     public void actualizarRolesBD(ResultSet result){
         ResultSet rs;
        if(filtroTablaRol){
            rs = result;
        }else{
            String sql = "SELECT Id,IdEmpleado, Corrida FROM Roles";
            rs = conexionBD.ejecutarSQLSelect(sql);
        }
        try
        {
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
             
             String rol = String.valueOf(fila[0]);
             String empleado = String.valueOf(fila[1]);
             String corrida = String.valueOf(fila[2]);
             String nombre = nombreEmpleado(empleado);
             Roles r = new Roles(Integer.parseInt(rol),empleado,corrida,nombre);
             colocarDatosRoles();
             tablaRoles.getItems().add(r);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", "Error al llenar la tabla.");
        }
     }
     
     void colocarDatosRoles(){
         colEmpleado.setCellValueFactory(new PropertyValueFactory<>("IdEmpleado"));
         colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
         colRol.setCellValueFactory(new PropertyValueFactory<>("Id"));
         colCorrida.setCellValueFactory(new PropertyValueFactory<>("Corrida"));
     }
    
     void limpiarCampos(){
         txtCorrida.clear();
         txtRol.clear();
     }
     
     void limpiarTablaRoles(){
         for (int i = 0; i < tablaRoles.getItems().size(); i++) {
             tablaRoles.getItems().clear();
         }
     }
     
     void insertarRefrescar(){
         limpiarCampos();
         limpiarTablaRoles();
         ResultSet r = null;
         actualizarRolesBD(r);
     }
     
     public void eliminarYActualizar() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            limpiarCampos();
            limpiarTablaRoles();
            ResultSet r = null;
            actualizarRolesBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ningún rol. Favor de seleccionar.");
        }
    }
     void mostrarRolesCampos(){
         Roles rol = tablaRoles.getSelectionModel().getSelectedItem();
         if(rol == null){
             limpiarCampos();
         }else{
             int id = rol.getId();
             String empleado = rol.getIdEmpleado();
             String corrida = rol.getCorrida();
             
             txtRol.setText(String.valueOf(id));
             txtCorrida.setText(corrida);
         }
     }
     
     // FILTRADO.
     @FXML
     private void handleRol(ActionEvent e){
         filtroTablaRol =! filtroTablaRol;
         if(filtroTablaRol){
             btnAgregar.setDisable(true);
             btnModificar.setDisable(true);
             btnEliminar.setDisable(true);
             txtCorrida.textProperty().addListener(evento);
             txtCorrida.setPromptText("Buscar por corrida.");
         }else{
             btnAgregar.setDisable(false);
             btnModificar.setDisable(false);
             btnEliminar.setDisable(false);
             txtCorrida.textProperty().removeListener(evento);
             ResultSet r = null;
             actualizarRolesBD(r);
         }
     }
     
     void manejadorFiltro(){
        if(filtroTablaRol){
            String corrida = txtCorrida.getText();
            leerFiltroRol(corrida);
        }
    }
     
     private void leerFiltroRol(String corrida){
        Roles filtro = new Roles();
        limpiarTablaRoles();
        actualizarRolesBD(filtro.filtroRoles(corrida));
    }
     
     class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }
     
     /* ***************** TABLA ASIGNACION DE ROLES**************** */
     @FXML 
     public void agregarAsignacion(ActionEvent e){
         String conductor = txtConductor.getText();
         String rol = txtRolAsignado.getText();
          if((!(conductor.equals(""))) && (!(rol.equals("")))){
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea asignar un rol?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
                if(isIdConductorCorrect()){
                    try{
                    ConductoresRoles bean = new ConductoresRoles();
                    bean.setIdRol(Integer.parseInt(rol));
                    bean.setIdConductor(conductor);
                    if(bean.agregarRolConductor(Integer.parseInt(rol)) == true){
                        insertarRefrescarAsignacion();
                        showAlert(AlertType.INFORMATION, "Information Message.","Se añadió el rol asignado correctamente.");
                    }else{
                        showAlert(AlertType.ERROR, "Error Message.","Error al añadir el rol asignado.");
                    }
                      }catch(NumberFormatException ex){
                     showAlert(AlertType.ERROR, "Error Message.","Error al añadir el rol asignado.");
                   
                     }
                }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.","Favor de rellenar los campos.");
        } 
     }
     
     @FXML 
     public void modificarAsignacion(ActionEvent e){
         ConductoresRoles asig = tablaConRol.getSelectionModel().getSelectedItem();
         String conductor = txtConductor.getText();
         String rol = txtRolAsignado.getText();
          
         if(asig !=null){
             if((!(conductor.equals(""))) && (!(rol.equals("")))){
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea modificar un rol?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
                if(isIdEmployeeCorrect()){
                    if(isIdRolCorrect()){
                        try{
                    ConductoresRoles bean = new ConductoresRoles();
                    bean.setIdRol(Integer.parseInt(rol));
                    bean.setIdConductor(conductor);
                    if(bean.modificarRolConductor(Integer.parseInt(rol)) == true){
                        insertarRefrescarAsignacion();
                        showAlert(AlertType.INFORMATION, "Information Message.","Se modificó el rol asignado correctamente.");
                    }else{
                        showAlert(AlertType.ERROR, "Error Message.","Error al modificar el rol asignado.");
                    }
                      }catch(NumberFormatException ex){
                     showAlert(AlertType.ERROR, "Error Message.","Error al modificar el rol asignado.");
                   
                     }
                    }
                }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.","Favor de rellenar los campos.");
        }
         }else{
              showAlert(AlertType.WARNING, "Warning Message.","Favor de seleccionar un dato.");
         }
     }
     
     @FXML 
     public void eliminarAsignacion(ActionEvent e){
         ConductoresRoles eliminar = tablaConRol.getSelectionModel().getSelectedItem();
         String conductor = txtConductor.getText();
         String rol = txtRolAsignado.getText();
         
         if(eliminar!= null){
              if((!(conductor.equals(""))) && (!(rol.equals("")))){
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea eliminar un rol asignado?");
            Optional<ButtonType> result = confirm.showAndWait();
            
            if(result.get() == ButtonType.OK){
                if(isIdConductorCorrect()){
                    try{
                    ConductoresRoles bean = new ConductoresRoles();
                    bean.setIdRol(Integer.parseInt(rol));
                    bean.setIdConductor(conductor);
                    if(bean.eliminarRolConductor(Integer.parseInt(rol),conductor) == true){
                       eliminarYActualizarAsignacion();
                        showAlert(AlertType.INFORMATION, "Information Message.","Se eliminó el rol asignado correctamente.");
                    }else{
                        showAlert(AlertType.ERROR, "Error Message.","Error al eliminar el rol asignado.");
                    }
                      }catch(NumberFormatException ex){
                     showAlert(AlertType.ERROR, "Error Message.","Error al eliminar el rol asignado.");
                   
                     }
                }
            }
        }else{
            showAlert(AlertType.WARNING, "Warning Message.","Favor de rellenar los campos.");
        } 
         }else{
             showAlert(AlertType.WARNING, "Warning Message.","Favor de seleccionar un rol asignado.");
         }
     }
     
     // TABLA ROL ASIGNADO.
     @FXML
     public void tablaRolAsAction(MouseEvent event){
         mostrarAsignacionesCampos();
     }
     
     public void llenarRolesAsignadosBD(){
         ResultSet rs;
        String sql = "SELECT IdRol,IdConductor FROM ConductoresRoles";
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
             
             String rol = String.valueOf(fila[0]);
             String conductor = String.valueOf(fila[1]);
             String nombre = nombreEmpleado(conductor);
             ConductoresRoles cr = new ConductoresRoles(Integer.parseInt(rol), conductor, nombre);
             colocarDatosAsignacion();
             tablaConRol.getItems().add(cr);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", "Error al llenar la tabla.");
            e.getMessage();
        }
     }
     
     public void insertarRolesAsignadosBD(ResultSet result){
         ResultSet rs;
        if(filtroTablaAsignar){
            rs = result;
        }else{
            String sql = "SELECT IdRol,IdConductor FROM ConductoresRoles";
            rs = conexionBD.ejecutarSQLSelect(sql);
        }
         
        try
        {
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
             
             String rol = String.valueOf(fila[0]);
             String conductor = String.valueOf(fila[1]);
             String nombre = nombreEmpleado(conductor);
             ConductoresRoles cr = new ConductoresRoles(Integer.parseInt(rol), conductor, nombre);
             colocarDatosAsignacion();
             tablaConRol.getItems().add(cr);
         }
        }catch(SQLException e){
            showAlert(AlertType.ERROR, "Error Message.", "Error al llenar la tabla.");
            e.getMessage();
        }
     }
     
     
     
     void colocarDatosAsignacion(){
         colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
         colNombreConductor.setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
         colRolAsignacion.setCellValueFactory(new PropertyValueFactory<>("IdRol"));
     }
     
     void limpiarCamposAsignacion(){
         txtRolAsignado.clear();
         txtConductor.clear();
     }
     
     void limpiarTablaRolesAsignacion(){
         for (int i = 0; i < tablaConRol.getItems().size(); i++) {
             tablaConRol.getItems().clear();
         }
     }
     
     void insertarRefrescarAsignacion(){
         limpiarCamposAsignacion();
         limpiarTablaRolesAsignacion();
         ResultSet r = null;
        insertarRolesAsignadosBD(r);
     }
     
     public void eliminarYActualizarAsignacion() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            limpiarCamposAsignacion();
            limpiarTablaRolesAsignacion();
            ResultSet r = null;
             insertarRolesAsignadosBD(r);
        } catch (Exception error) {
            showAlert(AlertType.ERROR, "Error Message", " No se ha seleccionado ningún rol asignado. Favor de seleccionar.");
        }
    }
     
      public void mostrarAsignacionesCampos(){
          ConductoresRoles asignar = tablaConRol.getSelectionModel().getSelectedItem();
          if(asignar == null){
              limpiarCamposAsignacion();
          }else{
              String id = String.valueOf(asignar.getIdRol());
              String conductor = asignar.getIdConductor();
              
              txtRolAsignado.setText(id);
              txtConductor.setText(conductor);
          }
      }
     
      // FILTRADO.
     @FXML
     private void handleRolAsignacion(ActionEvent e){
         filtroTablaAsignar =! filtroTablaAsignar;
         if(filtroTablaAsignar){
             btnAgregar2.setDisable(true);
             btnModificar2.setDisable(true);
             btnEliminar2.setDisable(true);
             txtRolAsignado.setDisable(true);
             txtConductor.textProperty().addListener(eventoAsign);
             txtConductor.setPromptText("Buscar por conductor...");
         }else{
             btnAgregar2.setDisable(false);
             btnModificar2.setDisable(false);
             btnEliminar2.setDisable(false);
             txtRolAsignado.setDisable(false);
             txtConductor.textProperty().removeListener(eventoAsign);
             ResultSet r = null;
             insertarRolesAsignadosBD(r);
         }
     }
     
     void manejadorFiltroAsignacion(){
        if(filtroTablaAsignar){
            String conductor = txtConductor.getText();
            leerFiltroRolAsignacion(conductor);
        }
    }
     
     private void leerFiltroRolAsignacion(String conductor){
        ConductoresRoles filtro = new ConductoresRoles();
        limpiarTablaRolesAsignacion();
        insertarRolesAsignadosBD(filtro.filtroRolesAsignados(conductor));
    }
     
     class ManejadorEventosAsignacion implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltroAsignacion(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }
      
      
      
    /* EXTRAS */
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
                showAlert(Alert.AlertType.ERROR,"Error","Clave de conductor incorrecta, favor de verificar");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public boolean isIdEmployeeCorrect(){
        try {
            PreparedStatement statement = conexionBD.getConexion().prepareStatement("Select Id "
                    + "From Empleado "
                    + "where Id=?");
             statement.setString(1, txtEmpleado.getText()); 
            ResultSet result = statement.executeQuery();
            if(result.next()){
             return true;   
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","Clave de empleado incorrecta, favor de verificar.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public boolean isIdRolCorrect(){
        try {
            PreparedStatement statement = conexionBD.getConexion().prepareStatement("Select Id "
                    + "From Roles "
                    + "where Id=?");
             statement.setString(1, txtRolAsignado.getText()); 
            ResultSet result = statement.executeQuery();
            if(result.next()){
             return true;   
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","Clave de rol incorrecta, favor de verificar.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public String nombreEmpleado(String id){ //debe ser el id del conductor.
        String nombre = "";
        try{
            PreparedStatement ps = conexionBD.getConexion().prepareStatement("SELECT Nombres, ApellidoPaterno FROM Empleado WHERE Id=? ");
            ps.setString(1, id);
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
    
     public void showAlert(Alert.AlertType error, String header, String body) {
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }
    
}
