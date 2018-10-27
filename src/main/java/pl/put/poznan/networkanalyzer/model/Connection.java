package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.put.poznan.networkanalyzer.model.Node;

@Getter
@Setter
@EqualsAndHashCode
public class Connection {
	Node from;
	Node to;
	int value;
}
