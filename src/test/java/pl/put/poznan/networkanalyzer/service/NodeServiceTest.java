package pl.put.poznan.networkanalyzer.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.model.NodeType;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeServiceTest {

    @Mock
    private NodeRepository nodeRepository;

    @InjectMocks
    private NodeService nodeService;

    @Test
    public void getAll_whenNodesFound_returnsThem() {
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();

        when(nodeRepository.findAll()).thenReturn(Lists.newArrayList(node1, node2, node3));

        assertThat(nodeService.getAll()).containsExactlyInAnyOrder(node1, node2, node3);
    }

    @Test
    public void getAll_whenNoNodesFound_returnsEmptyList() {
        when(nodeRepository.findAll()).thenReturn(Lists.emptyList());

        assertThat(nodeService.getAll()).isEmpty();
    }

    @Test
    public void getById_whenNodeNotFound_throwRuntimeException() {
        when(nodeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                nodeService.getById(6L)
        );
    }

    @Test
    public void getById_whenNodeExists_returnIt() {
        Node node = new Node();
        node.setId(7L);

        when(nodeRepository.findById(7L)).thenReturn(Optional.of(node));

        assertThat(nodeService.getById(7L)).isEqualTo(node);
    }

    @Test
    public void getByType_whenNoNodesMatch_returnEmptyList() {
        when(nodeRepository.getByType(any())).thenReturn(Lists.emptyList());

        assertThat(nodeService.getByType(NodeType.ENTRY)).isEmpty();
    }

    @Test
    public void save_whenNodeCorrect_saveIt() {
        Node node1 = new Node();
        node1.setId(2L);
        nodeService.save(node1);
        verify(nodeRepository, times(1)).saveAll(any());
    }
}
