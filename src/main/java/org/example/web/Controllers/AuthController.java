package org.example.web.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.Models.CartModel;

import org.example.web.Models.UserModel;
import org.example.web.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;
import java.util.logging.Logger;

@Controller
public class AuthController {
    @Autowired
    IUserRepository iUserRepository;
    UserModel users = new UserModel();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        HttpServletResponse response,
                        Model model) {

        UserModel user = iUserRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);

            Cookie userCookie = new Cookie("user", user.getUsername());
            userCookie.setMaxAge(30 * 24 * 60 * 60);
            userCookie.setPath("/");
            response.addCookie(userCookie);

            return "redirect:/profile";
        } else {

            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
            Cookie usernameCookie = new Cookie("user", null);
            usernameCookie.setMaxAge(0);
            usernameCookie.setPath("/");
            response.addCookie(usernameCookie);


        return "redirect:/login";
    }
}

