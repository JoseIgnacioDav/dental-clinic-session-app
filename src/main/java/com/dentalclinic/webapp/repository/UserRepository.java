package com.dentalclinic.webapp.repository;

import com.dentalclinic.webapp.model.Role;
import com.dentalclinic.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //returns optional because User could not exist and we make it safer
    Optional<User> findByEmail(String email);//
    //they are used to find if an user already exist in the db
    Optional<User> findBySocialsecuritynumber(String socialsecuritynumber);


    //it will be used to find every user that has a certain role
    List<User> findByRole(Role role);

}
