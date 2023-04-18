package S10.VVSS.lab1;

import S10.VVSS.lab1.config.jwt.JwtTokenService;
import S10.VVSS.lab1.entities.user.User;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;


public class AuthenticatedAbstractTest extends AbstractTest{
    private final String username;
    protected JwtTokenService tokenService;
    protected User user;

    @Autowired
    public AuthenticatedAbstractTest(WebApplicationContext context) {
        this(context, "user");
        tokenService = context.getBean(JwtTokenService.class);
    }

    @Autowired
    public AuthenticatedAbstractTest(WebApplicationContext context, String username) {
        super(context);
        this.username = username;
        tokenService = context.getBean(JwtTokenService.class);
    }

    protected String generateToken() {
        user = userService.findByUsername(username).orElse(null);
        Assumptions.assumeFalse(user == null, String.format("user[username='%s'] not found", username));
        return tokenService.createToken(user.getId());
    }

    @Override
    protected MockHttpServletRequestBuilder defaultRequestBuilder() {
        return super.defaultRequestBuilder()
                .header(HttpHeaders.AUTHORIZATION, generateToken());
    }

    protected void restartTransaction() {
        TestTransaction.flagForRollback();
        TestTransaction.end();
        TestTransaction.start();
        TestTransaction.flagForRollback();
    }

}
