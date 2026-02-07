package thapo.pocspring.web.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import thapo.pocspring.infrastructure.auth.CustomOidcUser;

@Controller
@RequestMapping(MpaController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MpaController {
    public static final String PATH = "/mpa";

    @GetMapping(path = "")
    public String indexPage() {
        return "redirect:/mpa/home";
    }


    @GetMapping(path = "home")
    public String homePage(@AuthenticationPrincipal final CustomOidcUser customOidcUser, final Model model) {
        if (customOidcUser != null) {
            log.info("customOidcUser class: {}, roles={}", customOidcUser.getClass().getName(), customOidcUser.getClaims());
            model.addAttribute("name", customOidcUser.getName());
            model.addAttribute("authorities", String.join(",", customOidcUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
            model.addAttribute("claims", customOidcUser.getClaims().values().stream().map(Object::toString).toList());
        }
        return "home/index";
    }


    @GetMapping("403")
    public String forbidden(Model model) {
        return "403";
    }
}
