package src.models;

import javax.persistence.*;

@Entity
@Table(name = "Referral")
public class Referral {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Intent")
    private String intent;
    @ManyToOne
    @JoinColumn(name = "TermId")
    private Term term;

    public Referral() {
    }

    public Referral(String intent, Term term) {
        this.intent = intent;
        this.term = term;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
