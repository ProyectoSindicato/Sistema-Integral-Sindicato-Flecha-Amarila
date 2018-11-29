/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author abhdg
 */
public class FXMLDocumentController implements Initializable {
    private Empleado employee;
    private ConexionAccess conexion;

    @FXML
    private Label label;
    
    @FXML
    private Button btnButton1;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Yo soy un:"+ employee.getType());        
    }
    public void setParameters(Empleado employee, ConexionAccess conexion){
        this.employee = employee;
        this.conexion = conexion;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
