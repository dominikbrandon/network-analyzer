package pl.put.poznan.networkanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "NODES")
@ToString(exclude = {"outgoing", "incoming"})
public class Node implements Serializable {

    @Id
    private Long id;

    private String name;

    @Column(nullable = false)
    private NodeType type;

    @OneToMany(mappedBy = "id.from", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Connection> outgoing;

    @OneToMany(mappedBy = "id.to", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Connection> incoming;
}

