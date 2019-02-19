package dist.sys.pdilemma.models;

public class PrisonerModel {

    private int prisonerId;
    private Choice prisonerChoice;

    public PrisonerModel() {
    }

    public PrisonerModel(int prisonerId, Choice prisonerChoice) {
        this.prisonerId = prisonerId;
        this.prisonerChoice = prisonerChoice;
    }

    public int getPrisonerId() {
        return prisonerId;
    }

    public void setPrisonerId(int prisonerId) {
        this.prisonerId = prisonerId;
    }

    public Choice getPrisonerChoice() {
        return prisonerChoice;
    }

    public void setPrisonerChoice(Choice prisonerChoice) {
        this.prisonerChoice = prisonerChoice;
    }

}
