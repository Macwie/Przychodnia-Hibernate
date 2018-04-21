package src.tableModels;

import src.models.Referral;
import src.models.Term;

public class ReferralTermModel {

    private Referral referral;
    private Term term;

    public ReferralTermModel(Referral referral, Term term) {
        this.referral = referral;
        this.term = term;
    }

    public int getId() {
        return this.referral.getId();
    }

    public void setId(int id) {
        this.referral.setId(id);
    }

    public String getIntent() {
        return this.referral.getIntent();
    }

    public void setIntent(String intent) {
        this.referral.setIntent(intent);
    }

    public int getTermId() {
        return this.term.getId();
    }

    public void setTermId(int termId) {
        this.term.setId(termId);
    }

    public Referral getReferral() {
        return this.referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
