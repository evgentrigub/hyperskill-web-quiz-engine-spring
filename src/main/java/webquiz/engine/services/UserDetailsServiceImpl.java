package webquiz.engine.services;

import webquiz.engine.UserDetailsImpl;
import webquiz.engine.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userResponse = userService.getUser(username);
        if (userResponse.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new UserDetailsImpl(userResponse.get());
    }
}
