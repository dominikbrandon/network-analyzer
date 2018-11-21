package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.networkanalyzer.algorithms.AlgorithmResult;
import pl.put.poznan.networkanalyzer.algorithms.BfsAlgorithm;
import pl.put.poznan.networkanalyzer.algorithms.DfsAlgorithm;
import pl.put.poznan.networkanalyzer.algorithms.GreedyAlgorithm;

@RestController
@RequestMapping("/algorithms")
public class AlgorithmController {
    private GreedyAlgorithm greedyAlgorithm;
    private DfsAlgorithm dfsAlgorithm;
    private BfsAlgorithm bfsAlgorithm;

    @Autowired
    public AlgorithmController(GreedyAlgorithm greedyAlgorithm, DfsAlgorithm dfsAlgorithm, BfsAlgorithm bfsAlgorithm) {
        this.greedyAlgorithm = greedyAlgorithm;
        this.dfsAlgorithm = dfsAlgorithm;
        this.bfsAlgorithm = bfsAlgorithm;
    }

    @GetMapping("/greedy")
    public AlgorithmResult runGreedyAlgorithm() {
        return greedyAlgorithm.compute();
    }

    @GetMapping("/dfs")
    public AlgorithmResult runDfsAlgorithm() {
        return dfsAlgorithm.compute();
    }

    @GetMapping("/bfs")
    public AlgorithmResult runBfsAlgorithm() {
        return bfsAlgorithm.compute();
    }
}
