package S10.VVSS.lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping
    public String mainPage() {
        return "mainPage";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
