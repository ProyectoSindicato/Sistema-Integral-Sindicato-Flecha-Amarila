package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    private ConexionAccess conexion;
    private Empleado employee;
    private String user, password, idEmpleado;
    private boolean isThere = false;
    private LoginController loginController;
    private PreparedStatement sentencia;
    private ResultSet result;
    private Parent root;
    @FXML
    private Button login;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField passwordTextField;
    FXMLDocumentController controller;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        conexion = new ConexionAccess();
        // Verificamos la conexion a la base de datos
        if (conexion.conectar()) {
            // Si se preciona el boton login y todos los campos fueron llenados
            if (event.getSource() == login
                    && (!userTextField.getText().equals("") && !passwordTextField.getText().equals(""))) {
                // Se buscan los datos ingresados
                try {
                    sentencia = conexion.getConexion().prepareStatement("select "
                            + "Usuario,Contraseña,IdEmpleado "
                            + "from Login "
                            + "where Usuario=? and Contraseña=?");
                    sentencia.setString(1, userTextField.getText());
                    sentencia.setString(2, passwordTextField.getText());
                    result = sentencia.executeQuery();
                    while (result.next()) {
                        /*Access no distingue entre mayusculas y minisculas
                        por lo que la contraseña "HOLA" es gual a "hola"
                        y el usuario "ALBERTO96" es igual a "alberto96"
                        por lo que se almacena cada resultado disponible en
                        variables string que nos ayudaran mediante
                        el metodo equals a distinguir entre todos los resultados
                        aquel que respete las minisculas y mayusculas correspondientes*/
                        user = result.getString(1);
                        password = result.getString(2);
                        idEmpleado = result.getString(3);
                        // Se usuario y contraseña concuerda con uno de los resultados...
                        if ((user.equals(userTextField.getText())) && (password.equals(passwordTextField.getText()))) {
                            isThere = true;
                            getKindEmployee();
                            break;
                        }
                    }
                    if (isThere) {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/Vista/FXMLDocument.fxml"));
                        loader.load();
                        FXMLDocumentController document = loader.getController();
                        document.setParameters(employee,conexion);
                        Parent p = loader.getRoot();
                        Scene scene = new Scene(p);
                        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                        s.setScene(scene);
                        s.setMaximized(true);
                        s.setResizable(true);
                        s.show();
                    } else {
                        System.out.println("Usuario o contraseña incorrecto");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println("Por favor llena todos los campos");
            }
        } else {
            System.out.println("Imposible conectar a la base de datos");
        }
    }
    /*
    Delegado = 0
    Directores:
        - General = 1
        - Finanzas = 2
        - Organizacion = 3
        - Seguridad y prevencion = 4
        - Trabajo = 5
        - Actas y acuerdos = 6
        - Asistente1 (aquellas que hacen descuentos) = 7
        - Asistente2 (Aquellas que hacen altas) = 8
    */
    public int getKindEmployee() {
        try {
            sentencia = conexion.getConexion().prepareStatement("select "
                    + "Id "
                    + "from Delegado "
                    + "where Id=?");
            sentencia.setString(1, idEmpleado);
            result = sentencia.executeQuery();
            if (result.next()) {
                employee = new Empleado(0, idEmpleado);
                return 0;
            } else {
                sentencia = conexion.getConexion().prepareStatement("select "
                        + "Tipo "
                        + "from Director "
                        + "where Id=?");
                sentencia.setString(1, idEmpleado);
                result = sentencia.executeQuery();
                if (result.next()) {
                    employee = new Empleado(result.getInt(1), idEmpleado);
                    return result.getInt(1);
                } else {
                    System.out.println("Usuario no definido");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
