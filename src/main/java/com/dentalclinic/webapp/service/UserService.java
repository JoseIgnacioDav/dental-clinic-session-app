package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.dto.request.user.UserRequestDTO;
import com.dentalclinic.webapp.dto.response.user.UserResponseDTO;
import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    // Repository interface for user data persistence operations
    private final UserRepository userRepository;
    // Encoder used to securely hash passwords before database storage
    private final PasswordEncoder passwordEncoder;

    //Constructor-based dependency injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    // safe dtos to return and get an user
    public UserResponseDTO userRegistration (UserRequestDTO receiveduserdto){
        User receivedUser = new User();
        receivedUser.setFirstnames(receiveduserdto.getFirstnames());
        receivedUser.setSurname(receiveduserdto.getSurname());
        receivedUser.setSocialsecuritynumber(receiveduserdto.getSocialsecuritynumber());
        receivedUser.setEmail(receiveduserdto.getEmail());
        receivedUser.setPassword(receiveduserdto.getPassword());

        // check if the user already exists
        Optional queryDbSocialSecNum = userRepository.findBySocialsecuritynumber(receivedUser.getSocialsecuritynumber());
        Optional queryDbEmail = userRepository.findByEmail(receivedUser.getEmail());

        if(queryDbEmail.isPresent()||queryDbSocialSecNum.isPresent()){
            throw new RuntimeException("The user already exists");
        }
        //this ciphers the password(bcrypt)
        String plainTextPswd = receivedUser.getPassword();
        String cipheredPaswd = passwordEncoder.encode(plainTextPswd);
        //set the safe passwd
        receivedUser.setPassword(cipheredPaswd);
        //save
        userRepository.save(receivedUser);
        //return the created user by a dto to avoid sensible information
        UserResponseDTO secureUserReturn = new UserResponseDTO();

        secureUserReturn.setId(receivedUser.getId());
        secureUserReturn.setFirstnames(receivedUser.getFirstnames());
        secureUserReturn.setSurname(receivedUser.getSurname());
        secureUserReturn.setEmail(receivedUser.getEmail());
        secureUserReturn.setSocialsecuritynumber(receivedUser.getSocialsecuritynumber());
        secureUserReturn.setRole(receivedUser.getRole());

        return secureUserReturn;
    }
}
