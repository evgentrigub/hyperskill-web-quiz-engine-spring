package webquiz.engine.services;

import webquiz.engine.models.User;
import webquiz.engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean isEmailExists(String email){
        return this.userRepository.existsByEmail(email);
    }

    public User getUserIfExists(String email){
        Optional<User> userResponse = this.userRepository.findByEmail(email);
        if(userResponse.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. User email: " + email);
        }
        return userResponse.get();
    }

    public Optional<User> getUser(String email){
        return userRepository.findByEmail(email);
    }

    public void postUser(User user){
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        } catch (Error error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
