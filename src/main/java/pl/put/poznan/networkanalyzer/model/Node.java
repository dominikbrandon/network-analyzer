package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

public enum typ {
	entry, 
	exit, 
	regular;
}


@Getter
@Setter
@EqualsAndHashCode
public class Node {
	int id;
	string name;
	typ type;
	ArrayList outgoing = new ArrayList();
	ArrayList incoming = new ArrayList();	
	
}
public class Connection {
	int from;
	int to;
	int value;
}
