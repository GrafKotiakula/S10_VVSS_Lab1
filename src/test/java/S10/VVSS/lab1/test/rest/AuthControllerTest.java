package S10.VVSS.lab1.test.rest;

import S10.VVSS.lab1.AbstractTest;
import S10.VVSS.lab1.entities.user.User;
import S10.VVSS.lab1.entities.user.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("auth")
public class AuthControllerTest extends AbstractTest {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthControllerTest(WebApplicationContext context) {
        super(context);
        passwordEncoder = context.getBean(PasswordEncoder.class);
        userService = context.getBean(UserService.class);
    }

    @Test
    @DisplayName("login success")
    @Tag("login") @Tag("success")
    public void login_success() throws Exception {
        final String username = "user";
        final String password = "Ab12345";
        User user = userService.findByUsername(username).orElse(null);

        Assumptions.assumeFalse(user == null, "User not found");
        Assumptions.assumeTrue(passwordEncoder.matches(password, user.getPassword()), "Wrong password");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.user.id", is(user.getId().toString()) ),
                        jsonPath("$.user.username", is(username)),
                        jsonPath("$.user.password").doesNotExist(),
                        jsonPath("$.token", notNullValue()) );
    }

    @Test
    @DisplayName("login fail - wrong password")
    @Tag("login") @Tag("fail")
    public void login_fail_wrongPassword() throws Exception {
        final String username = "user";
        final String password = "wrong_password";
        User user = userService.findByUsername(username).orElse(null);

        Assumptions.assumeFalse(user == null, "User not found");
        Assumptions.assumeFalse(passwordEncoder.matches(password, user.getPassword()), "Password is correct");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("login fail - wrong username")
    @Tag("login") @Tag("fail")
    public void login_fail_wrongUsername() throws Exception {
        final String username = "wrong_username";
        final String password = "wrong_password";

        Assumptions.assumeFalse(userService.findByUsername(username).isPresent(), "User is found");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("login fail - password not presented")
    @Tag("login") @Tag("fail")
    public void login_fail_noPassword() throws Exception {
        final String username = "user";

        Assumptions.assumeTrue(userService.findByUsername(username).isPresent(), "User not found");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("login fail - username not presented")
    @Tag("login") @Tag("fail")
    public void login_fail_noUsername() throws Exception {
        final String password = "wrong_password";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("login fail - password is null")
    @Tag("login") @Tag("fail")
    public void login_fail_nullPassword() throws Exception {
        final String username = "user";

        Assumptions.assumeTrue(userService.findByUsername(username).isPresent(), "User not found");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.putNull("password");

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("login fail - username is null")
    @Tag("login") @Tag("fail")
    public void login_fail_nullUsername() throws Exception {
        final String password = "wrong_password";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.putNull("username");
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1002)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("signup success")
    @Tag("signup") @Tag("success")
    public void signup_success() throws Exception {
        final String username = "new_user";
        final String password = "Pa55woRD";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Assumptions.assumeFalse(userService.findByUsername(username).isPresent(), "username is already used");
        Assertions.assertDoesNotThrow(() -> userService.validate(user), "validation failed");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/signup")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.user.id", notNullValue() ),
                        jsonPath("$.user.username", is(username)),
                        jsonPath("$.user.password").doesNotExist(),
                        jsonPath("$.token", notNullValue()) );

        Assertions.assertTrue(userService.findByUsername(username).isPresent(), "created user not found");
    }

    public Stream<Arguments> invalidUsernames() {
        String existingUsername = "user";
        Assertions.assertTrue(userService.findByUsername(existingUsername).isPresent(),
                "existingUsername not found");

        return Stream.of(
                Arguments.of(Named.of("already exists", existingUsername), 1203),
                Arguments.of(Named.of("is null", null), 1200),
                Arguments.of(Named.of("is blank", " \t"), 1206),
                Arguments.of(Named.of("invalid characters", "inval!d#character$$"), 1201),
                Arguments.of(Named.of("too long", buildRepeatableString("A", 51)), 1205)
        );
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("signup fail - invalid username")
    @Tag("signup") @Tag("fail")
    @MethodSource("invalidUsernames")
    public void signup_fail_invalidUsername(String username, int code) throws Exception {
        final String password = "Pa55woRD";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/signup")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    public Stream<Arguments> invalidPasswords() {
        return Stream.of(
                Arguments.of(Named.of("is null", null), 1200),
                Arguments.of(Named.of("no lowercased letters", "PA55WORD"), 1202),
                Arguments.of(Named.of("no uppercased letters", "pa55word"), 1202),
                Arguments.of(Named.of("no digits", "PaSSwoRD"), 1202),
                Arguments.of(Named.of("too long", "Pa55woRD"
                        + buildRepeatableString("A", 43)), 1205),
                Arguments.of(Named.of("too short", "Ab1"), 1204)
        );
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("signup fail - invalid password")
    @Tag("signup") @Tag("fail")
    @MethodSource("invalidPasswords")
    public void signup_fail_invalidPassword(String password, int code) throws Exception {
        final String username = "New_user";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/signup")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("signup fail - password not presented")
    @Tag("signup") @Tag("fail")
    public void signup_fail_noPassword() throws Exception {
        final String username = "New_user";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("username", username);

        mockMvc.perform(post("/api/auth/signup")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1200)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("signup fail - username not presented")
    @Tag("signup") @Tag("fail")
    public void signup_fail_invalidPassword() throws Exception {
        final String password = "Pa55woRD";

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("password", password);

        mockMvc.perform(post("/api/auth/signup")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.user").doesNotExist(),
                        jsonPath("$.token").doesNotExist(),
                        jsonPath("$.code", is(1200)),
                        jsonPath("$.message").isString() );
    }
}
