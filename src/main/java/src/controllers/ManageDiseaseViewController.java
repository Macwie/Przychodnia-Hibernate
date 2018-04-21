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

public class ManageDiseaseViewController implements Initializable{

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

    private List<Medicine> medicineData;

    private List<Disease> diseaseData;
    private ObservableList<Disease> tableData;
    private SortedList<Disease> sortedData;

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

        diseaseName.setCellFactory(TextFieldTableCell.forTableColumn());
        diseaseInformation.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void loadData() {

        diseaseData = session.createQuery("from Disease ").list();
        tableData =  FXCollections.observableArrayList(diseaseData);
        FilteredList<Disease> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(disease -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (disease.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (disease.getInformation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(diseaseTable.comparatorProperty());
        diseaseTable.setItems(sortedData);

        medicineData = session.createQuery("from Medicine ").list();

        medicineTable.getItems().setAll(medicineData);

    }


    public void deleteDisease(ActionEvent actionEvent) {
        Disease disease = (Disease) diseaseTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(disease);
        session.getTransaction().commit();
        tableData.remove(disease);
    }

    public void updateDisease(TableColumn.CellEditEvent edditedCell) {

        Disease disease = (Disease) diseaseTable.getSelectionModel().getSelectedItem();

        if(edditedCell.getTableColumn().getText().equals("Nazwa")){
            disease.setName(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Informacje")){
            disease.setInformation(edditedCell.getNewValue().toString());
        }

        session.beginTransaction();
        session.update(disease);
        session.getTransaction().commit();
    }

    public void addDisease(ActionEvent actionEvent) {

        session.beginTransaction();

        Disease disease = new Disease(nameTextField.getText(), informationTextField.getText());

        Medicine medicine = (Medicine) medicineTable.getSelectionModel().getSelectedItem();

        if(medicine != null) {
            disease.addMedicines(medicine);
            medicine.addDisease(disease);
        }

        try{
            session.save(disease);
            session.getTransaction().commit();
            tableData.add(disease);
            Notification.makeNotification("INFO","Choroba dodana",3).showInformation();
        }catch (Exception e){
            Notification.makeNotification("ERROR","Blad podczas zapisu",3).showError();
        }

        diseaseTable.getSelectionModel().clearSelection();
        medicineTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
}
