package controladores;

import ConexionAccess.ConexionAccess;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class VistaRescuePasswordController implements Initializable {
    private Alert alert;
    private String remitente = "jomahelo.st"; 
    private ConexionAccess conexion;
    private PreparedStatement sentencia;
    private ResultSet result;
    private String user;
    private String password;
    private String destinatario;
    private boolean isThere;
    
    @FXML
    private Button send,back;
    @FXML
    private TextField userTextField;
    
    public void showAlert(Alert.AlertType error,String header, String body){
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    } 

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == send){
            password = "";
            user = "";
            destinatario = "";
            isThere = false;
            if(userTextField.getText().equals("")){
                showAlert(AlertType.WARNING,"","Ingresa tu nombre de usuario");
            }else{
                conexion = new ConexionAccess();
                if(conexion.conectar()){
                    try {
                        sentencia = conexion.getConexion().prepareStatement(
                                "select l.Usuario,l.Contraseña,e.CorreoElectronico "
                                        + "from Login l inner join Empleado e"
                                        + " on l.IdEmpleado=e.Id"
                                        + " where Usuario=?"
                        );
                        sentencia.setString(1, userTextField.getText());
                        result = sentencia.executeQuery();
                        while(result.next()){
                            user = result.getString(1);
                            if(user.equals(userTextField.getText())){
                                password = result.getString(2);
                                destinatario = result.getString(3);
                                isThere = true;
                                break;
                            }
                        }
                        if(isThere){
                            // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
                            Properties props = System.getProperties();
                            props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
                            props.put("mail.smtp.user", remitente);
                            props.put("mail.smtp.clave", "hernandez2018*");    //La clave de la cuenta
                            props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
                            props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
                            props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

                            Session session = Session.getDefaultInstance(props);
                            MimeMessage message = new MimeMessage(session);

                            try {
                                message.setFrom(new InternetAddress(remitente));
                                message.addRecipients(Message.RecipientType.TO, destinatario);   //Se podrían añadir varios de la misma manera
                                message.setSubject("Recuperacion de contraseña");//Asunto
                                message.setText("Su contraseña es: "+password);
                                Transport transport = session.getTransport("smtp");
                                transport.connect("smtp.gmail.com", remitente, "hernandez2018*");
                                transport.sendMessage(message, message.getAllRecipients());
                                transport.close();
                                showAlert(AlertType.INFORMATION,"¡Listo!","Enviamos tu contraseña al correo "+destinatario+ 
                                        "\nexitosamente.");
                                conexion.close();
                            }
                            catch (MessagingException me) {
                                me.printStackTrace();   //Si se produce un error
                            }
                        }else{
                            showAlert(AlertType.INFORMATION,"","El nombre de usuario no existe");
                        }
                    }
                    catch (SQLException ex) {
                        Logger.getLogger(VistaRescuePasswordController.class.getName()).log(Level.SEVERE, null, ex);   
                    } 
                }
            }
        }else if(event.getSource() == back){
            try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Vista/VistaLogin.fxml"));
            loader.load();
            Parent p = loader.getRoot();
            Scene scene = new Scene(p);
            Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
            s.setScene(scene);
            s.setTitle("Login");
            s.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
