/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    private Button btnButton1, btnReporte, btnAccidentes;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Yo soy un:" + employee.getType());
    }

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexion = conexion;
    }

    @FXML
    void handleReportes(ActionEvent event) throws IOException {
        conexion = new ConexionAccess();
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaReportes.fxml"));
        loader.load();
        VistaReportesController document = loader.getController();
        document.setParameters(employee, conexion);
        Parent p = loader.getRoot();
        Scene scene = new Scene(p);
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setScene(scene);
        s.setMaximized(true);
        s.setResizable(true);
        s.show();
    }
    
     @FXML
    void handleAccidentes(ActionEvent event) throws IOException {
        conexion = new ConexionAccess();
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaAccidentes.fxml"));
        loader.load();
        VistaAccidentesController document = loader.getController();
        document.setParameters(employee, conexion);
        
        Parent p = loader.getRoot();
        Scene scene = new Scene(p);
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setScene(scene);
        s.setMaximized(true);
        s.setResizable(true);
        s.show();
    }
    
    
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
