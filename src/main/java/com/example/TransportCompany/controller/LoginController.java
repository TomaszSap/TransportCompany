package com.example.TransportCompany.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/home")

public class LoginController {
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   @RequestParam(value = "register", required = false) String register
    ) {
        String errorMsg = null;
        if (error != null)
            errorMsg = "Username or Password is incorrect!!";
        if (logout != null)
            errorMsg = "You have been successfully logged out";
        else if (register != null)
            errorMsg = "You registered successful";
        // model.addAttribute("errorMsg",errorMsg);
        return "redirect:/login";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //current authentication details of the user
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect/:login?logout=true";
        } else
            return "redirect/:login";
    }
}
