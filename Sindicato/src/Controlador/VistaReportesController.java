package Controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Aideé Alvarez
 */
public class VistaReportesController implements Initializable {

    @FXML private TextField txtReporte, txtUsuario, txtConductor, txtLugar,txtDia, txtMes, txtAño;
    @FXML private Button   btnAgregar, btnModificar, btnEliminar, btnBuscar;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    
}
