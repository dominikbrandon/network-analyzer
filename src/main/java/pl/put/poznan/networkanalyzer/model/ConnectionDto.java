package pl.put.poznan.networkanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto {
    public Long from;
    public Long to;
    public int value;
}
