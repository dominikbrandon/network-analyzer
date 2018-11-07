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
        Node currentNode = getEntry();
        Node exit = getExit();
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

    private Node getEntry() {
        List<Node> listOfNodes = nodeService.getAll();
        Node entry = new Node();
        entry.setType(NodeType.REGULAR);

        for (int i = 0; i < listOfNodes.size(); i++) {

            if (listOfNodes.get(i).getType() == NodeType.ENTRY) {
                if (entry.getType() == NodeType.ENTRY) {
                    throw new RuntimeException("There can't be more than 1 Node with ENTRY type");
                }
                entry = listOfNodes.get(i);
            }
        }

        if (entry.getType() == NodeType.REGULAR) {
            throw new RuntimeException("There must be 1 Node with ENTRY type");
        } else {
            return entry;
        }
    }

    private Node getExit() {
        List<Node> listOfNodes = nodeService.getAll();
        Node exit = new Node();
        exit.setType(NodeType.REGULAR);

        for (int i = 0; i < listOfNodes.size(); i++) {

            if (listOfNodes.get(i).getType() == NodeType.EXIT) {
                if (exit.getType() == NodeType.EXIT) {
                    throw new RuntimeException("There can't be more than 1 Node with EXIT type");
                }
                exit = listOfNodes.get(i);
            }
        }

        if (exit.getType() == NodeType.REGULAR) {
            throw new RuntimeException("There must be 1 Node with EXIT type");
        } else {
            return exit;
        }
    }
}
