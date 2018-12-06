package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Modelo.ModeloExcel;

/**
 * FXML Controller class
 *
 * @author Personal
 */
public class DescuentosController implements Initializable {

//    ModeloExcel modeloE = new ModeloExcel();
//    ControladorExcel obk = new ControladorExcel(modeloE);
    File archivo;
    int contAccion = 0;
    boolean statusbar=false;
    static int contadorbarra;
    JFileChooser selecArchivo = new JFileChooser();
    ModeloExcel modeloE = new ModeloExcel();
    ConexionAccess conexionBD;
    @FXML
    private Button btnagregar, btnmodificar, btneliminar, btnbuscar, btnimportar;

    @FXML
    private Label estado;

    @FXML
    private ProgressBar barraProgreso2;
    
    Task copyWorker;

    private Empleado employee;

    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
    }
    
    public DescuentosController(){
        conexionBD = new ConexionAccess();
    }
    
    //Para boton "agregar"
    @FXML
    private void agregar(ActionEvent event) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog, "PRESIONASTE AGREGAR.");
    }

    //Para boton "modificar"
    @FXML
    private void modificar(ActionEvent event) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog, "PRESIONASTE MODIFICAR.");
    }

    //Para boton "eliminar"
    @FXML
    private void eliminar(ActionEvent event) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog, "PRESIONASTE ELIMINAR.");
    }

    //Para boton "buscar"
    @FXML
    private void buscar(ActionEvent event) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog, "PRESIONASTE BUSCAR.");
    }

    //Para boton "Cargar"
    @FXML
    private void importar(ActionEvent event) {
        System.out.println("Presionaste Importar!!...");
        FileChooser fileChooser = new FileChooser();
        archivo = fileChooser.showOpenDialog(null);

        if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {
            barraProgreso2.setProgress(0.0);

            copyWorker = createWorker();

            barraProgreso2.progressProperty().unbind();

            barraProgreso2.progressProperty().bind(copyWorker.progressProperty());

            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue.equals("Subiendo : 100%")) {
                        System.out.println("Completado");
                        estado.setText("Completado");
                        System.out.println(modeloE.Importar(archivo) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1));
//                        JOptionPane.showMessageDialog(null, 
//                            modeloE.Importar(archivo) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1), 
//                            "IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        System.out.println(newValue);
                        estado.setText(newValue);
                    }
                    
                    if ("Completado".equals(estado.getText())) {
                        
                        //JOptionPane.showMessageDialog(null, 
                        //modeloE.Importar(archivo, vistaE.jtDatos) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1), 
                        //"IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            new Thread(copyWorker).start();

        } else {
            barraProgreso2.setProgress(0.0);

            copyWorker = createWorker();

            barraProgreso2.progressProperty().unbind();

            barraProgreso2.progressProperty().bind(copyWorker.progressProperty());

            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue.equals("Subiendo : 100%")) {
                        System.out.println("Error: Formato de archivo");
                        estado.setText("Error: Formato de archivo");
                    }else{
                        System.out.println(newValue);
                        estado.setText(newValue);
                    }
                    
                    if ("Error: Formato de archivo".equals(estado.getText())) {
                        
                        //JOptionPane.showMessageDialog(null, 
                        //modeloE.Importar(archivo, vistaE.jtDatos) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1), 
                        //"IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            new Thread(copyWorker).start();
        }
    }
    
    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 101; i++) {
                    Thread.sleep(200);
                    updateMessage("Subiendo : " + i  + "%");
                    updateProgress(i + 1, 100);
                }
                return true;
            }
        };
    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
