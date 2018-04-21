package src.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Medicine")
public class Medicine {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Information")
    private String information;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Disases_Medicines",
            joinColumns = @JoinColumn(name = "MedicineId"),
            inverseJoinColumns = @JoinColumn(name = "DisaseId")
    )
    private Set<Disease> diseases;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private Set<Prescription> prescriptions;


    public Medicine() {
    }

    public Medicine(String name, String information) {
        this.name = name;
        this.information = information;
        this.diseases = new HashSet<>();
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(Set<Disease> diseases) {
        this.diseases = diseases;
    }

    public void addDisease(Disease disease){
        this.diseases.add(disease);
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescription(Prescription prescription){
        this.prescriptions.add(prescription);
    }
}
