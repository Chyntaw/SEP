package com.gruppe_f.sep.entities.user;


import com.gruppe_f.sep.businesslogic.FileUploadUtil;
import com.gruppe_f.sep.mail.MailSenderService;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.hibernate.engine.jdbc.spi.ConnectionObserverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Observable;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
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
                System.out.println("TESTEST");
                user.setProfilePicture(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
                user.setBirthDate(birthDate);

                User newUser = service.addUser(user);
                String uploadDir = "Pictures/user-photos/" + user.getId();
                FileUploadUtil.saveFile(uploadDir, StringUtils.cleanPath(multipartFile.getOriginalFilename()), multipartFile);
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

    @PostMapping("/user/2FA")
    public ResponseEntity<?> zweiFaUser(@RequestBody User userData){
        //System.out.println(userData.toString());

        String user_mail = userData.geteMail();

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

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

}
