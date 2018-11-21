/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import ConexionAccess.ConexionAccess;
import Modelo.Reportes;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Aideé Alvarez
 */
/**
 * FXML Controller class
 *
 * @author Aideé Alvarez
 */
public class VistaReportesController implements Initializable {

     ResultSetMetaData metadata = null;
    private final ConexionAccess conexionBD;
    
    @FXML private TextField txtReporte, txtUsuario, txtConductor, txtLugar,txtDia, txtMes, txtAño;
    @FXML private TextArea txtDescripcion;
    @FXML private Button   btnAgregar, btnModificar, btnEliminar, btnBuscar;
    @FXML private TableView<Reportes> tablaReporte;
    @FXML private TableColumn<Reportes, String> colTipo,colFecha, colDescripcion;
    @FXML private TableColumn<Reportes, Integer> colID, colConductor;
    @FXML private ComboBox box;
    
    //pruebas con el datepicker.
    @FXML private GridPane DatePickerPanel;
    @FXML private DatePicker date;
    
    private String idUsuario; // Usuario que insertará o modificará.
    
    public VistaReportesController()
    {
        conexionBD = new ConexionAccess();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idUsuario = ""; //prueba.
        datePicker();
    }    
    
    /* 
            Sección 1. Funcionamiento de botones.
    */
    
    // Función de agregar.
    public void agregarReporte(ActionEvent e)
    {
        
    }
    
    // Función de modificar.
    public void modificarReporte(ActionEvent e)
    {
        
    }
    
    // Funcion de eliminar.
    public void eliminarReporte(ActionEvent e)
    {
        
    }
    
    /* 
        Sección 2. Manipulación de tableview y tablecolumn.
    */
    
    public void LlenarTablaBD()
    {   
         ResultSet rs;
        String sql = "SELECT Id, IdConductor,Tipo,Fecha,Descripcion FROM Reportes";
          conexionBD.conectar();
        try{
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
               // FALTA CODIGO.
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    void colocarDatosColumna()
    {
        colID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
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
                 // tipo es un combobox.
                  String lugar = String.valueOf(reportes.getLugar());
                  // fecha sigue pendiente.
                  String fecha = String.valueOf(reportes.getFecha());
                  String descripcion = String.valueOf(reportes.getDescripcion());
                  
                  
                 txtReporte.setText(IdReporte);
                 txtConductor.setText(IdConductor);
                 
                 txtLugar.setText(lugar);
                 txtDescripcion.setText(descripcion);
          }
      }
        
        /* Sección DatePicker -- pruebas*/
      void datePicker()
      {
           date = new DatePicker();
           DatePickerPanel.add(date, 0, 0);
      }
    
}
