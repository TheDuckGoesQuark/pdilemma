package dist.sys.pdilemma.models;

public class PrisonerModel {

    private int prisonerId;
    private Choice choice;

    public PrisonerModel() {
    }

    public PrisonerModel(int prisonerId, Choice choice) {
        this.prisonerId = prisonerId;
        this.choice = choice;
    }

    public int getPrisonerId() {
        return prisonerId;
    }

    public void setPrisonerId(int prisonerId) {
        this.prisonerId = prisonerId;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

}
