package src.tableModels;

import src.models.Doctor;
import src.models.Patient;
import src.models.Term;

import java.sql.Timestamp;

public class TermDoctorPatientModel {

    private Term term;
    private Doctor doctor;
    private Patient patient;

    public TermDoctorPatientModel(Term term, Doctor doctor, Patient patient) {
        this.term = term;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getId() {
        return term.getId();
    }

    public void setId(int id) {
        this.term.setId(id);
    }

    public String getPlace() {
        return this.term.getPlace();
    }

    public void setPlace(String place) {
        this.term.setPlace(place);
    }

    public Timestamp getTermDate() {
        return this.getTerm().getTermDate();
    }

    public void setTermDate(Timestamp termDate) {
        this.term.setTermDate(termDate);
    }

    public String getDoctorName() {
        return this.doctor.getName();
    }

    public void setDoctorName(String lastName) {
        this.doctor.setName(lastName);
    }

    public String getDoctorLastName() {
        return this.doctor.getLastName();
    }

    public void setDoctorLastName(String lastName) {
        this.doctor.setLastName(lastName);
    }

    public String getPatientName() {
        return this.patient.getName();
    }

    public void setPatientName(String name) {
        this.patient.setName(name);
    }

    public String getPatientLastName() {
        return this.patient.getLastName();
    }

    public void setPatienLastName(String lastName) {
        this.patient.setLastName(lastName);
    }
}
