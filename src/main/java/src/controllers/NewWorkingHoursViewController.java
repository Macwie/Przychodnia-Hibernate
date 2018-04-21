package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.models.Doctor;
import src.models.WorkingHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewWorkingHoursViewController implements Initializable{

    private Session session;
    private Doctor doctor;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField placeTextField;

    @FXML
    private TextField startHourTextField;

    @FXML
    private TextField endHourTextField;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
    }

    @FXML
    void saveHours(ActionEvent event) throws IOException {
        System.out.println(doctor.getName());
        session.beginTransaction();
        String place = placeTextField.getText();
        //String date = datePicker.getValue().toString();
        String startHour = startHourTextField.getText();
        String endHour = startHourTextField.getText();

        WorkingHours workingHours = new WorkingHours(place,Timestamp.valueOf(startHour),Timestamp.valueOf(endHour),true,doctor);
        session.save(workingHours);
        session.getTransaction().commit();
        backToDoctorManage();

    }

    public void setData(Doctor doctor){
        this.doctor = doctor;
    }

    @FXML
    public void backToDoctorManage() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/ManageDoctorView.fxml"));
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
    }
}
