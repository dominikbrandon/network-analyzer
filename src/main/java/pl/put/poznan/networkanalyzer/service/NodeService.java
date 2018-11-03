package pl.put.poznan.networkanalyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import java.util.Collections;
import java.util.List;

@Service
public class NodeService {
    private NodeRepository nodeRepository;

    @Autowired
    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public List<Node> getAll() {
        return nodeRepository.findAll();
    }

    public Node getById(Long nodeId) {
        return nodeRepository.getOne(nodeId);
    }

    public void save(Node node) {
        saveAll(Collections.singletonList(node));
    }

    public void saveAll(List<Node> nodes) {
        nodes.forEach(node -> {
            Long nodeId = node.getId();
            if (nodeId != null && nodeRepository.existsById(nodeId)) {
                throw new RuntimeException("Node with given ID already exists: " + nodeId);
            }
        });
        nodeRepository.saveAll(nodes);
    }

    public void update(Long nodeId, Node updatedNode) {
        updatedNode.setId(nodeId);
        nodeRepository.save(updatedNode);
    }

    public void deleteById(Long nodeId) {
        nodeRepository.deleteById(nodeId);
    }
}
