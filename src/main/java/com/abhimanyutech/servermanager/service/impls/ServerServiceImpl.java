package com.abhimanyutech.servermanager.service.impls;

import com.abhimanyutech.servermanager.enums.Status;
import com.abhimanyutech.servermanager.model.Server;
import com.abhimanyutech.servermanager.repository.ServerRepository;
import com.abhimanyutech.servermanager.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;

    @Autowired
    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server get(long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public boolean delete(long id) {
        log.info("Deleting server by id: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers...");
        return serverRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    private String setServerImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/server/image/server.png")
                .toUriString();
    }
}
