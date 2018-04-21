package src.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Doctor")
public class Doctor {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name")
    private String name;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "Specialization")
    private String specialization;
    @Column(name = "PhoneNumber")
    private String phoneNumber;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<WorkingHours> workingHours;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Term> terms;


    public Doctor() {
    }

    public Doctor(String name, String lastName, String specialization, String phoneNumber) {
        this.name = name;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.workingHours = new HashSet<>();
        this.terms = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Set<WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }
    public void addWorkingHours(WorkingHours workingHours){
        this.workingHours.add(workingHours);
    }

    public Set<Term> getTerms() {
        return terms;
    }

    public void setTerms(Set<Term> terms) {
        this.terms = terms;
    }

    public void addTerms(Term term){
        this.terms.add(term);
    }
}
