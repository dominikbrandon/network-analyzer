package pl.put.poznan.networkanalyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.ConnectionId;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;
import pl.put.poznan.networkanalyzer.searching.ConnectionSearchParameters;
import pl.put.poznan.networkanalyzer.searching.ConnectionSpecification;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The API for managing connections containing basic CRUD operations.
 * Users should use this facade class instead of repositories directly.
 *
 * @author Dominik Grzelak
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class ConnectionService {
    private ConnectionRepository connectionRepository;
    private NodeRepository nodeRepository;

    /**
     * Creates a new instance of ConnectionService.
     * @param connectionRepository repository of connections
     * @param nodeRepository repository of nodes
     */
    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, NodeRepository nodeRepository) {
        log.debug("Creating an instance of ConnectionService");
        this.connectionRepository = connectionRepository;
        this.nodeRepository = nodeRepository;
    }

    /**
     * Returns list of all connections.
     * @return never null
     */
    public List<Connection> getAll() {
        log.info("Getting all connections");
        return connectionRepository.findAll();
    }

    /**
     * Returns list of all connections that match search criteria.
     * @param searchParameters search criteria, null values are omitted
     * @return never null
     */
    public List<Connection> getBySearchParams(ConnectionSearchParameters searchParameters) {
        log.info("Getting connections by search criteria: " + searchParameters.toString());
        return connectionRepository.findAll(new ConnectionSpecification(searchParameters));
    }

    /**
     * Saves a single connection between two nodes given by their ids.
     * @param connectionDto DTO of the connection
     * @see ConnectionService#saveAll(List)
     */
    public void save(ConnectionDto connectionDto) {
        log.info("Saving single connection by DTO: " + connectionDto.toString());
        saveAll(Collections.singletonList(connectionDto));
    }

    /**
     * Saves all connections given as DTOs.
     * If any of below errors occurs, no connections will be saved.
     * Please note that connected nodes must exist before creating connection between them.
     * @param connectionDtos list of DTOs of connections to be saved
     * @throws javax.persistence.EntityNotFoundException if nodes being connected don't exist
     * @throws RuntimeException if nodes' ids are null
     * @throws RuntimeException if connection already exists between nodes of given ids
     */
    public void saveAll(List<ConnectionDto> connectionDtos) {
        List<Connection> connections = new LinkedList<>();
        connectionDtos.forEach(connectionDto -> {
            log.debug("Creating connection by DTO: " + connectionDto.toString());
            if (connectionDto.from == null || connectionDto.to == null) {
                throw new RuntimeException("Nodes ids must not be null");
            }
            ConnectionId connId = createConnectionId(connectionDto.from, connectionDto.to);
            if (connectionRepository.existsById(connId)) {
                throw new RuntimeException("Connection already exists between: " + connectionDto.from + " " + connectionDto.to);
            }
            connections.add(new Connection(connId, connectionDto.value));
        });
        log.info("Saving all created connections");
        connectionRepository.saveAll(connections);
    }

    /**
     * Saves connection between nodes of ids given as parameters.
     * Please note that it overrides existing connections as well.
     * Also, nodes' ids given in DTO don't matter, as they are always overridden by values of params.
     * @param fromId id of output node
     * @param toId id of input node
     * @param connectionDto DTO of connection being saved
     * @throws javax.persistence.EntityNotFoundException if nodes being connected don't exist
     */
    public void update(Long fromId, Long toId, ConnectionDto connectionDto) {
        log.info("Updating connection from " + fromId + " to " + toId + " with DTO: " + connectionDto.toString());
        ConnectionId connId = createConnectionId(fromId, toId);
        Connection connection = new Connection(connId, connectionDto.value);
        connectionRepository.save(connection);
    }

    /**
     * Deletes connection between two nodes.
     * @param fromId id of output node
     * @param toId if of input node
     * @throws javax.persistence.EntityNotFoundException if nodes don't exist
     * @throws org.springframework.dao.EmptyResultDataAccessException if connection between nodes doesn't exist
     */
    public void delete(Long fromId, Long toId) {
        log.info("Deleting connection from " + fromId + " to " + toId);
        ConnectionId connId = createConnectionId(fromId, toId);
        connectionRepository.deleteById(connId);
    }

    private ConnectionId createConnectionId(Long fromId, Long toId) {
        Node fromNode = nodeRepository.getOne(fromId);
        Node toNode = nodeRepository.getOne(toId);
        return new ConnectionId(fromNode, toNode);
    }
}
