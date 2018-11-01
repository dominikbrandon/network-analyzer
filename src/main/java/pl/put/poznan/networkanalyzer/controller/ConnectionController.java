package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.ConnectionId;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionController {
    private ConnectionRepository connectionRepository;
    private NodeRepository nodeRepository;

    @Autowired
    public ConnectionController(ConnectionRepository connectionRepository, NodeRepository nodeRepository) {
        this.connectionRepository = connectionRepository;
        this.nodeRepository = nodeRepository;
    }

    @GetMapping
    public List<Connection> findAll(@RequestParam(value = "from", required = false) Long fromId,
                                    @RequestParam(value = "to", required = false) Long toId) {
        if (fromId == null && toId == null) {
            return connectionRepository.findAll();
        } else if (fromId == null) {
            return connectionRepository.findById_To_Id(toId);
        } else if (toId == null) {
            return connectionRepository.findById_From_Id(fromId);
        } else {
            ConnectionId connId = getConnectionIdFromNodesIds(fromId, toId);
            return Collections.singletonList(connectionRepository.getOne(connId));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@RequestBody List<ConnectionDto> connectionDtos) {
        List<Connection> connections = new LinkedList<>();
        connectionDtos.forEach(connectionDto -> {
            ConnectionId connId = getConnectionIdFromNodesIds(connectionDto.getFrom(), connectionDto.getTo());
            connections.add(new Connection(connId, connectionDto.getValue()));
        });
        connectionRepository.saveAll(connections);
    }

    @PutMapping(value = "/{from}/{to}")
    public void update(@PathVariable("from") Long fromId, @PathVariable("to") Long toId,
                       @RequestBody ConnectionDto connectionDto) {
        ConnectionId connId = getConnectionIdFromNodesIds(fromId, toId);
        Connection connection = connectionRepository.getOne(connId);
        connection.setValue(connectionDto.getValue());
        connectionRepository.save(connection);
    }

    @DeleteMapping(value = "/{from}/{to}")
    public void delete(@PathVariable("from") Long fromId, @PathVariable("to") Long toId) {
        ConnectionId connId = getConnectionIdFromNodesIds(fromId, toId);
        connectionRepository.deleteById(connId);
    }

    private ConnectionId getConnectionIdFromNodesIds(Long fromId, Long toId) {
        Node fromNode = nodeRepository.getOne(fromId);
        Node toNode = nodeRepository.getOne(toId);
        return new ConnectionId(fromNode, toNode);
    }
}
