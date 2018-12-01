package pl.put.poznan.networkanalyzer.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.persistence.NodeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
}
