package sindicato;
import ConexionAccess.ConexionAccess;
import controladores.FXMLDocumentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Empleado.Empleado;
import controladores.IncapacidadesController;
import controladores.VistaAsesoriaLegalController;
import controladores.VistaDemandasController;
import controladores.VistaMantenimientoController;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Principal extends Application {
    private PreparedStatement statement;
    private ResultSet result;
    private ConexionAccess conexion;
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/Vista/VistaLogin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Vista/VistaInfracciones.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        /*ConexionAccess conexion = new ConexionAccess();
        conexion.conectar();
        Empleado employee = new Empleado(5,"123456789");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/VistaMantenimiento.fxml"));
        loader.load();
        VistaMantenimientoController document = loader.getController();
        document.setParameters(employee,conexion);
        document.fillTable("");
        Parent p = loader.getRoot();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show(); */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
