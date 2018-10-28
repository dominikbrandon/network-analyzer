package pl.put.poznan.networkanalyzer.model;

import javax.persistence.*;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.put.poznan.networkanalyzer.model.Node;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CONNECTIONS")
public class Connection implements Serializable {
	@Id 
	@Column(name = "FROM")
	@ManyToOne
	@JoinColumn(name = "ID")	
	Node from;

	@Id 
	@Column(name = "TO")
	@ManyToOne
	@JoinColumn(name = "ID")
	Node to;

	@Id 
	@Column(name = "VALUE")
	int value;
}
