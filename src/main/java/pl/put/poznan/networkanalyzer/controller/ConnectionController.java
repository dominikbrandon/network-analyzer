package pl.put.poznan.networkanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.networkanalyzer.model.Connection;
import pl.put.poznan.networkanalyzer.model.ConnectionDto;
import pl.put.poznan.networkanalyzer.searching.ConnectionSearchParameters;
import pl.put.poznan.networkanalyzer.service.ConnectionService;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionController {
    private ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping
    public List<Connection> findByNodes(@RequestParam(value = "from", required = false) Long fromId,
                                        @RequestParam(value = "to", required = false) Long toId) {
        ConnectionSearchParameters searchParameters = new ConnectionSearchParameters();
        searchParameters.from = fromId;
        searchParameters.to = toId;
        return connectionService.getBySearchParams(searchParameters);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@RequestBody List<ConnectionDto> connectionDtos) {
        connectionService.saveAll(connectionDtos);
    }

    @PutMapping(value = "/{from}/{to}")
    public void update(@PathVariable("from") Long fromId, @PathVariable("to") Long toId,
                       @RequestBody ConnectionDto connectionDto) {
        connectionService.update(fromId, toId, connectionDto);
    }

    @DeleteMapping(value = "/{from}/{to}")
    public void delete(@PathVariable("from") Long fromId, @PathVariable("to") Long toId) {
        connectionService.delete(fromId, toId);
    }
}
