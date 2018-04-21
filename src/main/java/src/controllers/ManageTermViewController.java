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
import src.main.DateConverter;
import src.main.HibernateUtil;
import src.main.TimeStampConverter;
import src.models.Doctor;
import src.models.Patient;
import src.models.Term;
import src.tableModels.TermDoctorPatientModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageTermViewController implements Initializable{

    private Session session;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<TermDoctorPatientModel> termTable;

    @FXML
    private TableColumn<TermDoctorPatientModel, Integer> termId;

    @FXML
    private TableColumn<TermDoctorPatientModel, String> termPlace;

    @FXML
    private TableColumn<TermDoctorPatientModel, Timestamp> termDate;

    @FXML
    private TableColumn<TermDoctorPatientModel, String> doctorName;

    @FXML
    private TableColumn<TermDoctorPatientModel, String> doctorSurname;

    @FXML
    private TableColumn<TermDoctorPatientModel, String> patientName;

    @FXML
    private TableColumn<TermDoctorPatientModel, String> patientSurname;

    @FXML
    private TextField searchTextField;


    private List<Term> basicTermData;
    private List<TermDoctorPatientModel> termData;

    private ObservableList<TermDoctorPatientModel> tableData;
    private SortedList<TermDoctorPatientModel> sortedData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        termId.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, Integer>("Id"));
        termPlace.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, String>("Place"));
        termDate.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, Timestamp>("TermDate"));
        doctorName.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, String>("doctorName"));
        doctorSurname.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, String>("doctorLastName"));
        patientName.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, String>("patientName"));
        patientSurname.setCellValueFactory(new PropertyValueFactory<TermDoctorPatientModel, String>("patientLastName"));

        loadTermData();

        termPlace.setCellFactory(TextFieldTableCell.forTableColumn());
        termDate.setCellFactory(TextFieldTableCell.forTableColumn(new TimeStampConverter()));
    }
    private void loadTermData() {

        basicTermData = session.createQuery("from Term ").list();
        termData = new ArrayList<TermDoctorPatientModel>();
        for (Term term : basicTermData) {
            TermDoctorPatientModel model = new TermDoctorPatientModel(term, term.getDoctor(), term.getPatient());
            termData.add(model);
        }
        tableData =  FXCollections.observableArrayList(termData);
        FilteredList<TermDoctorPatientModel> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(TermDoctorPatientModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (TermDoctorPatientModel.getTerm().getPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (TermDoctorPatientModel.getDoctor().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (TermDoctorPatientModel.getDoctor().getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (TermDoctorPatientModel.getPatient().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (TermDoctorPatientModel.getPatient().getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(termTable.comparatorProperty());
        termTable.setItems(sortedData);
    }

    @FXML
    void deleteTerm(ActionEvent event) {

        TermDoctorPatientModel model = termTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(termData.get(termData.indexOf(model)).getPatient());
        session.getTransaction().commit();
        termData.remove(model);
        basicTermData.remove(model.getPatient());
        tableData.remove(model);
    }

    @FXML
    void updateTerm(TableColumn.CellEditEvent edditedCell) {

        TermDoctorPatientModel model = termTable.getSelectionModel().getSelectedItem();
        TermDoctorPatientModel modelBefore = model;

        if(edditedCell.getTableColumn().getText().equals("Miejsce")){
            model.setPlace(edditedCell.getNewValue().toString());
        }else if(edditedCell.getTableColumn().getText().equals("Data")){
            model.setTermDate(Timestamp.valueOf(edditedCell.getNewValue().toString()));
        }

        termData.set(termData.indexOf(modelBefore),model);
        basicTermData.set(basicTermData.indexOf(model.getTerm()),model.getTerm());
        session.beginTransaction();
        session.update(termData.get(termData.indexOf(model)).getPatient());
        session.getTransaction().commit();

    }

    @FXML
    private void addReferral() throws IOException {
        Term term = (Term) termTable.getSelectionModel().getSelectedItem().getTerm();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NewRefferalsView.fxml"));
        AnchorPane pane = loader.load();
        NewReferralViewController newReferralViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
        newReferralViewController.setData(term);
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void addExemption(ActionEvent event) throws IOException {
        Term term = (Term) termTable.getSelectionModel().getSelectedItem().getTerm();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NewExemptionView.fxml"));
        AnchorPane pane = loader.load();
        NewExemptionViewController newExemptionViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
        newExemptionViewController.setData(term);
    }



    @FXML
    void addPrescription(ActionEvent event) throws IOException {
        Term term = (Term) termTable.getSelectionModel().getSelectedItem().getTerm();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NewPrescriptionView.fxml"));
        AnchorPane pane = loader.load();
        NewPrescriptionViewController newPrescriptionViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
        newPrescriptionViewController.setData(term);
    }

}
