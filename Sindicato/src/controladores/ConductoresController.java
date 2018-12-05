/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import ConexionAccess.ConexionAccess;
import Empleado.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import Modelo.Conductor;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConductoresController implements Initializable {
    
    ResultSetMetaData metadata = null;
    ResultSetMetaData metadataId = null;
    ResultSetMetaData metadataIdTel = null;
    ResultSetMetaData metadataIdBen = null;
    ManejadorEventos eventoFiltro;
    private ConexionAccess conexionBD;
    double[] dimension;
    DatePicker[] dpCampos;
    TextField[] txtCAMPOS;
    Label[] lblCamposTxt, lblCamposRest, lblCamposArea;
    TextArea[] txaCampos;
    private boolean filtroActivo;
    //ManejadorFiltroKey manejador;
    private Alert alert;
    int IDDomicilio, IDTelefono, IDBeneficiarios;
    int idDom, idTel, idBen;
    
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar, btnBuscar, btnLimpiar, btnBack;
    
    @FXML
    private Label lblId, lblApellidoPaterno, lblApellidoMaterno, lblNombres, 
                  lblLugarNacimiento, lblTelefono, lblCalle, lblNumExt, lblColonia,
                  lblCP, lblEstudios, lblNoIMSS, lblAfore, lblCURP,
                  lblRFC, lblClaveElector, lblIdLicencia,lblNoCuenta, lblBase, lblServicio;
    
    @FXML
    private Label lblFechaNacimiento, lblEstadoCivil, lblFechaIngreso, lblFechaSindicato, 
                  lblEstado, lblCiudad, lblFechaExpiracion, lblFechaExpedicion;
    
    @FXML
    private Label lblBeneficiarios, lblParentesco, lblPorcentaje;
    
    @FXML
    private TextField txtId, txtApellidoPaterno, txtApellidoMaterno, txtNombres, 
                      txtLugarNacimiento, txtTelefono, txtIdDomicilio,
                      txtColonia, txtCalle, txtNumExt, txtCP, 
                      txtEstudios, txtNoIMSS, txtAfore, txtCURP,
                      txtRFC, txtClaveElector, txtNoCuenta, txtBase, txtServicio, 
                      txtIdLicencia, txtIdTelefono, txtIdBeneficiarios, txtIdConductorBen;
    
    @FXML
    private TextArea txaBeneficiarios, txaParentesco, txaPorcentaje;
    
    private ToggleGroup tggGrupoEstadoCivil;
    
    @FXML
    private RadioButton rbtSoltero, rbtCasado, rbtUnionLibre;
    
    @FXML
    private ComboBox cbxEstado, cbxCiudad;
    
    @FXML 
    private GridPane gridPaneSindicato, gridPaneDatosGenerales, gridPaneLicencia;
    
    @FXML 
    private SplitPane spConductores;
    
    @FXML
    private DatePicker dpFechaNacimiento, dpFechaIngreso, dpFechaSindicato,
                       dpFechaExpiracion, dpFechaExpedicion;
    
    @FXML
    private ImageView foto; 
    
    @FXML
    private TableView<Conductor> tblDatosConductor, tblIdDomicilio, tblIdTelefono, tblIdBeneficiarios;
    
    @FXML
    private TableColumn<Conductor, String> tbcID, tbcNombres, tbcApellidoPaterno, 
            tbcApellidoMaterno, tbcIdLicencia, tbcFechaExpiracion, tbcFechaExpedicion, 
            tbcBase, tbcServicio, tbcNoCuenta, tbcIdDomicilio, tbcEstado, tbcCiudad, 
            tbcColonia, tbcCalle, tbcNumExt, tbcCp, tbcFechaNacimiento, tbcLugarNacimiento,
            tbcEstadoCivil, tbcTelefono, tbcFechaIngreso, tbcFechaSidicato, tbcEstudios,
            tbcNoIMSS, tbcAfore, tbcCURP, tbcRFC, tbcClaveElector, tbcIdDomicilio2, tbcIdTelefono2, 
            tbcIdBeneficiarios, tbcBeneficiarios, tbcParentesco, tbcPorcentaje, tbcIdBeneficiarios2;
    
    public ConductoresController() {
        conexionBD = new ConexionAccess();
    }
    
    private Empleado employee;
    
    public void setParameters(Empleado employee, ConexionAccess conexion) {
        this.employee = employee;
        this.conexionBD = conexion;
    }
    
    @FXML
    public void backDesk(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/Vista/FXMLDocument.fxml"));
                        loader.load();
                        FXMLDocumentController document = loader.getController();
                        document.setParameters(employee,conexionBD);
                        Parent p = loader.getRoot();
                        Scene scene = new Scene(p);
                        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                        s.setScene(scene);
                        s.setMaximized(true);
                        s.setResizable(true);
                        s.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dimension = new double[2];
        dimension = spConductores.getDividerPositions();
 
        filtroActivo = false;
        
        tggGrupoEstadoCivil = new ToggleGroup();
        rbtSoltero.setToggleGroup(tggGrupoEstadoCivil);
        rbtCasado.setToggleGroup(tggGrupoEstadoCivil);
        rbtUnionLibre.setToggleGroup(tggGrupoEstadoCivil);                
        
        txtCAMPOS = new TextField[]{txtId, txtApellidoPaterno, txtApellidoMaterno, txtNombres, 
                                    txtLugarNacimiento, txtTelefono, txtCalle, txtNumExt, txtColonia,
                                    txtCP, txtEstudios, txtNoIMSS, txtAfore, txtCURP,
                                    txtRFC, txtClaveElector, txtIdLicencia,txtNoCuenta, txtBase, txtServicio};
        
        lblCamposTxt = new Label[] {lblId, lblApellidoPaterno, lblApellidoMaterno, lblNombres, 
                                    lblLugarNacimiento, lblTelefono, lblCalle, lblNumExt, lblColonia,
                                    lblCP, lblEstudios, lblNoIMSS, lblAfore, lblCURP,
                                    lblRFC, lblClaveElector, lblIdLicencia,lblNoCuenta, lblBase, lblServicio};
        
        lblCamposRest = new Label[] {lblFechaNacimiento, lblFechaIngreso, lblFechaSindicato, 
                                     lblFechaExpiracion, lblFechaExpedicion};
        
        lblCamposArea = new Label[]{lblBeneficiarios, lblParentesco, lblPorcentaje};
        
        txaCampos = new TextArea[]{txaBeneficiarios, txaParentesco, txaPorcentaje};
        
        dpFechaNacimiento = new DatePicker();
        //dpFechaNacimiento.setValue(LocalDate.now());
        gridPaneDatosGenerales.add(dpFechaNacimiento, 1, 4);     
        
        dpFechaIngreso = new DatePicker();
        dpFechaSindicato = new DatePicker();
        
        dpFechaExpiracion = new DatePicker();
        dpFechaExpedicion = new DatePicker();
        
        dpCampos = new DatePicker[]{dpFechaNacimiento, dpFechaIngreso, dpFechaSindicato, 
                                    dpFechaExpiracion, dpFechaExpedicion};
        
        //dpFechaIngreso.setValue(LocalDate.now());
        //dpFechaSindicato.setValue(LocalDate.now());
        gridPaneSindicato.add(dpFechaIngreso, 1, 1);     
        gridPaneSindicato.add(dpFechaSindicato, 1, 2);
        
        gridPaneLicencia.add(dpFechaExpiracion, 1, 2);
        gridPaneLicencia.add(dpFechaExpedicion, 1, 3);

        cbxEstado.getItems().clear();
        cbxEstado.getItems().addAll("Aguascalientes","Baja California", "Baja California Sur",
                                    "Campeche", "Chiapas","Chihuahua", 
                                    "Ciudad de México","Coahuila", "Colima",
                                    "Durango","Guanajuato", "Guerrero", 
                                    "Hidalgo", "Jalisco","Estado de México", 
                                    "Michoacán", "Morelos", "Nayarit",
                                    "Nuevo León", "Oaxaca", "Puebla",
                                    "Querétaro", "Quintana Roo", "San Luis Potosí",
                                    "Sinaloa","Sonora", "Tabasco",
                                    "Tamaulipas", "Tlaxcala", "Veracruz",
                                    "Yucatán", "Zacatecas"
                                    );
        cbxCiudad.setDisable(true);
        btnLimpiar.setVisible(false);

        foto.setImage(new Image("FotosConductor/default-photo.png"));
        foto.setOpacity(0.65);        
        llenarTablaBD();
        eventoFiltro = new ManejadorEventos(); //Activamos el evento.   
        tblIdDomicilio.setVisible(false);
        tblIdTelefono.setVisible(false);
        txtIdTelefono.setVisible(false);
                
        if(tblDatosConductor.getItems().size() > 0) obtenerSizeTabla();
        else {
            txtIdDomicilio.setText("1");        
            IDDomicilio = 1;
            txtIdDomicilio.setDisable(true);
        }
        
        if(tblIdTelefono.getItems().size() > 0) obtenerSizeTablaTel();
        else {
            txtIdTelefono.setText("1");        
            IDTelefono = 1;
            txtIdTelefono.setDisable(true);
        }
        if(tblIdBeneficiarios.getItems().size() > 0) obtenerSizeTablaBen();
        else {
            txtIdBeneficiarios.setText("1");        
            IDBeneficiarios = 1;
            txtIdBeneficiarios.setDisable(true);
        }
    }
    
    public void obtenerSizeTabla() {
        // obtener el ultimo id de la columna idDomicilio
        int size = tblIdDomicilio.getItems().size() - 1;
        //int idDom  = tblIdDomicilio.getItems().get(size).getIdDomicilio() + 1;
        Object value = tblIdDomicilio.getColumns().get(0).getCellObservableValue(size).getValue();
        idDom = (int)value + 1;
        System.out.println("Valor ultimo IDDomicilio (siguiente valor): "+idDom);
        txtIdDomicilio.setText(String.valueOf(idDom));        
        IDDomicilio = idDom;
        txtIdDomicilio.setDisable(true);
        
    }
    
    public void obtenerSizeTablaTel() {
        // obtener el ultimo id de la columna idTelefono
        int sizePhones = tblIdTelefono.getItems().size() - 1;
        Object value2 = tblIdTelefono.getColumns().get(0).getCellObservableValue(sizePhones).getValue();
        idTel = (int)value2 + 1;
        System.out.println("Valor último IdTelefono (siguiente valor): "+idTel);
        IDTelefono = idTel;
        txtIdTelefono.setText(String.valueOf(idTel));
        txtIdTelefono.setDisable(true);
    }
    
    public void obtenerSizeTablaBen() {
        // obtener el ultimo id de la columna idTelefono
        int sizeBen = tblIdBeneficiarios.getItems().size() - 1;
        Object value3 = tblIdBeneficiarios.getColumns().get(0).getCellObservableValue(sizeBen).getValue();
        idBen = (int)value3 + 1;
        System.out.println("Valor último IdBeneficiarios (siguiente valor): "+idBen);
        IDBeneficiarios = idBen;
        txtIdBeneficiarios.setText(String.valueOf(idBen));
        txtIdBeneficiarios.setDisable(true);
        if(!txtId.getText().equals("")) txtIdConductorBen.setText(txtId.getText());
        txtIdConductorBen.setDisable(true);
    }

    public void cbxEstadoUpdated() {
        cbxCiudad.getItems().clear();
        cbxCiudad.setDisable(false);
        switch(this.cbxEstado.getValue().toString()) {
            case "Aguascalientes":
                cbxCiudad.getItems().addAll("Aguascalientes","Asientos","Calvillo",
                                            "Cosío", "El Llano", "Jesús María",
                                            "Pabellón de Arteaga", "Rincón de Romos","San Francisco de los Romo",
                                            "San José de Gracia","Tepezalá");
                break;
            case "Baja California":
                cbxCiudad.getItems().addAll("Ensenada","Mexicali","Playas de Rosarito",
                                            "Tecate", "Tijuana");
                break;
            case "Baja California Sur":
                cbxCiudad.getItems().addAll("Comondú","La Paz","Loreto",
                                            "Los cabos", "Mulegé");
                break;
            case "Campeche":
                cbxCiudad.getItems().addAll("Calakmul","Calkiní","Campeche",
                                            "Candelaria", "Carmen","Champotón",
                                            "Escárcega","Hecelchakán","Hopelchén",
                                            "Palizada","Tenabo");
                break;
            case "Chiapas":
                cbxCiudad.getItems().addAll("Acacoyagua",
                                            "Acala",
                                            "Acapetahua",
                                            "Aldama",
                                            "Altamirano",
                                            "Amatán",
                                            "Amatenango De La Frontera",
                                            "Amatenango Del Valle",
                                            "Angel Albino Corzo",
                                            "Arriaga",
                                            "Bejucal De Ocampo",
                                            "Bella Vista",
                                            "Benemérito De Las Américas",
                                            "Berriozábal",
                                            "Bochil",
                                            "Cacahoatán",
                                            "Catazajá",
                                            "Chalchihuitán",
                                            "Chamula",
                                            "Chanal",
                                            "Chapultenango",
                                            "Chenalhó",
                                            "Chiapa De Corzo",
                                            "Chiapilla",
                                            "Chicoasén",
                                            "Chicomuselo",
                                            "Chilón",
                                            "Cintalapa",
                                            "Coapilla",
                                            "Comitán De Domínguez",
                                            "Copainalá",
                                            "El Bosque",
                                            "El Porvenir",
                                            "Escuintla",
                                            "Francisco León",
                                            "Frontera Comalapa",
                                            "Frontera Hidalgo",
                                            "Huehuetán",
                                            "Huitiupán",
                                            "Huixtán",
                                            "Huixtla",
                                            "Ixhuatán",
                                            "Ixtacomitán",
                                            "Ixtapa",
                                            "Ixtapangajoya",
                                            "Jiquipilas",
                                            "Jitotol",
                                            "Juárez",
                                            "La Concordia",
                                            "La Grandeza",
                                            "La Independencia",
                                            "La Libertad",
                                            "La Trinitaria",
                                            "Larráinzar",
                                            "Las Margaritas",
                                            "Las Rosas",
                                            "Mapastepec",
                                            "Maravilla Tenejapa",
                                            "Marqués De Comillas",
                                            "Mazapa De Madero",
                                            "Mazatán",
                                            "Metapa",
                                            "Mitontic",
                                            "Montecristo De Guerrero",
                                            "Motozintla",
                                            "Nicolás Ruíz",
                                            "Ocosingo",
                                            "Ocotepec",
                                            "Ocozocoautla De Espinosa",
                                            "Ostuacán",
                                            "Osumacinta",
                                            "Oxchuc",
                                            "Palenque",
                                            "Pantelhó",
                                            "Pantepec",
                                            "Pichucalco",
                                            "Pijijiapan",
                                            "Pueblo Nuevo Solistahuacán",
                                            "Rayón",
                                            "Reforma",
                                            "Sabanilla",
                                            "Salto De Agua",
                                            "San Andrés Duraznal",
                                            "San Cristóbal De Las Casas",
                                            "San Fernando",
                                            "San Juan Cancuc",
                                            "San Lucas",
                                            "Santiago El Pinar",
                                            "Siltepec",
                                            "Simojovel",
                                            "Sitalá",
                                            "Socoltenango",
                                            "Solosuchiapa",
                                            "Soyaló",
                                            "Suchiapa",
                                            "Suchiate",
                                            "Sunuapa",
                                            "Tapachula",
                                            "Tapalapa",
                                            "Tapilula",
                                            "Tecpatán",
                                            "Tenejapa",
                                            "Teopisca",
                                            "Tila",
                                            "Tonalá",
                                            "Totolapa",
                                            "Tumbalá",
                                            "Tuxtla Chico",
                                            "Tuxtla Gutiérrez",
                                            "Tuzantán",
                                            "Tzimol",
                                            "Unión Juárez",
                                            "Venustiano Carranza",
                                            "Villa Comaltitlán",
                                            "Villa Corzo",
                                            "Villaflores",
                                            "Yajalón",
                                            "Zinacantán");

                break;
            case "Chihuahua":
                cbxCiudad.getItems().addAll("Ahumada",
                                            "Aldama",
                                            "Allende",
                                            "Aquiles Serdán",
                                            "Ascensión",
                                            "Bachíniva",
                                            "Balleza",
                                            "Batopilas",
                                            "Bocoyna",
                                            "Buenaventura",
                                            "Camargo",
                                            "Carichí",
                                            "Casas Grandes",
                                            "Chihuahua",
                                            "Chínipas",
                                            "Coronado",
                                            "Coyame Del Sotol",
                                            "Cuauhtémoc",
                                            "Cusihuiriachi",
                                            "Delicias",
                                            "Dr. Belisario Domínguez",
                                            "El Tule",
                                            "Galeana",
                                            "Gómez Farías",
                                            "Gran Morelos",
                                            "Guachochi",
                                            "Guadalupe Y Calvo",
                                            "Guadalupe",
                                            "Guazapares",
                                            "Guerrero",
                                            "Hidalgo Del Parral",
                                            "Huejotitán",
                                            "Ignacio Zaragoza",
                                            "Janos",
                                            "Jiménez",
                                            "Juárez",
                                            "Julimes",
                                            "La Cruz",
                                            "López",
                                            "Madera",
                                            "Maguarichi",
                                            "Manuel Benavides",
                                            "Matachí",
                                            "Matamoros",
                                            "Meoqui",
                                            "Morelos",
                                            "Moris",
                                            "Namiquipa",
                                            "Nonoava",
                                            "Nuevo Casas Grandes",
                                            "Ocampo",
                                            "Ojinaga",
                                            "Praxedis G. Guerrero",
                                            "Riva Palacio",
                                            "Rosales",
                                            "Rosario",
                                            "San Francisco De Borja",
                                            "San Francisco De Conchos",
                                            "San Francisco Del Oro",
                                            "Santa Bárbara",
                                            "Santa Isabel",
                                            "Satevó",
                                            "Saucillo",
                                            "Temósachic",
                                            "Urique",
                                            "Uruachi",
                                            "Valle De Zaragoza");

                break;
            case "Ciudad de México":
                cbxCiudad.getItems().addAll("Álvaro Obregón",
                                            "Azcapotzalco",
                                            "Benito Juárez",
                                            "Coyoacán",
                                            "Cuajimalpa De Morelos",
                                            "Cuauhtémoc",
                                            "Gustavo A. Madero",
                                            "Iztacalco",
                                            "Iztapalapa",
                                            "La Magdalena Contreras",
                                            "Miguel Hidalgo",
                                            "Milpa Alta",
                                            "Tláhuac",
                                            "Tlalpan",
                                            "Venustiano Carranza",
                                            "Xochimilco");

                break;
            case "Coahuila":
                cbxCiudad.getItems().addAll("Abasolo",
                                            "Acuña",
                                            "Allende",
                                            "Arteaga",
                                            "Candela",
                                            "Castaños",
                                            "Cuatro Ciénegas",
                                            "Escobedo",
                                            "Francisco I. Madero",
                                            "Frontera",
                                            "General Cepeda",
                                            "Guerrero",
                                            "Hidalgo",
                                            "Jiménez",
                                            "Juárez",
                                            "Lamadrid",
                                            "Matamoros",
                                            "Monclova",
                                            "Morelos",
                                            "Múzquiz",
                                            "Nadadores",
                                            "Nava",
                                            "Ocampo",
                                            "Parras",
                                            "Piedras Negras",
                                            "Progreso",
                                            "Ramos Arizpe",
                                            "Sabinas",
                                            "Sacramento",
                                            "Saltillo",
                                            "San Buenaventura",
                                            "San Juan De Sabinas",
                                            "San Pedro",
                                            "Sierra Mojada",
                                            "Torreón",
                                            "Viesca",
                                            "Villa Unión",
                                            "Zaragoza");

            break;
            
            case "Colima":
                cbxCiudad.getItems().addAll("Armería",
                                            "Canatlán",
                                            "Canelas",
                                            "Colima",
                                            "Comala",
                                            "Coneto De Comonfort",
                                            "Coquimatlán",
                                            "Cuauhtémoc",
                                            "Cuencamé",
                                            "Durango",
                                            "Ixtlahuacán",
                                            "Manzanillo",
                                            "Minatitlán",
                                            "Tecomán",
                                            "Villa De Álvarez");
                                            
                break;
                
            case "Durango":
                cbxCiudad.getItems().addAll("El Oro",
                                            "General Simón Bolívar",
                                            "Gómez Palacio",
                                            "Guadalupe Victoria",
                                            "Guanaceví",
                                            "Hidalgo",
                                            "Indé",
                                            "Lerdo",
                                            "Mapimí",
                                            "Mezquital",
                                            "Nazas",
                                            "Nombre De Dios",
                                            "Nuevo Ideal",
                                            "Ocampo",
                                            "Otáez",
                                            "Pánuco De Coronado",
                                            "Peñón Blanco",
                                            "Poanas",
                                            "Pueblo Nuevo",
                                            "Rodeo",
                                            "San Bernardo",
                                            "San Dimas",
                                            "San Juan De Guadalupe",
                                            "San Juan Del Río",
                                            "San Luis Del Cordero",
                                            "San Pedro Del Gallo",
                                            "Santa Clara",
                                            "Santiago Papasquiaro",
                                            "Súchil",
                                            "Tamazula",
                                            "Tepehuanes",
                                            "Tlahualilo",
                                            "Topia",
                                            "Vicente Guerrero");
                break;
                
            case "Guanajuato":
                cbxCiudad.getItems().addAll("Abasolo","Acámbaro","Apaseo el alto",
                                            "Apaseo el Grande","Atarjea","Celaya",
                                            "Comonfort","Coroneo","Cortazar",
                                            "Cuerámaro","Doctor Mora","Dolores Hidalgo",
                                            "Guanajuato","Huanímaro","Irapuato",
                                            "Jaral del Progreso","Jerécuaro","León",
                                            "Manuel Doblado","Moroleón","Ocampo",
                                            "Pénjamo","Pueblo Nuevo","Purísima del Rincón",
                                            "Romita","Salamanca","Salvatierra",
                                            "San Diego de la Unión","San Felipe","San Francisco del Rincón",
                                            "San José Iturbide","San Luis de la Paz","San Miguel de Allende",
                                            "Santa Catarina","Juventino Rosas","Santiago Maravatío",
                                            "Silao","Tarandacuao","Tarimoro",
                                            "Tierra Blanca","Uriangato","Valle de Santiago",
                                            "Victoria","Villagrán","Xichú","Yuriria");
                break;
                
            case "Guerrero":
                cbxCiudad.getItems().addAll("Acapulco De Juárez",
                                            "Acatepec",
                                            "Ahuacuotzingo",
                                            "Ajuchitlán Del Progreso",
                                            "Alcozauca De Guerrero",
                                            "Alpoyeca",
                                            "Apaxtla",
                                            "Arcelia",
                                            "Atenango Del Río",
                                            "Atlamajalcingo Del Monte",
                                            "Atlixtac",
                                            "Atoyac De Álvarez",
                                            "Ayutla De Los Libres",
                                            "Azoyú",
                                            "Benito Juárez",
                                            "Buenavista De Cuéllar",
                                            "Chilapa De Álvarez",
                                            "Chilpancingo De Los Bravo",
                                            "Coahuayutla De José María Izazaga",
                                            "Cochoapa El Grande",
                                            "Cocula",
                                            "Copala",
                                            "Copalillo",
                                            "Copanatoyac",
                                            "Coyuca De Benítez",
                                            "Coyuca De Catalán",
                                            "Cuajinicuilapa",
                                            "Cualác",
                                            "Cuautepec",
                                            "Cuetzala Del Progreso",
                                            "Cutzamala De Pinzón",
                                            "Eduardo Neri",
                                            "Florencio Villarreal",
                                            "General Canuto A. Neri",
                                            "General Heliodoro Castillo",
                                            "Huamuxtitlán",
                                            "Huitzuco De Los Figueroa",
                                            "Iguala De La Independencia",
                                            "Igualapa",
                                            "Iliatenco",
                                            "Ixcateopan De Cuauhtémoc",
                                            "José Joaquín De Herrera",
                                            "Juan R. Escudero",
                                            "Juchitán",
                                            "La Unión De Isidoro Montes De Oca",
                                            "Leonardo Bravo",
                                            "Malinaltepec",
                                            "Marquelia",
                                            "Mártir De Cuilapan",
                                            "Metlatónoc",
                                            "Mochitlán",
                                            "Olinalá",
                                            "Ometepec",
                                            "Pedro Ascencio Alquisiras",
                                            "Petatlán",
                                            "Pilcaya",
                                            "Pungarabato",
                                            "Quechultenango",
                                            "San Luis Acatlán",
                                            "San Marcos",
                                            "San Miguel Totolapan",
                                            "Taxco De Alarcón",
                                            "Tecoanapa",
                                            "Técpan De Galeana",
                                            "Teloloapan",
                                            "Tepecoacuilco De Trujano",
                                            "Tetipac",
                                            "Tixtla De Guerrero",
                                            "Tlacoachistlahuaca",
                                            "Tlacoapa",
                                            "Tlalchapa",
                                            "Tlalixtaquilla De Maldonado",
                                            "Tlapa De Comonfort",
                                            "Tlapehuala",
                                            "Xalpatláhuac",
                                            "Xochihuehuetlán",
                                            "Xochistlahuaca",
                                            "Zapotitlán Tablas",
                                            "Zihuatanejo De Azueta",
                                            "Zirándaro",
                                            "Zitlala");
                break;
                
            case "Hidalgo":
                cbxCiudad.getItems().addAll("Acatlán",
                                            "Acaxochitlán",
                                            "Actopan",
                                            "Agua Blanca De Iturbide",
                                            "Ajacuba",
                                            "Alfajayucan",
                                            "Almoloya",
                                            "Apan",
                                            "Atitalaquia",
                                            "Atlapexco",
                                            "Atotonilco De Tula",
                                            "Atotonilco El Grande",
                                            "Calnali",
                                            "Cardonal",
                                            "Chapantongo",
                                            "Chapulhuacán",
                                            "Chilcuautla",
                                            "Cuautepec De Hinojosa",
                                            "El Arenal",
                                            "Eloxochitlán",
                                            "Emiliano Zapata",
                                            "Epazoyucan",
                                            "Francisco I. Madero",
                                            "Huasca De Ocampo",
                                            "Huautla",
                                            "Huazalingo",
                                            "Huehuetla",
                                            "Huejutla De Reyes",
                                            "Huichapan",
                                            "Ixmiquilpan",
                                            "Jacala De Ledezma",
                                            "Jaltocán",
                                            "Juárez Hidalgo",
                                            "La Misión",
                                            "Lolotla",
                                            "Metepec",
                                            "Metztitlán",
                                            "Mineral De La Reforma",
                                            "Mineral Del Chico",
                                            "Mineral Del Monte",
                                            "Mixquiahuala De Juárez",
                                            "Molango De Escamilla",
                                            "Nicolás Flores",
                                            "Nopala De Villagrán",
                                            "Omitlán De Juárez",
                                            "Pachuca De Soto",
                                            "Pacula",
                                            "Pisaflores",
                                            "Progreso De Obregón",
                                            "San Agustín Metzquititlán",
                                            "San Agustín Tlaxiaca",
                                            "San Bartolo Tutotepec",
                                            "San Felipe Orizatlán",
                                            "San Salvador",
                                            "Santiago De Anaya",
                                            "Santiago Tulantepec De Lugo Guerrero",
                                            "Singuilucan",
                                            "Tasquillo",
                                            "Tecozautla",
                                            "Tenango De Doria",
                                            "Tepeapulco",
                                            "Tepehuacán De Guerrero",
                                            "Tepeji Del Río De Ocampo",
                                            "Tepetitlán",
                                            "Tetepango",
                                            "Tezontepec De Aldama",
                                            "Tianguistengo",
                                            "Tizayuca",
                                            "Tlahuelilpan",
                                            "Tlahuiltepa",
                                            "Tlanalapa",
                                            "Tlanchinol",
                                            "Tlaxcoapan",
                                            "Tolcayuca",
                                            "Tula De Allende",
                                            "Tulancingo De Bravo",
                                            "Villa De Tezontepec",
                                            "Xochiatipan",
                                            "Xochicoatlán",
                                            "Yahualica",
                                            "Zacualtipán De Ángeles",
                                            "Zapotlán De Juárez",
                                            "Zempoala",
                                            "Zimapán");
                break;
                
            case "Jalisco":
                cbxCiudad.getItems().addAll("Acatic",
                                            "Acatlán De Juárez",
                                            "Ahualulco De Mercado",
                                            "Amacueca",
                                            "Amatitán",
                                            "Ameca",
                                            "Arandas",
                                            "Atemajac De Brizuela",
                                            "Atengo",
                                            "Atenguillo",
                                            "Atotonilco El Alto",
                                            "Atoyac",
                                            "Autlán De Navarro",
                                            "Ayotlán",
                                            "Ayutla",
                                            "Bolaños",
                                            "Cabo Corrientes",
                                            "Cañadas De Obregón",
                                            "Casimiro Castillo",
                                            "Chapala",
                                            "Chimaltitán",
                                            "Chiquilistlán",
                                            "Cihuatlán",
                                            "Cocula",
                                            "Colotlán",
                                            "Concepción De Buenos Aires",
                                            "Cuautitlán De García Barragán",
                                            "Cuautla",
                                            "Cuquío",
                                            "Degollado",
                                            "Ejutla",
                                            "El Arenal",
                                            "El Grullo",
                                            "El Limón",
                                            "El Salto",
                                            "Encarnación De Díaz",
                                            "Etzatlán",
                                            "Gómez Farías",
                                            "Guachinango",
                                            "Guadalajara",
                                            "Hostotipaquillo",
                                            "Huejúcar",
                                            "Huejuquilla El Alto",
                                            "Ixtlahuacán De Los Membrillos",
                                            "Ixtlahuacán Del Río",
                                            "Jalostotitlán",
                                            "Jamay",
                                            "Jesús María",
                                            "Jilotlán De Los Dolores",
                                            "Jocotepec",
                                            "Juanacatlán",
                                            "Juchitlán",
                                            "La Barca",
                                            "La Huerta",
                                            "La Manzanilla De La Paz",
                                            "Lagos De Moreno",
                                            "Magdalena",
                                            "Mascota",
                                            "Mazamitla",
                                            "Mexticacán",
                                            "Mezquitic",
                                            "Mixtlán",
                                            "Ocotlán",
                                            "Ojuelos De Jalisco",
                                            "Pihuamo",
                                            "Poncitlán",
                                            "Puerto Vallarta",
                                            "Quitupan",
                                            "San Cristóbal De La Barranca",
                                            "San Diego De Alejandría",
                                            "San Gabriel",
                                            "San Ignacio Cerro Gordo",
                                            "San Juan De Los Lagos",
                                            "San Juanito De Escobedo",
                                            "San Julián",
                                            "San Marcos",
                                            "San Martín De Bolaños",
                                            "San Martín Hidalgo",
                                            "San Miguel El Alto",
                                            "San Pedro Tlaquepaque",
                                            "San Sebastián Del Oeste",
                                            "Santa María De Los Ángeles",
                                            "Santa María Del Oro",
                                            "Sayula",
                                            "Tala",
                                            "Talpa De Allende",
                                            "Tamazula De Gordiano",
                                            "Tapalpa",
                                            "Tecalitlán",
                                            "Techaluta De Montenegro",
                                            "Tecolotlán",
                                            "Tenamaxtlán",
                                            "Teocaltiche",
                                            "Teocuitatlán De Corona",
                                            "Tepatitlán De Morelos",
                                            "Tequila",
                                            "Teuchitlán",
                                            "Tizapán El Alto",
                                            "Tlajomulco De Zúñiga",
                                            "Tolimán",
                                            "Tomatlán",
                                            "Tonalá",
                                            "Tonaya",
                                            "Tonila",
                                            "Totatiche",
                                            "Tototlán",
                                            "Tuxcacuesco",
                                            "Tuxcueca",
                                            "Tuxpan",
                                            "Unión De San Antonio",
                                            "Unión De Tula",
                                            "Valle De Guadalupe",
                                            "Valle De Juárez",
                                            "Villa Corona",
                                            "Villa Guerrero",
                                            "Villa Hidalgo",
                                            "Villa Purificación",
                                            "Yahualica De González Gallo",
                                            "Zacoalco De Torres",
                                            "Zapopan",
                                            "Zapotiltic",
                                            "Zapotitlán De Vadillo",
                                            "Zapotlán Del Rey",
                                            "Zapotlán El Grande",
                                            "Zapotlanejo");

                break;
            case "Estado de México":
                cbxCiudad.getItems().addAll("Acambay De Ruíz Castañeda",
                                            "Acolman",
                                            "Aculco",
                                            "Almoloya De Alquisiras",
                                            "Almoloya De Juárez",
                                            "Almoloya Del Río",
                                            "Amanalco",
                                            "Amatepec",
                                            "Amecameca",
                                            "Apaxco",
                                            "Atenco",
                                            "Atizapán De Zaragoza",
                                            "Atizapán",
                                            "Atlacomulco",
                                            "Atlautla",
                                            "Axapusco",
                                            "Ayapango",
                                            "Calimaya",
                                            "Capulhuac",
                                            "Chalco",
                                            "Chapa De Mota",
                                            "Chapultepec",
                                            "Chiautla",
                                            "Chicoloapan",
                                            "Chiconcuac",
                                            "Chimalhuacán",
                                            "Coacalco De Berriozábal",
                                            "Coatepec Harinas",
                                            "Cocotitlán",
                                            "Coyotepec",
                                            "Cuautitlán Izcalli",
                                            "Cuautitlán",
                                            "Donato Guerra",
                                            "Ecatepec De Morelos",
                                            "Ecatzingo",
                                            "El Oro",
                                            "Huehuetoca",
                                            "Hueypoxtla",
                                            "Huixquilucan",
                                            "Isidro Fabela",
                                            "Ixtapaluca",
                                            "Ixtapan De La Sal",
                                            "Ixtapan Del Oro",
                                            "Ixtlahuaca",
                                            "Jaltenco",
                                            "Jilotepec",
                                            "Jilotzingo",
                                            "Jiquipilco",
                                            "Jocotitlán",
                                            "Joquicingo",
                                            "Juchitepec",
                                            "La Paz",
                                            "Lerma",
                                            "Luvianos",
                                            "Malinalco",
                                            "Melchor Ocampo",
                                            "Metepec",
                                            "Mexicaltzingo",
                                            "Morelos",
                                            "Naucalpan De Juárez",
                                            "Nextlalpan",
                                            "Nezahualcóyotl",
                                            "Nicolás Romero",
                                            "Nopaltepec",
                                            "Ocoyoacac",
                                            "Ocuilan",
                                            "Otumba",
                                            "Otzoloapan",
                                            "Otzolotepec",
                                            "Ozumba",
                                            "Papalotla",
                                            "Polotitlán",
                                            "Rayón",
                                            "San Antonio La Isla",
                                            "San Felipe Del Progreso",
                                            "San José Del Rincón",
                                            "San Martín De Las Pirámides",
                                            "San Mateo Atenco",
                                            "San Simón De Guerrero",
                                            "Santo Tomás",
                                            "Soyaniquilpan De Juárez",
                                            "Sultepec",
                                            "Tecámac",
                                            "Tejupilco",
                                            "Temamatla",
                                            "Temascalapa",
                                            "Temascalcingo",
                                            "Temascaltepec",
                                            "Temoaya",
                                            "Tenancingo",
                                            "Tenango Del Aire",
                                            "Tenango Del Valle",
                                            "Teoloyucan",
                                            "Teotihuacán",
                                            "Tepetlaoxtoc",
                                            "Tepetlixpa",
                                            "Tepotzotlán",
                                            "Tequixquiac",
                                            "Texcaltitlán",
                                            "Texcalyacac",
                                            "Texcoco",
                                            "Tezoyuca",
                                            "Tianguistenco",
                                            "Timilpan",
                                            "Tlalmanalco",
                                            "Tlalnepantla De Baz",
                                            "Tlatlaya",
                                            "Toluca",
                                            "Tonanitla",
                                            "Tonatico",
                                            "Tultepec",
                                            "Tultitlán",
                                            "Valle De Bravo",
                                            "Valle De Chalco Solidaridad",
                                            "Villa De Allende",
                                            "Villa Del Carbón",
                                            "Villa Guerrero",
                                            "Villa Victoria",
                                            "Xalatlaco",
                                            "Xonacatlán",
                                            "Zacazonapan",
                                            "Zacualpan",
                                            "Zinacantepec",
                                            "Zumpahuacán",
                                            "Zumpango");
                break;
                  
            case "Michoacán":
                cbxCiudad.getItems().addAll("Acuitzio",
                                            "Aguililla",
                                            "Álvaro Obregón",
                                            "Angamacutiro",
                                            "Angangueo",
                                            "Apatzingán",
                                            "Aporo",
                                            "Aquila",
                                            "Ario",
                                            "Arteaga",
                                            "Briseñas",
                                            "Buenavista",
                                            "Carácuaro",
                                            "Charapan",
                                            "Charo",
                                            "Chavinda",
                                            "Cherán",
                                            "Chilchota",
                                            "Chinicuila",
                                            "Chucándiro",
                                            "Churintzio",
                                            "Churumuco",
                                            "Coahuayana",
                                            "Coalcomán De Vázquez Pallares",
                                            "Coeneo",
                                            "Cojumatlán De Régules",
                                            "Contepec",
                                            "Copándaro",
                                            "Cotija",
                                            "Cuitzeo",
                                            "Ecuandureo",
                                            "Epitacio Huerta",
                                            "Erongarícuaro",
                                            "Gabriel Zamora",
                                            "Hidalgo",
                                            "Huandacareo",
                                            "Huaniqueo",
                                            "Huetamo",
                                            "Huiramba",
                                            "Indaparapeo",
                                            "Irimbo",
                                            "Ixtlán",
                                            "Jacona",
                                            "Jiménez",
                                            "Jiquilpan",
                                            "José Sixto Verduzco",
                                            "Juárez",
                                            "Jungapeo",
                                            "La Huacana",
                                            "La Piedad",
                                            "Lagunillas",
                                            "Lázaro Cárdenas",
                                            "Los Reyes",
                                            "Madero",
                                            "Maravatío",
                                            "Marcos Castellanos",
                                            "Morelia",
                                            "Morelos",
                                            "Múgica",
                                            "Nahuatzen",
                                            "Nocupétaro",
                                            "Nuevo Parangaricutiro",
                                            "Nuevo Urecho",
                                            "Numarán",
                                            "Ocampo",
                                            "Pajacuarán",
                                            "Panindícuaro",
                                            "Paracho",
                                            "Parácuaro",
                                            "Pátzcuaro",
                                            "Penjamillo",
                                            "Peribán",
                                            "Purépero",
                                            "Puruándiro",
                                            "Queréndaro",
                                            "Quiroga",
                                            "Sahuayo",
                                            "Salvador Escalante",
                                            "San Lucas",
                                            "Santa Ana Maya",
                                            "Senguio",
                                            "Susupuato",
                                            "Tacámbaro",
                                            "Tancítaro",
                                            "Tangamandapio",
                                            "Tangancícuaro",
                                            "Tanhuato",
                                            "Taretan",
                                            "Tarímbaro",
                                            "Tepalcatepec",
                                            "Tingambato",
                                            "Tingüindín",
                                            "Tiquicheo De Nicolás Romero",
                                            "Tlalpujahua",
                                            "Tlazazalca",
                                            "Tocumbo",
                                            "Tumbiscatío",
                                            "Turicato",
                                            "Tuxpan",
                                            "Tuzantla",
                                            "Tzintzuntzan",
                                            "Tzitzio",
                                            "Uruapan",
                                            "Venustiano Carranza",
                                            "Villamar",
                                            "Vista Hermosa",
                                            "Yurécuaro",
                                            "Zacapu",
                                            "Zamora",
                                            "Zináparo",
                                            "Zinapécuaro",
                                            "Ziracuaretiro",
                                            "Zitácuaro");
                break;
                
            case "Morelos":
                cbxCiudad.getItems().addAll("Amacuzac",
                                            "Atlatlahucan",
                                            "Axochiapan",
                                            "Ayala",
                                            "Coatlán Del Río",
                                            "Cuautla",
                                            "Cuernavaca",
                                            "Emiliano Zapata",
                                            "Huitzilac",
                                            "Jantetelco",
                                            "Jiutepec",
                                            "Jojutla",
                                            "Jonacatepec De Leandro Valle",
                                            "Mazatepec",
                                            "Miacatlán",
                                            "Ocuituco",
                                            "Puente De Ixtla",
                                            "Temixco",
                                            "Temoac",
                                            "Tepalcingo",
                                            "Tepoztlán",
                                            "Tetecala",
                                            "Tetela Del Volcán",
                                            "Tlalnepantla",
                                            "Tlaltizapán De Zapata",
                                            "Tlaquiltenango",
                                            "Tlayacapan",
                                            "Totolapan",
                                            "Xochitepec",
                                            "Yautepec",
                                            "Yecapixtla",
                                            "Zacatepec",
                                            "Zacualpan De Amilpas");
                break;
                
            case "Nayarit":
                cbxCiudad.getItems().addAll("Acaponeta",
                                            "Ahuacatlán",
                                            "Amatlán De Cañas",
                                            "Bahía De Banderas",
                                            "Compostela",
                                            "Del Nayar",
                                            "Huajicori",
                                            "Ixtlán Del Río",
                                            "Jala",
                                            "La Yesca",
                                            "Rosamorada",
                                            "Ruíz",
                                            "San Blas",
                                            "San Pedro Lagunillas",
                                            "Santa María Del Oro",
                                            "Santiago Ixcuintla",
                                            "Tecuala",
                                            "Tepic",
                                            "Tuxpan",
                                            "Xalisco");
                break;
                
            case "Nuevo León":
                cbxCiudad.getItems().addAll("Abasolo",
                                            "Agualeguas",
                                            "Allende",
                                            "Anáhuac",
                                            "Apodaca",
                                            "Aramberri",
                                            "Bustamante",
                                            "Cadereyta Jiménez",
                                            "Cerralvo",
                                            "China",
                                            "Ciénega De Flores",
                                            "Doctor Arroyo",
                                            "Doctor Coss",
                                            "Doctor González",
                                            "El Carmen",
                                            "Galeana",
                                            "García",
                                            "General Bravo",
                                            "General Escobedo",
                                            "General Terán",
                                            "General Treviño",
                                            "General Zaragoza",
                                            "General Zuazua",
                                            "Guadalupe",
                                            "Hidalgo",
                                            "Higueras",
                                            "Hualahuises",
                                            "Iturbide",
                                            "Juárez",
                                            "Lampazos De Naranjo",
                                            "Linares",
                                            "Los Aldamas",
                                            "Los Herreras",
                                            "Los Ramones",
                                            "Marín",
                                            "Melchor Ocampo",
                                            "Mier Y Noriega",
                                            "Mina",
                                            "Montemorelos",
                                            "Monterrey",
                                            "Parás",
                                            "Pesquería",
                                            "Rayones",
                                            "Sabinas Hidalgo",
                                            "Salinas Victoria",
                                            "San Nicolás De Los Garza",
                                            "San Pedro Garza García",
                                            "Santa Catarina",
                                            "Santiago",
                                            "Vallecillo",
                                            "Villaldama");
                break;
                
            case "Oaxaca":
                cbxCiudad.getItems().addAll("Abejones",
                                            "Acatlán De Pérez Figueroa",
                                            "Ánimas Trujano",
                                            "Asunción Cacalotepec",
                                            "Asunción Cuyotepeji",
                                            "Asunción Ixtaltepec",
                                            "Asunción Nochixtlán",
                                            "Asunción Ocotlán",
                                            "Asunción Tlacolulita",
                                            "Ayoquezco De Aldama",
                                            "Ayotzintepec",
                                            "Calihualá",
                                            "Candelaria Loxicha",
                                            "Capulálpam De Méndez",
                                            "Chahuites",
                                            "Chalcatongo De Hidalgo",
                                            "Chiquihuitlán De Benito Juárez",
                                            "Ciénega De Zimatlán",
                                            "Ciudad Ixtepec",
                                            "Coatecas Altas",
                                            "Coicoyán De Las Flores",
                                            "Concepción Buenavista",
                                            "Concepción Pápalo",
                                            "Constancia Del Rosario",
                                            "Cosolapa",
                                            "Cosoltepec",
                                            "Cuilápam De Guerrero",
                                            "Cuyamecalco Villa De Zaragoza",
                                            "El Barrio De La Soledad",
                                            "El Espinal",
                                            "Eloxochitlán De Flores Magón",
                                            "Fresnillo De Trujano",
                                            "Guadalupe De Ramírez",
                                            "Guadalupe Etla",
                                            "Guelatao De Juárez",
                                            "Guevea De Humboldt",
                                            "Heroica Ciudad De Ejutla De Crespo",
                                            "Heroica Ciudad De Huajuapan De León",
                                            "Heroica Ciudad De Juchitán De Zaragoza",
                                            "Heroica Ciudad De Tlaxiaco",
                                            "Huautepec",
                                            "Huautla De Jiménez",
                                            "Ixpantepec Nieves",
                                            "Ixtlán De Juárez",
                                            "La Compañía",
                                            "La Pe",
                                            "La Reforma",
                                            "La Trinidad Vista Hermosa",
                                            "Loma Bonita",
                                            "Magdalena Apasco",
                                            "Magdalena Jaltepec",
                                            "Magdalena Mixtepec",
                                            "Magdalena Ocotlán",
                                            "Magdalena Peñasco",
                                            "Magdalena Teitipac",
                                            "Magdalena Tequisistlán",
                                            "Magdalena Tlacotepec",
                                            "Magdalena Yodocono De Porfirio Díaz",
                                            "Magdalena Zahuatlán",
                                            "Mariscala De Juárez",
                                            "Mártires De Tacubaya",
                                            "Matías Romero Avendaño",
                                            "Mazatlán Villa De Flores",
                                            "Mesones Hidalgo",
                                            "Miahuatlán De Porfirio Díaz",
                                            "Mixistlán De La Reforma",
                                            "Monjas",
                                            "Natividad",
                                            "Nazareno Etla",
                                            "Nejapa De Madero",
                                            "Nuevo Zoquiápam",
                                            "Oaxaca De Juárez",
                                            "Ocotlán De Morelos",
                                            "Pinotepa De Don Luis",
                                            "Pluma Hidalgo",
                                            "Putla Villa De Guerrero",
                                            "Reforma De Pineda",
                                            "Reyes Etla",
                                            "Rojas De Cuauhtémoc",
                                            "Salina Cruz",
                                            "San Agustín Amatengo",
                                            "San Agustín Atenango",
                                            "San Agustín Chayuco",
                                            "San Agustín De Las Juntas",
                                            "San Agustín Etla",
                                            "San Agustín Loxicha",
                                            "San Agustín Tlacotepec",
                                            "San Agustín Yatareni",
                                            "San Andrés Cabecera Nueva",
                                            "San Andrés Dinicuiti",
                                            "San Andrés Huaxpaltepec",
                                            "San Andrés Huayápam",
                                            "San Andrés Ixtlahuaca",
                                            "San Andrés Lagunas",
                                            "San Andrés Nuxiño",
                                            "San Andrés Paxtlán",
                                            "San Andrés Sinaxtla",
                                            "San Andrés Solaga",
                                            "San Andrés Teotilálpam",
                                            "San Andrés Tepetlapa",
                                            "San Andrés Yaá",
                                            "San Andrés Zabache",
                                            "San Andrés Zautla",
                                            "San Antonino Castillo Velasco",
                                            "San Antonino El Alto",
                                            "San Antonino Monte Verde",
                                            "San Antonio Acutla",
                                            "San Antonio De La Cal",
                                            "San Antonio Huitepec",
                                            "San Antonio Nanahuatípam",
                                            "San Antonio Sinicahua",
                                            "San Antonio Tepetlapa",
                                            "San Baltazar Chichicápam",
                                            "San Baltazar Loxicha",
                                            "San Baltazar Yatzachi El Bajo",
                                            "San Bartolo Coyotepec",
                                            "San Bartolo Soyaltepec",
                                            "San Bartolo Yautepec",
                                            "San Bartolomé Ayautla",
                                            "San Bartolomé Loxicha",
                                            "San Bartolomé Quialana",
                                            "San Bartolomé Yucuañe",
                                            "San Bartolomé Zoogocho",
                                            "San Bernardo Mixtepec",
                                            "San Blas Atempa",
                                            "San Carlos Yautepec",
                                            "San Cristóbal Amatlán",
                                            "San Cristóbal Amoltepec",
                                            "San Cristóbal Lachirioag",
                                            "San Cristóbal Suchixtlahuaca",
                                            "San Dionisio Del Mar",
                                            "San Dionisio Ocotepec",
                                            "San Dionisio Ocotlán",
                                            "San Esteban Atatlahuca",
                                            "San Felipe Jalapa De Díaz",
                                            "San Felipe Tejalápam",
                                            "San Felipe Usila",
                                            "San Francisco Cahuacuá",
                                            "San Francisco Cajonos",
                                            "San Francisco Chapulapa",
                                            "San Francisco Chindúa",
                                            "San Francisco Del Mar",
                                            "San Francisco Huehuetlán",
                                            "San Francisco Ixhuatán",
                                            "San Francisco Jaltepetongo",
                                            "San Francisco Lachigoló",
                                            "San Francisco Logueche",
                                            "San Francisco Nuxaño",
                                            "San Francisco Ozolotepec",
                                            "San Francisco Sola",
                                            "San Francisco Telixtlahuaca",
                                            "San Francisco Teopan",
                                            "San Francisco Tlapancingo",
                                            "San Gabriel Mixtepec",
                                            "San Ildefonso Amatlán",
                                            "San Ildefonso Sola",
                                            "San Ildefonso Villa Alta",
                                            "San Jacinto Amilpas",
                                            "San Jacinto Tlacotepec",
                                            "San Jerónimo Coatlán",
                                            "San Jerónimo Silacayoapilla",
                                            "San Jerónimo Sosola",
                                            "San Jerónimo Taviche",
                                            "San Jerónimo Tecóatl",
                                            "San Jerónimo Tlacochahuaya",
                                            "San Jorge Nuchita",
                                            "San José Ayuquila",
                                            "San José Chiltepec",
                                            "San José Del Peñasco",
                                            "San José Del Progreso",
                                            "San José Estancia Grande",
                                            "San José Independencia",
                                            "San José Lachiguiri",
                                            "San José Tenango",
                                            "San Juan Achiutla",
                                            "San Juan Atepec",
                                            "San Juan Bautista Atatlahuca",
                                            "San Juan Bautista Coixtlahuaca",
                                            "San Juan Bautista Cuicatlán",
                                            "San Juan Bautista Guelache",
                                            "San Juan Bautista Jayacatlán",
                                            "San Juan Bautista Lo De Soto",
                                            "San Juan Bautista Suchitepec",
                                            "San Juan Bautista Tlachichilco",
                                            "San Juan Bautista Tlacoatzintepec",
                                            "San Juan Bautista Tuxtepec",
                                            "San Juan Bautista Valle Nacional",
                                            "San Juan Cacahuatepec",
                                            "San Juan Chicomezúchil",
                                            "San Juan Chilateca",
                                            "San Juan Cieneguilla",
                                            "San Juan Coatzóspam",
                                            "San Juan Colorado",
                                            "San Juan Comaltepec",
                                            "San Juan Cotzocón",
                                            "San Juan De Los Cués",
                                            "San Juan Del Estado",
                                            "San Juan Del Río",
                                            "San Juan Diuxi",
                                            "San Juan Evangelista Analco",
                                            "San Juan Guelavía",
                                            "San Juan Guichicovi",
                                            "San Juan Ihualtepec",
                                            "San Juan Juquila Mixes",
                                            "San Juan Juquila Vijanos",
                                            "San Juan Lachao",
                                            "San Juan Lachigalla",
                                            "San Juan Lajarcia",
                                            "San Juan Lalana",
                                            "San Juan Mazatlán",
                                            "San Juan Mixtepec",
                                            "San Juan Mixtepec",
                                            "San Juan Ñumí",
                                            "San Juan Ozolotepec",
                                            "San Juan Petlapa",
                                            "San Juan Quiahije",
                                            "San Juan Quiotepec",
                                            "San Juan Sayultepec",
                                            "San Juan Tabaá",
                                            "San Juan Tamazola",
                                            "San Juan Teita",
                                            "San Juan Teitipac",
                                            "San Juan Tepeuxila",
                                            "San Juan Teposcolula",
                                            "San Juan Yaeé",
                                            "San Juan Yatzona",
                                            "San Juan Yucuita",
                                            "San Lorenzo Albarradas",
                                            "San Lorenzo Cacaotepec",
                                            "San Lorenzo Cuaunecuiltitla",
                                            "San Lorenzo Texmelúcan",
                                            "San Lorenzo Victoria",
                                            "San Lorenzo",
                                            "San Lucas Camotlán",
                                            "San Lucas Ojitlán",
                                            "San Lucas Quiaviní",
                                            "San Lucas Zoquiápam",
                                            "San Luis Amatlán",
                                            "San Marcial Ozolotepec",
                                            "San Marcos Arteaga",
                                            "San Martín De Los Cansecos",
                                            "San Martín Huamelúlpam",
                                            "San Martín Itunyoso",
                                            "San Martín Lachilá",
                                            "San Martín Peras",
                                            "San Martín Tilcajete",
                                            "San Martín Toxpalan",
                                            "San Martín Zacatepec",
                                            "San Mateo Cajonos",
                                            "San Mateo Del Mar",
                                            "San Mateo Etlatongo",
                                            "San Mateo Nejápam",
                                            "San Mateo Peñasco",
                                            "San Mateo Piñas",
                                            "San Mateo Río Hondo",
                                            "San Mateo Sindihui",
                                            "San Mateo Tlapiltepec",
                                            "San Mateo Yoloxochitlán",
                                            "San Mateo Yucutindoo",
                                            "San Melchor Betaza",
                                            "San Miguel Achiutla",
                                            "San Miguel Ahuehuetitlán",
                                            "San Miguel Aloápam",
                                            "San Miguel Amatitlán",
                                            "San Miguel Amatlán",
                                            "San Miguel Chicahua",
                                            "San Miguel Chimalapa",
                                            "San Miguel Coatlán",
                                            "San Miguel Del Puerto",
                                            "San Miguel Del Río",
                                            "San Miguel Ejutla",
                                            "San Miguel El Grande",
                                            "San Miguel Huautla",
                                            "San Miguel Mixtepec",
                                            "San Miguel Panixtlahuaca",
                                            "San Miguel Peras",
                                            "San Miguel Piedras",
                                            "San Miguel Quetzaltepec",
                                            "San Miguel Santa Flor",
                                            "San Miguel Soyaltepec",
                                            "San Miguel Suchixtepec",
                                            "San Miguel Tecomatlán",
                                            "San Miguel Tenango",
                                            "San Miguel Tequixtepec",
                                            "San Miguel Tilquiápam",
                                            "San Miguel Tlacamama",
                                            "San Miguel Tlacotepec",
                                            "San Miguel Tulancingo",
                                            "San Miguel Yotao",
                                            "San Nicolás Hidalgo",
                                            "San Nicolás",
                                            "San Pablo Coatlán",
                                            "San Pablo Cuatro Venados",
                                            "San Pablo Etla",
                                            "San Pablo Huitzo",
                                            "San Pablo Huixtepec",
                                            "San Pablo Macuiltianguis",
                                            "San Pablo Tijaltepec",
                                            "San Pablo Villa De Mitla",
                                            "San Pablo Yaganiza",
                                            "San Pedro Amuzgos",
                                            "San Pedro Apóstol",
                                            "San Pedro Atoyac",
                                            "San Pedro Cajonos",
                                            "San Pedro Comitancillo",
                                            "San Pedro Coxcaltepec Cántaros",
                                            "San Pedro El Alto",
                                            "San Pedro Huamelula",
                                            "San Pedro Huilotepec",
                                            "San Pedro Ixcatlán",
                                            "San Pedro Ixtlahuaca",
                                            "San Pedro Jaltepetongo",
                                            "San Pedro Jicayán",
                                            "San Pedro Jocotipac",
                                            "San Pedro Juchatengo",
                                            "San Pedro Mártir Quiechapa",
                                            "San Pedro Mártir Yucuxaco",
                                            "San Pedro Mártir",
                                            "San Pedro Mixtepec",
                                            "San Pedro Mixtepec",
                                            "San Pedro Molinos",
                                            "San Pedro Nopala",
                                            "San Pedro Ocopetatillo",
                                            "San Pedro Ocotepec",
                                            "San Pedro Pochutla",
                                            "San Pedro Quiatoni",
                                            "San Pedro Sochiápam",
                                            "San Pedro Tapanatepec",
                                            "San Pedro Taviche",
                                            "San Pedro Teozacoalco",
                                            "San Pedro Teutila",
                                            "San Pedro Tidaá",
                                            "San Pedro Topiltepec",
                                            "San Pedro Totolápam",
                                            "San Pedro Y San Pablo Ayutla",
                                            "San Pedro Y San Pablo Teposcolula",
                                            "San Pedro Y San Pablo Tequixtepec",
                                            "San Pedro Yaneri",
                                            "San Pedro Yólox",
                                            "San Pedro Yucunama",
                                            "San Raymundo Jalpan",
                                            "San Sebastián Abasolo",
                                            "San Sebastián Coatlán",
                                            "San Sebastián Ixcapa",
                                            "San Sebastián Nicananduta",
                                            "San Sebastián Río Hondo",
                                            "San Sebastián Tecomaxtlahuaca",
                                            "San Sebastián Teitipac",
                                            "San Sebastián Tutla",
                                            "San Simón Almolongas",
                                            "San Simón Zahuatlán",
                                            "San Vicente Coatlán",
                                            "San Vicente Lachixío",
                                            "San Vicente Nuñú",
                                            "Santa Ana Ateixtlahuaca",
                                            "Santa Ana Cuauhtémoc",
                                            "Santa Ana Del Valle",
                                            "Santa Ana Tavela",
                                            "Santa Ana Tlapacoyan",
                                            "Santa Ana Yareni",
                                            "Santa Ana Zegache",
                                            "Santa Ana",
                                            "Santa Catalina Quierí",
                                            "Santa Catarina Cuixtla",
                                            "Santa Catarina Ixtepeji",
                                            "Santa Catarina Juquila",
                                            "Santa Catarina Lachatao",
                                            "Santa Catarina Loxicha",
                                            "Santa Catarina Mechoacán",
                                            "Santa Catarina Minas",
                                            "Santa Catarina Quiané",
                                            "Santa Catarina Quioquitani",
                                            "Santa Catarina Tayata",
                                            "Santa Catarina Ticuá",
                                            "Santa Catarina Yosonotú",
                                            "Santa Catarina Zapoquila",
                                            "Santa Cruz Acatepec",
                                            "Santa Cruz Amilpas",
                                            "Santa Cruz De Bravo",
                                            "Santa Cruz Itundujia",
                                            "Santa Cruz Mixtepec",
                                            "Santa Cruz Nundaco",
                                            "Santa Cruz Papalutla",
                                            "Santa Cruz Tacache De Mina",
                                            "Santa Cruz Tacahua",
                                            "Santa Cruz Tayata",
                                            "Santa Cruz Xitla",
                                            "Santa Cruz Xoxocotlán",
                                            "Santa Cruz Zenzontepec",
                                            "Santa Gertrudis",
                                            "Santa Inés De Zaragoza",
                                            "Santa Inés Del Monte",
                                            "Santa Inés Yatzeche",
                                            "Santa Lucía Del Camino",
                                            "Santa Lucía Miahuatlán",
                                            "Santa Lucía Monteverde",
                                            "Santa Lucía Ocotlán",
                                            "Santa Magdalena Jicotlán",
                                            "Santa María Alotepec",
                                            "Santa María Apazco",
                                            "Santa María Atzompa",
                                            "Santa María Camotlán",
                                            "Santa María Chachoápam",
                                            "Santa María Chilchotla",
                                            "Santa María Chimalapa",
                                            "Santa María Colotepec",
                                            "Santa María Cortijo",
                                            "Santa María Coyotepec",
                                            "Santa María Del Rosario",
                                            "Santa María Del Tule",
                                            "Santa María Ecatepec",
                                            "Santa María Guelacé",
                                            "Santa María Guienagati",
                                            "Santa María Huatulco",
                                            "Santa María Huazolotitlán",
                                            "Santa María Ipalapa",
                                            "Santa María Ixcatlán",
                                            "Santa María Jacatepec",
                                            "Santa María Jalapa Del Marqués",
                                            "Santa María Jaltianguis",
                                            "Santa María La Asunción",
                                            "Santa María Lachixío",
                                            "Santa María Mixtequilla",
                                            "Santa María Nativitas",
                                            "Santa María Nduayaco",
                                            "Santa María Ozolotepec",
                                            "Santa María Pápalo",
                                            "Santa María Peñoles",
                                            "Santa María Petapa",
                                            "Santa María Quiegolani",
                                            "Santa María Sola",
                                            "Santa María Tataltepec",
                                            "Santa María Tecomavaca",
                                            "Santa María Temaxcalapa",
                                            "Santa María Temaxcaltepec",
                                            "Santa María Teopoxco",
                                            "Santa María Tepantlali",
                                            "Santa María Texcatitlán",
                                            "Santa María Tlahuitoltepec",
                                            "Santa María Tlalixtac",
                                            "Santa María Tonameca",
                                            "Santa María Totolapilla",
                                            "Santa María Xadani",
                                            "Santa María Yalina",
                                            "Santa María Yavesía",
                                            "Santa María Yolotepec",
                                            "Santa María Yosoyúa",
                                            "Santa María Yucuhiti",
                                            "Santa María Zacatepec",
                                            "Santa María Zaniza",
                                            "Santa María Zoquitlán",
                                            "Santiago Amoltepec",
                                            "Santiago Apoala",
                                            "Santiago Apóstol",
                                            "Santiago Astata",
                                            "Santiago Atitlán",
                                            "Santiago Ayuquililla",
                                            "Santiago Cacaloxtepec",
                                            "Santiago Camotlán",
                                            "Santiago Chazumba",
                                            "Santiago Choápam",
                                            "Santiago Comaltepec",
                                            "Santiago Del Río",
                                            "Santiago Huajolotitlán",
                                            "Santiago Huauclilla",
                                            "Santiago Ihuitlán Plumas",
                                            "Santiago Ixcuintepec",
                                            "Santiago Ixtayutla",
                                            "Santiago Jamiltepec",
                                            "Santiago Jocotepec",
                                            "Santiago Juxtlahuaca",
                                            "Santiago Lachiguiri",
                                            "Santiago Lalopa",
                                            "Santiago Laollaga",
                                            "Santiago Laxopa",
                                            "Santiago Llano Grande",
                                            "Santiago Matatlán",
                                            "Santiago Miltepec",
                                            "Santiago Minas",
                                            "Santiago Nacaltepec",
                                            "Santiago Nejapilla",
                                            "Santiago Niltepec",
                                            "Santiago Nundiche",
                                            "Santiago Nuyoó",
                                            "Santiago Pinotepa Nacional",
                                            "Santiago Suchilquitongo",
                                            "Santiago Tamazola",
                                            "Santiago Tapextla",
                                            "Santiago Tenango",
                                            "Santiago Tepetlapa",
                                            "Santiago Tetepec",
                                            "Santiago Texcalcingo",
                                            "Santiago Textitlán",
                                            "Santiago Tilantongo",
                                            "Santiago Tillo",
                                            "Santiago Tlazoyaltepec",
                                            "Santiago Xanica",
                                            "Santiago Xiacuí",
                                            "Santiago Yaitepec",
                                            "Santiago Yaveo",
                                            "Santiago Yolomécatl",
                                            "Santiago Yosondúa",
                                            "Santiago Yucuyachi",
                                            "Santiago Zacatepec",
                                            "Santiago Zoochila",
                                            "Santo Domingo Albarradas",
                                            "Santo Domingo Armenta",
                                            "Santo Domingo Chihuitán",
                                            "Santo Domingo De Morelos",
                                            "Santo Domingo Ingenio",
                                            "Santo Domingo Ixcatlán",
                                            "Santo Domingo Nuxaá",
                                            "Santo Domingo Ozolotepec",
                                            "Santo Domingo Petapa",
                                            "Santo Domingo Roayaga",
                                            "Santo Domingo Tehuantepec",
                                            "Santo Domingo Teojomulco",
                                            "Santo Domingo Tepuxtepec",
                                            "Santo Domingo Tlatayápam",
                                            "Santo Domingo Tomaltepec",
                                            "Santo Domingo Tonalá",
                                            "Santo Domingo Tonaltepec",
                                            "Santo Domingo Xagacía",
                                            "Santo Domingo Yanhuitlán",
                                            "Santo Domingo Yodohino",
                                            "Santo Domingo Zanatepec",
                                            "Santo Tomás Jalieza",
                                            "Santo Tomás Mazaltepec",
                                            "Santo Tomás Ocotepec",
                                            "Santo Tomás Tamazulapan",
                                            "Santos Reyes Nopala",
                                            "Santos Reyes Pápalo",
                                            "Santos Reyes Tepejillo",
                                            "Santos Reyes Yucuná",
                                            "Silacayoápam",
                                            "Sitio De Xitlapehua",
                                            "Soledad Etla",
                                            "Tamazulápam Del Espíritu Santo",
                                            "Tanetze De Zaragoza",
                                            "Taniche",
                                            "Tataltepec De Valdés",
                                            "Teococuilco De Marcos Pérez",
                                            "Teotitlán De Flores Magón",
                                            "Teotitlán Del Valle",
                                            "Teotongo",
                                            "Tepelmeme Villa De Morelos",
                                            "Tlacolula De Matamoros",
                                            "Tlacotepec Plumas",
                                            "Tlalixtac De Cabrera",
                                            "Totontepec Villa De Morelos",
                                            "Trinidad Zaachila",
                                            "Unión Hidalgo",
                                            "Valerio Trujano",
                                            "Villa De Chilapa De Díaz",
                                            "Villa De Etla",
                                            "Villa De Tamazulápam Del Progreso",
                                            "Villa De Tututepec",
                                            "Villa De Zaachila",
                                            "Villa Díaz Ordaz",
                                            "Villa Hidalgo",
                                            "Villa Sola De Vega",
                                            "Villa Talea De Castro",
                                            "Villa Tejúpam De La Unión",
                                            "Villa Tezoatlán De Segura Y Luna",
                                            "Yaxe",
                                            "Yogana",
                                            "Yutanduchi De Guerrero",
                                            "Zapotitlán Lagunas",
                                            "Zapotitlán Palmas",
                                            "Zimatlán De Álvarez");
                break;
                
            case "Puebla":
                cbxCiudad.getItems().addAll("Acajete",
                                            "Acateno",
                                            "Acatlán",
                                            "Acatzingo",
                                            "Acteopan",
                                            "Ahuacatlán",
                                            "Ahuatlán",
                                            "Ahuazotepec",
                                            "Ahuehuetitla",
                                            "Ajalpan",
                                            "Albino Zertuche",
                                            "Aljojuca",
                                            "Altepexi",
                                            "Amixtlán",
                                            "Amozoc",
                                            "Aquixtla",
                                            "Atempan",
                                            "Atexcal",
                                            "Atlequizayan",
                                            "Atlixco",
                                            "Atoyatempan",
                                            "Atzala",
                                            "Atzitzihuacán",
                                            "Atzitzintla",
                                            "Axutla",
                                            "Ayotoxco De Guerrero",
                                            "Calpan",
                                            "Caltepec",
                                            "Camocuautla",
                                            "Cañada Morelos",
                                            "Caxhuacan",
                                            "Chalchicomula De Sesma",
                                            "Chapulco",
                                            "Chiautla",
                                            "Chiautzingo",
                                            "Chichiquila",
                                            "Chiconcuautla",
                                            "Chietla",
                                            "Chigmecatitlán",
                                            "Chignahuapan",
                                            "Chignautla",
                                            "Chila De La Sal",
                                            "Chila",
                                            "Chilchotla",
                                            "Chinantla",
                                            "Coatepec",
                                            "Coatzingo",
                                            "Cohetzala",
                                            "Cohuecan",
                                            "Coronango",
                                            "Coxcatlán",
                                            "Coyomeapan",
                                            "Coyotepec",
                                            "Cuapiaxtla De Madero",
                                            "Cuautempan",
                                            "Cuautinchán",
                                            "Cuautlancingo",
                                            "Cuayuca De Andrade",
                                            "Cuetzalan Del Progreso",
                                            "Cuyoaco",
                                            "Domingo Arenas",
                                            "Eloxochitlán",
                                            "Epatlán",
                                            "Esperanza",
                                            "Francisco Z. Mena",
                                            "General Felipe Ángeles",
                                            "Guadalupe Victoria",
                                            "Guadalupe",
                                            "Hermenegildo Galeana",
                                            "Honey",
                                            "Huaquechula",
                                            "Huatlatlauca",
                                            "Huauchinango",
                                            "Huehuetla",
                                            "Huehuetlán El Chico",
                                            "Huehuetlán El Grande",
                                            "Huejotzingo",
                                            "Hueyapan",
                                            "Hueytamalco",
                                            "Hueytlalpan",
                                            "Huitzilan De Serdán",
                                            "Huitziltepec",
                                            "Ixcamilpa De Guerrero",
                                            "Ixcaquixtla",
                                            "Ixtacamaxtitlán",
                                            "Ixtepec",
                                            "Izúcar De Matamoros",
                                            "Jalpan",
                                            "Jolalpan",
                                            "Jonotla",
                                            "Jopala",
                                            "Juan C. Bonilla",
                                            "Juan Galindo",
                                            "Juan N. Méndez",
                                            "La Magdalena Tlatlauquitepec",
                                            "Lafragua",
                                            "Libres",
                                            "Los Reyes De Juárez",
                                            "Mazapiltepec De Juárez",
                                            "Mixtla",
                                            "Molcaxac",
                                            "Naupan",
                                            "Nauzontla",
                                            "Nealtican",
                                            "Nicolás Bravo",
                                            "Nopalucan",
                                            "Ocotepec",
                                            "Ocoyucan",
                                            "Olintla",
                                            "Oriental",
                                            "Pahuatlán",
                                            "Palmar De Bravo",
                                            "Pantepec",
                                            "Petlalcingo",
                                            "Piaxtla",
                                            "Puebla",
                                            "Quecholac",
                                            "Quimixtlán",
                                            "Rafael Lara Grajales",
                                            "San Andrés Cholula",
                                            "San Antonio Cañada",
                                            "San Diego La Mesa Tochimiltzingo",
                                            "San Felipe Teotlalcingo",
                                            "San Felipe Tepatlán",
                                            "San Gabriel Chilac",
                                            "San Gregorio Atzompa",
                                            "San Jerónimo Tecuanipan",
                                            "San Jerónimo Xayacatlán",
                                            "San José Chiapa",
                                            "San José Miahuatlán",
                                            "San Juan Atenco",
                                            "San Juan Atzompa",
                                            "San Martín Texmelucan",
                                            "San Martín Totoltepec",
                                            "San Matías Tlalancaleca",
                                            "San Miguel Ixitlán",
                                            "San Miguel Xoxtla",
                                            "San Nicolás Buenos Aires",
                                            "San Nicolás De Los Ranchos",
                                            "San Pablo Anicano",
                                            "San Pedro Cholula",
                                            "San Pedro Yeloixtlahuaca",
                                            "San Salvador El Seco",
                                            "San Salvador El Verde",
                                            "San Salvador Huixcolotla",
                                            "San Sebastián Tlacotepec",
                                            "Santa Catarina Tlaltempan",
                                            "Santa Inés Ahuatempan",
                                            "Santa Isabel Cholula",
                                            "Santiago Miahuatlán",
                                            "Santo Tomás Hueyotlipan",
                                            "Soltepec",
                                            "Tecali De Herrera",
                                            "Tecamachalco",
                                            "Tecomatlán",
                                            "Tehuacán",
                                            "Tehuitzingo",
                                            "Tenampulco",
                                            "Teopantlán",
                                            "Teotlalco",
                                            "Tepanco De López",
                                            "Tepango De Rodríguez",
                                            "Tepatlaxco De Hidalgo",
                                            "Tepeaca",
                                            "Tepemaxalco",
                                            "Tepeojuma",
                                            "Tepetzintla",
                                            "Tepexco",
                                            "Tepexi De Rodríguez",
                                            "Tepeyahualco De Cuauhtémoc",
                                            "Tepeyahualco",
                                            "Tetela De Ocampo",
                                            "Teteles De Avila Castillo",
                                            "Teziutlán",
                                            "Tianguismanalco",
                                            "Tilapa",
                                            "Tlachichuca",
                                            "Tlacotepec De Benito Juárez",
                                            "Tlacuilotepec",
                                            "Tlahuapan",
                                            "Tlaltenango",
                                            "Tlanepantla",
                                            "Tlaola",
                                            "Tlapacoya",
                                            "Tlapanalá",
                                            "Tlatlauquitepec",
                                            "Tlaxco",
                                            "Tochimilco",
                                            "Tochtepec",
                                            "Totoltepec De Guerrero",
                                            "Tulcingo",
                                            "Tuzamapan De Galeana",
                                            "Tzicatlacoyan",
                                            "Venustiano Carranza",
                                            "Vicente Guerrero",
                                            "Xayacatlán De Bravo",
                                            "Xicotepec",
                                            "Xicotlán",
                                            "Xiutetelco",
                                            "Xochiapulco",
                                            "Xochiltepec",
                                            "Xochitlán De Vicente Suárez",
                                            "Xochitlán Todos Santos",
                                            "Yaonáhuac",
                                            "Yehualtepec",
                                            "Zacapala",
                                            "Zacapoaxtla",
                                            "Zacatlán",
                                            "Zapotitlán De Méndez",
                                            "Zapotitlán",
                                            "Zaragoza",
                                            "Zautla",
                                            "Zihuateutla",
                                            "Zinacatepec",
                                            "Zongozotla",
                                            "Zoquiapan",
                                            "Zoquitlán");
                break;
            
            case "Querétaro":
                cbxCiudad.getItems().addAll("Amealco De Bonfil",
                                            "Arroyo Seco",
                                            "Cadereyta De Montes",
                                            "Colón",
                                            "Corregidora",
                                            "El Marqués",
                                            "Ezequiel Montes",
                                            "Huimilpan",
                                            "Jalpan De Serra",
                                            "Landa De Matamoros",
                                            "Pedro Escobedo",
                                            "Peñamiller",
                                            "Pinal De Amoles",
                                            "Querétaro",
                                            "San Joaquín",
                                            "San Juan Del Río",
                                            "Tequisquiapan",
                                            "Tolimán");
                break;
                
            case "Quintana Roo":
                cbxCiudad.getItems().addAll("Bacalar",
                                            "Benito Juárez",
                                            "Cozumel",
                                            "Felipe Carrillo Puerto",
                                            "Isla Mujeres",
                                            "José María Morelos",
                                            "Lázaro Cárdenas",
                                            "Othón P. Blanco",
                                            "Puerto Morelos",
                                            "Solidaridad",
                                            "Tulum");
                break;
                
            case "San Luis Potosí":
                cbxCiudad.getItems().addAll("Ahualulco",
                                            "Alaquines",
                                            "Aquismón",
                                            "Armadillo De Los Infante",
                                            "Axtla De Terrazas",
                                            "Cárdenas",
                                            "Catorce",
                                            "Cedral",
                                            "Cerritos",
                                            "Cerro De San Pedro",
                                            "Charcas",
                                            "Ciudad Del Maíz",
                                            "Ciudad Fernández",
                                            "Ciudad Valles",
                                            "Coxcatlán",
                                            "Ebano",
                                            "El Naranjo",
                                            "Guadalcázar",
                                            "Huehuetlán",
                                            "Lagunillas",
                                            "Matehuala",
                                            "Matlapa",
                                            "Mexquitic De Carmona",
                                            "Moctezuma",
                                            "Rayón",
                                            "Rioverde",
                                            "Salinas",
                                            "San Antonio",
                                            "San Ciro De Acosta",
                                            "San Luis Potosí",
                                            "San Martín Chalchicuautla",
                                            "San Nicolás Tolentino",
                                            "San Vicente Tancuayalab",
                                            "Santa Catarina",
                                            "Santa María Del Río",
                                            "Santo Domingo",
                                            "Soledad De Graciano Sánchez",
                                            "Tamasopo",
                                            "Tamazunchale",
                                            "Tampacán",
                                            "Tampamolón Corona",
                                            "Tamuín",
                                            "Tancanhuitz",
                                            "Tanlajás",
                                            "Tanquián De Escobedo",
                                            "Tierra Nueva",
                                            "Vanegas",
                                            "Venado",
                                            "Villa De Arista",
                                            "Villa De Arriaga",
                                            "Villa De Guadalupe",
                                            "Villa De La Paz",
                                            "Villa De Ramos",
                                            "Villa De Reyes",
                                            "Villa Hidalgo",
                                            "Villa Juárez",
                                            "Xilitla",
                                            "Zaragoza");
                break;
                
            case "Sinaloa":
                cbxCiudad.getItems().addAll("Ahome",
                                            "Angostura",
                                            "Badiraguato",
                                            "Choix",
                                            "Concordia",
                                            "Cosalá",
                                            "Culiacán",
                                            "El Fuerte",
                                            "Elota",
                                            "Escuinapa",
                                            "Guasave",
                                            "Mazatlán",
                                            "Mocorito",
                                            "Navolato",
                                            "Rosario",
                                            "Salvador Alvarado",
                                            "San Ignacio",
                                            "Sinaloa");
                break;
                
            case "Sonora":
                cbxCiudad.getItems().addAll("Aconchi",
                                            "Agua Prieta",
                                            "Alamos",
                                            "Altar",
                                            "Arivechi",
                                            "Arizpe",
                                            "Atil",
                                            "Bacadéhuachi",
                                            "Bacanora",
                                            "Bacerac",
                                            "Bacoachi",
                                            "Bácum",
                                            "Banámichi",
                                            "Baviácora",
                                            "Bavispe",
                                            "Benito Juárez",
                                            "Benjamín Hill",
                                            "Caborca",
                                            "Cajeme",
                                            "Cananea",
                                            "Carbó",
                                            "Cucurpe",
                                            "Cumpas",
                                            "Divisaderos",
                                            "Empalme",
                                            "Etchojoa",
                                            "Fronteras",
                                            "General Plutarco Elías Calles",
                                            "Granados",
                                            "Guaymas",
                                            "Hermosillo",
                                            "Huachinera",
                                            "Huásabas",
                                            "Huatabampo",
                                            "Huépac",
                                            "Imuris",
                                            "La Colorada",
                                            "Magdalena",
                                            "Mazatán",
                                            "Moctezuma",
                                            "Naco",
                                            "Nácori Chico",
                                            "Nacozari De García",
                                            "Navojoa",
                                            "Nogales",
                                            "Onavas",
                                            "Opodepe",
                                            "Oquitoa",
                                            "Pitiquito",
                                            "Puerto Peñasco",
                                            "Quiriego",
                                            "Rayón",
                                            "Rosario",
                                            "Sahuaripa",
                                            "San Felipe De Jesús",
                                            "San Ignacio Río Muerto",
                                            "San Javier",
                                            "San Luis Río Colorado",
                                            "San Miguel De Horcasitas",
                                            "San Pedro De La Cueva",
                                            "Santa Ana",
                                            "Santa Cruz",
                                            "Sáric",
                                            "Soyopa",
                                            "Suaqui Grande",
                                            "Tepache",
                                            "Trincheras",
                                            "Tubutama",
                                            "Ures",
                                            "Villa Hidalgo",
                                            "Villa Pesqueira",
                                            "Yécora");
                break;
                
            case "Tabasco":
                cbxCiudad.getItems().addAll("Balancán",
                                            "Cárdenas",
                                            "Centla",
                                            "Centro",
                                            "Comalcalco",
                                            "Cunduacán",
                                            "Emiliano Zapata",
                                            "Huimanguillo",
                                            "Jalapa",
                                            "Jalpa De Méndez",
                                            "Jonuta",
                                            "Macuspana",
                                            "Nacajuca",
                                            "Paraíso",
                                            "Tacotalpa",
                                            "Teapa",
                                            "Tenosique");
                break;
                
            case "Tamaulipas":
                cbxCiudad.getItems().addAll("Abasolo",
                                            "Aldama",
                                            "Altamira",
                                            "Antiguo Morelos",
                                            "Burgos",
                                            "Bustamante",
                                            "Camargo",
                                            "Casas",
                                            "Ciudad Madero",
                                            "Cruillas",
                                            "El Mante",
                                            "Gómez Farías",
                                            "González",
                                            "Güémez",
                                            "Guerrero",
                                            "Gustavo Díaz Ordaz",
                                            "Hidalgo",
                                            "Jaumave",
                                            "Jiménez",
                                            "Llera",
                                            "Mainero",
                                            "Matamoros",
                                            "Méndez",
                                            "Mier",
                                            "Miguel Alemán",
                                            "Miquihuana",
                                            "Nuevo Laredo",
                                            "Nuevo Morelos",
                                            "Ocampo",
                                            "Padilla",
                                            "Palmillas",
                                            "Reynosa",
                                            "Río Bravo",
                                            "San Carlos",
                                            "San Fernando",
                                            "San Nicolás",
                                            "Soto La Marina",
                                            "Tampico",
                                            "Tula",
                                            "Valle Hermoso",
                                            "Victoria",
                                            "Villagrán",
                                            "Xicoténcatl");
                break;
                
            case "Tlaxcala":
                cbxCiudad.getItems().addAll("Acuamanala De Miguel Hidalgo",
                                            "Amaxac De Guerrero",
                                            "Apetatitlán De Antonio Carvajal",
                                            "Apizaco",
                                            "Atlangatepec",
                                            "Atltzayanca",
                                            "Benito Juárez",
                                            "Calpulalpan",
                                            "Chiautempan",
                                            "Contla De Juan Cuamatzi",
                                            "Cuapiaxtla",
                                            "Cuaxomulco",
                                            "El Carmen Tequexquitla",
                                            "Emiliano Zapata",
                                            "Españita",
                                            "Huamantla",
                                            "Hueyotlipan",
                                            "Ixtacuixtla De Mariano Matamoros",
                                            "Ixtenco",
                                            "La Magdalena Tlaltelulco",
                                            "Lázaro Cárdenas",
                                            "Mazatecochco De José María Morelos",
                                            "Muñoz De Domingo Arenas",
                                            "Nanacamilpa De Mariano Arista",
                                            "Natívitas",
                                            "Panotla",
                                            "Papalotla De Xicohténcatl",
                                            "San Damián Texóloc",
                                            "San Francisco Tetlanohcan",
                                            "San Jerónimo Zacualpan",
                                            "San José Teacalco",
                                            "San Juan Huactzinco",
                                            "San Lorenzo Axocomanitla",
                                            "San Lucas Tecopilco",
                                            "San Pablo Del Monte",
                                            "Sanctórum De Lázaro Cárdenas",
                                            "Santa Ana Nopalucan",
                                            "Santa Apolonia Teacalco",
                                            "Santa Catarina Ayometla",
                                            "Santa Cruz Quilehtla",
                                            "Santa Cruz Tlaxcala",
                                            "Santa Isabel Xiloxoxtla",
                                            "Tenancingo",
                                            "Teolocholco",
                                            "Tepetitla De Lardizábal",
                                            "Tepeyanco",
                                            "Terrenate",
                                            "Tetla De La Solidaridad",
                                            "Tetlatlahuca",
                                            "Tlaxcala",
                                            "Tlaxco",
                                            "Tocatlán",
                                            "Totolac",
                                            "Tzompantepec",
                                            "Xaloztoc",
                                            "Xaltocan",
                                            "Xicohtzinco",
                                            "Yauhquemehcan",
                                            "Zacatelco",
                                            "Ziltlaltépec De Trinidad Sánchez Santos");
                break;
                
            case "Veracruz":
                cbxCiudad.getItems().addAll("Acajete",
                                            "Acatlán",
                                            "Acayucan",
                                            "Actopan",
                                            "Acula",
                                            "Acultzingo",
                                            "Agua Dulce",
                                            "Álamo Temapache",
                                            "Alpatláhuac",
                                            "Alto Lucero De Gutiérrez Barrios",
                                            "Altotonga",
                                            "Alvarado",
                                            "Amatitlán",
                                            "Amatlán De Los Reyes",
                                            "Angel R. Cabada",
                                            "Apazapan",
                                            "Aquila",
                                            "Astacinga",
                                            "Atlahuilco",
                                            "Atoyac",
                                            "Atzacan",
                                            "Atzalan",
                                            "Ayahualulco",
                                            "Banderilla",
                                            "Benito Juárez",
                                            "Boca Del Río",
                                            "Calcahualco",
                                            "Camarón De Tejeda",
                                            "Camerino Z. Mendoza",
                                            "Carlos A. Carrillo",
                                            "Carrillo Puerto",
                                            "Castillo De Teayo",
                                            "Catemaco",
                                            "Cazones De Herrera",
                                            "Cerro Azul",
                                            "Chacaltianguis",
                                            "Chalma",
                                            "Chiconamel",
                                            "Chiconquiaco",
                                            "Chicontepec",
                                            "Chinameca",
                                            "Chinampa De Gorostiza",
                                            "Chocamán",
                                            "Chontla",
                                            "Chumatlán",
                                            "Citlaltépetl",
                                            "Coacoatzintla",
                                            "Coahuitlán",
                                            "Coatepec",
                                            "Coatzacoalcos",
                                            "Coatzintla",
                                            "Coetzala",
                                            "Colipa",
                                            "Comapa",
                                            "Córdoba",
                                            "Cosamaloapan De Carpio",
                                            "Cosautlán De Carvajal",
                                            "Coscomatepec",
                                            "Cosoleacaque",
                                            "Cotaxtla",
                                            "Coxquihui",
                                            "Coyutla",
                                            "Cuichapa",
                                            "Cuitláhuac",
                                            "El Higo",
                                            "Emiliano Zapata",
                                            "Espinal",
                                            "Filomeno Mata",
                                            "Fortín",
                                            "Gutiérrez Zamora",
                                            "Hidalgotitlán",
                                            "Huatusco",
                                            "Huayacocotla",
                                            "Hueyapan De Ocampo",
                                            "Huiloapan De Cuauhtémoc",
                                            "Ignacio De La Llave",
                                            "Ilamatlán",
                                            "Isla",
                                            "Ixcatepec",
                                            "Ixhuacán De Los Reyes",
                                            "Ixhuatlán De Madero",
                                            "Ixhuatlán Del Café",
                                            "Ixhuatlán Del Sureste",
                                            "Ixhuatlancillo",
                                            "Ixmatlahuacan",
                                            "Ixtaczoquitlán",
                                            "Jalacingo",
                                            "Jalcomulco",
                                            "Jáltipan",
                                            "Jamapa",
                                            "Jesús Carranza",
                                            "Jilotepec",
                                            "José Azueta",
                                            "Juan Rodríguez Clara",
                                            "Juchique De Ferrer",
                                            "La Antigua",
                                            "La Perla",
                                            "Landero Y Coss",
                                            "Las Choapas",
                                            "Las Minas",
                                            "Las Vigas De Ramírez",
                                            "Lerdo De Tejada",
                                            "Los Reyes",
                                            "Magdalena",
                                            "Maltrata",
                                            "Manlio Fabio Altamirano",
                                            "Mariano Escobedo",
                                            "Martínez De La Torre",
                                            "Mecatlán",
                                            "Mecayapan",
                                            "Medellín De Bravo",
                                            "Miahuatlán",
                                            "Minatitlán",
                                            "Misantla",
                                            "Mixtla De Altamirano",
                                            "Moloacán",
                                            "Nanchital De Lázaro Cárdenas Del Río",
                                            "Naolinco",
                                            "Naranjal",
                                            "Naranjos Amatlán",
                                            "Nautla",
                                            "Nogales",
                                            "Oluta",
                                            "Omealca",
                                            "Orizaba",
                                            "Otatitlán",
                                            "Oteapan",
                                            "Ozuluama De Mascareñas",
                                            "Pajapan",
                                            "Pánuco",
                                            "Papantla",
                                            "Paso De Ovejas",
                                            "Paso Del Macho",
                                            "Perote",
                                            "Platón Sánchez",
                                            "Playa Vicente",
                                            "Poza Rica De Hidalgo",
                                            "Pueblo Viejo",
                                            "Puente Nacional",
                                            "Rafael Delgado",
                                            "Rafael Lucio",
                                            "Río Blanco",
                                            "Saltabarranca",
                                            "San Andrés Tenejapan",
                                            "San Andrés Tuxtla",
                                            "San Juan Evangelista",
                                            "San Rafael",
                                            "Santiago Sochiapan",
                                            "Santiago Tuxtla",
                                            "Sayula De Alemán",
                                            "Sochiapa",
                                            "Soconusco",
                                            "Soledad Atzompa",
                                            "Soledad De Doblado",
                                            "Soteapan",
                                            "Tamalín",
                                            "Tamiahua",
                                            "Tampico Alto",
                                            "Tancoco",
                                            "Tantima",
                                            "Tantoyuca",
                                            "Tatahuicapan De Juárez",
                                            "Tatatila",
                                            "Tecolutla",
                                            "Tehuipango",
                                            "Tempoal",
                                            "Tenampa",
                                            "Tenochtitlán",
                                            "Teocelo",
                                            "Tepatlaxco",
                                            "Tepetlán",
                                            "Tepetzintla",
                                            "Tequila",
                                            "Texcatepec",
                                            "Texhuacán",
                                            "Texistepec",
                                            "Tezonapa",
                                            "Tierra Blanca",
                                            "Tihuatlán",
                                            "Tlachichilco",
                                            "Tlacojalpan",
                                            "Tlacolulan",
                                            "Tlacotalpan",
                                            "Tlacotepec De Mejía",
                                            "Tlalixcoyan",
                                            "Tlalnelhuayocan",
                                            "Tlaltetela",
                                            "Tlapacoyan",
                                            "Tlaquilpa",
                                            "Tlilapan",
                                            "Tomatlán",
                                            "Tonayán",
                                            "Totutla",
                                            "Tres Valles",
                                            "Tuxpan",
                                            "Tuxtilla",
                                            "Ursulo Galván",
                                            "Uxpanapa",
                                            "Vega De Alatorre",
                                            "Veracruz",
                                            "Villa Aldama",
                                            "Xalapa",
                                            "Xico",
                                            "Xoxocotla",
                                            "Yanga",
                                            "Yecuatla",
                                            "Zacualpan",
                                            "Zaragoza",
                                            "Zentla",
                                            "Zongolica",
                                            "Zontecomatlán De López Y Fuentes",
                                            "Zozocolco De Hidalgo");
                break;
                
            case "Yucatán":
                cbxCiudad.getItems().addAll("Abalá",
                                            "Acanceh",
                                            "Akil",
                                            "Baca",
                                            "Bokobá",
                                            "Buctzotz",
                                            "Cacalchén",
                                            "Calotmul",
                                            "Cansahcab",
                                            "Cantamayec",
                                            "Celestún",
                                            "Cenotillo",
                                            "Chacsinkín",
                                            "Chankom",
                                            "Chapab",
                                            "Chemax",
                                            "Chichimilá",
                                            "Chicxulub Pueblo",
                                            "Chikindzonot",
                                            "Chocholá",
                                            "Chumayel",
                                            "Conkal",
                                            "Cuncunul",
                                            "Cuzamá",
                                            "Dzán",
                                            "Dzemul",
                                            "Dzidzantún",
                                            "Dzilam De Bravo",
                                            "Dzilam González",
                                            "Dzitás",
                                            "Dzoncauich",
                                            "Espita",
                                            "Halachó",
                                            "Hocabá",
                                            "Hoctún",
                                            "Homún",
                                            "Huhí",
                                            "Hunucmá",
                                            "Ixil",
                                            "Izamal",
                                            "Kanasín",
                                            "Kantunil",
                                            "Kaua",
                                            "Kinchil",
                                            "Kopomá",
                                            "Mama",
                                            "Maní",
                                            "Maxcanú",
                                            "Mayapán",
                                            "Mérida",
                                            "Mocochá",
                                            "Motul",
                                            "Muna",
                                            "Muxupip",
                                            "Opichén",
                                            "Oxkutzcab",
                                            "Panabá",
                                            "Peto",
                                            "Progreso",
                                            "Quintana Roo",
                                            "Río Lagartos",
                                            "Sacalum",
                                            "Samahil",
                                            "San Felipe",
                                            "Sanahcat",
                                            "Santa Elena",
                                            "Seyé",
                                            "Sinanché",
                                            "Sotuta",
                                            "Sucilá",
                                            "Sudzal",
                                            "Suma",
                                            "Tahdziú",
                                            "Tahmek",
                                            "Teabo",
                                            "Tecoh",
                                            "Tekal De Venegas",
                                            "Tekantó",
                                            "Tekax",
                                            "Tekit",
                                            "Tekom",
                                            "Telchac Pueblo",
                                            "Telchac Puerto",
                                            "Temax",
                                            "Temozón",
                                            "Tepakán",
                                            "Tetiz",
                                            "Teya",
                                            "Ticul",
                                            "Timucuy",
                                            "Tinum",
                                            "Tixcacalcupul",
                                            "Tixkokob",
                                            "Tixmehuac",
                                            "Tixpéhual",
                                            "Tizimín",
                                            "Tunkás",
                                            "Tzucacab",
                                            "Uayma",
                                            "Ucú",
                                            "Umán",
                                            "Valladolid",
                                            "Xocchel",
                                            "Yaxcabá",
                                            "Yaxkukul",
                                            "Yobaín");
                break;
                
            case "Zacatecas":
                cbxCiudad.getItems().addAll("Apozol",
                                            "Apulco",
                                            "Atolinga",
                                            "Benito Juárez",
                                            "Calera",
                                            "Cañitas De Felipe Pescador",
                                            "Chalchihuites",
                                            "Concepción Del Oro",
                                            "Cuauhtémoc",
                                            "El Plateado De Joaquín Amaro",
                                            "El Salvador",
                                            "Fresnillo",
                                            "Genaro Codina",
                                            "General Enrique Estrada",
                                            "General Francisco R. Murguía",
                                            "General Pánfilo Natera",
                                            "Guadalupe",
                                            "Huanusco",
                                            "Jalpa",
                                            "Jerez",
                                            "Jiménez Del Teul",
                                            "Juan Aldama",
                                            "Juchipila",
                                            "Loreto",
                                            "Luis Moya",
                                            "Mazapil",
                                            "Melchor Ocampo",
                                            "Mezquital Del Oro",
                                            "Miguel Auza",
                                            "Momax",
                                            "Monte Escobedo",
                                            "Morelos",
                                            "Moyahua De Estrada",
                                            "Nochistlán De Mejía",
                                            "Noria De Ángeles",
                                            "Ojocaliente",
                                            "Pánuco",
                                            "Pinos",
                                            "Río Grande",
                                            "Sain Alto",
                                            "Santa María De La Paz",
                                            "Sombrerete",
                                            "Susticacán",
                                            "Tabasco",
                                            "Tepechitlán",
                                            "Tepetongo",
                                            "Teúl De González Ortega",
                                            "Tlaltenango De Sánchez Román",
                                            "Trancoso",
                                            "Trinidad García De La Cadena",
                                            "Valparaíso",
                                            "Vetagrande",
                                            "Villa De Cos",
                                            "Villa García",
                                            "Villa González Ortega",
                                            "Villa Hidalgo",
                                            "Villanueva",
                                            "Zacatecas");
                break;
        }
    }
    
    public void userClickedOnTable() {
        this.btnEliminar.setDisable(false);
        this.btnEditar.setDisable(false);
        //this.btnAgregar.setVisible(false);
        //this.btnLimpiar.setVisible(true);
    }    
    
     @FXML
    public void agregarConductor(ActionEvent e) {
        String idConductor = txtId.getText();
        String nombres = txtNombres.getText();
        String apellidoPaterno = txtApellidoPaterno.getText();
        String apellidoMaterno = txtApellidoMaterno.getText();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        String lugarNacimiento = txtLugarNacimiento.getText();
        String estadoCivil = null;
        if(tggGrupoEstadoCivil.getSelectedToggle() != null) {
            RadioButton seleccion = (RadioButton) tggGrupoEstadoCivil.getSelectedToggle();
            estadoCivil = seleccion.getText();
        }
       
        LocalDate fechaIngreso = dpFechaIngreso.getValue();
        LocalDate fechaSindicato = dpFechaSindicato.getValue();
        String estudios = txtEstudios.getText();
        String noIMSS = txtNoIMSS.getText();
        String afore = txtAfore.getText();
        String curp = txtCURP.getText();
        String rfc = txtRFC.getText();
        String claveElector = txtClaveElector.getText();
        int idDomicilio = idDom;
        String estado = null;
        String ciudad = null;
        
        if(cbxEstado.getValue() != null) {
            estado = cbxEstado.getSelectionModel().getSelectedItem().toString();
        }
        if(cbxCiudad.getValue() != null) {
            ciudad = cbxCiudad.getSelectionModel().getSelectedItem().toString();
        }
        
        String colonia = txtColonia.getText();
        String calle = txtCalle.getText();
        String numeroExt = txtNumExt.getText();
        String cp = txtCP.getText();
        
        String idLicencia = txtIdLicencia.getText();
        LocalDate fechaExpiracion = dpFechaExpiracion.getValue(); 
        LocalDate fechaExpedicion = dpFechaExpedicion.getValue();
        
        String noCuenta = txtNoCuenta.getText();
        String base = txtBase.getText();
        String servicio = txtServicio.getText();
        
        int idTelefono = idTel;
        String telefono = txtTelefono.getText();
        
        int idBeneficiarios = idBen;
        String idConductorBen = idConductor;
        String beneficiarios = txaBeneficiarios.getText();
        String parentesco = txaParentesco.getText();
        String porcentaje = txaPorcentaje.getText();
        
        if (estadoCivil != null && estado != null && ciudad != null && 
            !idConductor.equals("") && !nombres.equals("") && !apellidoPaterno.equals("") &&
            !apellidoMaterno.equals("") && fechaNacimiento != null && !lugarNacimiento.equals("") &&
            fechaIngreso != null && fechaSindicato != null && !estudios.equals("") &&
            !noIMSS.equals("") && !afore.equals("") && !curp.equals("") && !rfc.equals("") &&
            !claveElector.equals("") &&
            !colonia.equals("") && !telefono.equals("")
            && !calle.equals("") && !numeroExt.equals("") && !cp.equals("")
            && !idLicencia.equals("") && fechaExpiracion != null && fechaExpedicion != null
            && !noCuenta.equals("") && !base.equals("") && !servicio.equals("")
            && !beneficiarios.equals("") && !parentesco.equals("") && !porcentaje.equals("")) {
            
            identificarCamposVacios(0);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea ingresar un conductor?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    Conductor bean = new Conductor();
                    bean.setIdConductor(idConductor);
                    bean.setNombres(nombres);
                    bean.setApellidoPaterno(apellidoPaterno);
                    bean.setApellidoMaterno(apellidoMaterno);
                    bean.setFechaNacimiento(String.valueOf(fechaNacimiento));
                    bean.setLugarNacimiento(lugarNacimiento);
                    bean.setEstadoCivil(estadoCivil);
                    
                    bean.setFechaIngreso(String.valueOf(fechaIngreso));
                    bean.setFechaSindicato(String.valueOf(fechaSindicato));
                    bean.setEstudios(estudios);
                    bean.setNoIMSS(noIMSS);
                    bean.setAfore(afore);
                    bean.setCurp(curp);
                    bean.setRfc(rfc);
                    bean.setClaveElector(claveElector);
                    
                    bean.setIdDomicilio(idDomicilio);
                    bean.setEstado(estado);
                    bean.setCiudad(ciudad);
                    bean.setColonia(colonia);
                    bean.setCalle(calle);
                    //bean.setNumExt(Integer.parseInt(numeroExt));
                    bean.setNumExt(numeroExt);
                    //bean.setCp(Integer.parseInt(cp));
                    bean.setCp(cp);
                    
                    bean.setIdLicencia(idLicencia);
                    bean.setFechaExpiracion(String.valueOf(fechaExpiracion));
                    bean.setFechaExpedicion(String.valueOf(fechaExpedicion));
                    
                    bean.setNoCuenta(noCuenta);
                    bean.setBase(base);
                    bean.setServicio(servicio);
                                        
                    bean.setIdTelefono(idTelefono);
                    bean.setTelefono(telefono);
                    
                    bean.setIdBeneficiarios(idBeneficiarios);
                    bean.setBeneficiarios(beneficiarios);
                    bean.setParentesco(parentesco);
                    bean.setPorcentaje(porcentaje);
                    
                    if (bean.insertarDomicilio() && bean.insertarEmpleado() && bean.insertarTelefono()
                            && bean.insertarLicencia() && bean.insertarConductor(idConductor, idLicencia)
                            && bean.insertarBeneficiarios()) {
                        obtenerSizeTabla();
                        obtenerSizeTablaTel();
                        obtenerSizeTablaBen();
                        insertarYRefrescar(); //Aquí se cambia por el insertar(que contiene el rs).
                        showAlert(Alert.AlertType.INFORMATION, "Information Message", " El conductor se ha ingresado correctamente.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error Message", " Error al añadir el conductor.");
                    }

                } catch (HeadlessException | NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error Message", " Error al añadir conductor.");
                }
            }
        } else { //Else de validación de campos vacíos.
            identificarCamposVacios(0);            
            identificarCamposVacios(1);            
            showAlert(Alert.AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
        }
    } //botón.
    
    public void showAlert(Alert.AlertType error, String header, String body) {
        alert = new Alert(error);
        alert.setTitle(header);
        alert.setHeaderText(body);
        alert.showAndWait();
    }
    
    @FXML
    public void modificarReporte(ActionEvent e) {
        Conductor modificar = tblDatosConductor.getSelectionModel().getSelectedItem();        
 
        if (modificar != null) {
            String idConductor = tblDatosConductor.getSelectionModel().getSelectedItem().getIdConductor();
            int idDomicilio = tblDatosConductor.getSelectionModel().getSelectedItem().getIdDomicilio();
            int idTelefono = tblDatosConductor.getSelectionModel().getSelectedItem().getIdTelefono();
            int idBeneficiarios = tblDatosConductor.getSelectionModel().getSelectedItem().getIdBeneficiarios();
            
            String idConductorBen = idConductor;
            String nombres = txtNombres.getText();
            String apellidoPaterno = txtApellidoPaterno.getText();
            String apellidoMaterno = txtApellidoMaterno.getText();
            LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
            String lugarNacimiento = txtLugarNacimiento.getText();
            String estadoCivil = null;
            RadioButton seleccion = (RadioButton) tggGrupoEstadoCivil.getSelectedToggle();
            estadoCivil = seleccion.getText();

            LocalDate fechaIngreso = dpFechaIngreso.getValue();
            LocalDate fechaSindicato = dpFechaSindicato.getValue();
            String estudios = txtEstudios.getText();
            String noIMSS = txtNoIMSS.getText();
            String afore = txtAfore.getText();
            String curp = txtCURP.getText();
            String rfc = txtRFC.getText();
            String claveElector = txtClaveElector.getText();

            String estado = cbxEstado.getSelectionModel().getSelectedItem().toString();
            String ciudad = cbxCiudad.getSelectionModel().getSelectedItem().toString();
            String colonia = txtColonia.getText();
            String calle = txtCalle.getText();
            String numeroExt = txtNumExt.getText();
            String cp = txtCP.getText();

            String idLicencia = txtIdLicencia.getText();
            LocalDate fechaExpiracion = dpFechaExpiracion.getValue(); 
            LocalDate fechaExpedicion = dpFechaExpedicion.getValue();

            String noCuenta = txtNoCuenta.getText();
            String base = txtBase.getText();
            String servicio = txtServicio.getText();

            String telefono = txtTelefono.getText();        

            String beneficiarios = txaBeneficiarios.getText();
            String parentesco = txaParentesco.getText();
            String porcentaje = txaPorcentaje.getText();
            
            if (estadoCivil != null && estado != null && ciudad != null && 
                !idConductor.equals("") && !nombres.equals("") && !apellidoPaterno.equals("") &&
                !apellidoMaterno.equals("") && fechaNacimiento != null && !lugarNacimiento.equals("") &&
                fechaIngreso != null && fechaSindicato != null && !estudios.equals("") &&
                !noIMSS.equals("") && !afore.equals("") && !curp.equals("") && !rfc.equals("") &&
                !claveElector.equals("") &&
                !colonia.equals("") && !telefono.equals("")
                && !calle.equals("") && !numeroExt.equals("") && !cp.equals("")
                && !idLicencia.equals("") && fechaExpiracion != null && fechaExpedicion != null
                && !noCuenta.equals("") && !base.equals("") && !servicio.equals("")
                && !beneficiarios.equals("") && !parentesco.equals("") && !porcentaje.equals("")) {
                
                identificarCamposVacios(0);
                
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("¿Desea modificar un conductor?");

                Optional<ButtonType> result = confirm.showAndWait();

                //System.out.println(idConductor);
                if (result.get() == ButtonType.OK) {
                    try {
                        Conductor m = new Conductor();
                        m.setIdConductor(idConductor);
                        m.setNombres(nombres);
                        m.setApellidoPaterno(apellidoPaterno);
                        m.setApellidoMaterno(apellidoMaterno);
                        m.setFechaNacimiento(String.valueOf(fechaNacimiento));
                        m.setLugarNacimiento(lugarNacimiento);
                        m.setEstadoCivil(estadoCivil);

                        m.setFechaIngreso(String.valueOf(fechaIngreso));
                        m.setFechaSindicato(String.valueOf(fechaSindicato));
                        m.setEstudios(estudios);
                        m.setNoIMSS(noIMSS);
                        m.setAfore(afore);
                        m.setCurp(curp);
                        m.setRfc(rfc);
                        m.setClaveElector(claveElector);

                        m.setEstado(estado);
                        m.setCiudad(ciudad);
                        m.setColonia(colonia);
                        m.setCalle(calle);
                        //bean.setNumExt(Integer.parseInt(numeroExt));
                        m.setNumExt(numeroExt);
                        //bean.setCp(Integer.parseInt(cp));
                        m.setCp(cp);
                        
                        m.setIdLicencia(idLicencia);
                        m.setFechaExpiracion(String.valueOf(fechaExpiracion));
                        m.setFechaExpedicion(String.valueOf(fechaExpedicion));

                        m.setNoCuenta(noCuenta);
                        m.setBase(base);
                        m.setServicio(servicio);
                        
                        m.setTelefono(telefono);
                        
                        m.setBeneficiarios(beneficiarios);
                        m.setParentesco(parentesco);
                        m.setPorcentaje(porcentaje);
                        
                        if (m.modificarBeneficiarios(idBeneficiarios)
                            && m.modificarConductor(idConductor) 
                            && m.modificarTelefono(idTelefono)
                            && m.modificarEmpleado(idConductor)                              
                            && m.modificarDomicilio(idDomicilio)
                            && m.modificarLicencia(idLicencia)) {
                            insertarYRefrescar();
                            showAlert(Alert.AlertType.INFORMATION, "Information Message", " El conductor se ha modificado correctamente.");

                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error Message", " Error al modificar conductor.");
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error Message", " Error al modificar conductor.");
                    }
                }
            } else { //Else de validación de campos vacíos.
                identificarCamposVacios(0);            
                identificarCamposVacios(1);            
                showAlert(Alert.AlertType.WARNING, "Warning Message", " Favor de llenar todos los campos.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning Message", "Favor de seleccionar un conductor.");
        }
    }
    
    private void identificarCamposVacios(int valor) {
        if(valor == 1){
            for (int i = 0; i < txtCAMPOS.length; i++) {
                if(txtCAMPOS[i].getText().equals("")) {
                    lblCamposTxt[i].setTextFill(Color.RED);
                    txtCAMPOS[i].getStyleClass().add("txtBorderRed");
                }
            }        
            for (int i = 0; i < dpCampos.length; i++) {
                if(dpCampos[i].getValue() == null) {
                    lblCamposRest[i].setTextFill(Color.RED);
                }
            }
            for (int i = 0; i < txaCampos.length; i++) {
                if(txaCampos[i].getText().equals("")) {
                    lblCamposArea[i].setTextFill(Color.RED);
                }
            }
            if(cbxEstado.getValue() == null)
                lblEstado.setTextFill(Color.RED);
            if(cbxCiudad.getValue() == null)
                lblCiudad.setTextFill(Color.RED);
            if(tggGrupoEstadoCivil.getSelectedToggle() == null)
                lblEstadoCivil.setTextFill(Color.RED);
        } else if (valor == 0) {
            for (int i = 0; i < txtCAMPOS.length; i++) {                
                lblCamposTxt[i].setTextFill(Color.BLACK);
                txtCAMPOS[i].getStyleClass().remove("txtBorderRed");
            }        
            for (int i = 0; i < dpCampos.length; i++) {                
                lblCamposRest[i].setTextFill(Color.BLACK);
            }
            for (int i = 0; i < txaCampos.length; i++) {                
                lblCamposArea[i].setTextFill(Color.BLACK);
            } 
            lblEstado.setTextFill(Color.BLACK);            
            lblCiudad.setTextFill(Color.BLACK);
            lblEstadoCivil.setTextFill(Color.BLACK);
        }
        
    }

    @FXML
    public void eliminarReporte(ActionEvent e) {
        Conductor borrar = tblDatosConductor.getSelectionModel().getSelectedItem();        
        
        if (borrar != null) {
            String idConductor = tblDatosConductor.getSelectionModel().getSelectedItem().getIdConductor();
            int idDomicilio = tblDatosConductor.getSelectionModel().getSelectedItem().getIdDomicilio();
            int idTelefono = tblDatosConductor.getSelectionModel().getSelectedItem().getIdTelefono();
            String idLicencia = tblDatosConductor.getSelectionModel().getSelectedItem().getIdLicencia();
            int idBeneficiarios = tblDatosConductor.getSelectionModel().getSelectedItem().getIdBeneficiarios();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("¿Desea eliminar un conductor?");

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                //int idReporte = Integer.parseInt(txtReporte.getText());

                Conductor eliminar = new Conductor();
               // eliminar.setId(id);
                if (eliminar.eliminarBeneficiarios(idBeneficiarios)
                        && eliminar.eliminarConductor(idConductor) 
                        && eliminar.eliminarTelefono(idTelefono)
                        && eliminar.eliminarEmpleado(idConductor) 
                        && eliminar.eliminarDomicilio(idDomicilio)
                        && eliminar.eliminarLicencia(idLicencia)) {
                    eliminarYActualizar();
                    showAlert(Alert.AlertType.INFORMATION, "Information Message", " El conductor ha sido eliminado correctamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error Message", " Error al modificar conductor.");
                }
            }

        } else if (borrar == null) {
            showAlert(Alert.AlertType.WARNING, "Warning Message", " Favor de seleccionar un conductor.");
        }
    }
    
    @FXML
    private void tablaDomicilioAction(MouseEvent e) {
        identificarCamposVacios(0);
        mostrarFilaEnCampos();
    }

    public void llenarTablaBD() {
        ResultSet rs, rsId, rsIdTel, rsIdBen;
        /*String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono, Beneficiarios.Beneficiarios, Beneficiarios.Parentesco, Beneficiarios.Porcentaje "
                + "FROM (((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.Id = Conductor.IdLicencia) "
                + "ON Empleado.id  = Conductor.id) INNER JOIN Telefono ON Empleado.id = Telefono.idEmpleado)"
                + "INNER JOIN Beneficiarios ON Beneficiarios.[id] = Conductor.[idConductor];";*/
        String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono, Beneficiarios.Id AS Beneficiarios_id, Beneficiarios.Beneficiarios, Beneficiarios.Parentesco, Beneficiarios.Porcentaje "
                + "FROM (((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN Telefono ON Empleado.[Id] = Telefono.[IdEmpleado]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.[Id] = Conductor.[IdLicencia]) ON Empleado.[Id] = Conductor.[Id]) "
                + "INNER JOIN Beneficiarios ON Conductor.[Id] = Beneficiarios.[IdConductor];";
        
        String sqlId = "SELECT Id from Domicilio";
        String sqlIdTel = "SELECT Id from Telefono";
        String sqlIdBen = "SELECT Id from Beneficiarios";
        
        try {
            conexionBD.conectar();
            rs = conexionBD.ejecutarSQLSelect(sql);
            rsId = conexionBD.ejecutarSQLSelect(sqlId);
            rsIdTel = conexionBD.ejecutarSQLSelect(sqlIdTel);
            rsIdBen = conexionBD.ejecutarSQLSelect(sqlIdBen);
            metadata = rs.getMetaData();
            metadataId = rsId.getMetaData();
            metadataIdTel = rsIdTel.getMetaData();
            metadataIdBen = rsIdBen.getMetaData();
            int cols = metadata.getColumnCount();
            int colsId = metadataId.getColumnCount();
            int colsIdTel = metadataIdTel.getColumnCount();
            int colsIdBen = metadataIdBen.getColumnCount();
            //System.out.println("Columnas: "+cols);
            while (rs.next()) {
                Object[] fila = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    if (rs.getObject(i + 1) == null) {
                        fila[i] = "";

                    } else {
                        fila[i] = rs.getObject(i + 1);
                    }
                }
                
                
                String idConductor = String.valueOf(fila[0]);
                String nombres = String.valueOf(fila[1]);
                String apellidoPaterno = String.valueOf(fila[2]);
                String apellidoMaterno = String.valueOf(fila[3]);
                String fechaNacimiento = String.valueOf(rs.getDate(5));
                String lugarNacimiento = String.valueOf(fila[5]);                
                String estadoCivil = String.valueOf(fila[6]);
                String fechaIngreso = String.valueOf(rs.getDate(9)); //[8]
                String fechaSindicato = String.valueOf(rs.getDate(10)); //[9]
                String estudios = String.valueOf(fila[10]);
                String noIMSS = String.valueOf(fila[11]);
                String afore = String.valueOf(fila[12]);
                String curp = String.valueOf(fila[13]);
                String rfc = String.valueOf(fila[14]);
                String claveElector = String.valueOf(fila[15]);
                //String foto = String.valueOf(fila[16]);
                
                String idDomicilio = String.valueOf(fila[16]);                
                String estado = String.valueOf(fila[17]);
                String ciudad = (String) fila[18];
                String colonia = String.valueOf(fila[19]);
                String calle = String.valueOf(fila[20]);
                String num = String.valueOf(fila[21]);
                String cp = String.valueOf(fila[22]);
                
                String idLicencia = String.valueOf(fila[23]);
                String fechaExpiracion = String.valueOf(rs.getDate(25)); //[24]
                String fechaExpedicion = String.valueOf(rs.getDate(26)); //[25]
                
                String base = String.valueOf(fila[26]);
                String servicio = String.valueOf(fila[27]);
                String noCuenta = String.valueOf(fila[28]);
                
                String telefonoID = String.valueOf(fila[29]);
                String telefono = String.valueOf(fila[30]);
                
                String idBeneficiarios = String.valueOf(fila[31]);
                String beneficiarios = String.valueOf(fila[32]);
                String parentesco = String.valueOf(fila[33]);
                String porcentaje = String.valueOf(fila[34]);

                Conductor dom = new Conductor(idConductor, nombres, apellidoPaterno, 
                        apellidoMaterno, fechaNacimiento, lugarNacimiento,Integer.parseInt(telefonoID),telefono,estadoCivil,
                        fechaIngreso, fechaSindicato, estudios, noIMSS, afore, curp, rfc, claveElector,
                         Integer.parseInt(idDomicilio),
                        estado, ciudad, colonia, calle, num, cp,
                        base, servicio, noCuenta, idLicencia, fechaExpiracion, fechaExpedicion,
                        Integer.parseInt(idBeneficiarios), beneficiarios, parentesco, porcentaje);
                        
                colocarDatosColumna();
                tblDatosConductor.getItems().add(dom);
            }
            
            while(rsId.next()) {
                Object[] filaId = new Object[colsId];
                for (int i = 0; i < colsId; i++) {
                    if (rsId.getObject(i + 1) == null) {
                        filaId[i] = "";

                    } else {
                        filaId[i] = rsId.getObject(i + 1);
                    }
                }
                
                String idDom = String.valueOf(filaId[0]);
                Conductor conId = new Conductor(Integer.parseInt(idDom),"Domicilio");        
                colocarDatosColumna();                
                tblIdDomicilio.getItems().add(conId);
            }
            
            while(rsIdTel.next()) {
                Object[] filaIdTel = new Object[colsIdTel];
                for (int i = 0; i < colsIdTel; i++) {
                    if (rsIdTel.getObject(i + 1) == null) {
                        filaIdTel[i] = "";

                    } else {
                        filaIdTel[i] = rsIdTel.getObject(i + 1);
                    }
                }
                
                String idDomTel = String.valueOf(filaIdTel[0]);
                Conductor conIdTel = new Conductor(Integer.parseInt(idDomTel), "Telefono");        
                colocarDatosColumna();                
                tblIdTelefono.getItems().add(conIdTel);
            }
            
            while(rsIdBen.next()) {
                Object[] filaIdBen = new Object[colsIdBen];
                for (int i = 0; i < colsIdBen; i++) {
                    if (rsIdBen.getObject(i + 1) == null) {
                        filaIdBen[i] = "";

                    } else {
                        filaIdBen[i] = rsIdBen.getObject(i + 1);
                    }
                }
                
                String idDomBen = String.valueOf(filaIdBen[0]);
                Conductor conIdBen = new Conductor(Integer.parseInt(idDomBen), "Beneficiarios");        
                colocarDatosColumna();                
                tblIdBeneficiarios.getItems().add(conIdBen);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al rellenar la tabla." + e.getMessage());
        }
    }

    public void actualizarTablaBD(ResultSet result) //Llenar la tabla con un result set.
    {
        ResultSet rs, rsId, rsIdTel, rsIdBen;
        try {
            if (filtroActivo) { //Si el filtro está activado, quiere decir que el txtField está en escucha y se sustituye el result set por ese.
                rs = result;
                rsId = result;
                rsIdTel = result;
                rsIdBen = result;
            } else { //En caso contrario, solamente volvemos a ejecutar la sentencia normal de traer todo a la tabla.                
                /*String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono "
                + "FROM ((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.Id = Conductor.IdLicencia) "
                + "ON Empleado.id  = Conductor.id) INNER JOIN Telefono ON Empleado.id = Telefono.idEmpleado;";*/
                String sql = "SELECT Empleado.Id AS Empleado_Id,Empleado.Nombres,Empleado.ApellidoPaterno,Empleado.ApellidoMaterno,Empleado.FechaNacimiento,Empleado.LugarNacimiento,"
                + "Empleado.EstadoCivil,Empleado.IdDomicilio,Empleado.FechaIngreso,Empleado.FechaSindicato,Empleado.Estudios,Empleado.NumeroIMSS,Empleado.Afore,Empleado.Curp,"
                + "Empleado.RFC,Empleado.ClaveElector,"
                + "Domicilio.Id AS Domicilio_Id, Domicilio.Estado, Domicilio.Ciudad, Domicilio.Colonia, Domicilio.Calle, Domicilio.NumeroExt, Domicilio.CodigoPostal,"
                + "Licencia.Id AS Licencia_Id, Licencia.FechaExpiracion,Licencia.FechaExpedicion, Conductor.base, Conductor.Servicio, Conductor.NoCuenta,"
                + "Telefono.Id AS Telefono_id, Telefono.NumeroTelefono, Beneficiarios.Id AS Beneficiarios_id, Beneficiarios.Beneficiarios, Beneficiarios.Parentesco, Beneficiarios.Porcentaje "
                + "FROM (((Domicilio INNER JOIN Empleado ON Domicilio.[Id] = Empleado.[IdDomicilio]) "
                + "INNER JOIN Telefono ON Empleado.[Id] = Telefono.[IdEmpleado]) "
                + "INNER JOIN (Licencia INNER JOIN Conductor ON Licencia.[Id] = Conductor.[IdLicencia]) ON Empleado.[Id] = Conductor.[Id]) "
                + "INNER JOIN Beneficiarios ON Conductor.[Id] = Beneficiarios.[IdConductor];";
                
                String sqlId = "SELECT Id from Domicilio";
                String sqlIdTel = "SELECT Id from Telefono";
                String sqlIdBen = "SELECT Id from Beneficiarios";
                
                rs = conexionBD.ejecutarSQLSelect(sql);
                rsId = conexionBD.ejecutarSQLSelect(sqlId);
                rsIdTel = conexionBD.ejecutarSQLSelect(sqlIdTel);
                rsIdBen = conexionBD.ejecutarSQLSelect(sqlIdBen);
            }
            try {
                conexionBD.conectar();
                metadata = rs.getMetaData();
                metadataId = rsId.getMetaData();
                metadataIdTel = rsIdTel.getMetaData();
                metadataIdBen = rsIdBen.getMetaData();
                int cols = metadata.getColumnCount();
                int colsId = metadataId.getColumnCount();
                int colsIdTel = metadataIdTel.getColumnCount();
                int colsIdBen = metadataIdBen.getColumnCount();

                while (rs.next()) {
                    Object[] fila = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        if (rs.getObject(i + 1) == null) {
                            fila[i] = "";

                        } else {
                            fila[i] = rs.getObject(i + 1);
                        }
                    }
                    String idConductor = String.valueOf(fila[0]);
                    String nombres = String.valueOf(fila[1]);
                    String apellidoPaterno = String.valueOf(fila[2]);
                    String apellidoMaterno = String.valueOf(fila[3]);
                    String fechaNacimiento = String.valueOf(rs.getDate(5));
                    String lugarNacimiento = String.valueOf(fila[5]);                
                    String estadoCivil = String.valueOf(fila[6]);
                    String fechaIngreso = String.valueOf(rs.getDate(9)); //[8]
                    String fechaSindicato = String.valueOf(rs.getDate(10)); //[9]
                    String estudios = String.valueOf(fila[10]);
                    String noIMSS = String.valueOf(fila[11]);
                    String afore = String.valueOf(fila[12]);
                    String curp = String.valueOf(fila[13]);
                    String rfc = String.valueOf(fila[14]);
                    String claveElector = String.valueOf(fila[15]);
                    //String foto = String.valueOf(fila[16]);

                    String idDomicilio = String.valueOf(fila[16]);                
                    String estado = String.valueOf(fila[17]);
                    String ciudad = (String) fila[18];
                    String colonia = String.valueOf(fila[19]);
                    String calle = String.valueOf(fila[20]);
                    String num = String.valueOf(fila[21]);
                    String cp = String.valueOf(fila[22]);

                    String idLicencia = String.valueOf(fila[23]);
                    String fechaExpiracion = String.valueOf(rs.getDate(25)); //[24]
                    String fechaExpedicion = String.valueOf(rs.getDate(26)); //[25]

                    String base = String.valueOf(fila[26]);
                    String servicio = String.valueOf(fila[27]);
                    String noCuenta = String.valueOf(fila[28]);

                    String telefonoID = String.valueOf(fila[29]);
                    String telefono = String.valueOf(fila[30]);
                    
                    String idBeneficiarios = String.valueOf(fila[31]);
                    String beneficiarios = String.valueOf(fila[32]);
                    String parentesco = String.valueOf(fila[33]);
                    String porcentaje = String.valueOf(fila[34]);

                    Conductor dom = new Conductor(idConductor, nombres, apellidoPaterno, 
                            apellidoMaterno, fechaNacimiento, lugarNacimiento,Integer.parseInt(telefonoID),telefono,estadoCivil,
                            fechaIngreso, fechaSindicato, estudios, noIMSS, afore, curp, rfc, claveElector,
                             Integer.parseInt(idDomicilio),
                            estado, ciudad, colonia, calle, num, cp,
                            base, servicio, noCuenta, idLicencia, fechaExpiracion, fechaExpedicion,
                            Integer.parseInt(idBeneficiarios), beneficiarios, parentesco, porcentaje);

                    colocarDatosColumna();
                    tblDatosConductor.getItems().add(dom);
                }
                

                while(rsId.next()) {
                    Object[] filaId = new Object[colsId];
                    for (int i = 0; i < colsId; i++) {
                        if (rsId.getObject(i + 1) == null) {
                            filaId[i] = "";

                        } else {
                            filaId[i] = rsId.getObject(i + 1);
                        }
                    }

                    String idDom = String.valueOf(filaId[0]);
                    Conductor conId = new Conductor(Integer.parseInt(idDom), "Domicilio");        
                    colocarDatosColumna();                
                    tblIdDomicilio.getItems().add(conId);
            
                }
                
                while(rsIdTel.next()) {
                    Object[] filaIdTel = new Object[colsIdTel];
                    for (int i = 0; i < colsIdTel; i++) {
                        if (rsIdTel.getObject(i + 1) == null) {
                            filaIdTel[i] = "";

                        } else {
                            filaIdTel[i] = rsIdTel.getObject(i + 1);
                        }
                    }

                    String idDomTel = String.valueOf(filaIdTel[0]);
                    Conductor conIdTel = new Conductor(Integer.parseInt(idDomTel), "Telefono");        
                    colocarDatosColumna();                
                    tblIdTelefono.getItems().add(conIdTel);
            
                }
                
                while(rsIdBen.next()) {
                    Object[] filaIdBen = new Object[colsIdBen];
                    for (int i = 0; i < colsIdBen; i++) {
                        if (rsIdBen.getObject(i + 1) == null) {
                            filaIdBen[i] = "";

                        } else {
                            filaIdBen[i] = rsIdBen.getObject(i + 1);
                        }
                    }

                    String idDomBen = String.valueOf(filaIdBen[0]);
                    Conductor conIdBen = new Conductor(Integer.parseInt(idDomBen), "Beneficiarios");        
                    colocarDatosColumna();                
                    tblIdBeneficiarios.getItems().add(conIdBen);
                }
                
            } catch (SQLException e) {
                System.out.println("Error al filtrar la tabla." + e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Message - Database", " Error al llenar la tabla.");
        }
    }

    void colocarDatosColumna() {
        tbcID.setCellValueFactory(new PropertyValueFactory<>("IdConductor"));
        tbcNombres.setCellValueFactory(new PropertyValueFactory<>("Nombres"));
        tbcApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("ApellidoPaterno"));
        tbcApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("ApellidoMaterno"));
        tbcFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("FechaNacimiento"));
        tbcLugarNacimiento.setCellValueFactory(new PropertyValueFactory<>("LugarNacimiento"));
        tbcTelefono.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        tbcEstadoCivil.setCellValueFactory(new PropertyValueFactory<>("EstadoCivil"));
        tbcFechaIngreso.setCellValueFactory(new PropertyValueFactory<>("FechaIngreso"));
        tbcFechaSidicato.setCellValueFactory(new PropertyValueFactory<>("fechaSindicato"));
        tbcEstudios.setCellValueFactory(new PropertyValueFactory<>("Estudios"));
        tbcNoIMSS.setCellValueFactory(new PropertyValueFactory<>("NoIMSS"));
        tbcAfore.setCellValueFactory(new PropertyValueFactory<>("Afore"));
        tbcCURP.setCellValueFactory(new PropertyValueFactory<>("Curp"));
        tbcRFC.setCellValueFactory(new PropertyValueFactory<>("Rfc"));
        tbcClaveElector.setCellValueFactory(new PropertyValueFactory<>("ClaveElector"));
        tbcIdDomicilio.setCellValueFactory(new PropertyValueFactory<>("IdDomicilio"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbcCiudad.setCellValueFactory(new PropertyValueFactory<>("Ciudad"));
        tbcColonia.setCellValueFactory(new PropertyValueFactory<>("Colonia"));
        tbcCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        tbcNumExt.setCellValueFactory(new PropertyValueFactory<>("NumExt"));
        tbcCp.setCellValueFactory(new PropertyValueFactory<>("Cp"));
        tbcBase.setCellValueFactory(new PropertyValueFactory<>("Base"));
        tbcServicio.setCellValueFactory(new PropertyValueFactory<>("Servicio"));
        tbcNoCuenta.setCellValueFactory(new PropertyValueFactory<>("NoCuenta"));
        tbcIdLicencia.setCellValueFactory(new PropertyValueFactory<>("IdLicencia"));
        tbcFechaExpiracion.setCellValueFactory(new PropertyValueFactory<>("FechaExpiracion"));
        tbcFechaExpedicion.setCellValueFactory(new PropertyValueFactory<>("FechaExpedicion"));
        tbcIdDomicilio2.setCellValueFactory(new PropertyValueFactory<>("IdDomicilio"));
        tbcIdTelefono2.setCellValueFactory(new PropertyValueFactory<>("IdTelefono"));
        tbcIdBeneficiarios.setCellValueFactory(new PropertyValueFactory<>("IdBeneficiarios"));
        tbcBeneficiarios.setCellValueFactory(new PropertyValueFactory<>("Beneficiarios"));
        tbcParentesco.setCellValueFactory(new PropertyValueFactory<>("Parentesco"));
        tbcPorcentaje.setCellValueFactory(new PropertyValueFactory<>("Porcentaje"));
        tbcIdBeneficiarios2.setCellValueFactory(new PropertyValueFactory<>("IdBeneficiarios"));
    }
/*
    void ActualizaRefresca() {
        ResultSet r = null;
        LimpiarTabla();
        actualizarTablaBD(r);
        LimpiarCampos();
    }
*/
    public void insertarYRefrescar() {
        LimpiarTabla();
        ResultSet r = null; //Se regresa un nulo porque no lo estamos usando al momento de insertar o modificar.
        actualizarTablaBD(r);
        obtenerSizeTabla();
        obtenerSizeTablaTel();
        LimpiarCampos();

    }

    public void eliminarYActualizar() { //Intenta eliminarlo. Es por precaución por el resultset.
        try {
            LimpiarCampos();
            LimpiarTabla();
            ResultSet r = null;
            actualizarTablaBD(r);
            obtenerSizeTabla();
            obtenerSizeTablaTel();
        } catch (Exception error) {
            showAlert(Alert.AlertType.ERROR, "Error Message", " No se ha seleccionado ningún conductor. Favor de seleccionar.");
        }
    }

    void LimpiarCampos() {
        for (TextField campos : txtCAMPOS) {
            campos.clear();
        }
        for (TextArea areas : txaCampos) {
            areas.clear();
        }
        cbxEstado.setValue("");
        cbxCiudad.setValue("");
        cbxCiudad.setDisable(true);
        dpFechaNacimiento.setValue(null);
        dpFechaIngreso.setValue(null);
        dpFechaSindicato.setValue(null);
        tggGrupoEstadoCivil.selectToggle(null);
        
        dpFechaExpiracion.setValue(null);
        dpFechaExpedicion.setValue(null);
    }

    void LimpiarTabla() {
        for (int i = 0; i < tblDatosConductor.getItems().size(); i++) {
            tblDatosConductor.getItems().clear();
            tblIdDomicilio.getItems().clear();
        }
    }

    public void mostrarFilaEnCampos() { //Aquí se llama otra vez el bean para obtener los datos seleccionados de la tabla.
        Conductor conductor = tblDatosConductor.getSelectionModel().getSelectedItem();
        if (conductor == null) {
            LimpiarCampos();
        } else {
            String idConductor = conductor.getIdConductor();
            String nombres = conductor.getNombres();
            String apellidoPaterno = conductor.getApellidoPaterno();
            String apellidoMaterno = conductor.getApellidoMaterno();
            String fechaNacimiento = conductor.getFechaNacimiento();            
            String lugarNacimiento = conductor.getLugarNacimiento();
            String estadoCivil = conductor.getEstadoCivil();
            //System.out.println("ESTADO CIVIL: "+estadoCivil);
            int idDomicilio = conductor.getIdDomicilio();
            String fechaIngreso = conductor.getFechaIngreso();
            String fechaSindicato = conductor.getFechaSindicato();
            String estudios = conductor.getEstudios();
            String noIMSS = conductor.getNoIMSS();
            String afore = conductor.getAfore();
            String curp = conductor.getCurp();
            String rfc = conductor.getRfc();
            String claveElector = conductor.getClaveElector();
            String foto = conductor.getFoto();            
            
            String estado = conductor.getEstado();
            String ciudad = conductor.getCiudad();
            String colonia = conductor.getColonia();
            String calle = conductor.getCalle();
            String numero = conductor.getNumExt();
            String cp = conductor.getCp();
            
            String idLicencia = conductor.getIdLicencia();
            String fechaExpiracion = conductor.getFechaExpiracion();
            String fechaExpedicion = conductor.getFechaExpedicion();
            
            String noCuenta = conductor.getNoCuenta();
            String base = conductor.getBase();
            String servicio = conductor.getServicio();
            
            String telefono = conductor.getTelefono();
            int idTelefono = conductor.getIdTelefono();
            
            int idBeneficiarios = conductor.getIdBeneficiarios();
            String beneficiarios = conductor.getBeneficiarios();
            String parentesco = conductor.getParentesco();
            String porcentaje = conductor.getPorcentaje();
            
            txtId.setText(idConductor);
            txtNombres.setText(nombres);
            txtApellidoPaterno.setText(apellidoPaterno);
            txtApellidoMaterno.setText(apellidoMaterno);
            dpFechaNacimiento.setValue(LocalDate.parse(fechaNacimiento));
            txtLugarNacimiento.setText(lugarNacimiento);
            if(estadoCivil.equals("Soltero")) tggGrupoEstadoCivil.selectToggle(rbtSoltero);
            else if(estadoCivil.equals("Casado")) tggGrupoEstadoCivil.selectToggle(rbtCasado);
            else if(estadoCivil.equals("Union Libre")) tggGrupoEstadoCivil.selectToggle(rbtUnionLibre);            
            txtIdDomicilio.setText(String.valueOf(idDomicilio));
            
            dpFechaIngreso.setValue(LocalDate.parse(fechaIngreso));
            dpFechaSindicato.setValue(LocalDate.parse(fechaSindicato));
            txtEstudios.setText(estudios);
            txtNoIMSS.setText(noIMSS);
            txtAfore.setText(afore);
            txtCURP.setText(curp);
            txtRFC.setText(rfc);
            txtClaveElector.setText(claveElector);
            
            estado = firstLetterCaps(estado);
            ciudad = firstLetterCaps(ciudad);
            cbxEstado.setValue(estado);
            cbxCiudad.setValue(ciudad);
            txtColonia.setText(String.valueOf(colonia));
            txtCalle.setText(String.valueOf(calle));
            txtNumExt.setText(String.valueOf(numero));
            txtCP.setText(String.valueOf(cp));
            
            txtIdLicencia.setText(idLicencia);
            dpFechaExpiracion.setValue(LocalDate.parse(fechaExpiracion));
            dpFechaExpedicion.setValue(LocalDate.parse(fechaExpedicion));
            
            txtNoCuenta.setText(noCuenta);
            txtBase.setText(base);
            txtServicio.setText(servicio);
            
            txtIdTelefono.setText(String.valueOf(idTelefono));
            txtTelefono.setText(telefono);
            
            txtIdBeneficiarios.setText(String.valueOf(idBeneficiarios));
            txaBeneficiarios.setText(beneficiarios);
            txaParentesco.setText(parentesco);
            txaPorcentaje.setText(porcentaje);
        }
    }
    
    static public String firstLetterCaps ( String data ) {
      String firstLetter = data.substring(0,1).toUpperCase();
      String restLetters = data.substring(1).toLowerCase();
      return firstLetter + restLetters;
  }

    void manejadorFiltro() {
        if (filtroActivo) {
            String idConductor;
            idConductor = txtId.getText();
            leerFiltro(idConductor);
        }
    }

    /* Sección de búsqueda. */
    @FXML
    private void handleFiltro() { //Acción del botón.
        filtroActivo = !filtroActivo; //Si el filtro es true (osea, se le dio el clic).

        if (filtroActivo) { //Deshabilitamos todo y solamente dejamos al txtfield que queremos que nos escuche.
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnEditar.setDisable(true);
            
            identificarCamposVacios(0);
            LimpiarCampos();
            txtId.textProperty().addListener(eventoFiltro);
            for (TextField campo : txtCAMPOS) {
                campo.setDisable(true);
            }
            for (TextArea area : txaCampos) {
                area.setDisable(true);
            }
            dpFechaExpedicion.setDisable(true);
            dpFechaExpiracion.setDisable(true);
            dpFechaIngreso.setDisable(true);
            dpFechaNacimiento.setDisable(true);
            dpFechaSindicato.setDisable(true);
            cbxCiudad.setDisable(true);
            cbxEstado.setDisable(true);
            rbtCasado.setDisable(true);
            rbtSoltero.setDisable(true);
            rbtUnionLibre.setDisable(true);
            
            txtId.requestFocus();
            txtId.setFocusTraversable(true);            

        } else { // Sino, se vuelve un false y activamos todo a como estaba antes.
            habilitarCampos();
            
            //LimpiarCampos();
            LimpiarTabla();
            LimpiarCampos();
            llenarTablaBD();
            //tblDatosConductor.getSelectionModel().select();
        }
        txtId.setDisable(false);
    }
    
    void habilitarCampos() {
        btnAgregar.setDisable(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        txtId.textProperty().removeListener(eventoFiltro);
        for (TextField campo : txtCAMPOS) {
             campo.setDisable(false);
        }
        for (TextArea area : txaCampos) {
             area.setDisable(false);
        }
        dpFechaExpedicion.setDisable(false);
        dpFechaExpiracion.setDisable(false);
        dpFechaIngreso.setDisable(false);
        dpFechaNacimiento.setDisable(false);
        dpFechaSindicato.setDisable(false);
        cbxCiudad.setDisable(false);
        cbxEstado.setDisable(false);
        rbtCasado.setDisable(false);
        rbtSoltero.setDisable(false);
        rbtUnionLibre.setDisable(false);
    }

    // Este filtro limpia toda la tabla y busca por medio del resultset toda la información que yo quiero ver.
    private void leerFiltro(String idConductor) {
        Conductor filtro = new Conductor();
        LimpiarTabla();
        actualizarTablaBD(filtro.filtrarConductor(idConductor)); //Checa en Modelo -> Reportes.

    }

    class ManejadorEventos implements ChangeListener { //Manejador de eventos para el changelistener.

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            manejadorFiltro(); //Aquí metes el manejadorFiltro, ya que contiene el leerFiltro(con su resultset).
        }
    }    
    
}