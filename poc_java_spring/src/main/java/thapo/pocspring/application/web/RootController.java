package thapo.pocspring.application.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping(path = "")
    public String indexPage() {
        return "redirect:/web";
    }
}
