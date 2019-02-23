package dist.sys.pdilemma.models;

import javax.validation.constraints.NotNull;

public class ChoiceRequestModel {

    @NotNull
    private Choice choice;

    public ChoiceRequestModel() {
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }
}
