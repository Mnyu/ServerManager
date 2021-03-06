package com.abhimanyutech.servermanager.repository;

import com.abhimanyutech.servermanager.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, Integer> {
    Server findByIpAddress(String ipAddress);
}
