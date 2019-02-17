package dist.sys.pdilemma.models;

public class ChoiceRequestModel {

    public static final String COOPERATE = "C";
    public static final String BETRAY = "B";

    private String choice;

    public ChoiceRequestModel() {
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
