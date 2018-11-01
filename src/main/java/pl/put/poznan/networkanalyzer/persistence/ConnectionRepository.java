package pl.put.poznan.networkanalyzer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionId;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {

    List<Connection> findById_From_Id(Long fromId);

    List<Connection> findById_To_Id(Long toId);

    List<Connection> findById_From_IdAndId_To_Id(Long fromId, Long toId);
}
