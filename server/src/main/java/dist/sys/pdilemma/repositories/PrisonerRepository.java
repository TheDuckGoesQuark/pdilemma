package dist.sys.pdilemma.repositories;

import dist.sys.pdilemma.entities.Prisoner;
import org.springframework.data.repository.CrudRepository;

public interface PrisonerRepository extends CrudRepository<Prisoner, Integer> {
}
