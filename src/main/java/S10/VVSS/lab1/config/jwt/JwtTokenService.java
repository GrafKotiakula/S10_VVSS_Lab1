package S10.VVSS.lab1.config.jwt;

import S10.VVSS.lab1.entities.user.User;
import S10.VVSS.lab1.entities.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenService {
    public static final String bearerTokenPrefix = "Bearer ";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final String secret;
    private final Long tokenExpirationMillis;

    @Autowired
    public JwtTokenService(@Value("${jwt.token.secret}") String secret,
                           @Value("${jwt.token.expired}")Long tokenExpirationMillis,
                           UserService userService) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.tokenExpirationMillis = tokenExpirationMillis;
        this.userService = userService;
    }

    public String createToken(UUID id, Date enabled, Date expired){
        Claims claims = Jwts.claims().setSubject(id.toString());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setNotBefore(enabled)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String createToken(UUID id){
        Date enabled = new Date(); //now
        Date expired = new Date(enabled.getTime() + tokenExpirationMillis);
        return createToken(id, enabled, expired);
    }

    private User getUser(String token) throws JwtException {
        String subject = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        try {
            UUID id = UUID.fromString(subject);
            return userService.findById(id).orElseThrow(() -> {
                logger.warn(String.format("User with id %s does not exist", subject));
                return new JwtException("invalid token");
            });
        } catch (IllegalArgumentException ex) {
            logger.warn("Jwt token's subject is not UUID");
            throw new JwtException("invalid token");
        }
    }

    public Authentication getAuthentication(String token) throws JwtException {
        if(token.startsWith(bearerTokenPrefix)){
            token = token.substring(bearerTokenPrefix.length());
        }
        User user = getUser(token);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
