package S10.VVSS.lab1.database.user;

import S10.VVSS.lab1.database.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, UserRepository> implements UserDetailsService {

    @Autowired
    public UserService(UserRepository repo) {
        super(repo);
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }

    public boolean isUsernameUsed(String username) {
        return repo.findByUsername(username).isPresent();
    }
}
