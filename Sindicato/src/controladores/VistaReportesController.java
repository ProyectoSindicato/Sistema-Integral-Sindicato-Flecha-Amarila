package controladores;

import ConexionAccess.ConexionAccess;
import Modelo.Reportes;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Aideé.
 */
public class VistaReportesController implements Initializable {
 ResultSetMetaData metadata = null;
    private final ConexionAccess conexionBD;
    private TextField txtReporte;
    
    @FXML private TextField txtUsuario, txtConductor, txtLugar;
    @FXML private TextArea txtDescripcion;
    @FXML private TableView<Reportes> tablaReporte;
    @FXML private TableColumn<Reportes, String> colTipo;
    @FXML private TableColumn<Reportes, String>  colDescripcion;
    @FXML private TableColumn<Reportes, String> colDate;
    @FXML private TableColumn<Reportes, Integer> colID;
    @FXML private TableColumn<Reportes, Integer> colConductor;
    @FXML ComboBox<String> comboBox = new ComboBox<String>();
    
    //pruebas con el datepicker.
    @FXML private GridPane DatePickerPanel;
    private DatePicker date;
    
    private String idUsuario; // Usuario que insertará o modificará.
    
    ObservableList<String> boxOpciones = 
    FXCollections.observableArrayList(  "General", "Inspección", "Tacografía",
            "Mantenimiento", "Call Center","Divisiones", "Boletos", 
            "Servicio", "Otro"
    );

    public VistaReportesController()
    {
        conexionBD = new ConexionAccess();
        date = new DatePicker();
    }
    
    void cargarElementos()
    {
     comboBox.getItems().addAll(boxOpciones);
    DatePickerPanel.add(date, 0, 0);     
    date.setValue(LocalDate.now());
    }
    
    
    /* SECCION FXMLLoader */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idUsuario = ""; //prueba.
        cargarElementos();
    //    LlenarTablaBD();
    }    
    
    /* 
            Sección 1. Funcionamiento de botones.
    */
    // Función de agregar.
    public void agregarReporte(ActionEvent e)
    {
        int confirmar;
        String idRep = txtReporte.getText();
        String idUsr = txtUsuario.getText();
        String idUsr2 = "0";
        String idCond = txtConductor.getText();
        String lugar = txtLugar.getText();
        String fecha = String.valueOf(date.getValue());
        String combo = comboBox.getSelectionModel().getSelectedItem();
        String desc = txtDescripcion.getText();
        
        if( !(idCond.equals("")) && !(lugar.equals("")) && !(fecha.equals("")) &&
            !(combo.equals("")) && !(desc.equals("")))
        {
            confirmar = JOptionPane.showConfirmDialog(null, "¿Desea ingresar un reporte?", "Confirmar agregar reporte.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if(confirmar == 0)
            {
                try
                {
                    Reportes bean = new Reportes();
                    bean.setIdConductor(Integer.parseInt(idCond));
                    bean.setIdDirector(Integer.parseInt(idUsr2));
                    bean.setIdDelegado(Integer.parseInt(idUsr));
                    bean.setTipo(combo);
                    bean.setLugar(lugar);
                    bean.setFecha(fecha);
                    bean.setDescripcion(desc);
                    
                    if(bean.insertarReporte() == true)
                    {
                        ActualizaRefresca();
                        JOptionPane.showMessageDialog(null, "Se ha agregado el reporte.");
                    }else
                        {
                      JOptionPane.showMessageDialog(null, "Error al agregar un reporte.");
                        }
                    
                }catch (Exception ex)
                {
                     JOptionPane.showMessageDialog(null, "Error al ingresar. Checar datos.");
                }
            }
        }else
        {
            JOptionPane.showMessageDialog(null, "Favor de ingresar todos los datos requeridos.",
                    "Error al agregar datos.", JOptionPane.PLAIN_MESSAGE);
        }
        
    } //botón.
    
    // Función de modificar.
    public void modificarReporte(ActionEvent e)
    {
        int confirmar;
        Reportes modificar = tablaReporte.getSelectionModel().getSelectedItem();
        
        String idConductor = txtConductor.getText();
        String tipo = comboBox.getValue();
        String lugar = txtLugar.getText();
        String fecha = String.valueOf(date.getValue());
        String desc = txtDescripcion.getText();
        
        if(modificar != null)
        {
            if(!(idConductor.equals("")) && !(tipo.equals("")) && !(lugar.equals(""))
                    && !(fecha.equals("")) && !(desc.equals("")))
            {
                 confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente quiere modificar el reporte?", 
                         "Confirmar modificación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          
                 if(confirmar == 0)
                 {
                     try
                     {
                         Reportes m = new Reportes();
                         
                         m.setIdConductor(Integer.parseInt(idConductor));
                         m.setTipo(tipo);
                         m.setLugar(lugar);
                         m.setFecha(fecha);
                         m.setDescripcion(desc);
                         if(m.modificarReporte() == true)
                         {
                             JOptionPane.showMessageDialog(null, "Modificación exitosa.");
                             ActualizaRefresca();
                         }else
                         {
                             JOptionPane.showMessageDialog(null, "Error al modificar reporte.");
                         }
                     }catch (Exception ex)
                     {
                         JOptionPane.showMessageDialog(null, "Error al modificar el reporte. Checar datos.");
                     }
                 }
                         
            }
        } else
        {
            JOptionPane.showMessageDialog(null, "No se a seleccionado nada", "Error al modificar.", 
                                            JOptionPane.PLAIN_MESSAGE);
        }
        
        
    }
    
    // Funcion de eliminar.
    public void eliminarReporte(ActionEvent e)
    {
        int confirmar;
        Reportes borrar = tablaReporte.getSelectionModel().getSelectedItem();
        
        if(borrar != null)
        {
            if(!(txtReporte.getText().equals("")))
            {
                confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar el reporte?",
                        "Confirmar eliminación.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if(confirmar == 0)
                {
                    int idReporte = Integer.parseInt(txtReporte.getText());
                    
                        Reportes eliminar = new Reportes();
                        eliminar.setId(idReporte);
                        
                        if(eliminar.eliminarReporte(idReporte))
                        {
                            ActualizaRefresca();
                            JOptionPane.showMessageDialog(null, "Reporte eliminado exitosamente.");
                        }else
                        {
                             JOptionPane.showMessageDialog(null, "Error al eliminar reporte. ");
                        }
                }
            }else if (txtReporte.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "No se a seleccionado nada.", "Eliminar.", JOptionPane.PLAIN_MESSAGE);
            }
        }else if(borrar == null)
        {
             JOptionPane.showMessageDialog(null, "No se a seleccionado nada.", "Eliminar.", JOptionPane.PLAIN_MESSAGE);
        }
        
    }
    // Buscar reporte.
    public void buscarReporte()
    {
        
    }
    /* 
        Sección 2. Manipulación de tableview y tablecolumn.
    */
    
    public void LlenarTablaBD() //Llena la tabla principal.
    {   
         ResultSet rs;
        String sql = "SELECT Id, IdConductor,Tipo,Fecha,Descripcion FROM Reportes";
          
        try{
            conexionBD.conectar();
            rs = conexionBD.ejecutarSQLSelect(sql);
            metadata = rs.getMetaData();
            int cols = metadata.getColumnCount();
            
            while(rs.next())
            {
               Object[] fila = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    if(rs.getObject(i+1) == null)
                    {
                      fila[i] = "";  
                    } else
                    {
                      fila[i] = rs.getObject(i+1);
                    }
                }
                int id = (int) fila[0];
                int idCond = (int) fila[1];
                String tipo = (String) fila[2];
                String fecha = (String) fila[3];
                String desc = (String) fila[4];
               Reportes r = new Reportes(id, idCond,tipo,fecha,desc);
               colocarDatosColumna();
               tablaReporte.getItems().add(r);
            }
        }catch(SQLException e){
            System.out.println("Error al rellenar la tabla." + e.getMessage());
        }
    }
    
    void colocarDatosColumna()
    {
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
    }
    
    void ActualizaRefresca()
    {
        LimpiarTabla();
        LlenarTablaBD();
        LimpiarCampos();
    }
    
    void LimpiarCampos()
    {
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
    
        public void mostrarFilaEnCampos()
      { //Aquí se llama otra vez el bean para obtener los datos seleccionados de la tabla.
          Reportes reportes = tablaReporte.getSelectionModel().getSelectedItem();
          if(reportes == null) //Si no hay nada, no muestra nada.
          {
               LimpiarCampos();
          }else{
                  String IdReporte = String.valueOf(reportes.getId());
                  String IdConductor = String.valueOf(reportes.getIdConductor());
                  String lugar = String.valueOf(reportes.getLugar());
                  String fecha = String.valueOf(reportes.getFecha());
                  String descripcion = String.valueOf(reportes.getDescripcion());
                  String valorComboBox = String.valueOf(reportes.getTipo());
                  txtReporte.setText(IdReporte); 
                  txtConductor.setText(IdConductor);
                  comboBox.setValue(valorComboBox);
                  date.setValue(LocalDate.parse(fecha));
                  txtLugar.setText(lugar);
                  txtDescripcion.setText(descripcion);
          }
      }
}