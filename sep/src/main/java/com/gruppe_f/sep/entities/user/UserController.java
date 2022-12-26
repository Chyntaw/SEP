package com.gruppe_f.sep.entities.user;


import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.businesslogic.ImageLogic.ImageRepository;
import com.gruppe_f.sep.date.DateService;
import com.gruppe_f.sep.date.SystemDate;
import com.gruppe_f.sep.entities.friends.FriendRepository;
import com.gruppe_f.sep.entities.friends.FriendService;
import com.gruppe_f.sep.mail.MailSenderService;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;
    private final ImageRepository imageRepository;

    private final FriendService friendService;
    private final DateService dateService;


    @Autowired
    MailSenderService mailSenderService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService service, ImageRepository imageRepository, FriendService friendService, DateService dateService,
                          UserRepository userRepository) {
        this.service = service;
        this.imageRepository = imageRepository;
        this.friendService = friendService;
        this.dateService = dateService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/findall")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user/add")
    public ResponseEntity<User> addUser(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("firstName") String firstName,
                                        @RequestParam(value = "birthDate", required = false) String birthDate,
                                        @RequestParam("eMail") String eMail,
                                        @RequestParam("password") String password,
                                        @RequestParam("role") String role) throws IOException {


        User user = new User(firstName, lastName, eMail, password, role);

        for(User userDatabase: service.findAllUsers()) {
            if (user.geteMail().equals(userDatabase.geteMail())) {                  //wenn userData Mail(eingegebene Mail) in der Datenbank ist -> Forbidden
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(role.equals("BASIC")){
            if(multipartFile != null){

                ImageModel img = new ImageModel(eMail, multipartFile.getContentType(),
                        compressBytes(multipartFile.getBytes()));

                user.setBirthDate(birthDate);

                user.setImage(img);
                User newUser = service.addUser(user);
                imageRepository.save(img);
                return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            }
            else{
                user.setBirthDate(birthDate);
                User newUser = service.addUser(user);
                return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            }
        }
        else{
            User newUser = service.addUser(user);

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody User userData) {

        SecretGenerator secretGenerator = new DefaultSecretGenerator(8);                //Generierter Code 6 Zeichen lang
        userData.setSecret(secretGenerator.generate());                                             //generiere Code


        User user = service.findUserByeMail(userData.geteMail());
        if(user.getPassword().equals(userData.getPassword())) {
            mailSenderService.sendEmail(userData.geteMail(),
                    "Verifikationscode",
                    "Ihr Verifikationsvode lautet: " + userData.getSecret());      //schick Email hoffentlich
            User userToUpdate = service.getReferenceById(user.getId());
            userToUpdate.setCode(userData.getSecret());
            service.save(userToUpdate);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PostMapping("/user/loginInvite")
    public ResponseEntity<?> loginUser2(@RequestParam("eMail") String eMail,
                                        @RequestParam("password") String password) {

        User user = service.findUserByeMail(eMail);
        if(user.getPassword().equals(password)) {

            User userToUpdate = service.getReferenceById(user.getId());
            service.save(userToUpdate);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/user/2FA")
    public ResponseEntity<?> zweiFaUser(@RequestBody User userData){

        String user_mail = userData.geteMail();

        User user = service.findUserByeMail(user_mail);

        //SuperSicherheits Code um 2FA zu umgehen
        if(userData.getCode().equals("SecureLogin")) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(user.getCode().equals(userData.getCode())){                          //vergleich generierten code(secret) und eingegebenen code(code)
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
    @PostMapping("/user/getUser")
    public ResponseEntity<?> getProfile(@RequestParam("email") String email){
        for(User userDatabase: service.findAllUsers()){
            if(userDatabase.geteMail().equals(email)){
                return new ResponseEntity<>(userDatabase, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/user/getUserbyID/{userid}")
    public ResponseEntity<?> getUserByID(@PathVariable("userid") Long userid){
            List<User> userList = service.findAllUsers();
       for(User userDatbase: userList){
           if(userDatbase.getId()==userid){
               return new ResponseEntity<>(userDatbase,HttpStatus.OK);
           }
       }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @GetMapping("/user/{eMail}/image")
    public ImageModel getImage(@PathVariable("eMail") String currentEmail){

        User currentUser = service.findUserByeMail(currentEmail);

        if(currentUser.getImage() == null){

            return new ImageModel("StandardBild", "image/jpeg", null);
        }
        else{
            System.out.println("Bild");
            final Optional<ImageModel> retrievedImage = imageRepository.findByuserMail(currentEmail);
            ImageModel img = new ImageModel(currentEmail, retrievedImage.get().getType(),
                    decompressBytes(retrievedImage.get().getPicByte()));
            System.out.println(img.getName() + " "+ img.getType());
            return img;
        }
    }

    @GetMapping("/user/isOldEnough/{eMail}")
    public boolean isOldEnough(@PathVariable("eMail") String currentEmail){
        User currentUser = service.findUserByeMail(currentEmail);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate systemDate = LocalDate.parse(dateService.getAll().get(0).getLocalDate());
        LocalDate geburtstag = LocalDate.parse(currentUser.getBirthDate(), df);

        Period period = Period.between(geburtstag, systemDate);

        if(period.getYears() >= 18){
            return true;
        }
        else{
            return false;
        }
    }

    @GetMapping("/user/isBeantragt/{eMail}")
    public boolean isBeantragt(@PathVariable("eMail") String currentEmail){
        User currentUser = service.findUserByeMail(currentEmail);
        if(currentUser.isFreischaltungBeantragt()){
            return true;
        }
        else{
            return false;
        }
    }

    @GetMapping("/user/istFreigeschaltet/{eMail}")
    public boolean istFreigeschaltet(@PathVariable("eMail") String currentEmail){
        User currentUser = service.findUserByeMail(currentEmail);
        if(currentUser.isFreigeschaltet()){
            return true;
        }
        else{
            return false;
        }
    }


    @GetMapping("/user/beantrageFreischaltung/{eMail}")
    public ResponseEntity<?> beantrageFreischaltung(@PathVariable("eMail") String currentEmail){
        User currentUser = service.findUserByeMail(currentEmail);
        currentUser.setFreischaltungBeantragt(true);
        userRepository.save(currentUser);

        for(User userData: service.findAllUsers()){
            if(userData.getRole().equals("ADMIN")){
                mailSenderService.sendEmail(userData.geteMail(),
                        "Freischaltung",
                        "Merkel mach Tipico für: " + currentEmail + " auf!");

            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/listAcceptedUsers")
    public ResponseEntity<List<User>> listAcceptedUsers() {
        List<User> pendingUsers = new ArrayList<>();

        for (User userData : userRepository.findAll()) {
            if (userData.isFreigeschaltet()) {
                pendingUsers.add(userData);
            }
        }
        return new ResponseEntity<>(pendingUsers, HttpStatus.OK);
    }

        @GetMapping("/user/listPendingUsers")
    public ResponseEntity<List<User>> listPendingUsers(){
        List<User> pendingUsers = new ArrayList<>();

        for(User userData: userRepository.findAll()){
            if(userData.isFreischaltungBeantragt() && !userData.isFreigeschaltet()){
                pendingUsers.add(userData);
            }
        }
        return new ResponseEntity<>(pendingUsers, HttpStatus.OK);
    }

    @GetMapping("/user/acceptUser/{eMail}")
    public ResponseEntity<?> acceptUser(@PathVariable("eMail") String eMail){

        for(User userData: userRepository.findAll()){
            if(userData.geteMail().equals(eMail)){
                userData.setFreigeschaltet(true);
                userRepository.save(userData);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/declineUser/{eMail}")
    public ResponseEntity<?> declineUser(@PathVariable("eMail") String eMail){

        for(User userData: userRepository.findAll()){
            if(userData.geteMail().equals(eMail)){
                userData.setFreischaltungBeantragt(false);
                userRepository.save(userData);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
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

