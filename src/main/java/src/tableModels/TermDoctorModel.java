package src.tableModels;

import src.models.Doctor;
import src.models.Term;

import java.sql.Timestamp;

public class TermDoctorModel {

    private Term term;
    private Doctor doctor;

    public TermDoctorModel(Term term, Doctor doctor) {
        this.term = term;
        this.doctor = doctor;
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
}
