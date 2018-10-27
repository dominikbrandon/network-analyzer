package pl.put.poznan.networkanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class Connection {
	int from;
	int to;
	int value;
}
