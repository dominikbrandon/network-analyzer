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

/**
 * Class used for computing greedy algorithm in order to
 * receive path from ENTRY to EXIT
 *
 * @author Kacper Maciejewski
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
@Lazy
public class GreedyAlgorithm {
    private NodeService nodeService;
    /**
     * Creates a new instance of GreedyAlgorithm and links it to currently tested graph
     */
    @Autowired
    public GreedyAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }
    /**
     *Method which does all of the computation in a greedy way also prints to the log
     * how much time it was computing
     * @return list of nodes on the path and total cost if it
     */
    public AlgorithmResult compute() {
        log.info("Running greedy algorithm");
        long startTime = System.nanoTime();
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
        long elapsedTime = System.nanoTime() - startTime;
        log.info("Finished greedy algorithm in " + elapsedTime / 1000000 + " ms");
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
    /**
     *Method which checks if graph is compliant - has one ENTRY and one EXIT
     * @param type points if we are looking for ENTRY or EXIT
     * @return ENTRY or EXIT node
     */
    private Node getNodeOfTypeWhenOnlyOneExists(NodeType type) {
        List<Node> entries = nodeService.getByType(type);
        if (entries.size() != 1) {
            throw new RuntimeException("There must be 1 node of type " + type + ", found: " + entries.size());
        }
        return entries.get(0);
    }
}
