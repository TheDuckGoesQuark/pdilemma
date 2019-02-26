package dist.sys.pdilemma.entities;


import dist.sys.pdilemma.models.GameModel;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "game")
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameId")
    private int gameId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "game")
    private Set<Prisoner> prisoners = new HashSet<>();

    public Game() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Set<Prisoner> getPrisoners() {
        return prisoners;
    }

    public void setPrisoners(Set<Prisoner> prisoners) {
        this.prisoners = prisoners;
    }

    public void addPrisoner(Prisoner prisoner) {
        this.prisoners.add(prisoner);
    }

    public GameModel toModel() {
        return new GameModel(
                this.gameId,
                this.prisoners.stream().map(Prisoner::toModel).collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return getGameId() == game.getGameId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId());
    }
}
