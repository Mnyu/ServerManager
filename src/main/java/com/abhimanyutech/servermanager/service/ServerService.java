package com.abhimanyutech.servermanager.service;

import com.abhimanyutech.servermanager.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    Server create(Server server);
    Server get(long id);
    Server update(Server server);
    boolean delete(long id);
    Collection<Server> list(int limit);
    Server ping(String ipAddress) throws IOException;
}
