package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.database.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @GetMapping
    public User getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return user;
    }
}
