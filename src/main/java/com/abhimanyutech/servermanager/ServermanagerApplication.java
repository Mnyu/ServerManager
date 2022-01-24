package com.abhimanyutech.servermanager;

import com.abhimanyutech.servermanager.enums.Status;
import com.abhimanyutech.servermanager.model.Server;
import com.abhimanyutech.servermanager.repository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
				"Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
