package com.abhimanyutech.servermanager.controller;

import com.abhimanyutech.servermanager.model.Response;
import com.abhimanyutech.servermanager.enums.Status;
import com.abhimanyutech.servermanager.model.Server;
import com.abhimanyutech.servermanager.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("server")
public class ServerController {

    private final ServerService serverService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ServerController(ResourceLoader resourceLoader,
                            ServerService serverService) {
        this.resourceLoader = resourceLoader;
        this.serverService = serverService;
    }

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() throws InterruptedException {
        Thread.sleep(1000); // Deliberate delay to check UI
        Map<String, Collection<Server>> map = new HashMap<>();
        map.put("servers", serverService.list(30));
        return ResponseEntity.ok(Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .data(map)
                    .message("Servers retrieved successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        Map<String, Server> map = new HashMap<>();
        map.put("server", server);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(map)
                .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        Server newServer = serverService.create(server);
        Map<String, Server> map = new HashMap<>();
        map.put("server", newServer);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(map)
                .message("Server created successfully")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") int id) {
        Server server = serverService.get(id);
        Map<String, Server> map = new HashMap<>();
        map.put("server", server);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(map)
                .message("Server retrieved successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") int id) {
        boolean isDeleted = serverService.delete(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("deleted", isDeleted);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(map)
                .message("Server deleted successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping(path = "/image/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:images/" + fileName);
        return Files.readAllBytes(Paths.get(resource.getURI()));
    }
}
