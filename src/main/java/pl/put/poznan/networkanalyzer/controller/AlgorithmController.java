package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.networkanalyzer.algorithms.*;
import pl.put.poznan.networkanalyzer.service.NodeService;

@RestController
@RequestMapping("/algorithms")
public class AlgorithmController {
    private AnalyzerContext analyzerContext;
    private NodeService nodeService;

    @Autowired
    public AlgorithmController(AnalyzerContext analyzerContext, NodeService nodeService) {
        this.analyzerContext = analyzerContext;
        this.nodeService = nodeService;
    }

    @GetMapping("/greedy")
    public AlgorithmResult runGreedyAlgorithm() {
        analyzerContext.setAnalyzerAlgorithm(new GreedyAlgorithm(nodeService));
        return analyzerContext.compute();
    }

    @GetMapping("/dfs")
    public AlgorithmResult runDfsAlgorithm() {
        analyzerContext.setAnalyzerAlgorithm(new DfsAlgorithm(nodeService));
        return analyzerContext.compute();
    }

    @GetMapping("/bfs")
    public AlgorithmResult runBfsAlgorithm() {
        analyzerContext.setAnalyzerAlgorithm(new BfsAlgorithm(nodeService));
        return analyzerContext.compute();
    }
}
