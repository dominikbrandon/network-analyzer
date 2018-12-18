package pl.put.poznan.networkanalyzer.algorithms;

import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.put.poznan.networkanalyzer.model.Node;

import java.util.LinkedList;
/**
 * Class used for storing the result of computing any kind of algorithm
 *
 * @author Kacper Maciejewski
 * @version 1.0
 * @since 1.0
 */

@ToString
@NoArgsConstructor
public class AlgorithmResult {
    public LinkedList<Node> nodes = new LinkedList<>();
    public int totalValue = 0;
    public AlgorithmResult(AlgorithmResult result) {
        this.nodes = (LinkedList<Node>) result.nodes.clone();
        this.totalValue = result.totalValue;
    }
}

