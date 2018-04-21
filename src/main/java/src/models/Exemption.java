package src.models;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Exemption")
public class Exemption {
    @Id
    @GeneratedValue(generator = "foreigngen")
    @GenericGenerator(strategy = "foreign", name="foreigngen",
            parameters = @Parameter(name = "property", value="term"))
    @Column(name = "Id")
    private int id;
    @Column(name = "SinceDate")
    private Date sinceDate;
    @Column(name = "UntilDate")
    private Date untilDate;
    @OneToOne(mappedBy = "exemption")
    private Term term;

    public Exemption() {
    }

    public Exemption(Date sinceDate, Date untilDate, Term term) {
        this.sinceDate = sinceDate;
        this.untilDate = untilDate;
        this.term = term;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
