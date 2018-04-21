package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import src.main.HibernateUtil;
import src.main.Notification;
import src.models.*;

import java.io.IOException;
import java.io.PipedReader;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class NewPrescriptionViewController implements Initializable {

    private Session session;

    private Term term;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView medicineTable;

    @FXML
    private TableColumn<Medicine, Integer> medicineId;

    @FXML
    private TableColumn<Medicine, String> medicineName;

    @FXML
    private TableColumn<Medicine, String> medicineInformation;

    @FXML
    private TextField amountField;

    private List<Medicine> medicineData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        session = HibernateUtil.getInstance();


        medicineId.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("Id"));
        medicineName.setCellValueFactory(new PropertyValueFactory<Medicine, String>("Name"));
        medicineInformation.setCellValueFactory(new PropertyValueFactory<Medicine, String>("Information"));

        loadData();

    }

    private void loadData() {
        medicineData = session.createQuery("from Medicine ").list();

        medicineTable.getItems().setAll(medicineData);
    }

    public void setData(Term term) {
        this.term = term;
    }

    @FXML
    void addPrescription() throws IOException {

        int amount = Integer.valueOf(amountField.getText());

        Medicine medicine = (Medicine) medicineTable.getSelectionModel().getSelectedItem();
        Prescription prescription = new Prescription(amount,medicine,term);
        session.beginTransaction();
        session.save(prescription);
        session.getTransaction().commit();
        backToDoctorManage();

    }


    @FXML
    private void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageTermView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void backToDoctorManage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageTermView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
}
