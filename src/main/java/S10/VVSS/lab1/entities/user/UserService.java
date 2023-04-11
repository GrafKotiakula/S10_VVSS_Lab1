package S10.VVSS.lab1.entities.user;

import S10.VVSS.lab1.entities.AbstractService;
import S10.VVSS.lab1.exception.ClientException;
import S10.VVSS.lab1.exception.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserRepository> implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        super(repo, User.class);
        this.passwordEncoder = passwordEncoder;
    }

    private void validateUsernameUnique(User user) {
        boolean isRepeated = repo.findByUsername(user.getUsername())
                .map(u -> !u.getId().equals(user.getId()))
                .orElse(false);
        if(isRepeated) {
            throw UnprocessableEntityException.duplicatedField("username", user.getUsername(), User.class);
        }
    }

    @Override
    public void validate(User user) throws ClientException {
        validateNotNull("username", user.getUsername());
        validateStringLength("username", user.getUsername(), 50);
        validateNotBlank("username", user.getUsername());
        validateStringMatches(user.getUsername(), "[a-zA-Z0-9\\-_]*",
                UnprocessableEntityException.usernameInvalidCharacters());
        validateUsernameUnique(user);

        validateNotNull("password", user.getPassword());
        validateStringLength("password", user.getPassword(), 5, 30);
        validateStringMatches(user.getPassword(), "(?=[a-z])(?=[A-Z])(?=[0-9])",
                UnprocessableEntityException.passwordNoRequiredSymbols());
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public boolean isUsernameUsed(String username) {
        return repo.findByUsername(username).isPresent();
    }

    @Override
    public User save(User user) {
        if(user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return super.save(user);
    }
}
