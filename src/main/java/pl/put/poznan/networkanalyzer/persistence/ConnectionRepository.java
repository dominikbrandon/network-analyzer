package pl.put.poznan.networkanalyzer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionId;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {
}
