package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import src.main.HibernateUtil;
import src.models.*;
import src.tableModels.PatientAdressModel;
import src.tableModels.PrescriptionMedicineTermModel;
import src.tableModels.ReferralTermModel;
import src.tableModels.TermDoctorModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientDetailsController implements Initializable{

    private PatientAdressModel patient;
    private Session session;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<TermDoctorModel> termTable;

    @FXML
    private TableView<PrescriptionMedicineTermModel> prescriptionTable;

    @FXML
    private TableView<Exemption> exemptionTable;

    @FXML
    private TableView<ReferralTermModel> referralTable;

    @FXML
    private TableColumn<TermDoctorModel, Integer> termId;

    @FXML
    private TableColumn<TermDoctorModel, String> termPlace;

    @FXML
    private TableColumn<TermDoctorModel, Timestamp> termDate;

    @FXML
    private TableColumn<TermDoctorModel, String> doctorName;

    @FXML
    private TableColumn<TermDoctorModel, String> doctorSurname;

    @FXML
    private TableColumn<?, Integer> prescId;

    @FXML
    private TableColumn<?, Integer> medicineAmount;

    @FXML
    private TableColumn<?, String> medicineName;

    @FXML
    private TableColumn<?, Integer> prescTermId;

    @FXML
    private TableColumn<?, Integer> referralId;

    @FXML
    private TableColumn<?, String> referralIntent;

    @FXML
    private TableColumn<?, Integer> referralTermId;

    @FXML
    private TableColumn<?, Integer> exemptionId;

    @FXML
    private TableColumn<?, Date> exemptionSince;

    @FXML
    private TableColumn<?, Date> exemptionUntil;

    @FXML
    private Label name;

    @FXML
    private Label lastName;

    @FXML
    private Label pesel;

    @FXML
    private Label birth;

    @FXML
    private Label phone;

    @FXML
    private Label email;

    private List<TermDoctorModel> termData = new ArrayList<>();
    private List<PrescriptionMedicineTermModel> prescData = new ArrayList<>();
    private List<ReferralTermModel> referralData = new ArrayList<>();
    private List<Exemption> exemptionData = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        session = HibernateUtil.getInstance();

        termId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        termPlace.setCellValueFactory(new PropertyValueFactory<>("Place"));
        termDate.setCellValueFactory(new PropertyValueFactory<>("TermDate"));
        doctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        doctorSurname.setCellValueFactory(new PropertyValueFactory<>("doctorLastName"));

        prescId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        medicineAmount.setCellValueFactory(new PropertyValueFactory<>("medicinesAmount"));
        medicineName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        prescTermId.setCellValueFactory(new PropertyValueFactory<>("termId"));

        referralId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        referralIntent.setCellValueFactory(new PropertyValueFactory<>("Intent"));
        referralTermId.setCellValueFactory(new PropertyValueFactory<>("termId"));

        exemptionId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        exemptionSince.setCellValueFactory(new PropertyValueFactory<>("SinceDate"));
        exemptionUntil.setCellValueFactory(new PropertyValueFactory<>("UntilDate"));

    }

    public void setData(PatientAdressModel patient) {
        this.patient = patient;
        name.setText(patient.getName());
        lastName.setText(patient.getLastName());
        pesel.setText(patient.getPesel());
        birth.setText(String.valueOf(patient.getBirthDate()));
        phone.setText(patient.getPhoneNumber());
        email.setText(patient.getEmail());

        List<Term> basicTermData = session.createQuery("from Term t where t.patient.pesel ="+patient.getPesel()).list();
        for (Term term : basicTermData) {
            TermDoctorModel model = new TermDoctorModel(term,term.getDoctor());
            termData.add(model);
        }
        List<Prescription> basicPrescData = session.createQuery("from Prescription p where p.term.patient.pesel ="+patient.getPesel()).list();
        for (Prescription presc : basicPrescData) {
            PrescriptionMedicineTermModel model = new PrescriptionMedicineTermModel(presc,presc.getMedicine(), presc.getTerm());
            prescData.add(model);
        }
        List<Referral> basicReferralData = session.createQuery("from Referral r  where r.term.patient.pesel ="+patient.getPesel()).list();
        for (Referral referral : basicReferralData) {
            ReferralTermModel model = new ReferralTermModel(referral,referral.getTerm());
            referralData.add(model);
        }
        exemptionData = session.createQuery("from Exemption e where e.term.patient.pesel ="+patient.getPesel()).list();


        termTable.getItems().setAll(termData);
        prescriptionTable.getItems().setAll(prescData);
        referralTable.getItems().setAll(referralData);
        exemptionTable.getItems().setAll(exemptionData);
    }

    @FXML
    public void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManagePatientView.fxml"));
        AnchorPane pane = loader.load();
        rootPane.getChildren().setAll(pane);
    }
}
