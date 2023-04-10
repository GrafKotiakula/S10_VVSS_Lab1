package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.database.listitem.ListItem;
import S10.VVSS.lab1.database.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/list-item")
public class ListItemRestController {

    @GetMapping("/find-by/user")
    private List<ListItem> getAllByUser(@AuthenticationPrincipal User user){
        return user.getList();
    }
}
