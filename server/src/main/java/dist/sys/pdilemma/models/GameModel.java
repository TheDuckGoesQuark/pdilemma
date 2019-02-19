package dist.sys.pdilemma.models;

import java.util.Set;

public class GameModel {

    int gameId;
    Set<PrisonerModel> prisoners;

    public GameModel(int gameId, Set<PrisonerModel> prisoners) {
        this.gameId = gameId;
        this.prisoners = prisoners;
    }

    public int getGameId() {
        return gameId;
    }

    public Set<PrisonerModel> getPrisoners() {
        return prisoners;
    }

}
