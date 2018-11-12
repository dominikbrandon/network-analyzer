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

@Service
@Slf4j
@Lazy
public class DfsAlgorithm {
    private NodeService nodeService;
    private List<AlgorithmResult> results = new ArrayList<>();

    @Autowired
    public DfsAlgorithm(NodeService nodeService) {
        this.nodeService = nodeService;
    }
    private AlgorithmResult findBest(List<AlgorithmResult> rst){
        for(AlgorithmResult p: rst){
            for(int i : p.getNodes()) {
                System.out.print(i + " ");
            }
            System.out.println(" ");
        }
        AlgorithmResult best = rst.get(0);
        for (AlgorithmResult result : rst) {
            if (result.totalValue < best.totalValue) best = result;
        }
        return best;
    }

    public AlgorithmResult dfs() {
        Node entry = getNodeOfTypeWhenOnlyOneExists(NodeType.ENTRY);
        Node exit = getNodeOfTypeWhenOnlyOneExists(NodeType.EXIT);
        startDfs(entry.getId(), entry.getId(), new AlgorithmResult());
        return findBest(results);
    }

    private void startDfs(Long id, Long prev, AlgorithmResult path) {
        Nodes current = findNode(id);
        Connection connection
        path.getNodes().add(current.getId());
        Integer cost = findCost(prev, id);
        path.totalValue +=
        if (current.getId().equals(exit.getId())) {
            results.add(path);
            return;
        }
        if(path.getNodes).size() >= nodes.size()) return;
        current.getOutgoing().forEach(id1 -> startDfs(id1, id, new List<AlgorithmResult>(path)));
    }

    private Node findNode(Long id) {
        NodeService service = new NodeService();
        List<Node> nodes = service.getAll();
        for (Node node : nodes) {
            if (node.getId().equals(id))
                return node;
        }
        return null;
    }

    private Node getNodeOfTypeWhenOnlyOneExists(NodeType type) {
        List<Node> entries = nodeService.getByType(type);
        if (entries.size() != 1) {
            throw new RuntimeException("There must be 1 node of type " + type + ", found: " + entries.size());
        }
        return entries.get(0);
    }
}
