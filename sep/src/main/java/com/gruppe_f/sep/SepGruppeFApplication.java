package com.gruppe_f.sep;

import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.businesslogic.ImageLogic.ImageRepository;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.BettingRound.BettingRoundRepository;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.friends.Friend;
import com.gruppe_f.sep.entities.friends.FriendRepository;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.liga.Liga;
import com.gruppe_f.sep.entities.liga.LigaRepository;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SepGruppeFApplication {

	public static void main(String[] args) {
		SpringApplication.run(SepGruppeFApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository repository, DateRepository repo, LigaRepository ligarepo, LeagueDataRepository ldrepo, FriendRepository friendRepository, ImageRepository imageRepository) {
		return args -> {
			repository.save(new User("Matt","Murdock","02.02.2002", "lmao@gmx.ru", "passWORT!", "ADMIN"));
			repository.save(new User("Test","Hodenburg","02.02.1000", "t@t.de", "test", "Basic"));
			repository.save(new User("Test","Lmao>Burg","02.02.1000", "xD@xD.de", "xD", "Basic"));
			repository.save(new User("Clark","Kent","02.02.0002", "xD@yahoo.org", "passsssWORT!", "BASIC"));
			repository.save(new User("Peter","Parker","02.02.2002", "rofl@yahoo.de", "mirdochwayne", "ADMIN"));
			repository.save(new User("Matt","Huso","02.12.2002", "lmao@yahoo.de", "passsssWORT!", "BASIC"));
			repository.save(new User("Can","Kalafat","24.04.1997", "can.kalafat@outlook.de", "1", "BASIC"));

			repo.save(new SystemDate("2020-05-14"));


			Liga testLiga = new Liga("Nationalistische Nationalliga", null);
			ligarepo.save(testLiga);
			List<LeagueData> dataList = new ArrayList<>();
			dataList.add(new LeagueData(1, "Testspieler1", "Testspieler2", "1-0", (long)1));
			dataList.add(new LeagueData(2, "MEIN_BVB", "Dein BvB", "11-10", (long)1));
			dataList.add(new LeagueData(2, "User BVB", "Euer BvB", "2-0", (long)1));
			dataList.add(new LeagueData(3, "DeineMUdda", "DeinVadda", "0-2", (long)1));
			dataList.add(new LeagueData(3, "Bayern", "Dein BvB", "11-2", (long)1));
			dataList.add(new LeagueData(4, "FC Schalke", "BvB", "1-10", (long)1));
			dataList.add(new LeagueData(4, "Kaiserslautern", "Roz-weiss Essen", "0-0", (long)1));
			dataList.add(new LeagueData(5, "DurchfallKing", "Tütensuppe", "3-1", (long)1));
			dataList.add(new LeagueData(5, "BurgerKing", "Eintopf", "6-8", (long)1));
			dataList.add(new LeagueData(6, "McDonlads", "Die Simpson", "2-7", (long)1));
			int x = 10;
			for(LeagueData data: dataList) {
				data.setDate("2020-05-"+Integer.toString(x));
				ldrepo.save(data);
				testLiga.getLeagueData().add(data);
				x++;
			}
			ligarepo.save(testLiga);


			byte[] test = new byte[0];
			ImageModel imageModel = new ImageModel("StandardBild", "image/jpeg", test);
			imageRepository.save(imageModel);

		};
	}

}
