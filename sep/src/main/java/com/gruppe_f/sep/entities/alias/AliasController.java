package com.gruppe_f.sep.entities.alias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class AliasController {
    private AliasRepository aliasrepo;
    @Autowired
    public AliasController (AliasRepository aliasrepo){
        this.aliasrepo = aliasrepo;
    }
}
