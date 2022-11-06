package com.gruppe_f.sep.entities.user;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "users")
public class User implements Serializable {

    public enum Role {
        ADMIN,
        BASIC
    }
    @Transient
    private final String MASTER_PASSWORD = "mirdochwayne";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private Long id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "yearOfBirth")
    private String birthDate;
    @Column(name = "eMail")
    private String eMail;
    @Column(name = "password")
    private String password;
    @Lob
    @Column(name = "profilePicture")
    private byte[] profilePicture;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


    private String secret;         //Generierte OTP
    private String code;           //Eingegebene OTP

    protected User() {}

    public User(String firstName, String lastName, String birthDate, String eMail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.eMail = eMail;
        this.password = password;
        if(password.equals(MASTER_PASSWORD)) {
            this.role = Role.ADMIN;
        } else {this.role = Role.BASIC;}
    }

    //Create new User with Profile Picture (optional)
    public User(String firstName, String lastName, String birthDate, String eMail, String password, byte[] profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.eMail = eMail;
        this.password = password;
        this.profilePicture = profilePicture;
        if(password.equals(MASTER_PASSWORD)) {
            this.role = Role.ADMIN;
        } else {this.role = Role.BASIC;}
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", eMail='" + eMail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getSecret() {
        return secret;
    }

    public String getCode() {
        return code;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
