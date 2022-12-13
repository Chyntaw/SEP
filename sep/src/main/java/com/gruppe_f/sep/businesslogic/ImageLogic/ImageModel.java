package com.gruppe_f.sep.businesslogic.ImageLogic;

import javax.persistence.*;

@Entity
@Table(name = "image_table")
public class ImageModel {



    @Id
    @Column(name = "pictureid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userMail;

    @Column(name = "type")
    private String type;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "picByte", length = 1000)
    private byte[] picByte;

    public ImageModel() {
        super();
    }

    public ImageModel(String userMail, String type, byte[] picByte) {
        this.userMail = userMail;
        this.type = type;
        this.picByte = picByte;
    }

    public ImageModel(String userMail, String type) {
        this.userMail = userMail;
        this.type = type;
    }


    public String getName() {
        return userMail;
    }

    public void setName(String name) {
        this.userMail = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

}
