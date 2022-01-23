package com.abhimanyutech.servermanager;

import com.abhimanyutech.servermanager.enums.Status;
import com.abhimanyutech.servermanager.model.Server;
import com.abhimanyutech.servermanager.repository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServermanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServermanagerApplication.class, args);
	}

	@Bean
	CommandLineRunner run(ServerRepository serverRepository) {
		return args -> {
			serverRepository.save(Server.builder()
							.id(null).ipAddress("192.168.1.160")
							.name("Ubuntu Linux").memory("16 GB").type("Personal PC")
							.imageUrl("http://localhost:8080/server/image/server1.png")
							.status(Status.SERVER_DOWN).build());

			serverRepository.save(Server.builder()
					.id(null).ipAddress("192.168.1.58")
					.name("Fedora Linux").memory("16 GB").type("Dell Tower")
					.imageUrl("http://localhost:8080/server/image/server2.png")
					.status(Status.SERVER_DOWN).build());

			serverRepository.save(Server.builder()
					.id(null).ipAddress("192.168.1.21")
					.name("MS 2008").memory("32 GB").type("Web Server")
					.imageUrl("http://localhost:8080/server/image/server3.png")
					.status(Status.SERVER_DOWN).build());
		};
	}

}
