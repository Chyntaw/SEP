package com.gruppe_f.sep;

import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.liga.LigaService;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class SepGruppeFApplication {

	public static void main(String[] args) {
		SpringApplication.run(SepGruppeFApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository repository, DateRepository repo, BettingRoundRepository reposiTEST, LigaService service) {
		return args -> {
			repository.save(new User("Matt","Murdock","02.02.2002", "lmao@gmx.ru", "passWORT!", "ADMIN"));
			repository.save(new User("Clark","Kent","02.02.0002", "xD@yahoo.org", "passsssWORT!", "BASIC"));
			repository.save(new User("Peter","Parker","02.02.2002", "rofl@yahoo.de", "mirdochwayne", "ADMIN"));
			repository.save(new User("Matt","Huso","02.12.2002", "lmao@yahoo.de", "passsssWORT!", "BASIC"));
			repo.save(new SystemDate("1997-08-08"));

			BettingRound bet = new BettingRound();
			bet.getBetsList().add(new Bets("Bet1", (long) 1, (long) 2));
			bet.getBetsList().add(new Bets("Bet2", (long)4, (long) 2));
			bet.getBetsList().add(new Bets("Bet3", (long)5, (long) 12));

			reposiTEST.save(bet);

			BettingRound bet2 = new BettingRound();
			bet2.getBetsList().add(new Bets("Bet4", (long)1, (long) 2));
			bet2.getBetsList().add(new Bets("Bet5", (long)4, (long) 7));
			bet2.getBetsList().add(new Bets("Bet6", (long)3, (long) 1));

			reposiTEST.save(bet2);

		};
	}

}
