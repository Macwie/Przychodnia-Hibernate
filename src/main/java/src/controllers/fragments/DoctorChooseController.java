package src.controllers.fragments;

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
import src.controllers.NewWorkingHoursViewController;
import src.main.HibernateUtil;
import src.main.Notification;
import src.models.Doctor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorChooseController implements Initializable {

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
    }

    public void loadDoctorData() {
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

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public TableView getDoctorTable() {
        return doctorTable;
    }

    public Doctor getSelectedDoctor(){
        Doctor doctor = (Doctor) doctorTable.getSelectionModel().getSelectedItem();
        return doctor;
    }
}

