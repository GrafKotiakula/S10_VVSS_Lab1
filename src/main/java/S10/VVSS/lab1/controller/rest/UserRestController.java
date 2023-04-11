package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.entities.user.User;
import S10.VVSS.lab1.entities.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return user;
    }

    @PutMapping
    public User updateUser(@AuthenticationPrincipal User authUser, @RequestBody User user) {
        user.setId(authUser.getId());
        userService.validate(user);
        user = userService.save(user);
        return user;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal User user) {
        userService.delete(user);
    }
}
