package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    private ConexionAccess conexion;
    private Empleado employee;
    private String user, password, idEmpleado;
    private boolean isThere = false;
    private LoginController loginController;
    private PreparedStatement sentencia;
    private ResultSet result;

    @FXML
    private Button login;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField passwordTextField;
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
                            break;
                        }
                    }
                    if (isThere) {
                        /* Aqui se construye la vista segun el tipo de
                        empleado logeado*/
                        switch (getKindEmployee()) {
                            // Si es delegado...
                            case 0:

                                break;
                            // Si es director general
                            case 1:

                                break;
                            // Si es director de finanzas
                            case 2:

                                break;
                            // Si es director de organizacion
                            case 3:

                                break;
                            // Si es director de seguridad y prevencion
                            case 4:

                                break;
                            // Si es director de trabajo
                            case 5:

                                break;
                            // Si es director de actas y acuerdos
                            case 6:

                                break;
                            // Si es asistente1 (aquellas que hace descuentos
                            case 7:

                                break;
                            // Si es asistente2 (aquella que da altas)
                            case 8:

                                break;
                            // Lo que se hace si no se reconocio como empleado
                            default:
                                System.out.println("Usuario no identificado");
                                break;
                        }
                    } else {
                        System.out.println("Usuario o contraseña incorrecto");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println("Por favor llena todos los campos");
            }
        } else {
            System.out.println("Imposible conectar a la base de datos");
        }
    }

    public int getKindEmployee() {
        try {
            sentencia = conexion.getConexion().prepareStatement("select "
                    + "IdEmpleado "
                    + "from Delegado "
                    + "where IdEmpleado=?");
            sentencia.setString(1, idEmpleado);
            result = sentencia.executeQuery();
            if (result.next()) {
                employee = new Empleado(0, idEmpleado);
                return 0;
            } else {
                sentencia = conexion.getConexion().prepareStatement("select "
                        + "Tipo "
                        + "from Director "
                        + "where IdEmpleado=?");
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
