package dist.sys.pdilemma.repositories;

import dist.sys.pdilemma.entities.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

    @Query("select g from Game g " +
            "left join g.prisoners p " +
            "group by g.gameId " +
            "having count(p) < 2")
    Iterable<Game> findAllJoinable();

    @Query("select g from Game g " +
            "left join g.prisoners p " +
            "group by g.gameId " +
            "having count(p) = 2")
    Iterable<Game> findAllFull();

}
