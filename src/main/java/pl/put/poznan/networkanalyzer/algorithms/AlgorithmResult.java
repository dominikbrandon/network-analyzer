package pl.put.poznan.networkanalyzer.algorithms;

import lombok.ToString;
import pl.put.poznan.networkanalyzer.model.Node;

import java.util.LinkedList;

@ToString
public class AlgorithmResult {
    public LinkedList<Node> nodes = new LinkedList<>();
    public int totalValue = 0;
}
