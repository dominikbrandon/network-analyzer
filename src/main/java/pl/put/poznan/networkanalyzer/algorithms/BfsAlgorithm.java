package pl.put.poznan.networkanalyzer.algorithms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@Lazy
public class BfsAlgorithm {
    private NodeService nodeService;

    @Autowired
    public BfsAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public AlgorithmResult compute() {
        log.info("Running BFS algorithm");
        long startTime = System.nanoTime();
        Node entry = getNodeOfTypeWhenOnlyOneExists(NodeType.ENTRY);
        Node exit = getNodeOfTypeWhenOnlyOneExists(NodeType.EXIT);
        Node currentNode;
        Long exitId = exit.getId();
        AlgorithmResult currentResult = new AlgorithmResult();
        currentResult.nodes.add(entry);
        AlgorithmResult bestResult = null;
        LinkedList<AlgorithmResult> resultFifo = new LinkedList<>();          //creates FIFO for Results
        resultFifo.add(currentResult);

        while (resultFifo.size() != 0) {                                 //while there are Results that doesn't make it to the endNode
            currentResult = resultFifo.get(0);                          //get the value of first Result from the FIFO
            resultFifo.remove(0);                                       //delete it from the List
            currentNode = currentResult.nodes.getLast();                //set the last Node from the Result as currentNode

            if (currentNode.getId() == exitId) {                        //if Result reach the exit node
                if (bestResult == null || currentResult.totalValue < bestResult.totalValue) { // if it is first or new best result
                    bestResult = new AlgorithmResult(currentResult);
                }
            } else {                                                    //if it is still going
                List<Connection> neighbours = currentNode.getOutgoing();
                for (Connection neighbour : neighbours) {
                    Node nextNode = neighbour.getId().getTo();
                    currentResult.nodes.add(nextNode);                   //add succesor to a Result
                    currentResult.totalValue += neighbour.getValue();    //add its path value
                    resultFifo.add(new AlgorithmResult(currentResult));  //add this new Result to the List
                    currentResult.totalValue -= neighbour.getValue();    //return the staring values of the currentResult
                    currentResult.nodes.remove(nextNode);
                }
            }
        }
        long elapsedTime = System.nanoTime() - startTime;
        log.info("Finished BFS algorithm in " + elapsedTime / 1000000 + " ms");
        return bestResult;
    }

    private Node getNodeOfTypeWhenOnlyOneExists(NodeType type) {
        List<Node> entries = nodeService.getByType(type);
        if (entries.size() != 1) {
            throw new RuntimeException("There must be 1 node of type " + type + ", found: " + entries.size());
        }
        return entries.get(0);
    }
}