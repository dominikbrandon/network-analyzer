package pl.put.poznan.networkanalyzer.service;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.ConnectionId;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;
import pl.put.poznan.networkanalyzer.searching.ConnectionSearchParameters;
import pl.put.poznan.networkanalyzer.searching.ConnectionSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)

public class ConnectionServiceTest {
    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @Test
    public void save_onlyConnection_containsIt(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setId(1L);
        node2.setId(2L);
        ConnectionId con1 = new ConnectionId();
        con1.setFrom(node1);
        con1.setTo(node2);
        Connection c = new Connection();
        c.setId(con1);
        when(connectionRepository.save(c)).thenReturn(null);
        assertThat(connectionService.getAll().contains(c));
    }

    @Test
    public void save_nullNode_runtimeExeption(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setId(null);
        node2.setId(2L);
        ConnectionId con1 = new ConnectionId();
        con1.setFrom(node1);
        con1.setTo(node2);
        Connection c = new Connection();
        c.setId(con1);
        when(connectionRepository.save(c)).thenReturn(null);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                connectionService.save(c)
        );
    }

    @Test
    public void save_existingConnection_runtimeExeption(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setId(1l);
        node2.setId(2L);
        ConnectionId con1 = new ConnectionId();
        con1.setFrom(node1);
        con1.setTo(node2);
        Connection c = new Connection();
        c.setId(con1);
        connectionRepository.save(c);
        when(connectionRepository.save(c)).thenReturn(null);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                connectionService.save(c)
        );
    }

    @Test
    public void saveAll_emptyList_returnsIt(){
        when(connectionRepository.saveAll(any(List.class))).thenReturn(null);
        assertThat(connectionService.getAll()).isEmpty();
    }

    @Test
    public void delete_connection_deletesIt(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setId(1l);
        node2.setId(2L);
        ConnectionId con1 = new ConnectionId();
        con1.setFrom(node1);
        con1.setTo(node2);
        Connection c = new Connection();
        c.setId(con1);
        connectionRepository.save(c);
        connectionRepository.deleteById(con1);
        assertThat(connectionService.getAll()).isEmpty();
    }
}
