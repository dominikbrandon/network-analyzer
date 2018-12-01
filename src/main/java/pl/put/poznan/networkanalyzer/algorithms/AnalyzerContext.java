package pl.put.poznan.networkanalyzer.algorithms;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerContext {
    private AnalyzerAlgorithm analyzerAlgorithm;

    public void setAnalyzerAlgorithm(AnalyzerAlgorithm analyzerAlgorithm) {
        this.analyzerAlgorithm = analyzerAlgorithm;
    }

    public AlgorithmResult compute() {
        return analyzerAlgorithm.compute();
    }
}
