package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CONNECTIONS")
public class Connection implements Serializable {

    @EmbeddedId
    private ConnectionId id;

    @Column(nullable = false)
    private int value;
}
