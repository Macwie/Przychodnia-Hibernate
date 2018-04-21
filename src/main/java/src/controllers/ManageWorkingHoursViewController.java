package src.controllers;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.main.TimeStampConverter;
import src.models.Address;
import src.models.Doctor;
import src.models.WorkingHours;
import src.tableModels.WorkingHoursDoctorModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageWorkingHoursViewController implements Initializable {

    private Session session;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<WorkingHoursDoctorModel>  hoursTable;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, Integer> hoursId;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, String> hoursPlace;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, Timestamp> hoursStart;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, Timestamp> hoursEnd;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, Boolean> hoursFree;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, String> doctorName;

    @FXML
    private TableColumn<WorkingHoursDoctorModel, String> doctorSurname;

    @FXML
    private TextField searchTextField;

    private List<WorkingHours> basicHoursData;
    private List<WorkingHoursDoctorModel> hoursData;

    private ObservableList<WorkingHoursDoctorModel> tableData;
    private SortedList<WorkingHoursDoctorModel> sortedData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = HibernateUtil.getInstance();

        hoursId.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel, Integer>("Id"));
        hoursPlace.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel, String>("Place"));
        hoursStart.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel, Timestamp>("StartHour"));
        hoursEnd.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel, Timestamp>("EndHour"));
        hoursFree.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel, Boolean>("Free"));
        doctorName.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel,String>("Name"));
        doctorSurname.setCellValueFactory(new PropertyValueFactory<WorkingHoursDoctorModel,String>("LastName"));

        loadWorkingHoursData();

        hoursPlace.setCellFactory(TextFieldTableCell.forTableColumn());
        hoursStart.setCellFactory(TextFieldTableCell.forTableColumn(new TimeStampConverter()));
        hoursEnd.setCellFactory(TextFieldTableCell.forTableColumn(new TimeStampConverter()));
    }

    private void loadWorkingHoursData() {

        basicHoursData = session.createQuery("from WorkingHours ").list();
        hoursData = new ArrayList<WorkingHoursDoctorModel>();
        for (WorkingHours hours : basicHoursData) {
            WorkingHoursDoctorModel model = new WorkingHoursDoctorModel(hours,hours.getDoctor());
            hoursData.add(model);
        }
        tableData =  FXCollections.observableArrayList(hoursData);
        FilteredList<WorkingHoursDoctorModel> filteredData = new FilteredList<>(tableData, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(WorkingHoursDoctorModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (WorkingHoursDoctorModel.getWorkingHours().getPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (WorkingHoursDoctorModel.getDoctor().getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (WorkingHoursDoctorModel.getDoctor().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(hoursTable.comparatorProperty());
        hoursTable.setItems(sortedData);

    }

    public void deleteHour(ActionEvent actionEvent) {
        WorkingHoursDoctorModel model = hoursTable.getSelectionModel().getSelectedItem();
        session.beginTransaction();
        session.remove(hoursData.get(hoursData.indexOf(model)).getWorkingHours());
        session.getTransaction().commit();
        hoursData.remove(model);
        basicHoursData.remove(model.getWorkingHours());
        tableData.remove(model);
    }

    @FXML
    private void updateHour(TableColumn.CellEditEvent edditedCell){
        WorkingHoursDoctorModel model = hoursTable.getSelectionModel().getSelectedItem();
        WorkingHoursDoctorModel modelBefore = model;

        if(edditedCell.getTableColumn().getText().equals("Miejsce")){
            model.setPlace(edditedCell.getNewValue().toString());
        }
        else if(edditedCell.getTableColumn().getText().equals("Godzina otwarcia")){
            model.setStartHour((Timestamp.valueOf(edditedCell.getNewValue().toString())));
        }else if(edditedCell.getTableColumn().getText().equals("Godzina zamknięcia")) {
            model.setEndHour((Timestamp.valueOf(edditedCell.getNewValue().toString())));
        }

        hoursData.set(hoursData.indexOf(modelBefore),model);
        basicHoursData.set(basicHoursData.indexOf(model.getWorkingHours()),model.getWorkingHours());
        session.beginTransaction();
        session.update(hoursData.get(hoursData.indexOf(model)).getWorkingHours());
        session.getTransaction().commit();
    }

    @FXML
    private void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }

}
