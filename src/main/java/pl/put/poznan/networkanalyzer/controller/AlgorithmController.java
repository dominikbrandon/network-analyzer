package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.networkanalyzer.algorithms.AlgorithmResult;
import pl.put.poznan.networkanalyzer.algorithms.DfsAlgorithm;
import pl.put.poznan.networkanalyzer.algorithms.GreedyAlgorithm;

@RestController
@RequestMapping("/algorithms")
public class AlgorithmController {
    private GreedyAlgorithm greedyAlgorithm;
    private DfsAlgorithm dfsAlgorithm;

    @Autowired
    public AlgorithmController(GreedyAlgorithm greedyAlgorithm, DfsAlgorithm dfsAlgorithm) {
        this.greedyAlgorithm = greedyAlgorithm;
        this.dfsAlgorithm = dfsAlgorithm;
    }

    @GetMapping("/greedy")
    public AlgorithmResult runGreedyAlgorithm() {
        return greedyAlgorithm.compute();
    }

    @GetMapping("/dfs")
    public AlgorithmResult runDfsAlgorithm() {
        return dfsAlgorithm.compute();
    }
}
