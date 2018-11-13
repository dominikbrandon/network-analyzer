package pl.put.poznan.networkanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Service used for parsing JSON data into objects
 * and afterwards saving created {@link Node Nodes}
 * and {@link pl.put.poznan.networkanalyzer.model.Connection Connections}
 * to database.
 *
 * @author Dominik Grzelak
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
@Lazy
public class DbFiller {
    private NodeService nodeService;
    private ConnectionService connectionService;

    /**
     * Creates a new instance of DbFiller.
     * @param nodeService API for managing nodes
     * @param connectionService API for managing connections
     */
    @Autowired
    public DbFiller(NodeService nodeService, ConnectionService connectionService) {
        log.debug("Creating an instance of DbFiller");
        this.nodeService = nodeService;
        this.connectionService = connectionService;
    }

    /**
     * Reads JSON from the file of given path and saves read objects in the repositories.
     * @param filePath path to the JSON file
     * @throws IOException when JSON could not be parsed
     */
    public void fillFromJson(String filePath) throws IOException {
        log.info("Filling db from json: " + filePath);
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
}
