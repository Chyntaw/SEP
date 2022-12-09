package com.gruppe_f.sep.entities.BettingRound;

import com.gruppe_f.sep.businesslogic.TipHelper;
import com.gruppe_f.sep.date.DateRepository;
import com.gruppe_f.sep.entities.alias.Alias;
import com.gruppe_f.sep.entities.bets.Bets;
import com.gruppe_f.sep.entities.friends.FriendService;
import com.gruppe_f.sep.entities.leagueData.LeagueData;
import com.gruppe_f.sep.entities.leagueData.LeagueDataRepository;
import com.gruppe_f.sep.entities.liga.LigaRepository;
import com.gruppe_f.sep.entities.scores.Score;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserRepository;
import com.gruppe_f.sep.mail.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.gruppe_f.sep.businesslogic.GenerellLogisch.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bettingRound/")
public class BettingRoundController {
    private BettingRoundRepository repo;
    private UserRepository userRepo;
    private  LeagueDataRepository leagueDataRepo;
    private  DateRepository dateRepo;
    private LigaRepository ligaRepo;
    private MailSenderService mailService;
    private FriendService friendService;
    @Autowired
    public BettingRoundController (BettingRoundRepository repo, UserRepository userRepo, LigaRepository ligaRepo, DateRepository dateRepo, LeagueDataRepository leagueDataRepo, MailSenderService mailService, FriendService friendService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.leagueDataRepo = leagueDataRepo;
        this.dateRepo = dateRepo;
        this.ligaRepo = ligaRepo;
        this.mailService = mailService;
        this.friendService= friendService;
    }


    @PostMapping("create")
    public ResponseEntity<?> createBettingRound(@RequestParam("name")String name,
                                                @RequestParam("ownerID")Long ownerID,
                                                @RequestParam("ligaID")Long ligaID,
                                                @RequestParam("isPrivate")Boolean isPrivate,
                                                @RequestParam("corrScorePoints")int corrScorePoints,
                                                @RequestParam("corrGoalPoints")int corrGoalPoints,
                                                @RequestParam("corrWinnerPoints")int corrWinnerPoints,
                                                @RequestParam(value = "password", required = false)String password) {

        BettingRound bettingRound = new BettingRound(name, ownerID, ligaID, isPrivate, corrScorePoints, corrGoalPoints, corrWinnerPoints, password);
        repo.save(bettingRound);
        addParticipant(ownerID, bettingRound.getId(), "");
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);
    }

    @PostMapping("addParticipant")
    public ResponseEntity<?> addParticipant(@RequestParam("userid") Long userid, @RequestParam("bettingRoundid") Long bettingRoundid, @RequestParam("password")String password) {
        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        List<User> participants = bettingRound.getParticipants();
        User newParticipant = userRepo.findById(userid).get();
        //Check if user already participating
        if(participants.contains(newParticipant)) return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

        //If bettinground has no PW or if User is Owner, User is immediately added
        if(bettingRound.getPassword().equals("undefined")  || bettingRound.getOwnerID() == userid) {
            participants.add(newParticipant);
            bettingRound.getScoresList().add(new Score(0, newParticipant));
            return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
        } else {
            //If User submits either no or a wrong Password, HttpStatus Forbidden
            if(password==null || !bettingRound.getPassword().equals(password)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            //Otherwise add User to Participants and save to Repo.
            participants.add(newParticipant);
            bettingRound.getScoresList().add(new Score(0, newParticipant));
            return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
        }
    }

    @GetMapping("getRoundsbyUserID/{id}")
    public ResponseEntity<?> getRoundsbyUserid(@PathVariable("id")Long id) {
        //Get all Bettingrounds from Repo
        List<BettingRound> list = repo.findAll().stream()
                //only return Bettingrounds in which User with id {id} participates
                //by checking whether userlist of bettinground contains current User
                .filter(bettingRound -> bettingRound.getParticipants().stream().anyMatch(user -> user.getId() == id)).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("getAllPublicRounds")
    public ResponseEntity<?>getAllPublicRounds(){

        List<BettingRound> list = repo.findAll().stream().filter(x -> !x.isIsprivate()).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);

    }



    @GetMapping("getBets")
    public ResponseEntity<?> getBets(@RequestParam("bettingRoundid")Long bettingRoundid, @RequestParam("userid")Long userid) {

        List<Bets> bets = repo.findById(bettingRoundid).get().getBetsList().stream()
                    .filter(x -> x.getUserID() == userid).collect(Collectors.toList());

        return new ResponseEntity<>(bets, HttpStatus.OK);
    }

    @PutMapping("getBetsByLeagueDataID")
    public ResponseEntity<?> BetsByLDI(@RequestParam("userID")Long userID, @RequestParam("leagueDataids")Integer[] leagueDataids, @RequestParam("bettingRoundID")Long bettingRoundID) {
        List<Bets> betsList = repo.findById(bettingRoundID).get().getBetsList();
        List<String> returnList = new ArrayList<>();

        for(int x = 0; x < leagueDataids.length; x++) {
            for(Bets bet: betsList) {
                if(bet.getUserID() == userID && bet.getLeagueData().getId() == leagueDataids[x]) {
                    returnList.add(bet.getBets());
                }
            }
            if(x == returnList.size()-1) continue;
            returnList.add("N/A");
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }


    @PostMapping("placeBet")
    public ResponseEntity<?> placeBet(@RequestParam("bettingRoundid")Long bettingRoundid,
                                      @RequestParam("userid")Long userid,
                                      @RequestParam("leagueDataid")int leagueDataid,
                                      @RequestParam("newBet")String newBet) {

        BettingRound bettingRound = repo.findById(bettingRoundid).get();
        List<Bets> betsList = bettingRound.getBetsList();
        for(Bets bet: betsList) {
            //Check if user already placed Bet on this Game
            if(bet.getUserID() == userid && bet.getLeagueData().getId() == leagueDataid) {
                if(newBet.equals("undefined")) betsList.remove(bet);
                else bet.setBets(newBet);
                //getLeagueDataByDate(bettingRound.getLigaID());
                return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.OK);
            }
        }
        if(newBet != null && !newBet.equals("undefined")) {
            LeagueData data = leagueDataRepo.findByid(leagueDataid);
            betsList.add(new Bets(newBet, userid, data));
        }
        return new ResponseEntity<>(repo.save(bettingRound), HttpStatus.CREATED);

    }



    @PostMapping("getTipHelp")
    public ResponseEntity<?> getTipHelpByTeams(@RequestParam("id") int id) {

        TipHelper tipHelper = new TipHelper(dateRepo, leagueDataRepo);

        LeagueData leagueData = new LeagueData();
        leagueData.setResult(tipHelper.tipHelp(id));

        return new ResponseEntity<>(leagueData, HttpStatus.OK);
    }


    //Langsam versteh ich warum man das eigentlich in Controller und Serviceklassen aufteilen sollte. NAJA MACHSTE NIX
    public List<LeagueData> getLeagueDataByDate(Long id) {
        //Get current Systemdate from Repository
        String currDate =  dateRepo.findAll().get(0).getLocalDate();

        List<LeagueData> list = ligaRepo.findLigaByid(id).getLeagueData().stream()
                .filter(x -> compareDates(x.getDate(), currDate) <= 0).collect(Collectors.toList());

        return list;
    }

    @GetMapping("leaderboard/{bettingroundid}")
    public ResponseEntity<?> leaderboard(@PathVariable("bettingroundid")Long id) {
        BettingRound bettingRound = repo.findById(id).get();
        String currDate =  dateRepo.findAll().get(0).getLocalDate();

        //Save resulting Bets with Scores to repository
        repo.save(calculateScore(currDate, bettingRound));

        List<Bets> betsList = bettingRound.getBetsList();

        List<Score> scoreList = bettingRound.getScoresList();
        for(Score score:scoreList) {
            for(Bets bet: betsList) {
                if((score.getUser().getId() == bet.getUserID()) && !(bet.getScore() == -1)) score.setScores(score.getScores()+ bet.getScore());
            }
        }
        //Sort Leaderboard by totalScore
        List<Score> list = scoreList.stream().sorted((x,y) -> y.getScores() - x.getScores()).collect(Collectors.toList());

        for(Score score:list) {
            for(Alias alias: bettingRound.getAliasList()) {
                if(score.getUser().getId() == alias.getUserID()) {
                    score.getUser().setFirstName(alias.getAlias());
                }
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PutMapping("/changeAlias")
    public ResponseEntity<?> changeAlias(@RequestParam("userID")Long userID, @RequestParam("bettingroundID")Long bettingroundID, @RequestParam("alias")String alias ) {
        if(alias.equals("undefined")) return new ResponseEntity<>(HttpStatus.OK);
        BettingRound betR = repo.getById(bettingroundID);
        List<Alias> aliasList = betR.getAliasList();
        if(!aliasList.isEmpty()){
        for(Alias userAlias : aliasList) {
            if(userAlias.getUserID() == userID) {
                userAlias.setAlias(alias);
                repo.save(betR);
                return  new ResponseEntity<>(HttpStatus.OK);
            }
        }}
        aliasList.add(new Alias(alias, userID));
        repo.save(betR);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //    return this.http.get(`${this.databaseURL+'/shareBets/'+currentEmail+'/'+bettingroundid+'/'+friendEmail}`)
    @GetMapping("/shareBets/{currentEmail}/{bettingroundid}/{friendEmail}")
    public ResponseEntity<?> shareBets(@PathVariable("currentEmail")String currentEmail,
                                       @PathVariable("bettingroundid")Long bettingroundid,
                                       @PathVariable("friendEmail")String friendEmail) {

        BettingRound bettingRound = repo.findById(bettingroundid).get();
        List<Bets> betsList = bettingRound.getBetsList();

        User user = userRepo.findUserByeMail(currentEmail);

        User mailRecipient = userRepo.findUserByeMail(friendEmail);

        Long userID = user.getId();
        Long friendID = mailRecipient.getId();

        if(!betsList.isEmpty()) {

            //Eig unnötig, hab ich nur hinzugefügt, damit die Mail n bisschen schöner formatiert ist.
            int longestPlayer1 = 0;
            int longestPlayer2 = 0;
            for(Bets tempBet:betsList) {
                if(tempBet.getLeagueData().getPlayer1().length() > longestPlayer1) longestPlayer1 = tempBet.getLeagueData().getPlayer1().length();
                if(tempBet.getLeagueData().getPlayer2().length() > longestPlayer2) longestPlayer2 = tempBet.getLeagueData().getPlayer2().length();
            }
            //Beggining of Mail Message
            String mailBody ="Moin " +mailRecipient.getFirstName()+ ",\nfolgende Tipps hab ich abgegeben.\n\nGaLiGrü \nDein "+user.getFirstName()+" "+user.getLastName()+"\n\n\n"+rightPadtext("Player 1", longestPlayer1)+"\t\t"+rightPadtext("Player 2", longestPlayer2)+"\t\tMein Tipp\n";
            for(Bets bet: betsList) {
                if(bet.getUserID() == userID) {
                String player1 = rightPadtext(bet.getLeagueData().getPlayer1(), longestPlayer1);
                String player2 = rightPadtext(bet.getLeagueData().getPlayer2(), longestPlayer2);
                String myBet = bet.getBets();
                mailBody += player1 +"\t\t" + player2+"\t\t"+ myBet+"\n";
                }
            }
            mailBody += "\n\nWenn du auch so geile Tipps abgeben willst, KOMM IN DIE GRUPPE";

            mailService.sendEmail(mailRecipient.geteMail(),
                    "Meine Tipps in Tipprunde: "+bettingRound.getName(),
                    mailBody);      //schick Email hoffentlich

            //After Mail was sent, HttpStatus.OK
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //Forbidden, if User has no Tipps in this Tippround.
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/getTipprundeByLigaID/{userID}/{bettingroundID}")
    public ResponseEntity<?> getTipprundeByLigaID(@PathVariable("userID")Long userID, @PathVariable("bettingroundID")Long bettingroundID) {
        List<BettingRound> allRounds = repo.findAll();
        List<BettingRound> returnList = new ArrayList<>();
        User currUser = userRepo.findById(userID).get();
        Long currLigaID = repo.findById(bettingroundID).get().getLigaID();
        for(BettingRound round: allRounds) {
            if(round.getParticipants().contains(currUser) && (round.getLigaID() == currLigaID) && (round.getId() != bettingroundID)) returnList.add(round);
            }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("transferBets/{fromTipprundenID}/{toTipprundenID}/{userID}")
    public ResponseEntity<?> transferBets(@PathVariable("fromTipprundenID")Long fromID, @PathVariable("toTipprundenID")Long toID, @PathVariable("userID")Long userID) {
        BettingRound fromRound = repo.findById(fromID).get();
        BettingRound toRound = repo.findById(toID).get();

        //Get existing Bets, filtered by userID
        List<Bets> existingBets = fromRound.getBetsList().stream().filter(x -> x.getUserID() == userID).collect(Collectors.toList());
        //If no Bets present, End here
        if(existingBets.isEmpty()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        //If user already has bets in new Tipping round, get bets
        List<Bets> newBets = toRound.getBetsList().stream().filter(x -> x.getUserID() == userID).collect(Collectors.toList());
        for(Bets oldBet: existingBets) {
            boolean changed = false;
            if(!newBets.isEmpty()) {
                for(Bets newBet: newBets) {
                    //Check if there already exists a bet for this game by the user
                    if((oldBet.getLeagueData().getId() == newBet.getLeagueData().getId())) {
                        newBet.setBets(oldBet.getBets());
                        changed = true;
                    }
                }
            }
            //If nothing was changed before add a new Bet to Bettinground
            if(!changed) {
                Bets newBet = new Bets(oldBet.getBets(), oldBet.getUserID(), oldBet.getLeagueData());
                toRound.getBetsList().add(newBet);

            }
            System.out.println(oldBet.getBets());
        }
        repo.save(toRound);
        repo.save(fromRound);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getTippRoundByID/{bettingRoundID}")
    public ResponseEntity<?> getTipprundeByLigaID(@PathVariable("bettingRoundID")Long bettingRoundID) {
        BettingRound currRound = repo.findById(bettingRoundID).get();
       return new ResponseEntity<>(currRound, HttpStatus.OK);


    }
    @GetMapping("/getAllPrivateRounds/{userid}")
    public ResponseEntity<?> getAllPrivateRounds(@PathVariable Long userid) {
        List<User> friends =  friendService.getFriendsByUserID(userid);
        List<BettingRound> list = repo.findAll().stream().filter(x -> x.isIsprivate()).collect(Collectors.toList());
        List<BettingRound> returnList = new LinkedList<>();
        Long[] friendIds = new Long[friends.size()];

        for(int i=0; i<friendIds.length;i++){
            friendIds[i]=friends.get(i).getId();
        }
        for(int i=0;i<friendIds.length;i++) {
            for (BettingRound betsRound : list) {
                if (betsRound.getOwnerID() == friendIds[i]) {
                    returnList.add(betsRound);
                }
            }
        }

        return new ResponseEntity<>(returnList, HttpStatus.OK);

    }
   /* @GetMapping("/getAllPrivateRoundsFromOwner/{userid}")
    public ResponseEntity<?> getAllPrivateFromOwnerRounds(@PathVariable Long userid) {
        List<BettingRound> list = repo.findAll().stream().filter(x -> x.isIsprivate()).collect(Collectors.toList());
        List<BettingRound> returnList = new LinkedList<>();

        for(BettingRound betsRound:list){
            if(betsRound.getOwnerID()==userid){
                returnList.add(betsRound);
            }
        }

        return new ResponseEntity<>(returnList, HttpStatus.OK);

    }*/
   @PostMapping("/getDisabled")
    public ResponseEntity<?> getDisabled(@RequestParam(required = false, name = "date") String[] gameDate) {

        String currDate =  dateRepo.findAll().get(0).getLocalDate();
        boolean[] isDisabled = new boolean[gameDate.length];

        for(int i = 0; i < gameDate.length; i++) {
            if(compareDates(gameDate[i], currDate) >= 0) {isDisabled[i] = false;}
            else {isDisabled[i] = true;}
        }
        return new ResponseEntity<>(isDisabled, HttpStatus.OK);
    }
}
