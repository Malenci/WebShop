package org.example.web.Controllers;

import org.example.web.Models.UserModel;
import org.example.web.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.example.web.config.SecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserModel user, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        user.setPassword(user.getPassword());
        Set<String> role = new HashSet<>();
        role.add("ROLE_USER");
        user.setRoles(role);
        user.setRoles(Collections.singleton("ROLE_USER"));
        userRepository.save(user);
        return "redirect:/login";
    }


}