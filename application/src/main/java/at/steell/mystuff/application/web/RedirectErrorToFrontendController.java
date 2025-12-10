package at.steell.mystuff.application.web;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class RedirectErrorToFrontendController implements ErrorController {
    public static final String PATH = "/error";
    public static final String FORWARDED_PATH = "/index.html";
    static final String FORWARDED_URL = "forward:" + FORWARDED_PATH;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(PATH)
    public String error() {
        return FORWARDED_URL;
    }
}
