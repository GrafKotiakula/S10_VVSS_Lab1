package S10.VVSS.lab1.config.jwt;

import S10.VVSS.lab1.ExtendedLogger;
import S10.VVSS.lab1.dto.FailureDto;
import S10.VVSS.lab1.exception.AuthException;
import S10.VVSS.lab1.exception.ClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ExtendedLogger extendedLogger = new ExtendedLogger(logger);
    private final JwtTokenService provider;
    private final ObjectMapper mapper;

    @Autowired
    public JwtTokenFilter(JwtTokenService provider, ObjectMapper mapper) {
        this.provider = provider;
        this.mapper = mapper;
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private void onError(ClientException error, HttpServletResponse response) throws IOException {
        FailureDto dto = new FailureDto(error);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(dto));
        response.flushBuffer();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return getAuthorization(request) == null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication auth = provider.getAuthentication(getAuthorization(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            onError(extendedLogger.debug(AuthException.expiredToken(ex)), response);
        } catch (JwtException ex) {
            onError(extendedLogger.debug(AuthException.invalidToken(ex)), response);
        }
    }
}
