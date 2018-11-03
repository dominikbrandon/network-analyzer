package pl.put.poznan.networkanalyzer.service;

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

@Service
public class ConnectionService {
    private ConnectionRepository connectionRepository;
    private NodeRepository nodeRepository;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, NodeRepository nodeRepository) {
        this.connectionRepository = connectionRepository;
        this.nodeRepository = nodeRepository;
    }

    public List<Connection> getAll() {
        return connectionRepository.findAll();
    }

    public List<Connection> getBySearchParams(ConnectionSearchParameters searchParameters) {
        return connectionRepository.findAll(new ConnectionSpecification(searchParameters));
    }

    public void save(ConnectionDto connectionDto) {
        saveAll(Collections.singletonList(connectionDto));
    }

    public void saveAll(List<ConnectionDto> connectionDtos) {
        List<Connection> connections = new LinkedList<>();
        connectionDtos.forEach(connectionDto -> {
            ConnectionId connId = createConnectionId(connectionDto.from, connectionDto.to);
            if (connectionRepository.existsById(connId)) {
                throw new RuntimeException("Connection already exists between: " + connectionDto.from + " " + connectionDto.to);
            }
            connections.add(new Connection(connId, connectionDto.value));
        });
        connectionRepository.saveAll(connections);
    }

    public void update(Long fromId, Long toId, ConnectionDto connectionDto) {
        ConnectionId connId = createConnectionId(fromId, toId);
        Connection connection = connectionRepository.getOne(connId);
        connection.setValue(connectionDto.value);
        connectionRepository.save(connection);
    }

    public void delete(Long fromId, Long toId) {
        ConnectionId connId = createConnectionId(fromId, toId);
        connectionRepository.deleteById(connId);
    }

    private ConnectionId createConnectionId(Long fromId, Long toId) {
        Node fromNode = nodeRepository.getOne(fromId);
        Node toNode = nodeRepository.getOne(toId);
        return new ConnectionId(fromNode, toNode);
    }
}
