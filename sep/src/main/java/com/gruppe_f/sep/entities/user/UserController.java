package com.gruppe_f.sep.entities.user;

import com.gruppe_f.sep.mail.MailSenderService;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;


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
    public ResponseEntity<User> addUser(@RequestBody User userData) {

        for(User user: service.findAllUsers()) {
            if (user.geteMail().equals(userData.geteMail())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        User newUser = service.addUser(userData);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody User userData) {
        User user = service.findUserByeMail(userData.geteMail());
        if(user.getPassword().equals(userData.getPassword()))

            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/user/2FA")
    public ResponseEntity<?> zweiFaUser(@RequestBody User userData){

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);               //Generierter Code 30 Sekunden(Standard)

        SecretGenerator secretGenerator = new DefaultSecretGenerator(6);                //Generierter Code 6 Zeichen lang
        userData.setSecret(secretGenerator.generate());                                             //generiere Code

        mailSenderService.sendEmail(userData.geteMail(),
                "Verifikationscode",
                "Ihr Verifikationsvode lautet:" + userData.getSecret());      //schick Email hoffentlich

        User user = service.findUserByeMail(userData.geteMail());
        if(verifier.isValidCode(user.getSecret(), userData.getCode())){             //vergleich generierten code(secret) und eingegebenen code(code)
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }



    }

}
