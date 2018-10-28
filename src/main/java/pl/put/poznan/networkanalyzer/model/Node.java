package pl.put.poznan.networkanalyzer.model;

import javax.persistence.*;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.put.poznan.networkanalyzer.model.NodeType;
import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "NODES")
public class Node implements Serializable {
	@Id
	@Column(name = "ID")
	int id;
	@Column(name = "NAME")
	String name;
	@Column(name = "TYPE")
	NodeType type;

	@Column(name = "OUTGOING")
	@OneToMany(mappedBy = "FROM")
	ArrayList outgoing = new ArrayList();
	@Column(name = "INCOMING")
	@OneToMany(mappedBy = "TO")	
	ArrayList incoming = new ArrayList();	
}

