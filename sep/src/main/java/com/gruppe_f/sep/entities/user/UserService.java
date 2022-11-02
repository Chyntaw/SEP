package com.gruppe_f.sep.entities.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {this.repo = repo;}

    public User addUser(User user) {return repo.save(user);}

    public List<User> findAllUsers() {return repo.findAll();}

    public User findUserByeMail(String eMail) {return repo.findUserByeMail(eMail);}




}
