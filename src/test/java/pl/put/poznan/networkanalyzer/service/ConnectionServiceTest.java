package pl.put.poznan.networkanalyzer.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionId;
import pl.put.poznan.networkanalyzer.searching.ConnectionSearchParameters;
import pl.put.poznan.networkanalyzer.searching.ConnectionSpecification;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private NodeRepository nodeRepository;
  
    @InjectMocks
    private ConnectionService connectionService;

    @Test
    public void save_onlyConnection_callsSaveAllInRepo() {
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setId(1L);
        node2.setId(2L);
        ConnectionDto connectionDto = new ConnectionDto(1L, 2L, 5);

        when(connectionRepository.existsById(any())).thenReturn(false);
        when(nodeRepository.getOne(1L)).thenReturn(node1);
        when(nodeRepository.getOne(2L)).thenReturn(node2);

        connectionService.save(connectionDto);
        verify(connectionRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void save_nodeNotExisting_throwsEntityNotFound() {
        ConnectionDto connectionDto = new ConnectionDto(1L, 2L, 5);

        when(nodeRepository.getOne(1L)).thenThrow(EntityNotFoundException.class);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> {
            connectionService.save(connectionDto);
        });
    }

    @Test
    public void save_existingConnection_runtimeException() {
        ConnectionDto connectionDto = new ConnectionDto(1L, 2L, 5);

        when(connectionRepository.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            connectionService.save(connectionDto);
        });
    }

    @Test
    public void saveAll_fromNodeIsNull_throwException() {
        ConnectionDto connectionDto = new ConnectionDto(null, 2L, 5);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            connectionService.saveAll(Collections.singletonList(connectionDto));
        });
    }

    @Test
    public void delete_givenNodeDoesNotExist_throwEntityNotFound() {
        when(nodeRepository.getOne(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> {
            connectionService.delete(2L, 3L);
        });

    @Test
    public void getAll_whenConnectionsFound_returnsThem() {
        Connection con1 = new Connection();
        Connection con2 = new Connection();
        Connection con3 = new Connection();

        when(connectionRepository.findAll()).thenReturn(Lists.newArrayList(con1, con2, con3));

        assertThat(connectionService.getAll()).containsExactlyInAnyOrder(con1, con2, con3);
    }

    @Test
    public void getAll_whenNoConnectionsFound_returnsEmptyList() {
        when(connectionRepository.findAll()).thenReturn(Lists.emptyList());

        assertThat(connectionService.getAll()).isEmpty();
    }

    @Test
    public void getBySearchParam_whenConnectionsFound_returnIt() {
        //given
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        node1.setId(1L);
        node2.setId(2L);
        node3.setId(3L);

        ConnectionId conId1 = new ConnectionId();
        ConnectionId conId2 = new ConnectionId();
        conId1.setFrom(node1);
        conId1.setTo(node2);
        conId2.setFrom(node1);
        conId2.setTo(node3);

        Connection con1 = new Connection();
        Connection con2 = new Connection();
        con1.setId(conId1);
        con2.setId(conId2);

        //when
        when(connectionRepository.findAll(any(ConnectionSpecification.class))).thenReturn(Lists.newArrayList(con1, con2));

        //then
        ConnectionSearchParameters searchParameters = new ConnectionSearchParameters();
        searchParameters.from = 1L;
        assertThat(connectionService.getBySearchParams(searchParameters)).containsExactlyInAnyOrder(con1, con2);
    }

    @Test
    public void getBySearchParam_whenNoConnectionsFound_returnEmptyList() {
        ConnectionSearchParameters searchParameters = new ConnectionSearchParameters();
        searchParameters.from = 1L;
        searchParameters.to = 2L;

        when(connectionRepository.findAll(any(ConnectionSpecification.class))).thenReturn(Lists.emptyList());
        assertThat(connectionService.getBySearchParams(searchParameters)).isEmpty();
    }
}
