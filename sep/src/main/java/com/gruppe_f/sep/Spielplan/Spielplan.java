package com.gruppe_f.sep.Spielplan;

import com.gruppe_f.sep.Spieltag.Spieltag;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Spielplan {

    @Id
    private long spielplanID;

    private List<Spieltag> spieltag;






}
