package pl.put.poznan.networkanalyzer.algorithms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.List;
import java.util.Objects;

/**
 * Class used for computing greedy algorithm in order to
 * receive path from ENTRY to EXIT
 *
 * @author Mateusz Wisniewski
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class DfsAlgorithm implements AnalyzerAlgorithm {
    private final NodeService nodeService;
    private AlgorithmResult bestResult;


    /**
     *Method which does all of the computation in using deep first search algorithm also prints to the log
     * how much time it was computing
     * @return list of nodes on the path and total cost if it
     */
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
