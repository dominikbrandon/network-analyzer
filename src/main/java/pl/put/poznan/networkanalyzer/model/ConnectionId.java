package pl.put.poznan.networkanalyzer.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionId implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodeOut")
    private Node from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodeIn")
    private Node to;
}
