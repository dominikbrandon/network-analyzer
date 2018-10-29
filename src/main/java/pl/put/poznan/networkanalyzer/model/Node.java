package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "NODES")
public class Node implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    private String name;

    @Column(nullable = false)
    private NodeType type;

    @OneToMany(mappedBy = "id")
    private List<Node> outgoing;

    @OneToMany(mappedBy = "id")
    private List<Node> incoming;
}

