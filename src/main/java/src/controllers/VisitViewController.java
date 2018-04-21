package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import org.hibernate.Session;
import src.controllers.fragments.DoctorChooseController;
import src.controllers.fragments.HoursChooseController;
import src.controllers.fragments.PatientChooseController;
import src.main.HibernateUtil;
import src.main.Notification;
import src.models.Doctor;
import src.models.Patient;
import src.models.Term;
import src.models.WorkingHours;
import src.tableModels.PatientAdressModel;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class VisitViewController implements Initializable {

    private Session session;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView leftArr;

    @FXML
    private ImageView rightArr;

    @FXML
    private AnchorPane fragmentAnchor;

    @FXML
    private Button saveTermButton;

    @FXML
    private Label patientInfoLabel;

    @FXML
    private Label doctorInfoLabel;

    @FXML
    private PatientChooseController patientController;

    @FXML
    private DoctorChooseController doctorController;

    @FXML
    private HoursChooseController hoursController;

    @FXML
    private Button backButton;

    private int activeFragment;

    private Doctor doctor;
    protected WorkingHours workingHours;
    private Patient patient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        activeFragment = 1;
        patientController.loadPatientData();
        patientController.getRootPane().setVisible(true);
        saveTermButton.setDisable(true);
    }

    @FXML
    private void goRight() {
        if (activeFragment == 1) {
            try {
                PatientAdressModel patientAdressModel = patientController.getPatientTable().getSelectionModel().getSelectedItem();
                patient = patientAdressModel.getPatient();
                openDoctorFragment();
            } catch (Exception e) {
                error(1);
            }
        } else if (activeFragment == 2) {
            try {
                doctor = doctorController.getSelectedDoctor();
                doctor.getName(); // sprawdzam czy jest doktor pobrany
                openHoursFragment();
            } catch (Exception e) {
                error(2);
            }
        }
    }

    @FXML
    private void goLeft() {
        if (activeFragment == 2) {
                doctorInfoLabel.setText("niewybrane");
                doctor = null;
                patient = null;
                patientInfoLabel.setText("niewybrany");
                openPatientFragment();
        } else if (activeFragment == 3) {
                workingHours = null;
                doctorInfoLabel.setText("niewybrane");
                doctor = null;
                openDoctorFragment();
        }
    }

    @FXML
    private void saveTerm(){
        try {
            workingHours = hoursController.getSelectedHours();

            Term term = new Term(workingHours.getPlace(), workingHours.getStartHour(),doctor,patient);
            session.beginTransaction();
            session.save(term);
            session.getTransaction().commit();
            Notification.makeNotification("INFO","Wizyta zapisana",3).showInformation();
            back();
        } catch (Exception e) {
            Notification.makeNotification("ERROR","Blad podczas umawiania wizyty",3).showError();
        }
    }


    @FXML
    void back() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/MainView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    /////Fragments METHOD ------->
    private void openDoctorFragment() {
        activeFragment = 2;
        doctorController.loadDoctorData();
        patientController.getRootPane().setVisible(false);
        doctorController.getRootPane().setVisible(true);
        hoursController.getRootPane().setVisible(false);
        patientInfoLabel.setText(patient.getName() + " " + patient.getLastName() + " " + patient.getPesel());
        saveTermButton.setDisable(true);
    }

    private void openHoursFragment() {
        activeFragment = 3;
        hoursController.loadWorkingHoursData(doctor.getId());
        patientController.getRootPane().setVisible(false);
        doctorController.getRootPane().setVisible(false);
        hoursController.getRootPane().setVisible(true);
        doctorInfoLabel.setText(doctor.getName() + " " + doctor.getLastName() + " " + doctor.getSpecialization());
        saveTermButton.setDisable(false);
    }

    private void openPatientFragment() {
        activeFragment = 1;
        patientController.loadPatientData();
        patientController.getRootPane().setVisible(true);
        doctorController.getRootPane().setVisible(false);
        hoursController.getRootPane().setVisible(false);
        saveTermButton.setDisable(true);
    }


    private void error(int error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (error == 2) {
            alert.setTitle("ERROR");
            alert.setHeaderText("Nie wybrales Doktora !!");
            alert.setContentText("Wybierz Doktora");
        } else if (error == 1) {
            alert.setTitle("ERROR");
            alert.setHeaderText("Nie wybrales Pacjenta!!");
            alert.setContentText("Wybierz Pacjenta");
        } else if (error == 3) {
            alert.setTitle("ERROR");
            alert.setHeaderText("Nie wybrales godziny!!");
            alert.setContentText("Wybierz godzine");
        }
        alert.showAndWait();
    }

}
