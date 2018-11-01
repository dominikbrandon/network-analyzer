package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionId;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

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
            return connectionRepository.findById_From_IdAndId_To_Id(fromId, toId);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@RequestBody List<Connection> connections) {
        connections.forEach(connection -> {
            ConnectionId connId = connection.getId();
            if (connId != null && connectionRepository.existsById(connId)) {
                throw new RuntimeException("Connection between these nodes already exists: " + connId.getFrom().getId() + " " + connId.getTo().getId());
            }
        });
        connectionRepository.saveAll(connections);
    }

    @PutMapping(value = "/{from}/{to}")
    public void update(@PathVariable("from") Long fromId, @PathVariable("to") Long toId, @RequestBody Connection updatedConnection) {
        Node fromNode = nodeRepository.getOne(fromId);
        Node toNode = nodeRepository.getOne(toId);
        updatedConnection.setId(new ConnectionId(fromNode, toNode));
        connectionRepository.save(updatedConnection);
    }

    @DeleteMapping(value = "/{from}/{to}")
    public void delete(@PathVariable("from") Long fromId, @PathVariable("to") Long toId) {
        Node fromNode = nodeRepository.getOne(fromId);
        Node toNode = nodeRepository.getOne(toId);
        ConnectionId connId = new ConnectionId(fromNode, toNode);
        connectionRepository.deleteById(connId);
    }
}
