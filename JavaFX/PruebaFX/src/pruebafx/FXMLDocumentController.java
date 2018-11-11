package pruebafx;

import Beans.BeanPersonas;
import ConexionBD.AccessDB;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class FXMLDocumentController implements Initializable {
    
    
    /*  Primero lo primero. TableView no es como JTable en java swing, ya que cada tabla 
        y columna son independientes. Cada TableView es un objeto de Java, como si fuese un arraylist.
        Para ello debemos decirle de qué tipo es. Si queremos agregar columnas, basta con decirle el tipo 
        de objeto que vamos a recibir, que es Personas(pero le puse BeanPersonas) y de qué tipo es, si es String,
        int, float, etc(Que son los parámetros que recibe del objeto que le indicamos)*/
    
    ResultSetMetaData metadata = null;
   
    // Les damos un fx:id a los componentes que se van a utilizar.
    @FXML private Button btnAgregar, btnEliminar, btnActualizar;
    @FXML private TextField txtID, txtNombre,txtApellidoP, txtApellidoM, txtEdad, txtDescripcion;
    @FXML private TableView<BeanPersonas> tabla; 
    @FXML private TableColumn<BeanPersonas,String> columnaNombre,
                                                   columnaApellidoP, columnaApellidoM, columnaDescripcion;
    @FXML private TableColumn<BeanPersonas,Integer> columnaEdad, columnaID;
    
    //Conectamos a la base de datos.
    private final AccessDB conectionbd;
    
    public FXMLDocumentController()
    {
        conectionbd = new AccessDB(); //Este puede ir en el initialize del programa.
    }
    
      /* Aquí se pone el código que queremos que se visualice cuando se ejecute la ventana. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        leerBD_LlenarTabla(); 
    }
    // La funcion_nombre_ se les pasa a cada botón en la opción "On Action" de la sección 'Code' en el JFXBuilder.
    @FXML
     private void funcionAgregar(ActionEvent event) {
      int confirmar;
      //Aqui creo variables para obtener los datos de cada textfield.
      String nombreP = String.valueOf(txtNombre.getText());
      String apellidoPat = String.valueOf(txtApellidoP.getText());
      String apellidoMat = String.valueOf(txtApellidoM.getText());
      String edadPersona = String.valueOf(txtEdad.getText());
      String desc = String.valueOf(txtDescripcion.getText());
      
      // Verificamos si no están los campos en blanco. Aquí no entra ID porque es autonumérico.
      if((!(nombreP.equals(""))) && (!(apellidoPat.equals(""))) && (!(apellidoMat.equals("")))
              && (!(edadPersona.equals(""))) && (!(desc.equals(""))))
      {
          confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente quiere ingresar a una persona?", "Confirmar agregar persona",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          
          if(confirmar == 0)
          { //Hará inserción (SI = 0; NO = 1);
              try
              {
                  BeanPersonas per = new BeanPersonas(); //Llamamos al bean.
                  // Insertamos los datos.
                  per.setNombre(nombreP);
                  per.setApellidoP(apellidoPat);
                  per.setApellidoM(apellidoMat);
                  per.setEdad(Integer.parseInt(edadPersona));
                  per.setDescripcion(desc);
                  if(per.insertarPersona() == true) //Si es un true, es porque pudimos realizar la inserción.
                  {
                      actualizarYrefrescar();
                      JOptionPane.showMessageDialog(null, "Agregado exitoso.");
                  } else
                  {
                      JOptionPane.showMessageDialog(null, "Error al agregar.");
                  }
              }catch(Exception ex)
              {
                 JOptionPane.showMessageDialog(null, "Error al ingresar una persona. Checar datos.");
              }
          }
      } else{
         JOptionPane.showMessageDialog(null, "Favor de completar los datos", "Error al agregar, datos incompletos", JOptionPane.PLAIN_MESSAGE);    
      }
    }
     
     @FXML //Agrégenlo al Action Code. No se les vaya a olvidar.
    private void funcionEliminar(ActionEvent e) {
        int confirmar;
        BeanPersonas p = tabla.getSelectionModel().getSelectedItem();
        if(p != null)
        {
            //A pesar de que el ID es autonumérico, necesitamos de el para poder hacer los cambios.
            // Si se dieron cuenta, el txtID no está habilitado para las personas, sino que solamente es una visualización.
            if(!(txtID.getText().equals("")))
            {
                confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente quiere eliminar a una persona?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if(confirmar == 0)
                {
                    int id = Integer.parseInt(txtID.getText());
                    BeanPersonas per = new BeanPersonas(); // Llamamos al bean.
                    
                    per.setID(id);
                       if (per.eliminarPersona(id)) {//si se pudo eliminar
                        actualizarYrefrescar();
                        JOptionPane.showMessageDialog(null, "Persona eliminada exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar. ");
                    }
                }
            } else if (txtID.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No se a seleccionado nada", "Eliminar", JOptionPane.PLAIN_MESSAGE);
            } 
        }else if (p == null) {
            JOptionPane.showMessageDialog(null, "No se a seleccionado nada", "Eliminar", JOptionPane.PLAIN_MESSAGE);
        }
        
    }
    
    @FXML //Lo mismo que los dos anteriores.
    private void funcionActualizar(ActionEvent e) {
        int confirmar;
        BeanPersonas persona = tabla.getSelectionModel().getSelectedItem();
        
      String IDPersona = String.valueOf(txtID.getText());
      String nombreP = String.valueOf(txtNombre.getText());
      String apellidoPat = String.valueOf(txtApellidoP.getText());
      String apellidoMat = String.valueOf(txtApellidoM.getText());
      String edadPersona = String.valueOf(txtEdad.getText());
      String desc = String.valueOf(txtDescripcion.getText());
      
      if(persona != null)
      {     //Aquí tomamos en cuenta el id para modificarlo cuando está seleccionado en la tabla.
           if((!(IDPersona.equals(""))) && (!(nombreP.equals(""))) && (!(apellidoPat.equals(""))) && (!(apellidoMat.equals("")))
              && (!(edadPersona.equals(""))) && (!(desc.equals(""))))
           {
               confirmar = JOptionPane.showConfirmDialog(null, "¿Realmente quiere modificar a una persona?", "Confirmar modificación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               
                   if(confirmar == 0)
                   {
                     try
                      {
                         
                     BeanPersonas per = new BeanPersonas();
                   
                         per.setID(Integer.parseInt(IDPersona));
                         per.setNombre(nombreP);
                         per.setApellidoP(apellidoPat);
                         per.setApellidoM(apellidoMat);
                         per.setEdad(Integer.parseInt(edadPersona));
                         per.setDescripcion(desc);
                  
                   if(per.actualizarPersona(Integer.parseInt(IDPersona)) == true)
                  {
                      JOptionPane.showMessageDialog(null, "Modificación exitosa.");
                      actualizarYrefrescar();
                  } else
                  {
                      JOptionPane.showMessageDialog(null, "Error al modificar.");
                  }
                   }catch(Exception ex){
                          JOptionPane.showMessageDialog(null, "Error al modificar a una persona. Checar datos.");
                 } 
               } 
           } else{
                JOptionPane.showMessageDialog(null, "Por favor termine de llenar los campos", "Error al modificar. ", JOptionPane.PLAIN_MESSAGE);
           }
      } else{
            JOptionPane.showMessageDialog(null, "No se a seleccionado nada", "Error al modificar.", JOptionPane.PLAIN_MESSAGE);
      }     
    }
    
    /* El rellenado de la tabla es para que al momento en el que se inicie esta ventana, se muestren
        los datos que están contenidos en access. */
    public void leerBD_LlenarTabla()
    {
        ResultSet rs;
        String sql= "SELECT Id, Nombre, ApellidoP, ApellidoM, Edad, Descripción"+
                    " FROM Personas";
         conectionbd.conectar(); //Nos conectamos.
         try{
             rs = conectionbd.ejecutarSQLSelect(sql); //Vean en la conexión a la base de datos.
             metadata = rs.getMetaData();
             int cols = metadata.getColumnCount(); //Este me trae el número de campos por tabla.
           while(rs.next())
           {
               Object[] fila = new Object[cols]; //Cada resultado es un object. Se guarda en un arreglo para rellenar la tabla.
               for (int i = 0; i < cols; i++) 
               {
                   if(rs.getObject(i+1) == null){ //En caso de que esté vacío alguno, no se muestra nada.
                       fila[i] = "";
                   } else{
                       fila[i] = rs.getObject(i+1); //Obtenemos cada uno de los datos.
                   }
               }
               
              int f1 = (int) fila[0];
              int f4 = (int) fila[4];
              String f5 = (String) fila[5];
               /*Llamamos al constructor para poder visualizar los datos en la tabla  
                y pasamos todos los datos al bean.*/
               BeanPersonas p = new BeanPersonas(f1,String.valueOf(fila[1]),
                       String.valueOf(fila[2]), String.valueOf(fila[3]),
                       f4,f5);
               
               //Colocamos los CellValueFactory.
               colocarDatosEnColumnas();
               tabla.getItems().add(p);
           }  
         }catch(SQLException ex)
         {
             System.out.println("Error al rellenar la tabla.");
         }
    }
    /* Este void nos ayuda a traer todo lo contenido en la base de datos de access.*/
    public void leerBD_Actualizar(ResultSet result)
    {
        ResultSet rs; //Todo va a ser solamente un resultset.
        String sql= "SELECT Id, Nombre, ApellidoP, ApellidoM, Edad, Descripción"+ //Aquí se modifica para ver qué campos quieres mostrar.
                    " FROM Personas";
         conectionbd.conectar(); //Nos conectamos.
         try{
             rs = conectionbd.ejecutarSQLSelect(sql); //Vean en la conexión a la base de datos.
             metadata = rs.getMetaData();
             int cols = metadata.getColumnCount(); //Este me trae el número de campos por tabla.
           while(rs.next())
           {
               int id = rs.getInt("Id");
               
               txtID.setText(String.valueOf(id));
               Object[] fila = new Object[cols]; //Cada resultado es un object. Se guarda en un arreglo para rellenar la tabla.
               for (int i = 0; i < cols; i++) 
               {
                   if(rs.getObject(i+1) == null){ //En caso de que esté vacío alguno, no se muestra nada.
                       fila[i] = "";
                   } else{
                       fila[i] = rs.getObject(i+1); //Obtenemos cada uno de los datos.
                   }
               }
               /* Para evitarme problemas por los tipos de datos, decidí ponerlo así. */
              int f1 = (int) fila[0];
              int f4 = (int) fila[4];
              String f5 = String.valueOf(fila[5]);
               /*Llamamos al constructor para poder visualizar los datos en la tabla  
                y pasamos todos los datos al bean.*/
               BeanPersonas p = new BeanPersonas(f1,String.valueOf(fila[1]),
                       String.valueOf(fila[2]), String.valueOf(fila[3]),
                       f4,f5);
               
               //Colocamos los CellValueFactory.
               colocarDatosEnColumnas();
               tabla.getItems().add(p); //Añadimos todos los datos a la tabla.
           }  
         }catch(SQLException ex)
         {
             System.out.println("Error al rellenar la tabla.");
         }
    }
    /* Manejo de evento cuando se le da clic a la tabla y rellene los txtField*/
    @FXML 
     private void tablaPersonasAction(MouseEvent e)
     {
         mostrarFilaEnCampos();
     }
    
    void colocarDatosEnColumnas()
    { /* El TableView utiliza el cellvaluefactory para saber qué valor tiene esa columna.*/
        columnaID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        columnaApellidoP.setCellValueFactory(new PropertyValueFactory<>("ApellidoP"));
        columnaApellidoM.setCellValueFactory(new PropertyValueFactory<>("ApellidoM"));
        columnaEdad.setCellValueFactory(new PropertyValueFactory<>("Edad"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripción"));
    }
    
    public void actualizarYrefrescar() //Esto solamente es para volver a llenar la tabla con los registros nuevos.
    {
        limpiarTabla();
        leerBD_LlenarTabla();
        limpiarCampos();
    }
    // el limpiarTabla puede llegar a generar errores. Les recomiendo que siempre sea un ciclo for y no otras cláusulas.
      public void limpiarTabla() {
         for (int i = 0; i < tabla.getItems().size(); i++) {
            tabla.getItems().clear();
        }
    }
      
    //A comparación de JavaSwing, aquí se utiliza el clear para limpiar y no un string en blanco " ".  
      public void limpiarCampos() 
      {
          txtID.clear();
          txtApellidoM.clear();
          txtApellidoP.clear();
          txtEdad.clear();
          txtNombre.clear();
          txtDescripcion.clear();
      }
    
      public void mostrarFilaEnCampos()
      { //Aquí se llama otra vez el bean para obtener los datos seleccionados de la tabla.
          BeanPersonas p = tabla.getSelectionModel().getSelectedItem();
          if(p == null) //Si no hay nada, no muestra nada.
          {
               limpiarCampos();
          }else{
                  String IDPersona = String.valueOf(p.getID());
                  String nombreP = p.getNombre();
                  String apellidoPat = p.getApellidoP();
                  String apellidoMat = p.getApellidoM();
                  String edadPersona = String.valueOf(p.getEdad());
                  String desc = p.getDescripcion();
                  
                  txtID.setText(IDPersona);
                  txtNombre.setText(nombreP);
                  txtApellidoP.setText(apellidoPat);
                  txtApellidoM.setText(apellidoMat);
                  txtEdad.setText(edadPersona);
                  txtDescripcion.setText(desc);
          }
      }
        
} //class
