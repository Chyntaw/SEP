package com.gruppe_f.sep.businesslogic.ImageLogic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long > {

    Optional<ImageModel> findByuserMail(String userMail);
}
