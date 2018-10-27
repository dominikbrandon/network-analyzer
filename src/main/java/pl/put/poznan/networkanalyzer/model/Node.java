package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import NodeType.java


@Getter
@Setter
@EqualsAndHashCode
public class Node {
	int id;
	string name;
	NodeType type;
	ArrayList outgoing = new ArrayList();
	ArrayList incoming = new ArrayList();	
}

