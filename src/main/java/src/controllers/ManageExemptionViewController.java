package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.models.Doctor;
import src.models.Exemption;
import src.models.Patient;
import src.models.Term;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageExemptionViewController implements Initializable{

    private Session session;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Wrapper> exemptionTable;

    @FXML
    private TableColumn<Exemption, Integer> exemptionId;

    @FXML
    private TableColumn<Exemption, Date> exemptionSince;

    @FXML
    private TableColumn<Exemption, Date> exemptionUntil;


    private List<Exemption> exemptionData;
    private List<Wrapper> wrapper;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();

        exemptionId.setCellValueFactory(new PropertyValueFactory<Exemption, Integer>("id"));
        exemptionSince.setCellValueFactory(new PropertyValueFactory<Exemption, Date>("sinceDate"));
        exemptionUntil.setCellValueFactory(new PropertyValueFactory<Exemption, Date>("untilDate"));

        loadExemptionData();

    }
    private void loadExemptionData() {
        exemptionData = session.createQuery("from Exemption ").list();

        wrapper = new ArrayList<>();

        for (Exemption exemption : exemptionData) {
            wrapper.add(new Wrapper(exemption.getId(), exemption.getSinceDate(), exemption.getUntilDate(), exemption));
        }

        exemptionTable.getItems().setAll(wrapper);
    }

    @FXML
    void deleteExemption(ActionEvent event) {

        Wrapper wrap = (Wrapper) exemptionTable.getSelectionModel().getSelectedItem();
        int index = -1;

        for(int i=0;i<exemptionData.size();i++){
            if(exemptionData.get(i) == wrap.getExemption())
                index = i;
        }

        System.out.println(index);
        session.beginTransaction();
        exemptionData.get(index).getTerm().setExemption(null);
        session.remove(exemptionData.get(index));
        session.getTransaction().commit();
        exemptionData.remove(index);
        wrapper.remove(wrap);
        exemptionTable.getItems().setAll(wrapper);
        exemptionTable.refresh();

    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

    public class Wrapper {

        private int id;
        private Date sinceDate;
        private Date untilDate;
        private Exemption exemption;

        public Wrapper(int id, Date sinceDate, Date untilDate, Exemption exemption) {
            this.id = id;
            this.sinceDate = sinceDate;
            this.untilDate = untilDate;
            this.exemption = exemption;
        }

        public Wrapper(Exemption exemption) {
            this.id = exemption.getId();
            this.sinceDate = exemption.getSinceDate();
            this.untilDate = exemption.getUntilDate();
            this.exemption = exemption;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getSinceDate() {
            return sinceDate;
        }

        public void setSinceDate(Date sinceDate) {
            this.sinceDate = sinceDate;
        }

        public Date getUntilDate() {
            return untilDate;
        }

        public void setUntilDate(Date untilDate) {
            this.untilDate = untilDate;
        }

        public Exemption getExemption() {
            return exemption;
        }

        public void setExemption(Exemption exemption) {
            this.exemption = exemption;
        }

    }
}
