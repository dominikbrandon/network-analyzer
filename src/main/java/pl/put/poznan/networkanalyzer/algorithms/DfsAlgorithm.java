package pl.put.poznan.networkanalyzer.algorithms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Lazy
public class DfsAlgorithm {
    private NodeService nodeService;
    private AlgorithmResult bestResult;

    @Autowired
    public DfsAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public AlgorithmResult compute() {
        log.info("Running DFS algorithm");
        long startTime = System.nanoTime();
        Node entry = getNodeOfTypeWhenOnlyOneExists(NodeType.ENTRY);
        Node exit = getNodeOfTypeWhenOnlyOneExists(NodeType.EXIT);
        AlgorithmResult path = new AlgorithmResult();
        path.nodes.add(entry);
        bestResult = null;
        stepInto(entry, exit.getId(), path);
        long elapsedTime = System.nanoTime() - startTime;
        log.info("Finished DFS algorithm in " + elapsedTime / 1000000 + " ms");
        return bestResult;
    }

    private void stepInto(Node currentNode, Long exitId, AlgorithmResult path) {
        if (bestResult != null && path.totalValue >= bestResult.totalValue) {
            return;
        }
        if (Objects.equals(currentNode.getId(), exitId)) {
            bestResult = new AlgorithmResult(path);
            return;
        }
        List<Connection> neighbours = currentNode.getOutgoing();
        for (Connection neighbour : neighbours) {
            Node nextNode = neighbour.getId().getTo();
            path.nodes.add(nextNode);
            path.totalValue += neighbour.getValue();
            stepInto(nextNode, exitId, path);
            path.totalValue -= neighbour.getValue();
            path.nodes.remove(nextNode);
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
