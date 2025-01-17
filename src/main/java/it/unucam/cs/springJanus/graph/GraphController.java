package it.unucam.cs.springJanus.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    private final RemoteClientService remoteClientService;

    @Autowired
    public GraphController(RemoteClientService remoteClientService) {
        this.remoteClientService = remoteClientService;
        this.remoteClientService.init();
        System.out.println("Connection successfull");
    }

    @GetMapping("/connect")
    public String connect() {
        return this.remoteClientService.init();
    }

    @GetMapping("/vertices")
    public List<Map<Object, Object>> getAllVertices() {
        return remoteClientService.getAllVertices();
    }

    @GetMapping("/edges")
    public List<Map<Object, Object>> getAllEdges() {
        return remoteClientService.getAllEdges();
    }

    @GetMapping("/countV")
    public List<Long> countVertices() {
        return remoteClientService.countVertices();
    }

    @GetMapping("/countE")
    public List<Long> countEdges() {
        return remoteClientService.countEdges();
    }

    @GetMapping("/getDirector")
    public List<Map<Object, Object>> getDepartementDirector() {
        return remoteClientService.getDirector();
    }

    @PostMapping("/filter")
    ResponseEntity<List<Map<Object, Object>>> filter(@Valid @RequestBody Map<Attributes, String> params) {
        return ResponseEntity.ok(remoteClientService.filter(params));
    }

    @PostMapping("/filter_group")
    ResponseEntity<List<Map<Object, Object>>> filterGroup(@Valid @RequestBody Map<Attributes, String> params) {
        String groupField = params.get(Attributes.groupField);
        params.remove(Attributes.groupField);
        System.out.println(params.toString());
        System.out.println(groupField);
        return ResponseEntity.ok(remoteClientService.filterGroup(params, groupField));
    }

    @PostMapping("/find_children")
    ResponseEntity<List<Map<Object, Object>>> findChildrenByName(@Valid @RequestBody Map<Attributes, String> params) {
        String vertexName = params.get(Attributes.vertexName);
        params.remove(Attributes.vertexName);
        return ResponseEntity.ok(remoteClientService.findChildrenByName(params, vertexName));
    }

    @PostMapping("/find_entering")
    ResponseEntity<List<Map<Object, Object>>> findEnteringVertices(@Valid @RequestBody Map<Attributes, String> params) {
        String vertexName = params.get(Attributes.vertexName);
        params.remove(Attributes.vertexName);
        return ResponseEntity.ok(remoteClientService.findEnteringVertices(params, vertexName));
    }

    @GetMapping("/export")
    public void export() {
        remoteClientService.exportGraph();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleValidationExceptions2() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
    }

}
