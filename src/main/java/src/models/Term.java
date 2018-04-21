package src.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Term")
public class Term {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Place")
    private String place;
    @Column(name = "TermDate")
    private Timestamp termDate;
    @ManyToOne
    @JoinColumn(name = "DoctorId")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "PatientPesel")
    private Patient patient;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Exemption exemption;
    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
    private Set<Referral> referrals;
    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
    private Set<Prescription> prescriptions;

    public Term() {
    }

    public Term(String place, Timestamp termDate, Doctor doctor, Patient patient) {
        this.place = place;
        this.termDate = termDate;
        this.doctor = doctor;
        this.patient = patient;
        this.referrals = new HashSet<>();
        this.prescriptions = new HashSet<>();
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

    public Timestamp getTermDate() {
        return termDate;
    }

    public void setTermDate(Timestamp termDate) {
        this.termDate = termDate;
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

    public Exemption getExemption() {
        return exemption;
    }

    public void setExemption(Exemption exemption) {
        this.exemption = exemption;
    }

    public Set<Referral> getReferrals() {
        return referrals;
    }

    public void setReferrals(Set<Referral> referrals) {
        this.referrals = referrals;
    }

    public void addRefferal(Referral referral) {
        this.referrals.add(referral);
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }
}