package src.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "WorkingHours")
public class WorkingHours {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Place")
    private String place;
    @Column(name = "StartHour")
    private Timestamp startHour;
    @Column(name = "EndHour")
    private Timestamp endHour;
    @Column(name = "Free")
    private boolean free;
    @ManyToOne
    @JoinColumn(name = "DoctorId")
    private Doctor doctor;

    public WorkingHours() {
    }

    public WorkingHours(String place, Timestamp startHour, Timestamp endHour, boolean free, Doctor doctor) {
        this.place = place;
        this.startHour = startHour;
        this.endHour = endHour;
        this.free = free;
        this.doctor = doctor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Timestamp getStartHour() {
        return startHour;
    }

    public void setStartHour(Timestamp startHour) {
        this.startHour = startHour;
    }

    public Timestamp getEndHour() {
        return endHour;
    }

    public void setEndHour(Timestamp endHour) {
        this.endHour = endHour;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
