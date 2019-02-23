package dist.sys.pdilemma.entities;

import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.models.PrisonerModel;

import javax.persistence.*;

@Entity
@Table(name = "prisoner")
public class Prisoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prisonerId")
    private int prisonerId;

    @Column(name = "choice", columnDefinition = "CHAR(1)")
    private Choice choice;

    @ManyToOne
    private Game game;

    public Prisoner() {
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PrisonerModel toModel() {
        return new PrisonerModel(prisonerId, choice);
    }
}
