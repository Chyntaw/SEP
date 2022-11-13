package com.gruppe_f.sep;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class SepGruppeFApplication {

	public static void main(String[] args) {
		SpringApplication.run(SepGruppeFApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository repository, DateRepository repo) {
		return args -> {
			repository.save(new User("Matt","Murdock","02.02.2002", "lmao@gmx.ru", "passWORT!", "ADMIN"));
			repository.save(new User("Clark","Kent","02.02.0002", "xD@yahoo.org", "passsssWORT!", "BASIC"));
			repository.save(new User("Peter","Parker","02.02.2002", "rofl@yahoo.de", "mirdochwayne", "ADMIN"));
			repository.save(new User("Matt","Huso","02.12.2002", "lmao@yahoo.de", "passsssWORT!", "BASIC"));
			repo.save(new SystemDate("1997-08-08"));
		};
	}

}
