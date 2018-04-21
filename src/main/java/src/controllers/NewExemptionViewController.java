package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.models.Exemption;
import src.models.Referral;
import src.models.Term;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class NewExemptionViewController implements Initializable {

    private Session session;
    private Term term;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField SinceDate;

    @FXML
    private TextField UntilDate;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
    }

    @FXML
    void saveExemption() throws IOException {
        session.beginTransaction();

        Date Since = Date.valueOf(SinceDate.getText());
        Date Until = Date.valueOf(UntilDate.getText());
        Exemption exemption = new Exemption(Since,Until,term);
        session.save(exemption);
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
