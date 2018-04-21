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
import src.models.Doctor;
import src.models.Patient;
import src.models.Referral;
import src.models.Term;
import src.tableModels.ReferralTermModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageReferralViewController implements Initializable {

    private Session session;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<ReferralTermModel> referralTable;

    @FXML
    private TableColumn<ReferralTermModel, Integer> referralId;

    @FXML
    private TableColumn<ReferralTermModel, String> referralIntent;

    @FXML
    private TableColumn<ReferralTermModel, Integer> termId;

    @FXML
    private TextField searchTextField;

    private List<Referral> basicReferralData;
    private List<ReferralTermModel> referraltData;

    private ObservableList<ReferralTermModel> tableData;
    private SortedList<ReferralTermModel> sortedData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();
        referralId.setCellValueFactory(new PropertyValueFactory<ReferralTermModel, Integer>("Id"));
        referralIntent.setCellValueFactory(new PropertyValueFactory<ReferralTermModel, String>("Intent"));
        termId.setCellValueFactory(new PropertyValueFactory<ReferralTermModel, Integer>("termId"));

        loadReferralData();

        referralIntent.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void loadReferralData() {

        basicReferralData = session.createQuery("from Referral ").list();
        referraltData = new ArrayList<ReferralTermModel>();
        for (Referral referral : basicReferralData) {
            ReferralTermModel model = new ReferralTermModel(referral,referral.getTerm());
            referraltData.add(model);
        }
        tableData =  FXCollections.observableArrayList(referraltData);
        FilteredList<ReferralTermModel> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ReferralTermModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (ReferralTermModel.getIntent().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(referralTable.comparatorProperty());
        referralTable.setItems(sortedData);
    }

    @FXML
    void deleteReferral(ActionEvent event) {

        ReferralTermModel model = referralTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(referraltData.get(referraltData.indexOf(model)).getReferral());
        session.getTransaction().commit();
        referraltData.remove(model);
        basicReferralData.remove(model.getReferral());
        tableData.remove(model);
    }

    @FXML
    void updateReferral(TableColumn.CellEditEvent edditedCell) {

        ReferralTermModel model = referralTable.getSelectionModel().getSelectedItem();
        ReferralTermModel modelBefore = model;

        if (edditedCell.getTableColumn().getText().equals("Skierowanie")) {
            model.setIntent(edditedCell.getNewValue().toString());
        }

        referraltData.set(referraltData.indexOf(modelBefore),model);
        basicReferralData.set(basicReferralData.indexOf(model.getReferral()),model.getReferral());
        session.beginTransaction();
        session.update(referraltData.get(referraltData.indexOf(model)).getReferral());
        session.getTransaction().commit();
    }


    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

}
