package org.example.web.Controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.web.Models.CartModel;
import org.example.web.Models.ProductModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String home (@RequestParam (name = "name", required = false, defaultValue = "Bogdan") String name, Model model){
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts(Model model){
        return "contacts";
    }


    @GetMapping("/view")
    public String viewCart(@CookieValue(value = "cartItems", defaultValue = "") String cartItems) {
        return "Cart items: " + cartItems;
    }



}
