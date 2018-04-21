package src.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(generator = "foreigngen")
    @GenericGenerator(strategy = "foreign", name="foreigngen",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value="patient"))
    private String patientPesel;
    @Column(name = "Street")
    private String street;
    @Column(name = "City")
    private String city;
    @Column(name = "postalcode")
    private String postalCode;

    @OneToOne(mappedBy = "address")
    private Patient patient;

    public Address() {
    }

    public Address(String street, String city, String postalCode, Patient patient) {
        this.patientPesel = patientPesel;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.patient = patient;
    }

    public String getPatientPesel() {
        return patientPesel;
    }

    public void setPatientPesel(String patientPesel) {
        this.patientPesel = patientPesel;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
