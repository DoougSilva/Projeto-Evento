package com.projuris.projetoStag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;

@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ProjetoStagApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(ProjetoStagApplication.class);
		Environment env = app.run(args).getEnvironment();
		String protocol = "http";
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			log.trace("The host name could not be determined, using `localhost` as fallback");
		}
		log.info("\n    ----------------------------------------------------------\n\t"
						+ "Application '{}' is running! Access URLs:\n\t"
						+ "Local: \t\t{}://localhost:{}\n\t"
						+ "External: \t{}://{}:{}\n\t"
						+ "----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, env.getProperty("server.port"), protocol,
				hostAddress, env.getProperty("server.port"));
	}

}
