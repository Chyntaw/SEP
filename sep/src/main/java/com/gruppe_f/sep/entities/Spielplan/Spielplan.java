package com.gruppe_f.sep.entities.Spielplan;

import com.gruppe_f.sep.entities.Spieltag.Spieltag;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Spielplan {

    @Id
    private long spielplanID;

    private List<Spieltag> spieltag;






}
