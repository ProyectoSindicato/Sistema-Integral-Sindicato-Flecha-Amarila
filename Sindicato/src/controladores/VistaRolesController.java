package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import Modelo.Roles;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    
    private boolean filtroTablaRol;
    private boolean filtroTablaAsignar;
    Alert alert;
    ConexionAccess conexionBD;
    ResultSetMetaData metadata;
    
    public VistaRolesController(){
        conexionBD = new ConexionAccess();
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroTablaAsignar = false;
        filtroTablaRol     = false;
        llenarRolesBD();
         txtRol.setDisable(true);
      //  txtEmpleado.setDisable(true);
    }    
    
    /* Empleados.*/
    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
        /*¿Quiénes hacen esto? Dir. (1), Asistente 2(8) y Dir. Organización(3).*/
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
     
     /* ***************** TABLA ASIGNACION DE CORRIDAS**************** */
     
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
                showAlert(Alert.AlertType.ERROR,"Error","Clave de conductor incorrecta favor de verificar");
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
                showAlert(Alert.AlertType.ERROR,"Error","Clave de empleado incorrecta favor de verificar.");
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
