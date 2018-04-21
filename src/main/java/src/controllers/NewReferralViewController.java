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
import src.models.Referral;
import src.models.Term;
import src.models.WorkingHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class NewReferralViewController implements Initializable {

    private Session session;
    private Term term;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField intentTextField;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
    }

    @FXML
    void saveReferral() throws IOException {
        session.beginTransaction();
        String intent = intentTextField.getText();
        Referral referral = new Referral(intent, term);
        session.save(referral);
        session.getTransaction().commit();
        backToDoctorManage();

    }

    public void setData(Term term) {
        this.term = term;
    }

    @FXML
    public void backToDoctorManage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageTermView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
}
