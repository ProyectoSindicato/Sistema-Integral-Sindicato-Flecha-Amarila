package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Screen;
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
    private Button btnButton1, btnReporte, btnAccidentes, btnInfracciones, btnAutobuses, btnConductores, btnIncapacidades,
            btnDescuentos, btnEventos, btnPermisos, btnDemandas, btnAsesorias, btnMantenimiento, btnKilometraje;

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

    @FXML
    void handleInfracciones(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaInfracciones.fxml"));
        loader.load();
        VistaInfraccionesController document = loader.getController();
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
    void handleAutobuses(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaAutobuses.fxml"));
        loader.load();
        VistaAutobusesController document = loader.getController();
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
    void handleRoles(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaRoles.fxml"));
        loader.load();
        VistaRolesController document = loader.getController();
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
    void handleConductores(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaConductores.fxml"));
        loader.load();
        ConductoresController document = loader.getController();
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
    void handleIncapacidades(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/Incapacidades.fxml"));
        loader.load();
        VistaIncapacidadesController document = loader.getController();
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
    void handleDescuentos(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaDescuentos.fxml"));
        loader.load();
        DescuentosController document = loader.getController();
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
    void handleEventos(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaEventos.fxml"));
        loader.load();
        VistaEventosController document = loader.getController();
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
    void handlePermisos(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaPermisos.fxml"));
        loader.load();
        VistaPermisosController document = loader.getController();
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
    void handleDemandas(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaDemandas.fxml"));
        loader.load();
        VistaDemandasController document = loader.getController();
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
    void handleAsesorias(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaAsesoriaLegal.fxml"));
        loader.load();
        VistaAsesoriaLegalController document = loader.getController();
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
    void handleMantenimiento(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaMantenimiento.fxml"));
        loader.load();
        VistaMantenimientoController document = loader.getController();
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
    void handleKilometraje(ActionEvent event) throws IOException {
        conexion.conectar();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaKilometraje.fxml"));
        loader.load();
        KilometrajeController document = loader.getController();
        document.setParameters(employee, conexion);
        Parent p = loader.getRoot();
        Scene scene = new Scene(p);
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setScene(scene);
        s.setMaximized(true);
        s.setResizable(true);
        s.show();
    }

    public FXMLDocumentController() {
        conexion = new ConexionAccess();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void backLogin(ActionEvent event) throws IOException {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("¿Desea cerrar sesión?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Vista/VistaLogin.fxml"));
            loader.load();
            Parent p = loader.getRoot();
            Scene scene = new Scene(p);
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(scene);
            s.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
            s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
        }
    }
}
