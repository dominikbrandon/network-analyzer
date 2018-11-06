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
    public Node getEntry(){
        List<Node> listOfNodes = nodeService.getAll();
        Node entry = listOfNodes.get(1);
        for(int i=0 ; i< listOfNodes.size() ; i++) {
            entry = listOfNodes.get(i);
            if (entry.getType() == NodeType.ENTRY) {
                return entry;
            }
        }
        return entry;
    }

    public Node getExit(){
        List<Node> listOfNodes = nodeService.getAll();
        Node exit = listOfNodes.get(0);
        for(int i=0 ; i< listOfNodes.size() ; i++) {
            exit = listOfNodes.get(i);
            if (exit.getType() == NodeType.EXIT) {
                return exit;
            }
        }
        return exit;
    };

    public LinkedList<Node> getOutgoingNodes(Node examined){

        List<Connection> listOfConnections = examined.getOutgoing();
        LinkedList<Node> result = new LinkedList<Node>();
        Connection con = new Connection();
        ConnectionId conId = new ConnectionId();

        for(int i = 0; i < listOfConnections.size(); i++){  //for every connection in listOfConnections get ConnectionId
            con = listOfConnections.get(i);
            conId = con.getId();
            result.add(conId.getTo());
        }

        return  result;
    };

    public LinkedList<Node> getIncomingNodes(Node examined){

        List<Connection> listOfConnections = examined.getIncoming();
        LinkedList<Node> result = new LinkedList<Node>();
        Connection con = new Connection();
        ConnectionId conId = new ConnectionId();

        for(int i = 0; i < listOfConnections.size(); i++){  //for every connection in listOfConnections get ConnectionId
            con = listOfConnections.get(i);
            conId = con.getId();
            result.add(conId.getTo());
        }

        return  result;
    };

    public Node getCheapestOutgoing(Node examined){

        Node result = new Node();
        List<Connection> successors = examined.getOutgoing();       //make it royal
        Connection cheapest = successors.get(0);                    //initiate it with first connection
        Connection comparing = new Connection();

        for(int i = 0; i < successors.size(); i++){
            comparing = successors.get(i);
            if(cheapest.getValue() >= comparing.getValue()){
                cheapest = comparing;
            }
        }

        ConnectionId resultConnectionId = cheapest.getId();
        result = resultConnectionId.getTo();

        return result;
    }


    public LinkedList<Node> compute() {
        // stuff
        List<Node> wierzcholki = nodeService.getAll();
        List<Connection> luki = connectionService.getAll();
        LinkedList<Node> greedyResultNodes = new LinkedList<Node>();  //result of algorithm will be stored here

        Node first = getEntry();
        Node last = getExit();
        Node actual = getCheapestOutgoing(first);
        greedyResultNodes.add(actual);

        while(actual.getId() != last.getId()){
            actual = getCheapestOutgoing(actual);
            greedyResultNodes.add(actual);
        }

        return Lists.newLinkedList();
    }
}
