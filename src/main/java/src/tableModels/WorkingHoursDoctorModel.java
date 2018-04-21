package src.tableModels;

import src.models.Doctor;
import src.models.WorkingHours;

import java.sql.Timestamp;

public class WorkingHoursDoctorModel {

    private WorkingHours workingHours;
    private Doctor doctor;

    public WorkingHoursDoctorModel(WorkingHours workingHours, Doctor doctor) {
        this.workingHours = workingHours;
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getId() {
        return this.workingHours.getId();
    }

    public void setId(int id) {
        this.workingHours.setId(id);
    }

    public String getPlace() {
        return this.workingHours.getPlace();
    }

    public void setPlace(String place) {
        this.workingHours.setPlace(place);
    }

    public Timestamp getStartHour() {
        return this.workingHours.getStartHour();
    }

    public void setStartHour(Timestamp startHour) {
        this.workingHours.setStartHour(startHour);
    }

    public Timestamp getEndHour() {
        return this.workingHours.getEndHour();
    }

    public void setEndHour(Timestamp endHour) {
        this.workingHours.setEndHour(endHour);
    }

    public boolean isFree() {
        return this.workingHours.isFree();
    }

    public void setFree(boolean free) {
        this.workingHours.setFree(free);
    }

    public String getName() {
        return this.doctor.getName();
    }

    public void setName(String name) {
        this.doctor.setName(name);
    }

    public String getLastName() {
        return this.doctor.getLastName();
    }

    public void setLastName(String lastName) {
        this.doctor.setLastName(lastName);
    }

    public WorkingHours getWorkingHours() {
        return this.workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }
}
