package pl.put.poznan.networkanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // lazy serialization workaround
public class Node implements Serializable {

    @Id
    private Long id;

    private String name;

    @Column(nullable = false)
    private NodeType type;

    @OneToMany(mappedBy = "id")
    @JsonIgnore
    private List<Node> outgoing;

    @OneToMany(mappedBy = "id")
    @JsonIgnore
    private List<Node> incoming;
}

