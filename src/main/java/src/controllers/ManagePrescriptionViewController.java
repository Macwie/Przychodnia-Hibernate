package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.models.*;
import src.tableModels.PrescriptionMedicineTermModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagePrescriptionViewController implements Initializable {

    private Session session;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<PrescriptionMedicineTermModel> prescriptionTable;

    @FXML
    private TableColumn<PrescriptionMedicineTermModel, Integer> prescriptionId;

    @FXML
    private TableColumn<PrescriptionMedicineTermModel, Integer> prescriptionAmount;

    @FXML
    private TableColumn<PrescriptionMedicineTermModel, String> medicineName;

    @FXML
    private TableColumn<PrescriptionMedicineTermModel, Integer> termId;

    @FXML
    private TextField searchTextField;

    private List<Prescription> basicPrescriptionData;
    private List<PrescriptionMedicineTermModel> prescriptionData;

    private ObservableList<PrescriptionMedicineTermModel> tableData;
    private SortedList<PrescriptionMedicineTermModel> sortedData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        prescriptionId.setCellValueFactory(new PropertyValueFactory<PrescriptionMedicineTermModel, Integer>("Id"));
        prescriptionAmount.setCellValueFactory(new PropertyValueFactory<PrescriptionMedicineTermModel, Integer>("medicinesAmount"));
        medicineName.setCellValueFactory(new PropertyValueFactory<PrescriptionMedicineTermModel, String>("medicineName"));
        termId.setCellValueFactory(new PropertyValueFactory<PrescriptionMedicineTermModel, Integer>("termId"));

        loadPrescriptionData();
    }

    private void loadPrescriptionData() {

        basicPrescriptionData = session.createQuery("from Prescription ").list();
        prescriptionData = new ArrayList<PrescriptionMedicineTermModel>();
        for (Prescription prescription : basicPrescriptionData) {
            PrescriptionMedicineTermModel model = new PrescriptionMedicineTermModel(prescription,prescription.getMedicine(), prescription.getTerm());
            prescriptionData.add(model);
        }
        tableData =  FXCollections.observableArrayList(prescriptionData);
        FilteredList<PrescriptionMedicineTermModel> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PrescriptionMedicineTermModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (PrescriptionMedicineTermModel.getMedicineName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(prescriptionTable.comparatorProperty());
        prescriptionTable.setItems(sortedData);
    }

    @FXML
    void deletePrescription(ActionEvent event) {

        PrescriptionMedicineTermModel model = prescriptionTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(prescriptionData.get(prescriptionData.indexOf(model)).getPrescription());
        session.getTransaction().commit();
        prescriptionData.remove(model);
        basicPrescriptionData.remove(model.getPrescription());
        tableData.remove(model);
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

}
