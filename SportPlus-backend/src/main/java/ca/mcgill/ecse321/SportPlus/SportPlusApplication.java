package ca.mcgill.ecse321.SportPlus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
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
