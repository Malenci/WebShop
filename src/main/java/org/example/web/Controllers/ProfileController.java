package org.example.web.Controllers;

import org.example.web.Models.UserModel;
import org.example.web.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.web.Repo.IUserRepository;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private IUserRepository userRepository;
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/profile")
    public String updateProfile(@RequestParam("email") String email,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("password") String password,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserModel user = userRepository.findByUsername(currentUsername);
        if (user != null) {
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            userRepository.save(user);
        }

        model.addAttribute("user", user);
        model.addAttribute("message", "Profile updated successfully");

        return "profile";
    }
}