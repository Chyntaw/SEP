package com.gruppe_f.sep;

import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.businesslogic.ImageLogic.ImageRepository;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.date.DateService;
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
import com.gruppe_f.sep.entities.user.UserService;
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
	CommandLineRunner init(DateService dateServce, UserService userService, DateRepository repo, LigaRepository ligarepo, LeagueDataRepository ldrepo, FriendRepository friendRepository, ImageRepository imageRepository) {
		return args -> {
			dateServce.changeDate(new SystemDate("1000-05-14"));
			try {
			userService.addUser(new User("Anna","Bolika","02.02.2002", "lmao@gmx.ru", "passWORT!", "BASIC"));
			userService.addUser(new User("Peter","Haremberg","02.02.1000", "t@t.de", "test", "BASIC"));
			userService.addUser(new User("Martin","Huldensohn","02.02.1000", "xD@xD.de", "xD", "BASIC"));
			userService.addUser(new User("Dick","Tator","02.02.0002", "xD@yahoo.org", "passsssWORT!", "BASIC"));
			userService.addUser(new User("Georg","Asmus","02.02.2002", "rofl@yahoo.de", "mirdochwayne", "ADMIN"));
			userService.addUser(new User("Gutfried","Wurst","02.12.2002", "lmao@yahoo.de", "passsssWORT!", "BASIC"));
			userService.addUser(new User("Can","Kalifat","24.04.1997", "can.kalafat@outlook.de", "1", "BASIC"));
			userService.addUser(new User("Hella","Kot","24.04.1997", "kot@kotMail.com", "1", "BASIC"));
			userService.addUser(new User("Lars","Samenström","24.04.1997", "lars@lars.com", "1", "BASIC"));
			} catch	(Exception e) {}

			/*
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

			*/


		};
	}

}
