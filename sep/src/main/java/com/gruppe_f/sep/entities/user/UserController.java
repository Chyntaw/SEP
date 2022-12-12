package com.gruppe_f.sep.entities.user;


import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.businesslogic.ImageLogic.ImageRepository;
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
import java.util.ArrayList;
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


    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    public UserController(UserService service, ImageRepository imageRepository, FriendService friendService) {
        this.service = service;
        this.imageRepository = imageRepository;
        this.friendService = friendService;
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

        User user;
        user = new User(firstName, lastName, eMail, password, role);

        for(User userDatabase: service.findAllUsers()) {
            if (user.geteMail().equals(userDatabase.geteMail())) {                  //wenn userData Mail(eingegebene Mail) in der Datenbank ist -> Forbidden
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(role.equals("BASIC")){
            if(multipartFile != null){
                ImageModel img = new ImageModel(multipartFile.getOriginalFilename(),
                        multipartFile.getContentType(),
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
        //System.out.println(userData.toString());

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





/*
    @GetMapping("/user/{eMail}/imagesForPendingFriends")
    public ArrayList<ImageModel> getImagesForPendingFriends(@PathVariable("eMail") String currentEmail){

        User currentUser = service.findUserByeMail(currentEmail);

        List<User> friends = friendService.getPendingFriends(currentUser);

        ArrayList<ImageModel> retrievedImages = new ArrayList<ImageModel>();
        ImageModel imageModel;


        for(User userDatabase: friends){
            if(userDatabase.getImage() == null){
                Optional<ImageModel> retrievedImage= imageRepository.findByName("StandardBild");
                imageModel = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                        decompressBytes(retrievedImage.get().getPicByte()));
                retrievedImages.add(imageModel);
            }
            else{
                Optional<ImageModel> retrievedImage= imageRepository.findByName(userDatabase.getImage().getName());
                imageModel = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                        decompressBytes(retrievedImage.get().getPicByte()));
                retrievedImages.add(imageModel);

            }
        }
        return retrievedImages;
    }


 */
    @GetMapping("/user/{eMail}/image")
    public ImageModel getImage(@PathVariable("eMail") String currentEmail){

        User currentUser = service.findUserByeMail(currentEmail);

        if(currentUser.getImage() == null){
            System.out.println("kein Bild");
            final Optional<ImageModel> retrievedImage = imageRepository.findByName("StandardBild");
            System.out.println("1");
            ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType());
            System.out.println("2");
            System.out.println(img.getName() + " "+ img.getType());
            return img;
        }
        else{
            System.out.println("Bild");
            final Optional<ImageModel> retrievedImage = imageRepository.findByName(currentUser.getImage().getName());
            ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                    decompressBytes(retrievedImage.get().getPicByte()));
            System.out.println(img.getName() + " "+ img.getType());
            return img;
        }
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

