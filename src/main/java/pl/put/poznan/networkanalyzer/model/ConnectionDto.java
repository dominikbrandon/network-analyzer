package pl.put.poznan.networkanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConnectionDto {
    public Long from;
    public Long to;
    public int value;
}
