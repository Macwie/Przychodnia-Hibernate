package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import org.hibernate.Session;
import src.main.HibernateUtil;
import src.main.Notification;
import src.models.Doctor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageDoctorViewController implements Initializable {

    private Session session;


    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView doctorTable;

    @FXML
    private TableColumn<Doctor, Integer> doctorId;

    @FXML
    private TableColumn<Doctor, String> doctorName;

    @FXML
    private TableColumn<Doctor, String> doctorSurname;

    @FXML
    private TableColumn<Doctor, String> doctorSpecialization;

    @FXML
    private TableColumn<Doctor, String> doctorPhone;

    @FXML
    private MenuItem deleteItem;
    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label lastNameLabel;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Label specLabel;

    @FXML
    private TextField specTextField;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField searchTextField;

    private List<Doctor> doctorData;
    private ObservableList<Doctor> tableData;
    private SortedList<Doctor> sortedData;


    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        doctorId.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("Id"));
        doctorName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Name"));
        doctorSurname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("LastName"));
        doctorSpecialization.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Specialization"));
        doctorPhone.setCellValueFactory(new PropertyValueFactory<Doctor, String>("PhoneNumber"));
        loadDoctorData();

        doctorSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        doctorName.setCellFactory(TextFieldTableCell.forTableColumn());
        doctorSpecialization.setCellFactory(TextFieldTableCell.forTableColumn());
        doctorPhone.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void loadDoctorData() {
        doctorData = session.createQuery("from Doctor ").list();
        tableData =  FXCollections.observableArrayList(doctorData);
        FilteredList<Doctor> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(doctor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (doctor.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (doctor.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(doctorTable.comparatorProperty());
        doctorTable.setItems(sortedData);
    }


    @FXML
    public void addDoctor() {
        String name = nameTextField.getText();
        String lasName = lastNameTextField.getText();
        String spec = specTextField.getText();
        String phone = phoneNumberTextField.getText();
        Doctor doctor = new Doctor(name,lasName,spec,phone);
        try{
            session.beginTransaction();
            session.save(doctor);
            session.getTransaction().commit();
            tableData.add(doctor);
            Notification.makeNotification("INFO","Doktor dodany",3).showInformation();
        }catch (Exception e){
            Notification.makeNotification("ERROR","Blad podczas zapisu",3).showError();

        }
    }

    @FXML
    private void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void deleteDoctor() {
        Doctor doctor = (Doctor) doctorTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(doctor);
        session.getTransaction().commit();
        tableData.remove(doctor);
    }

    @FXML
    private void addHours() throws IOException {
        Doctor doctor = (Doctor) doctorTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NewWorkingHoursView.fxml"));
        AnchorPane pane = loader.load();
        NewWorkingHoursViewController newWorkingHoursViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
        newWorkingHoursViewController.setData(doctor);
    }

    @FXML
    private void updateDoctor(TableColumn.CellEditEvent edditedCell){
        Doctor doctor = (Doctor) doctorTable.getSelectionModel().getSelectedItem();
        if(edditedCell.getTableColumn().getText().equals("Imie")){
            doctor.setName(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Nazwisko")){
            doctor.setLastName(edditedCell.getNewValue().toString());
        }
        else if(edditedCell.getTableColumn().getText().equals("Specjalizacja")){
            doctor.setSpecialization(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Telefon")){
            doctor.setPhoneNumber(edditedCell.getNewValue().toString());
        }
        session.beginTransaction();
        session.update(doctor);
        session.getTransaction().commit();
    }


}

