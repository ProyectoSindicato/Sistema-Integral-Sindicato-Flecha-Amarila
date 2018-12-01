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

public class Principal extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/Vista/VistaLogin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Vista/VistaConductores.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        /*ConexionAccess conexion = new ConexionAccess();
        conexion.conectar();
        Empleado employee = new Empleado(0,"12345");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Vista/Incapacidades.fxml"));
        loader.load();
        IncapacidadesController document = loader.getController();
        document.setParameters(employee,conexion);
        document.fillTable("");
        Parent p = loader.getRoot();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
