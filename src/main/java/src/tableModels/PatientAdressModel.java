package src.tableModels;

import src.models.Address;
import src.models.Patient;

import java.sql.Date;


public class PatientAdressModel {
    private Patient patient;
    private Address address;

    public PatientAdressModel(Patient patient, Address address) {
        this.patient = patient;
        this.address = address;
    }

    public String getPesel() {
        return patient.getPesel();
    }

    public void setPesel(String pesel) {
        this.patient.setPesel(pesel);
    }

    public String getName() {
        return patient.getName();
    }

    public void setName(String name) {
        this.patient.setName(name);
    }

    public String getLastName() {
        return patient.getLastName();
    }

    public void setLastName(String lastName) {
        this.patient.setLastName(lastName);
    }

    public Date getBirthDate() {
        return patient.getBirthDate();
    }

    public void setBirthDate(Date birthDate) {
        this.patient.setBirthDate(birthDate);
    }

    public String getPhoneNumber() {
        return patient.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
       this.patient.setPhoneNumber(phoneNumber);
    }

    public String getEmail() {
        return patient.getEmail();
    }

    public void setEmail(String email) {
        this.patient.setEmail(email);
    }

    public String getPatientPesel() {
        return address.getPatientPesel();
    }

    public void setPatientPesel(String patientPesel) {
        this.address.setPatientPesel(patientPesel);
    }

    public String getStreet() {
        return address.getStreet();
    }

    public void setStreet(String street) {
        this.address.setStreet(street);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(String city) {
        this.address.setCity(city);
    }

    public String getPostalCode() {
        return address.getPostalCode();
    }

    public void setPostalCode(String postalCode) {
        this.address.setPostalCode(postalCode);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
