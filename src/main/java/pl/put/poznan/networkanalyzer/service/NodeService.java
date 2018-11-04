package pl.put.poznan.networkanalyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The API for managing nodes containing basic CRUD operations.
 * Users should use this facade class instead of repositories directly.
 *
 * @author Dominik Grzelak
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class NodeService {
    private NodeRepository nodeRepository;

    /**
     * Creates a new instance of NodeService.
     * @param nodeRepository repository of nodes
     */
    @Autowired
    public NodeService(NodeRepository nodeRepository) {
        log.debug("Creating instance of NodeService");
        this.nodeRepository = nodeRepository;
    }

    /**
     * Returns list of all nodes.
     * @return never null
     */
    public List<Node> getAll() {
        log.debug("Getting all nodes");
        return nodeRepository.findAll();
    }

    /**
     * Returns node of a given identifier.
     * @param nodeId identifier of the node
     * @return node of a given identifier
     * @throws RuntimeException if no node exists for a given id
     */
    public Node getById(Long nodeId) {
        log.debug("Getting node by id: " + nodeId);
        Optional<Node> node = nodeRepository.findById(nodeId);
        if (node.isPresent()) {
            return node.get();
        }
        throw new RuntimeException("Node with a given id doesn't exist: " + nodeId);
    }

    /**
     * Saves a single node given as a parameter in the repository.
     * @param node node to be saved
     * @throws RuntimeException if id is null
     * @throws RuntimeException if node with given id already exists in the repository
     */
    public void save(Node node) {
        log.debug("Saving node: " + node.toString());
        saveAll(Collections.singletonList(node));
    }

    /**
     * Saves all nodes given as a list in the repository.
     * If any of below errors occurs, no node will be saved.
     * @param nodes list of nodes to be saved
     * @throws RuntimeException if id is null
     * @throws RuntimeException if node with given id already exists in the repository
     */
    public void saveAll(List<Node> nodes) {
        nodes.forEach(node -> {
            log.debug("Checking id of node: " + node.toString());
            Long nodeId = node.getId();
            if (nodeId == null) {
                throw new RuntimeException("Node id must not be null");
            }
            if (nodeRepository.existsById(nodeId)) {
                throw new RuntimeException("Node with given id already exists: " + nodeId);
            }
        });
        log.debug("Saving all processed nodes");
        nodeRepository.saveAll(nodes);
    }

    /**
     * Saves node with an id given as a parameter.
     * Please note that node's id may be of any value, as it will be always overridden with the passed one.
     * @param nodeId expected id of the node
     * @param updatedNode node object to be saved
     */
    public void update(Long nodeId, Node updatedNode) {
        log.debug("Updating node of id " + nodeId + " with the following: " + updatedNode.toString());
        updatedNode.setId(nodeId);
        nodeRepository.save(updatedNode);
    }

    /**
     * Deletes node of a given id from the repository.
     * @param nodeId id of the node to be deleted
     * @throws org.springframework.dao.EmptyResultDataAccessException if node of a given id does not exist in the repository
     */
    public void deleteById(Long nodeId) {
        log.debug("Deleting node of id: " + nodeId);
        nodeRepository.deleteById(nodeId);
    }
}
