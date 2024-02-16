package ca.mcgill.ecse321.SportPlus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "ca.mcgill.ecse321.SportPlus.dao") // Specify the base package of your repositories
@EnableTransactionManagement
@RestController
public class SportPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportPlusApplication.class, args);
		// testing vscode
	}
	@RequestMapping("/")
	public String greeting() {
		return "Hello world!";
	}

}
