package pl.put.poznan.networkanalyzer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.ConnectionRepository;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

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
    }
}
