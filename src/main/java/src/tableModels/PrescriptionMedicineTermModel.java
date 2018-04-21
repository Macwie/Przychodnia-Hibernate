package src.tableModels;

import src.models.Medicine;
import src.models.Prescription;
import src.models.Term;

public class PrescriptionMedicineTermModel {

    private Prescription prescription;
    private Medicine medicine;
    private Term term;

    public PrescriptionMedicineTermModel(Prescription prescription, Medicine medicine, Term term) {
        this.prescription = prescription;
        this.medicine = medicine;
        this.term = term;
    }

    public int getId() {
        return this.prescription.getId();
    }

    public void setId(int id) {
        this.prescription.setId(id);
    }

    public int getMedicinesAmount() {
        return this.prescription.getMedicinesAmount();
    }

    public void setMedicinesAmount(int medicinesAmount) {
        this.prescription.setMedicinesAmount(medicinesAmount);
    }

    public String getMedicineName() {
        return this.medicine.getName();
    }

    public void setMedicineName(String medicineName) {
        this.medicine.setName(medicineName);
    }

    public int getTermId() {
        return this.term.getId();
    }

    public void setTermId(int termId) {
        this.term.setId(termId);
    }

    public Prescription getPrescription() {
        return this.prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
