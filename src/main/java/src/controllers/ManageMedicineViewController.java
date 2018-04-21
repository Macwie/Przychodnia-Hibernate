package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.controllers.NewWorkingHoursViewController;
import src.main.HibernateUtil;
import src.main.Notification;
import src.models.Disease;
import src.models.Disease;
import src.models.Medicine;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageMedicineViewController implements Initializable{

    private Session session;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView diseaseTable;

    @FXML
    private TableView medicineTable;

    @FXML
    private TableColumn<Disease, Integer> diseaseId;

    @FXML
    private TableColumn<Disease, String> diseaseName;

    @FXML
    private TableColumn<Disease, String> diseaseInformation;

    @FXML
    private TableColumn<Medicine, Integer> medicineId;

    @FXML
    private TableColumn<Medicine, String> medicineName;

    @FXML
    private TableColumn<Medicine, String> medicineInformation;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField informationTextField;

    @FXML
    private TextField searchTextField;

    private List<Disease> diseaseData;

    private List<Medicine> medicineData;
    private ObservableList<Medicine> tableData;
    private SortedList<Medicine> sortedData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        session = HibernateUtil.getInstance();

        diseaseId.setCellValueFactory(new PropertyValueFactory<Disease, Integer>("Id"));
        diseaseName.setCellValueFactory(new PropertyValueFactory<Disease, String>("Name"));
        diseaseInformation.setCellValueFactory(new PropertyValueFactory<Disease, String>("Information"));

        medicineId.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("Id"));
        medicineName.setCellValueFactory(new PropertyValueFactory<Medicine, String>("Name"));
        medicineInformation.setCellValueFactory(new PropertyValueFactory<Medicine, String>("Information"));

        loadData();

        medicineName.setCellFactory(TextFieldTableCell.forTableColumn());
        medicineInformation.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void loadData() {

        medicineData = session.createQuery("from Medicine ").list();
        tableData =  FXCollections.observableArrayList(medicineData);
        FilteredList<Medicine> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medicine -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (medicine.getInformation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(medicineTable.comparatorProperty());
        medicineTable.setItems(sortedData);

        diseaseData = session.createQuery("from Disease ").list();

        diseaseTable.getItems().setAll(diseaseData);

    }


    public void deleteMedicine(ActionEvent actionEvent) {
        Medicine medicine = (Medicine) medicineTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(medicine);
        session.getTransaction().commit();
        tableData.remove(medicine);
    }

    public void updateMedicine(TableColumn.CellEditEvent edditedCell) {

        Medicine medicine = (Medicine) medicineTable.getSelectionModel().getSelectedItem();

        if(edditedCell.getTableColumn().getText().equals("Nazwa")){
            medicine.setName(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Informacje")){
            medicine.setInformation(edditedCell.getNewValue().toString());
        }

        session.beginTransaction();
        session.update(medicine);
        session.getTransaction().commit();
    }

    public void addMedicine(ActionEvent actionEvent) {

        session.beginTransaction();

        Medicine medicine = new Medicine(nameTextField.getText(), informationTextField.getText());

        Disease disease = (Disease) diseaseTable.getSelectionModel().getSelectedItem();

        if(disease != null) {
            medicine.addDisease(disease);
            disease.addMedicines(medicine);
        }

        try{
            session.save(medicine);
            session.getTransaction().commit();
            tableData.add(medicine);
            Notification.makeNotification("INFO","Lekarstwo dodane",3).showInformation();
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
}
