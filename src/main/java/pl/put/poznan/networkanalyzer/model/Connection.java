package pl.put.poznan.networkanalyzer.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONNECTIONS")
public class Connection implements Serializable {

    @EmbeddedId
    private ConnectionId id;

    @Column(nullable = false)
    private int value;
}
