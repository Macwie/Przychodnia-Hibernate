package src.controllers;

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

public class ManagePatientViewController implements Initializable {

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
    private TextField pesel;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField birthdate;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField street;

    @FXML
    private TextField city;

    @FXML
    private TextField postal;

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

        loadPatientData();
        patientName.setCellFactory(TextFieldTableCell.forTableColumn());
        patientLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        patientPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        patientEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        patientStreet.setCellFactory(TextFieldTableCell.forTableColumn());
        patientCity.setCellFactory(TextFieldTableCell.forTableColumn());
        patientPostalCode.setCellFactory(TextFieldTableCell.forTableColumn());
        patientBirthdate.setCellFactory(TextFieldTableCell.forTableColumn(new DateConverter()));
    }

    private void loadPatientData() {
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

    public void deletePatient() {
        PatientAdressModel model = patientTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(patientData.get(patientData.indexOf(model)).getPatient());
        session.getTransaction().commit();
        patientData.remove(model);
        basicPatientData.remove(model.getPatient());
        tableData.remove(model);
    }


    @FXML
    void addPatient() {
        Patient patient = new Patient(pesel.getText(), name.getText(), surname.getText(), Date.valueOf(birthdate.getText()), phone.getText(), email.getText());
        Address address = new Address(street.getText(), city.getText(), postal.getText(), patient);
        patient.setAddress(address);

        session.beginTransaction();
        session.save(patient);
        session.getTransaction().commit();

        PatientAdressModel model = new PatientAdressModel(patient,address);
        basicPatientData.add(patient);
        patientData.add(model);
        tableData.add(model);

    }

    @FXML
    private void updatePatient(TableColumn.CellEditEvent edditedCell){
        PatientAdressModel model = patientTable.getSelectionModel().getSelectedItem();
        PatientAdressModel modelBefore = model;

        if(edditedCell.getTableColumn().getText().equals("Imie")){
            model.setName(edditedCell.getNewValue().toString());
        } else if(edditedCell.getTableColumn().getText().equals("Nazwisko")){
            model.setLastName(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Data urodzenia")){
            model.setBirthDate(Date.valueOf(edditedCell.getNewValue().toString()));
        }else if(edditedCell.getTableColumn().getText().equals("Telefon")){
            model.setPhoneNumber(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Email")){
            model.setEmail(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Ulica")){
            model.setStreet(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Miasto")){
            model.setCity(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Kod pocztowy")){
            model.setPostalCode(edditedCell.getNewValue().toString());
        }
        patientData.set(patientData.indexOf(modelBefore),model);
        basicPatientData.set(basicPatientData.indexOf(model.getPatient()),model.getPatient());
        session.beginTransaction();
        session.update(patientData.get(patientData.indexOf(model)).getPatient());
        session.getTransaction().commit();
    }

    @FXML
    public void showDetails() throws IOException {
        PatientAdressModel patient = (PatientAdressModel) patientTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/PatientDetailsView.fxml"));
        AnchorPane pane = loader.load();
        PatientDetailsController patientDetailsController = loader.getController();
        rootPane.getChildren().setAll(pane);
        patientDetailsController.setData(patient);
    }

    @FXML
    public void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
}
