package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

public class IncapacidadesController implements Initializable {
    @FXML
    private GridPane datePickerPanel,datePickerPanel2;
    @FXML 
    private DatePicker date,date2;
    @FXML
    private Button registrar;
    void datePicker()
      {
           date = new DatePicker();
           datePickerPanel.add(date, 0, 0);
           date2 = new DatePicker();
           datePickerPanel2.add(date2, 0, 0);
      }
    @FXML
    private void handleButtonAction(ActionEvent e){
        if(e.getSource() == registrar){
            System.out.println(date.getValue()); 
            System.out.println(date2.getValue());
        }
    } 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker();
    }    
    
}
