package pl.put.poznan.networkanalyzer.algorithms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.*;
import pl.put.poznan.networkanalyzer.service.ConnectionService;
import pl.put.poznan.networkanalyzer.service.NodeService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class GreedyAlgorithm {
    private NodeService nodeService;
    private ConnectionService connectionService;

    @Autowired
    public GreedyAlgorithm(NodeService nodeService, ConnectionService connectionService) {
        this.nodeService = nodeService;
        this.connectionService = connectionService;

        try {
            fillDb("graphs/graph1_v2.json");
        } catch (IOException e) {
            log.error("Error parsing JSON", e);
        }
        compute();
    }

    // ***************** TEMP *********************
    private void fillDb(String filePath) throws IOException {
        // this crazy shit loads data on startup so you dont have to do it manually - isnt that beautiful??
        // we will remove it someday
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        JsonNode content = objectMapper.readTree(file);
        String nodesJson = content.get("Node").toString();
        String connectionsJson = content.get("Connection").toString();

        Node[] nodesArray = objectMapper.readValue(nodesJson, Node[].class);
        ConnectionDto[] connectionDtosArray = objectMapper.readValue(connectionsJson, ConnectionDto[].class);
        List<Node> nodes = Arrays.asList(nodesArray);
        List<ConnectionDto> connectionDtos = Arrays.asList(connectionDtosArray);
        nodeService.saveAll(nodes);
        connectionService.saveAll(connectionDtos);
    }
    // ***************** TEMP *********************
    class Outcome                                                       //for storing results of computing
    {
        public LinkedList<Node> nodes = new LinkedList<Node>();
        public int connectionValue;
    }


    public Node getEntry(){
        List<Node> listOfNodes = nodeService.getAll();
        Node entry = new Node();
        entry.setType(NodeType.REGULAR);

        for(int i=0 ; i< listOfNodes.size() ; i++) {

            if (listOfNodes.get(i).getType() == NodeType.ENTRY) {
                if(entry.getType() == NodeType.ENTRY){
                    throw new RuntimeException("There can't be more than 1 Node with ENTRY type");
                }
                entry = listOfNodes.get(i);
            }
        }

        if (entry.getType() == NodeType.REGULAR){
            throw new RuntimeException("There must be 1 Node with ENTRY type");
        }else{
            return entry;
        }
    }

    public Node getExit(){
        List<Node> listOfNodes = nodeService.getAll();
        Node exit = new Node();
        exit.setType(NodeType.REGULAR);

        for(int i=0 ; i< listOfNodes.size() ; i++) {

            if (listOfNodes.get(i).getType() == NodeType.EXIT) {
                if(exit.getType() == NodeType.EXIT){
                    throw new RuntimeException("There can't be more than 1 Node with EXIT type");
                }
                exit = listOfNodes.get(i);
            }
        }

        if (exit.getType() == NodeType.REGULAR){
            throw new RuntimeException("There must be 1 Node with EXIT type");
        }else{
            return exit;
        }
    }


    public Outcome getCheapestOutgoing(Node examined){

        List<Connection> successors = examined.getOutgoing();       //make it royal
        Connection cheapest = successors.get(0);                    //initiate it with first connection
        Connection comparing;

        for(int i = 0; i < successors.size(); i++){
            comparing = successors.get(i);
            if(cheapest.getValue() >= comparing.getValue()){
                cheapest = comparing;
            }
        }

        ConnectionId resultConnectionId = cheapest.getId();
        Outcome result = new Outcome();

        result.nodes.add(resultConnectionId.getTo());
        result.connectionValue = cheapest.getValue();
        return result;
    }


    public Outcome compute() {
        // stuff
        Node first = getEntry();
        Node last = getExit();
        Outcome actualResult = new Outcome();           //greedyAlgorithm result will be stored here
                actualResult.nodes.add(first);          //initiate it with first element (entry)
                actualResult.connectionValue = 0;
        Outcome nextNode ;                              //variable which will be holding next Node on the path

        while((actualResult.nodes.getLast()).getId() != last.getId()){     //while we haven't reached the exit Node get cheapest next Node
            nextNode = getCheapestOutgoing(actualResult.nodes.getLast());
            actualResult.connectionValue += nextNode.connectionValue;
            actualResult.nodes.add(nextNode.nodes.getFirst());
        }

        //ToDo
        // - create class Outcome in another file

        return actualResult;
    }
}
