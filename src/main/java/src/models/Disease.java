package src.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Disease")
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    @Column(name = "Information")
    private String information;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "diseases")
    private Set<Medicine> medicines;

    public Disease() {
    }

    public Disease(String name, String information) {
        this.information = information;
        this.name = name;
        this.medicines = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(Set<Medicine> medicines) {
        this.medicines = medicines;
    }

    public void addMedicines(Medicine medicine){
        this.medicines.add(medicine);
    }
}
