package src.controllers.fragments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.DateConverter;
import src.main.HibernateUtil;
import src.models.Address;
import src.models.Patient;
import src.tableModels.PatientAdressModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientChooseController implements Initializable {

    private Session session;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<PatientAdressModel>  patientTable;

    @FXML
    private TableColumn<PatientAdressModel, Integer> patientPesel;

    @FXML
    private TableColumn<PatientAdressModel, String> patientName;

    @FXML
    private TableColumn<PatientAdressModel, String> patientLastName;

    @FXML
    private TableColumn<PatientAdressModel, Date> patientBirthdate;

    @FXML
    private TableColumn<PatientAdressModel, String> patientPhoneNumber;

    @FXML
    private TableColumn<PatientAdressModel, String> patientEmail;

    @FXML
    private TableColumn<PatientAdressModel, String> patientStreet;

    @FXML
    private TableColumn<PatientAdressModel, String> patientCity;

    @FXML
    private TableColumn<PatientAdressModel, String> patientPostalCode;

    @FXML
    private TextField searchTextField;

    private List<Patient> basicPatientData;
    private List<PatientAdressModel> patientData;

    private ObservableList<PatientAdressModel> tableData;
    private SortedList<PatientAdressModel> sortedData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        patientPesel.setCellValueFactory(new PropertyValueFactory<PatientAdressModel, Integer>("Pesel"));
        patientName.setCellValueFactory(new PropertyValueFactory<PatientAdressModel, String>("Name"));
        patientLastName.setCellValueFactory(new PropertyValueFactory<PatientAdressModel, String>("LastName"));
        patientBirthdate.setCellValueFactory(new PropertyValueFactory<PatientAdressModel, Date>("BirthDate"));
        patientPhoneNumber.setCellValueFactory(new PropertyValueFactory<PatientAdressModel, String>("PhoneNumber"));
        patientEmail.setCellValueFactory(new PropertyValueFactory<PatientAdressModel,String>("Email"));
        patientStreet.setCellValueFactory(new PropertyValueFactory<PatientAdressModel,String>("Street"));
        patientCity.setCellValueFactory(new PropertyValueFactory<PatientAdressModel,String>("City"));
        patientPostalCode.setCellValueFactory(new PropertyValueFactory<PatientAdressModel,String>("PostalCode"));
    }

    public void loadPatientData() {
        basicPatientData = session.createQuery("from Patient ").list();
        patientData = new ArrayList<PatientAdressModel>();
        for (Patient patient : basicPatientData) {
            PatientAdressModel model = new PatientAdressModel(patient,patient.getAddress());
            patientData.add(model);
        }
        tableData =  FXCollections.observableArrayList(patientData);
        FilteredList<PatientAdressModel> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PatientAdressModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (PatientAdressModel.getPatient().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (PatientAdressModel.getPatient().getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (PatientAdressModel.getAddress().getCity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(patientTable.comparatorProperty());
        patientTable.setItems(sortedData);
    }


    public AnchorPane getRootPane() {
        return rootPane;
    }

    public TableView<PatientAdressModel> getPatientTable() {
        return patientTable;
    }

}
