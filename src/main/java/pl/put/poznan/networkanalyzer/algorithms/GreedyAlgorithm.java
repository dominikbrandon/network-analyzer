package pl.put.poznan.networkanalyzer.algorithms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.List;

@Service
@Slf4j
public class GreedyAlgorithm {
    private NodeService nodeService;

    @Autowired
    public GreedyAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public AlgorithmResult compute() {
        Node currentNode = getNodeOfTypeWhenOnlyOneExists(NodeType.ENTRY);
        Node exit = getNodeOfTypeWhenOnlyOneExists(NodeType.EXIT);
        AlgorithmResult result = new AlgorithmResult();
        result.nodes.add(currentNode);

        Long currentId = currentNode.getId();
        Long exitId = exit.getId();
        while (!exitId.equals(currentId)) {
            Connection nextConnection = getCheapestOutgoing(currentNode);
            currentNode = nextConnection.getId().getTo();
            currentId = currentNode.getId();

            result.totalValue += nextConnection.getValue();
            result.nodes.add(currentNode);
        }
        return result;
    }

    private Connection getCheapestOutgoing(Node examined) {
        List<Connection> successors = examined.getOutgoing();
        Connection cheapest = successors.get(0);
        for (Connection successor : successors) {
            if (successor.getValue() < cheapest.getValue()) {
                cheapest = successor;
            }
        }
        return cheapest;
    }

    private Node getNodeOfTypeWhenOnlyOneExists(NodeType type) {
        List<Node> entries = nodeService.getByType(type);
        if (entries.size() != 1) {
            throw new RuntimeException("There must be 1 node of type " + type + ", found: " + entries.size());
        }
        return entries.get(0);
    }
}
