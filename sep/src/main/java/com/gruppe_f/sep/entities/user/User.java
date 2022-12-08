package com.gruppe_f.sep.entities.user;

import com.gruppe_f.sep.businesslogic.ImageLogic.ImageModel;
import com.gruppe_f.sep.entities.BettingRound.BettingRound;
import com.gruppe_f.sep.entities.leagueData.LeagueData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name = "users")
public class User implements Serializable {

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
    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pictureID" , referencedColumnName = "pictureid")
    private ImageModel image;

    @ManyToMany(mappedBy = "participants")
    private List<BettingRound> bettingRounds;

    private String secret;          //Generierte OTP

    private String code;           //Eingegebene OTP

    public User() {}

    public User(String firstName, String lastName, String birthDate, String eMail, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.eMail = eMail;
        this.password = password;
        this.role = role;
    }

    //Create new User with Profile Picture (optional)
    public User(String firstName, String lastName, String birthDate, String eMail, String password, ImageModel imageModel, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.eMail = eMail;
        this.password = password;
        this.image = imageModel;
        this.role = role;
    }

    public User (String firstName, String lastName, String eMail, String password, String role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.password = password;
        this.role = role;
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
                ", role='"+ role+ '\'' +
                ", bild='" + image+
                "'}";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Long getId() {return this.id;}

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
