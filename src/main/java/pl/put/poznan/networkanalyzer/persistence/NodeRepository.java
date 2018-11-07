package pl.put.poznan.networkanalyzer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {

    List<Node> getByType(NodeType type);
}
