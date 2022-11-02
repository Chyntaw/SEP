package com.gruppe_f.sep;

import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SepGruppeFApplication {

	public static void main(String[] args) {
		SpringApplication.run(SepGruppeFApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository repository) {
		return args -> {
			repository.save(new User("Matt","Murdock","02.02.2002", "lmao@yahoo.de", "passWORT!"));
			repository.save(new User("Clark","Kent","02.02.0002", "xD@yahoo.de", "passsssWORT!"));
			repository.save(new User("Peter","Parker","02.02.2002", "rofl@yahoo.de", "mirdochwayne"));
			repository.save(new User("Matt","Huso","02.12.2002", "lmao@yahoo.de", "passsssWORT!"));
		};
	}

}
