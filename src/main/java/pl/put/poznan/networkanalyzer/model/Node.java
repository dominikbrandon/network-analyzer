package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.put.poznan.networkanalyzer.model.NodeType;
import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode
public class Node {
	int id;
	String name;
	NodeType type;
	ArrayList outgoing = new ArrayList();
	ArrayList incoming = new ArrayList();	
}

