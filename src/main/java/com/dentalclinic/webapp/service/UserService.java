package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.dto.request.auth.LoginRequestDTO;
import com.dentalclinic.webapp.dto.request.user.UserRequestDTO;
import com.dentalclinic.webapp.dto.response.user.UserResponseDTO;
import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public UserResponseDTO userRegistration (UserRequestDTO receiveduserdto){
        User receivedUser = new User();
        receivedUser.setFirstnames(receiveduserdto.getFirstnames());
        receivedUser.setSurname(receiveduserdto.getSurname());
        receivedUser.setSocialsecuritynumber(receiveduserdto.getSocialsecuritynumber());
        receivedUser.setEmail(receiveduserdto.getEmail());
        receivedUser.setPassword(receiveduserdto.getPassword());

        // check if the user already exists
        Optional queryDbSocialSecNumOPT = userRepository.findBySocialsecuritynumber(receivedUser.getSocialsecuritynumber());
        Optional queryDbEmailOPT = userRepository.findByEmail(receivedUser.getEmail());

        if(queryDbEmailOPT.isPresent()||queryDbSocialSecNumOPT.isPresent()){
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
    // safe dtos to return and get information
    public UserResponseDTO login (LoginRequestDTO credentials){
        Optional<User> queryDbEmailOPT = userRepository.findByEmail(credentials.getEmail());

        //check if the user exists already in the db. if so we check if it matches the password
        if(queryDbEmailOPT.isEmpty()){
            throw new RuntimeException("The user does not exist");
        }
        User queriedUser = queryDbEmailOPT.get();
        if(!passwordEncoder.matches(credentials.getPassword(), queriedUser.getPassword())){
            throw new RuntimeException("The user does not exist"); // to avoid bruteforcing

        }

        // if it matches we return the safe DTO
        User user = queriedUser;
        UserResponseDTO secureUserReturn = new UserResponseDTO();
        secureUserReturn.setId(user.getId());
        secureUserReturn.setFirstnames(user.getFirstnames());
        secureUserReturn.setSurname(user.getSurname());
        secureUserReturn.setEmail(user.getEmail());
        secureUserReturn.setSocialsecuritynumber(user.getSocialsecuritynumber());
        secureUserReturn.setRole(user.getRole());

        return secureUserReturn;

    }
}
