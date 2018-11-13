package pl.put.poznan.networkanalyzer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionId;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId>, JpaSpecificationExecutor<Connection> {

}
