package com.gruppe_f.sep.entities.friends;

import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.businesslogic.ImageLogic.ImageRepository;
import com.gruppe_f.sep.entities.user.User;
import com.gruppe_f.sep.entities.user.UserService;
import com.gruppe_f.sep.mail.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FriendController {

    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    MailSenderService mailSenderService;

    private final ImageRepository imageRepository;

    @Autowired
    public FriendController(UserService userService, FriendService friendService, ImageRepository imageRepository) {
        this.userService = userService;
        this.friendService = friendService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/user/addFriend")             //send friendrequest
    public ResponseEntity<?> addFriend(@RequestParam("emailUser1") String emailUser1,
                                       @RequestParam("emailUser2") String emailUser2){

        User user1 = userService.findUserByeMail(emailUser1);
        User user2 = userService.findUserByeMail(emailUser2);

        if(user1.getId() != user2.getId()){
            friendService.sendFriendRequest(emailUser1, emailUser2, true);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }




    @GetMapping("/user/listFriends/{eMail}")                            //show accepted friends (pending = false)
    public ResponseEntity<List<User>> getFriends(@PathVariable("eMail") String email) {

        User currentUser = userService.findUserByeMail(email);
        List<User> myFriends = friendService.getFriends(currentUser);

        for(User user : myFriends) {
        if(user.getImage() == null){
            user.setImage(new ImageModel("StandardBild", "image/jpeg", null));
        }
        else{

            final Optional<ImageModel> retrievedImage = imageRepository.findByuserMail(user.geteMail());

            ImageModel img = new ImageModel(user.geteMail(), retrievedImage.get().getType(),
                    decompressBytes(retrievedImage.get().getPicByte()));

            user.setImage(img);
        }
        }

        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }


    @GetMapping("/user/listPendingFriends/{eMail}")                            //show Pending friends (pending = true)
    public ResponseEntity<List<User>> getPendingFriends(@PathVariable("eMail") String email) {

        User currentUser = userService.findUserByeMail(email);
        List<User> myFriends = friendService.getPendingFriends(currentUser);

        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }


    @GetMapping("/user/listPendingRequestedFriends/{eMail}")                            //show Pending friends (pending = true)
    public ResponseEntity<List<User>> getPendingRequestedFriends(@PathVariable("eMail") String email) {

        User currentUser = userService.findUserByeMail(email);
        List<User> myFriends = friendService.getPendingRequestedFriends(currentUser);

        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }


    @PostMapping("/user/acceptFriend")          //accept friendrequest
    public ResponseEntity<?> acceptFriendRequest(@RequestParam("currentEmail") String currentEmail,
                                                 @RequestParam("friendEmail") String friendEmail){

        if(friendService.isPending(currentEmail, friendEmail)){
            friendService.acceptFriend(currentEmail, friendEmail);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/declineFriend")
    public ResponseEntity<?> declineFriendRequest(@RequestParam("currentEmail") String currentEmail,
                                                  @RequestParam("friendEmail") String friendEmail) {

        if(friendService.isPending(currentEmail, friendEmail)){
            friendService.declineFriend(currentEmail, friendEmail);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/deleteFriend")
    public ResponseEntity<?> deleteFriend(@RequestParam("currentEmail") String currentEmail,
                                          @RequestParam("friendEmail") String friendEmail){

        friendService.unfriend(currentEmail, friendEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/user/inviteFriend/{currentEmail}/{friendEmail}/{bettingroundid}")
    public ResponseEntity<?>sendTipprundenInvite(@PathVariable("currentEmail") String currentUserEmail,
                                                 @PathVariable("friendEmail") String friendEmail,
                                                 @PathVariable("bettingroundid") Long id){

        mailSenderService.sendEmail(friendEmail,
                currentUserEmail + " möchte Sie zur Tipprunde: " + " einladen.",
                "http://localhost:4200/einladungs-uebersicht/" + id);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/keinBildVorhanden")
    public ResponseEntity<?>keinBildVorhanden(){
        String keinBild = "Kein Bild vorhanden";
        System.out.println(keinBild);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
