package pl.put.poznan.networkanalyzer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.put.poznan.networkanalyzer.model.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
}
