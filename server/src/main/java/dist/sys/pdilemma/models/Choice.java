package dist.sys.pdilemma.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Choice {
    BETRAY("B"),
    COOPERATE("C");

    private String value;

    Choice(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
