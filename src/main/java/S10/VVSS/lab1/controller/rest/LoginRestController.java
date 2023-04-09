package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.config.jwt.JwtTokenService;
import S10.VVSS.lab1.database.user.User;
import S10.VVSS.lab1.database.user.UserService;
import S10.VVSS.lab1.dto.LogInRequestDto;
import S10.VVSS.lab1.dto.LogInResponseDto;
import S10.VVSS.lab1.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    @Autowired
    public LoginRestController(UserService userService, PasswordEncoder passwordEncoder,
                               JwtTokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public LogInResponseDto login(@RequestBody LogInRequestDto request) throws AuthException {
        User user = userService.findByUsername(request.getUsername()).orElseThrow(() -> {
            logger.info(String.format("No user with username '%s' found", request.getUsername()));
            return AuthException.wrongUsernameOrPassword();
        });
        if( passwordEncoder.matches(request.getPassword(), user.getPassword()) ) {
            String token = tokenService.createToken(user.getId());
            return new LogInResponseDto(user.getId(), user.getUsername(), token);
        } else {
            logger.debug(String.format("Invalid password for user '%s'", request.getUsername()));
            throw AuthException.wrongUsernameOrPassword();
        }
    }
}
