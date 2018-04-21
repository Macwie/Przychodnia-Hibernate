package src.models;

import javax.persistence.*;

@Entity
@Table(name = "Prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    @Column(name = "MedicinesAmount")
    private int medicinesAmount;
    @ManyToOne
    @JoinColumn(name = "MedicineId")
    private Medicine medicine;
    @ManyToOne
    @JoinColumn(name = "TermId")
    private Term term;

    public Prescription() {
    }

    public Prescription(int medicinesAmount, Medicine medicine,Term term) {
        this.medicinesAmount = medicinesAmount;
        this.medicine = medicine;
        this.term = term;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicinesAmount() {
        return medicinesAmount;
    }

    public void setMedicinesAmount(int medicinesAmount) {
        this.medicinesAmount = medicinesAmount;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}