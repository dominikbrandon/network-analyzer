package pl.put.poznan.networkanalyzer.algorithms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Lazy
public class DfsAlgorithm {
    private NodeService nodeService;
    private AlgorithmResult bestResult = null;

    @Autowired
    public DfsAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public AlgorithmResult dfs() {
        Node entry = getNodeOfTypeWhenOnlyOneExists(NodeType.ENTRY);
        Node exit = getNodeOfTypeWhenOnlyOneExists(NodeType.EXIT);
        AlgorithmResult path = new AlgorithmResult();
        path.nodes.add(entry);
        startDfs(entry.getId(), exit.getId(), path);
        return bestResult;
    }

    private void startDfs(Long id, Long exit, AlgorithmResult path) {
        log.debug(path.toString());
        if(bestResult != null && path.totalValue >= bestResult.totalValue){
            return;
        }
        if (Objects.equals(id, exit)) {
            AlgorithmResult newPath = new AlgorithmResult(path);
            log.debug("Dodałem path");
            bestResult = newPath;
            return;
        }
        Node current = nodeService.getById(id);
        List<Connection> neighbours = current.getOutgoing();
        log.debug(neighbours.toString());
        for (Connection neighbour : neighbours) {
            Node nodeTo = neighbour.getId().getTo();
            path.nodes.add(nodeTo);
            path.totalValue += neighbour.getValue();
            startDfs(nodeTo.getId(), exit, path);
            path.totalValue -= neighbour.getValue();
            path.nodes.remove(nodeTo);
        }
    }
    
    private Node getNodeOfTypeWhenOnlyOneExists(NodeType type) {
        List<Node> entries = nodeService.getByType(type);
        if (entries.size() != 1) {
            throw new RuntimeException("There must be 1 node of type " + type + ", found: " + entries.size());
        }
        return entries.get(0);
    }
}
