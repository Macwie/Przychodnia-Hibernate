package src.controllers.fragments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.controllers.VisitViewController;
import src.main.HibernateUtil;
import src.models.WorkingHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HoursChooseController extends VisitViewController implements Initializable {

    private Session session;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<WorkingHours>  hoursTable;

    @FXML
    private TableColumn<WorkingHours, Integer> hoursId;

    @FXML
    private TableColumn<WorkingHours, String> hoursPlace;

    @FXML
    private TableColumn<WorkingHours, Timestamp> hoursStart;

    @FXML
    private TableColumn<WorkingHours, Timestamp> hoursEnd;

    @FXML
    private TableColumn<WorkingHours, Boolean> hoursFree;

    private List<WorkingHours> hoursData;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();

        hoursId.setCellValueFactory(new PropertyValueFactory<WorkingHours, Integer>("Id"));
        hoursPlace.setCellValueFactory(new PropertyValueFactory<WorkingHours, String>("Place"));
        hoursStart.setCellValueFactory(new PropertyValueFactory<WorkingHours, Timestamp>("StartHour"));
        hoursEnd.setCellValueFactory(new PropertyValueFactory<WorkingHours,Timestamp>("EndHour"));
        hoursFree.setCellValueFactory(new PropertyValueFactory<WorkingHours, Boolean>("Free"));
    }

    public void loadWorkingHoursData(int docId) {

        String hql = "from WorkingHours s where s.doctor.id = :id";

        hoursData = session.createQuery(hql)
                .setParameter("id", docId )
                .list();

        hoursTable.getItems().setAll(hoursData);

    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public TableView<WorkingHours> getHoursTable() {
        return hoursTable;
    }

    public WorkingHours getSelectedHours(){
        WorkingHours hours = (WorkingHours) hoursTable.getSelectionModel().getSelectedItem();
        return hours;
    }
}
