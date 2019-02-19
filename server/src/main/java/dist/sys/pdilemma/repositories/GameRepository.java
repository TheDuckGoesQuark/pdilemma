package dist.sys.pdilemma.repositories;

import dist.sys.pdilemma.entities.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

}
