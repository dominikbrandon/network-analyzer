package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import java.util.List;

@RestController
@RequestMapping("/nodes")
public class NodeController {
    private NodeRepository nodeRepository;

    @Autowired
    public NodeController(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @GetMapping
    public List<Node> findAll() {
        return nodeRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Node getOne(@PathVariable("id") Long nodeId) {
        return nodeRepository.getOne(nodeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Node node) {
        if (node.getId() != null && nodeRepository.existsById(node.getId())) {
            throw new RuntimeException("Node with given ID already exists: " + node.getId());
        }
        nodeRepository.save(node);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable("id") Long nodeId, Node updatedNode) {
        updatedNode.setId(nodeId);
        nodeRepository.save(updatedNode);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long nodeId) {
        nodeRepository.deleteById(nodeId);
    }
}
