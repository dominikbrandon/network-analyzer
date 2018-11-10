package pl.put.poznan.networkanalyzer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.List;

@RestController
@RequestMapping("/nodes")
public class NodeController {
    private NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<Node> getAll() {
        return nodeService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Node getById(@PathVariable("id") Long nodeId) {
        return nodeService.getById(nodeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@RequestBody List<Node> nodes) {
        nodeService.saveAll(nodes);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable("id") Long nodeId, @RequestBody Node updatedNode) {
        nodeService.update(nodeId, updatedNode);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long nodeId) {
        nodeService.deleteById(nodeId);
    }
}
